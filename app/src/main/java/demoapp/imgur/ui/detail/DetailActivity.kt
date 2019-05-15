package demoapp.imgur.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import demoapp.imgur.R
import demoapp.imgur.base.BaseActivity
import demoapp.imgur.data.model.Image

/*
    Activity to show image selected from search result with SharedTransition
 */

class DetailActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context, image: Image): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("DATA", image)
            return intent
        }
    }

    @BindView(R.id.detail_toolbar)
    @JvmField
    var toolbar: Toolbar? = null

    @BindView(R.id.iv_detail_image)
    @JvmField
    var imageView: ImageView? = null

    @BindView(R.id.tv_detail_image_description)
    @JvmField
    var imageDescriptionTextView: TextView? = null

    override fun layoutRes(): Int {
        return R.layout.activity_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //do not show activity until image is ready for transition
        postponeEnterTransition();
        if (intent != null && intent.extras != null && intent.hasExtra("DATA")) {
            val image = intent?.extras?.getParcelable<Image>("DATA") ?: return
            imageDescriptionTextView?.text = image.title
            val imagelink = image.link ?: return
            val linkGenerator = { url: String, ch: Char ->
                url.split(".").toMutableList().let {
                    it[it.size - 2] = it[it.size - 2] + ch
                    return@let it.joinToString(separator = ".")
                }
            }

            val imageView = imageView ?: return
            val largeThumbnailUrl = linkGenerator(imagelink, 'h')

            Glide.with(imageView)
                    .load(largeThumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(object : RequestListener<Drawable?> {
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            //once image is ready, start transition.
                            startPostponedEnterTransition()
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                            //once image is ready, start transition.
                            startPostponedEnterTransition()
                            return false
                        }

                    })
                    .centerCrop()
                    .into(imageView)

        } else {
            finish()
        }

        setSupportActionBar(toolbar!!)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            onBackPressed()
            return true;
        }

        return super.onOptionsItemSelected(item)
    }
}