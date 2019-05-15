package demoapp.imgur.di.module

import dagger.Module
import dagger.Provides
import demoapp.imgur.data.rest.RemoteService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/* Providers for various services

 */
@Module
abstract class ApplicationModule {

    @Module
    companion object {
        private val BASE_URL = "https://api.imgur.com/"

        @JvmStatic
        @Provides
        fun provideRetrofit(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

        }

        @JvmStatic
        @Provides
        fun provideRetrofitService(retrofit: Retrofit): RemoteService {
            return retrofit.create(RemoteService::class.java)
        }

    }
}

