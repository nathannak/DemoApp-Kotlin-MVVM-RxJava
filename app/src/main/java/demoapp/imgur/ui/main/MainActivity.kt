package demoapp.imgur.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.BindView
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import demoapp.imgur.R
import demoapp.imgur.base.BaseActivity
import demoapp.imgur.data.datasource.ImgurDataSource
import demoapp.imgur.data.model.Image
import demoapp.imgur.data.rest.RemoteService
import demoapp.imgur.ui.detail.DetailActivity
import demoapp.imgur.ui.list.ImageSelectedListener
import demoapp.imgur.ui.list.ImgurDiffUtil
import demoapp.imgur.ui.list.ResultListAdapter
import demoapp.imgur.util.VerticalSpaceItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/*
    Main Activity having search bar and search result.
 */

class MainActivity : BaseActivity(), ImageSelectedListener {

    @Inject
    lateinit var apiService: RemoteService

    @BindView(R.id.progress_circular)
    @JvmField
    var progressBar: ProgressBar? = null

    @BindView(R.id.toolbar)
    @JvmField
    var toolbar: Toolbar? = null

    @BindView(R.id.recyclerView)
    @JvmField
    var listView: RecyclerView? = null

    private lateinit var pagedListAdapter: ResultListAdapter

    private var query : String? = null

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        listView?.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.grid_cols))
        listView?.addItemDecoration(VerticalSpaceItemDecoration(resources.getInteger(R.integer.grid_cols), resources.getDimensionPixelOffset(R.dimen.list_item_vertical_space)))

        /*
            get the query when device is rotated.
         */
        savedInstanceState?.let {
            query = it.getString("query")
            if(!query.isNullOrBlank()) {
                observableViewModel(query!!)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView = menu?.findItem(R.id.menu_item_search) ?: return true

        setupSearchView(searchView)

        return true
    }

    private fun setupSearchView(searchMenuItem: MenuItem) {
        val searchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)

        /*
            RxBinging library for searchview
         */
        RxSearchView.queryTextChanges(searchView)
                .doOnEach { notification -> notification.value }
                .debounce(250, TimeUnit.MILLISECONDS) // to skip intermediate letters
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val query = it.toString()
                    this@MainActivity.query = query
                    when (query.isNullOrEmpty()) {
                        true -> {
                            listView?.adapter = null
                            progressBar?.visibility = View.GONE
                        }
                        false -> observableViewModel(query)
                    }
                }

        searchView.setOnSearchClickListener {
            listView?.adapter = null
            progressBar?.visibility = View.GONE
        }
    }

    /*
        Pagination related initialization when query is made in search view
     */
    private fun initializedPagedListBuilder(query: String, config: PagedList.Config):
            LivePagedListBuilder<Int, Image> {

        val dataSourceFactory = object : DataSource.Factory<Int, Image>() {
            override fun create(): DataSource<Int, Image> {
                return ImgurDataSource(apiService, query)
            }
        }
        return LivePagedListBuilder<Int, Image>(dataSourceFactory, config)
    }

    private fun initAdapter() {
        pagedListAdapter = ResultListAdapter(this, ImgurDiffUtil())
        pagedListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (pagedListAdapter.itemCount > 0)
                    progressBar?.visibility = View.GONE
            }
        })

        listView?.adapter = pagedListAdapter
    }

    private fun observableViewModel(query: String) {
        progressBar?.visibility = View.VISIBLE
        initAdapter()
        val config = PagedList.Config.Builder()
                .setPageSize(50)
                .setEnablePlaceholders(true)
                .build()

        observer?.let {
            liveData?.removeObserver(it)
            observer = null
            liveData = null
        }

        liveData = initializedPagedListBuilder(query, config).build()
        observer = Observer<PagedList<Image>> { pagedList ->
            if(listView?.adapter == null) {
                initAdapter()
            }
            pagedListAdapter.submitList(pagedList)
        }

        observer?.let { liveData?.observe(this, it) }
    }

    /*
    When image is selected, start DetailActivity with shared transition
     */
    override fun onImageSelected(imageView: ImageView, image: Image) {
        val intent = DetailActivity.getIntent(this, image)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,
                imageView,
                ViewCompat.getTransitionName(imageView)!!)
        startActivity(intent, options.toBundle());
    }

    /*
        When activity is being destroyed as a result of device rotation, saved the query in outstate
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("query", query)
        super.onSaveInstanceState(outState)

    }

    /*
        livedata and observer to be cleaned when new query is made.
     */

    private var liveData: LiveData<PagedList<Image>>? = null;
    private var observer: Observer<PagedList<Image>>? = null;

}
