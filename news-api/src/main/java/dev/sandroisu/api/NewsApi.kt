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
    val spacingSize = numRows - 2
    for (i in 0 until numRows) {
        map[i] = mutableListOf()
    }
    val arrayOfDesigion = mutableListOf<Int>()
    val chars = s.toCharArray()
    var offsetting = 0
    var offsettMultiplier = 0
    chars.forEachIndexed { index, c ->
        val orderNumber = index + 1 + offsettMultiplier*spacingSize
        if (offsetting!=0){
            if(offsetting == index){
                offsetting = 0
            }
        }else if (orderNumber%numRows == 0) {
            arrayOfDesigion.add(index)
            offsetting = index + spacingSize
            offsettMultiplier++
        }
    }
    print(arrayOfDesigion.toString())
    var fixing = false
    var zRow = 0
    for (i in chars.indices) {

        val multiplier = i/numRows
        val row = i - multiplier*numRows

        if(fixing){
            if (zRow == 0){
                fixing = false
                map[row]?.add(chars[i])
            } else {
                map[zRow]?.add(chars[i])
                zRow--
            }
        } else {
            map[row]?.add(chars[i])
        }
        if(arrayOfDesigion.contains(i)) {
            fixing = true
            zRow = row-1
        }
    }
    var maxPolindrome = ""
    for (i in 0 until numRows) {
        map[i]?.forEach {
            maxPolindrome += it
        }
    }
    print(maxPolindrome)
    return maxPolindrome
}
