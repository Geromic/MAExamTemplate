package network

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

abstract class RetrofitClientImpl {

    companion object {
        private var INSTANCE: RetrofitClient? = null
        val retrofitClientInstance: RetrofitClient?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = createInstance()
                }
                return INSTANCE
            }

        private fun createInstance(): RetrofitClient {
            @SuppressLint("NewApi", "LocalSuppress") val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(1))
                .build()
            return Retrofit.Builder()
                .baseUrl("http://86.124.3.59:4200")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(RetrofitClient::class.java)
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}