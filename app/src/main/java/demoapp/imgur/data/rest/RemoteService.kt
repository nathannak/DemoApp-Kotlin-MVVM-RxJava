package demoapp.imgur.data.rest


import demoapp.imgur.data.model.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {

    /*
    api call to get result result
    */
    @Headers("Authorization: Client-ID 126701cd8332f32")
    @GET("/3/gallery/search/time/{page_number}")
    fun getSearchResult(@Path("page_number") pageNo: Int,
                        @Query("q") organization: String): Call<SearchResult>


}
