package de.neone.simbroker.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.neone.simbroker.BuildConfig
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://coinranking1.p.rapidapi.com/"

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val headerInterceptor = okhttp3.Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("X-RapidAPI-Key", BuildConfig.API_KEY)
        .addHeader("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
        .build()
    chain.proceed(request)
}
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(headerInterceptor)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

object CoinbaseAPI {
    val retrofitService: APIService by lazy { retrofit.create(APIService::class.java) }
}

interface APIService {
    @GET("coins")
    suspend fun getCoins(
        @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
        @Query("tiers") tiers: String = "1",
        @Query("orderBy") orderBy: String = "price",
        @Query("orderDirection") orderDirection: String = "desc",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 10,
        @Query("timePeriod") timePeriod: String = "24h",
    ): CoinsResponse

    @GET("coin/{uuid}")
    suspend fun getCoin1h(@Path("uuid") uuid: String, @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
        @Query("timePeriod") timePeriod: String = "1h"): CoinResponse

    @GET("coin/{uuid}")
    suspend fun getCoin3h(@Path("uuid") uuid: String, @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
                        @Query("timePeriod") timePeriod: String = "3h"): CoinResponse

    @GET("coin/{uuid}")
    suspend fun getCoin24h(@Path("uuid") uuid: String, @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
                        @Query("timePeriod") timePeriod: String = "24h"): CoinResponse

    @GET("coin/{uuid}")
    suspend fun getCoin7d(@Path("uuid") uuid: String, @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
                        @Query("timePeriod") timePeriod: String = "7d"): CoinResponse

    @GET("coin/{uuid}")
    suspend fun getCoin30d(@Path("uuid") uuid: String, @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
                          @Query("timePeriod") timePeriod: String = "30d"): CoinResponse

    @GET("coin/{uuid}")
    suspend fun getCoin3m(@Path("uuid") uuid: String, @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
                          @Query("timePeriod") timePeriod: String = "3m"): CoinResponse

}



fun main() = runBlocking {
    val api = CoinbaseAPI.retrofitService
    try {
        val response = api.getCoin1h(
            uuid = "Qwsogvtv82FCd",
            referenceCurrencyUuid = "5k-_VTxqtCEI",
            timePeriod = "1h"
        )
        println("API Response: $response")
    } catch (e: Exception) {
        println("API Fehler: ${e.message}")
    }
}
