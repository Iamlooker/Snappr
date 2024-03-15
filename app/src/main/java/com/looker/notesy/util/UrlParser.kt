package com.looker.notesy.util

import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object UrlParser {

    private var document: Document? = null

    suspend fun connect(url: String) {
        withContext(Dispatchers.IO) {
            document = try {
                Jsoup.connect(url).get()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getTitle(): String? = withContext(Dispatchers.IO) {
        document?.title()
    }

    suspend fun previewImage(): String? = withContext(Dispatchers.IO) {
        val metaTags = document?.select("meta") ?: return@withContext null

        val tag = metaTags.find { meta ->
            meta.attr("property") in SUPPORTED_PROPERTIES
        }
        tag?.attr("content")
    }
}

private val SUPPORTED_PROPERTIES = setOf(
    "og:image", "og:image:url", "og:image:secure_url", "twitter:image"
)

fun String.favIcon(size: Int = 128): String = domain.favIconApi(size)

val String.domain: String
    get() {
        val uri = toUri()
        val domain = uri.host
            ?.removePrefix("https://")
            ?.removePrefix("https://")
            ?.removePrefix("www.")
        return domain ?: this
    }

private fun String.favIconApi(size: Int = 128): String =
    "https://www.google.com/s2/favicons?domain=${this}&sz=${size}"