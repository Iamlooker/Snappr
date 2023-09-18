package com.looker.notesy.util

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import javax.inject.Inject

class UrlParser @Inject constructor(
	@ApplicationContext private val context: Context
) {

	companion object {
		private val client = HttpClient(OkHttp)
	}

	suspend fun getTitle(url: String): String = withContext(Dispatchers.IO) {
		try {
			Jsoup.connect(url).get().title()
		} catch (e: Exception) {
			e.printStackTrace()
			url.domain
		}
	}

	suspend fun getFavIcon(url: String): String = withContext(Dispatchers.IO) {
		val domainName = url.favIcon()
		val fileName = System.currentTimeMillis().toString() + ".png"
		val newFile = File(imagesDirectory, fileName)
		domainName.downloadToFile(newFile)
		newFile.path ?: ""
	}

	private suspend fun String.downloadToFile(file: File) {
		val response = client.get(this)
		val body = response.body<ByteArray>()
		file.writeBytes(body)
	}

	private val imagesDirectory: File
		get() = File(
			context.externalCacheDir,
			"saved_images"
		).apply { isDirectory || mkdirs() || throw RuntimeException() }

}

fun String.favIcon(size: Int = 128): String = domain.favIconApi()

fun String.previewImage(): String {
	try {
		val document: Document = Jsoup.connect(this).get()
		val metaTags = document.select("meta")

		for (meta in metaTags) {
			val property = meta.attr("property")
			val content = meta.attr("content")

			if (property == "og:image") {
				// Open Graph image property
				return content
			} else if (property == "twitter:image") {
				// Twitter image property
				return content
			}
		}
		return ""
	} catch (e: Exception) {
		e.printStackTrace()
		return ""
	}
}

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