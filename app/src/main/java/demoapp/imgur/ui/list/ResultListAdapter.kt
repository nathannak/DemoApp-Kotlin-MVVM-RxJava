package demoapp.imgur.ui.list

import android.arch.paging.PagedListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import demoapp.imgur.R
import demoapp.imgur.data.model.Image

/*
    Adapter to show result of images.
    This is a PagedListAdapter from Jetpack Pagination Library.

 */
class ResultListAdapter internal constructor(private val imageSelectedListener: ImageSelectedListener,
                                             private val diffUtil: ImgurDiffUtil)
    : PagedListAdapter<Image, ResultListAdapter.ResultItemViewHolder>(diffUtil) {

    val ITEM_TYPE_IMAGE = 1
    val ITEM_TYPE_LOADING = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_result_list_item, parent, false)
        return ResultItemViewHolder(view, imageSelectedListener)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class ResultItemViewHolder(itemView: View, imageSelectedListener: ImageSelectedListener) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.iv_image)
        @JvmField
        var imageView: ImageView? = null

        @BindView(R.id.tv_image_description)
        @JvmField
        var imageDescriptionTextView: TextView? = null

        private var image: Image? = null

        init {
            ButterKnife.bind(this, itemView)
            imageView?.setOnClickListener { imageView ->
                image?.let {
                    imageSelectedListener.onImageSelected(imageView as ImageView, it)
                }
            }
        }

        fun bind(image: Image) {
            this.image = image
            val imagelink = image.link ?: return

            val linkGenerator = { url: String, ch: Char ->
                url.split(".").toMutableList().let {
                    it[it.size - 2] = it[it.size - 2] + ch
                    return@let it.joinToString(separator = ".")
                }
            }

            val smallThumbnailUrl = linkGenerator(imagelink, 't')
            val largeThumbnailUrl = linkGenerator(imagelink, 'h')

            val thumbnailRequest = Glide
                    .with(itemView)
                    .load(smallThumbnailUrl)

            imageView?.let {
                Glide.with(itemView)
                        .load(largeThumbnailUrl)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .thumbnail(thumbnailRequest)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_light_primary_48dp)
                        .into(it)
            }

            imageDescriptionTextView?.text = image.title

        }
    }
}
