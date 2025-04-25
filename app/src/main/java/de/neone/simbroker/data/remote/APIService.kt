package de.neone.simbroker.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.neone.simbroker.BuildConfig
import de.neone.simbroker.data.remote.models.CoinResponse
import de.neone.simbroker.data.remote.models.CoinsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//==============================================================================================
// 1) Basis-Konfiguration
//==============================================================================================
/** Basis-URL für alle API-Aufrufe von CoinRanking über RapidAPI. */
const val BASE_URL = "https://coinranking1.p.rapidapi.com/"

//==============================================================================================
// 2) HTTP-Interceptors für Logging und Header
//==============================================================================================
/** Interceptor zur Protokollierung aller HTTP-Requests und -Responses (Body-Level). */
val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

/** Interceptor zum Hinzufügen der RapidAPI-Header (Key & Host). */
val headerInterceptor = okhttp3.Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("X-RapidAPI-Key", BuildConfig.API_KEY)
        .addHeader("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
        .build()
    chain.proceed(request)
}

//==============================================================================================
// 3) OkHttpClient & Moshi-Setup
//==============================================================================================
/** OkHttpClient mit Logging- und Header-Interceptor. */
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(headerInterceptor)
    .build()

/** Moshi-Instanz mit Unterstützung für Kotlin-Reflection. */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//==============================================================================================
// 4) Retrofit-Instanz
//==============================================================================================
/** Retrofit-Instanz zur Ausführung der API-Requests. */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

//==============================================================================================
// 5) API Service Singleton
//==============================================================================================
/**
 * Singleton-Objekt, das den [APIService] über Retrofit bereitstellt.
 */
object CoinbaseAPI {
    /** Retrofit Service für API-Aufrufe; lazy initialisiert. */
    val retrofitService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}

//==============================================================================================
// 6) APIService Schnittstelle
//==============================================================================================
/**
 * Definition aller verfügbaren Endpunkte der CoinRanking-API.
 *
 * Die Implementierung erfolgt automatisch durch Retrofit.
 */
interface APIService {

    /**
     * Holt den aktuellen Preis eines Coins.
     *
     * GET https://coinranking1.p.rapidapi.com/coin/{uuid}/price
     *
     * @param uuid UUID des Coins.
     * @return Aktueller Preis als Double.
     */
    @GET("coin/{uuid}/price")
    suspend fun getCoinPrice(
        @Path("uuid") uuid: String
    ): Double

    /**
     * Holt eine Liste von Coins mit Marktdaten.
     *
     * GET https://coinranking1.p.rapidapi.com/coins
     *
     * @param referenceCurrencyUuid UUID der Referenzwährung (Standard: EUR).
     * @param tiers Marktsegmente (Standard: "1").
     * @param orderBy Sortierfeld (Standard: "marketCap").
     * @param orderDirection Sortierrichtung (Standard: "desc").
     * @param limit Maximale Anzahl der Ergebnisse (Standard: 100).
     * @param timePeriod Zeitraum für Sparkline (Standard: "3h").
     * @return Response-Objekt mit Coin-Liste.
     */
    @GET("coins")
    suspend fun getCoins(
        @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
        @Query("tiers") tiers: String = "1",
        @Query("orderBy") orderBy: String = "marketCap",
        @Query("orderDirection") orderDirection: String = "desc",
        @Query("limit") limit: Int = 100,
        @Query("timePeriod") timePeriod: String = "3h"
    ): CoinsResponse

    /**
     * Holt Details eines Coins inkl. Sparkline für 3 Stunden.
     *
     * GET https://coinranking1.p.rapidapi.com/coin/{uuid}?timePeriod=3h
     *
     * @param uuid UUID des Coins.
     * @param referenceCurrencyUuid UUID der Referenzwährung.
     * @param timePeriod Zeitraum für Sparkline ("3h").
     * @return Response-Objekt mit Coin-Details.
     */
    @GET("coin/{uuid}")
    suspend fun getCoin3h(
        @Path("uuid") uuid: String,
        @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
        @Query("timePeriod") timePeriod: String = "3h"
    ): CoinResponse

    /**
     * Holt Details eines Coins inkl. Sparkline für 24 Stunden.
     *
     * GET https://coinranking1.p.rapidapi.com/coin/{uuid}?timePeriod=24h
     *
     * @param uuid UUID des Coins.
     * @param referenceCurrencyUuid UUID der Referenzwährung.
     * @param timePeriod Zeitraum für Sparkline ("24h").
     * @return Response-Objekt mit Coin-Details.
     */
    @GET("coin/{uuid}")
    suspend fun getCoin24h(
        @Path("uuid") uuid: String,
        @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
        @Query("timePeriod") timePeriod: String = "24h"
    ): CoinResponse

    /**
     * Holt Details eines Coins inkl. Sparkline für 30 Tage.
     *
     * GET https://coinranking1.p.rapidapi.com/coin/{uuid}?timePeriod=30d
     *
     * @param uuid UUID des Coins.
     * @param referenceCurrencyUuid UUID der Referenzwährung.
     * @param timePeriod Zeitraum für Sparkline ("30d").
     * @return Response-Objekt mit Coin-Details.
     */
    @GET("coin/{uuid}")
    suspend fun getCoin30d(
        @Path("uuid") uuid: String,
        @Query("referenceCurrencyUuid") referenceCurrencyUuid: String = "5k-_VTxqtCEI",
        @Query("timePeriod") timePeriod: String = "30d"
    ): CoinResponse
}
