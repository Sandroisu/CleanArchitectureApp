package dev.sandroisu.api

import androidx.annotation.IntRange
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dev.sandroisu.api.models.ArticleDTO
import dev.sandroisu.api.models.Language
import dev.sandroisu.api.models.ResponseDTO
import dev.sandroisu.api.models.SortBy
import dev.sandroisu.api.utils.NewsApiKeyInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

/**
 * API Documentation [here](https://newsapi.org/docs)
 */
interface NewsApi {
    /**
     *  API details [here](https://newsapi.org/docs/endpoints/everything)
     */
    @GET("everything")
    @Suppress("LongParameterList")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("language") languages: List<@JvmSuppressWildcards Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Result<ResponseDTO<ArticleDTO>>
}

fun NewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json {
        coerceInputValues = true
    },
): NewsApi {
    val retrofit =
        retrofit(baseUrl = baseUrl, apiKey = apiKey, okHttpClient = okHttpClient, json = json)
    return retrofit.create()
}

private fun retrofit(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient?,
    json: Json,
): Retrofit {
    val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())
    val clientWithApi =
        (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
            .addInterceptor(NewsApiKeyInterceptor(apiKey = apiKey)).build()
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(clientWithApi)
        .build()
}

fun convert(s: String, numRows: Int): String {
    val map = mutableMapOf<Int, MutableList<Char>>()
    for (i in 0 until numRows) {
        map[i] = mutableListOf()
    }
    val chars = s.toCharArray()
    for (i in chars.indices) {
        val multiplier = i/numRows
        val offset = i - multiplier*numRows
        map[offset]?.add(chars[i])
    }
    var maxPolindrome = ""
    for (i in 0 until numRows) {
        map[i]?.forEach {
            maxPolindrome += it
        }
    }

    return maxPolindrome
}
