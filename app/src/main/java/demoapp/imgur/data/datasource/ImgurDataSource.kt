package demoapp.imgur.data.datasource

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import demoapp.imgur.data.model.Image
import demoapp.imgur.data.model.SearchResult
import demoapp.imgur.data.rest.RemoteService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
    Data Source for Jetpack's Pagination Library
    Here RemoteService will fetching data for query

    1. Load initial is called
    2. When on scroll, new page is required, loadAfter will be called from library.
    3. loadBefore will be used in case previous page is required.

    All the nsfw images are filtered here.

 */

class ImgurDataSource(private val apiService: RemoteService, private val query: String) :
        PageKeyedDataSource<Int, Image>() {

    var pageNumber = 0

    override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Image>) {
        apiService.getSearchResult(pageNumber, query)
                .enqueue(object : Callback<SearchResult> {

                    override fun onFailure(call: Call<SearchResult>?, t: Throwable?) {
                        Log.e("ImgurDataSource", "Failed to fetch data!")
                    }

                    override fun onResponse(
                            call: Call<SearchResult>?,
                            response: Response<SearchResult>) {

                        val listing = response.body()?.data
                        val images: MutableList<Image> = ArrayList()
                        listing?.forEach {
                            it.images?.forEach {
                                if (it.nsfw == null || it.nsfw == false)
                                    images.add(it)
                            }
                        }
                        pageNumber++;
                        callback.onResult(images ?: listOf(), null, pageNumber)
                    }
                })
    }

    override fun loadAfter(
            params: LoadParams<Int>,
            callback: LoadCallback<Int, Image>) {

        apiService.getSearchResult(pageNumber, query)
                .enqueue(object : Callback<SearchResult> {

                    override fun onFailure(call: Call<SearchResult>?, t: Throwable?) {
                        Log.e("ImgurDataSource", "Failed to fetch data!")
                    }

                    override fun onResponse(
                            call: Call<SearchResult>?,
                            response: Response<SearchResult>) {

                        val listing = response.body()?.data
                        val images: MutableList<Image> = ArrayList()
                        listing?.forEach {
                            it.images?.forEach {
                                if (it.nsfw == null || it.nsfw == false)
                                    images.add(it)
                            }
                        }
                        pageNumber++;
                        callback.onResult(images ?: listOf(), pageNumber)
                    }
                })
    }

    override fun loadBefore(
            params: LoadParams<Int>,
            callback: LoadCallback<Int, Image>) {

        apiService.getSearchResult(pageNumber, query)
                .enqueue(object : Callback<SearchResult> {

                    override fun onFailure(call: Call<SearchResult>?, t: Throwable?) {
                        Log.e("ImgurDataSource", "Failed to fetch data!")
                    }

                    override fun onResponse(
                            call: Call<SearchResult>?,
                            response: Response<SearchResult>) {

                        val listing = response.body()?.data
                        val images: MutableList<Image> = ArrayList()
                        listing?.forEach {
                            it.images?.forEach {
                                if (it.nsfw == null || it.nsfw == false)
                                    images.add(it)
                            }
                        }

                        pageNumber--;
                        callback.onResult(images ?: listOf(), pageNumber)
                    }
                })
    }
}

