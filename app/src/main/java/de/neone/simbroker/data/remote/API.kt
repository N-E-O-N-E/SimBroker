package de.neone.simbroker.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
        .addHeader("X-RapidAPI-Key", "a51a522017msha97b7e9335c6bf8p19d74ajsnd7326aa11de8")
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
        @Query("orderBy") orderBy: String = "marketCap",
        @Query("orderDirection") orderDirection: String = "desc",
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Query("timePeriod") timePeriod: String = "24h",
    ): CoinsResponse

    // Testcoin = Qwsogvtv82FCd
    @GET("coin/{uuid}")
    suspend fun getCoin(
        @Path("uuid") uuid: String,
        @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
        @Query("timePeriod") timePeriod: String = "24h",
    ): CoinResponse
}



fun main() = runBlocking {
    val api = CoinbaseAPI.retrofitService
    try {
        val response = api.getCoin(
            uuid = "Qwsogvtv82FCd",
            referenceCurrencyUuid = "5k-_VTxqtCEI",
            timePeriod = "3h"
        )
        println("API Response: $response")
    } catch (e: Exception) {
        println("API Fehler: ${e.message}")
    }
}
