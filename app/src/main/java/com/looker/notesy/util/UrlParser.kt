package com.looker.notesy.util

import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class UrlParser @Inject constructor(
) {

    var document: Document? = null

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

    suspend fun getTitle(url: String): String = withContext(Dispatchers.IO) {
        document?.title() ?: url.domain
    }

    suspend fun previewImage(): String? = withContext(Dispatchers.IO) {
        val metaTags = document?.select("meta") ?: return@withContext null

        for (meta in metaTags) {
            val property = meta.attr("property")
            val content = meta.attr("content")

            return@withContext content.takeIf {
                property == "og:image"
                        || property == "twitter:image"
                        || property == "og:image:url"
                        || property == "og:image:secure_url"
            }
        }
        null
    }
}

fun String.favIcon(size: Int = 128): String = domain.favIconApi(size)

private val String.domain: String
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