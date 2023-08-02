package com.looker.notesy.util

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import kotlinx.coroutines.*
import org.jsoup.Jsoup
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

	suspend fun getFavIcon(url: String): String = withContext(Dispatchers.IO){
		val domainName = url.domain.favIconApi()
		val fileName = System.currentTimeMillis().toString() + ".png"
		val newFile = File(imagesDirectory, fileName)
		domainName.downloadToFile(newFile)
		newFile.path ?: ""
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

	private suspend fun String.downloadToFile(file: File) {
		val response = client.get(this)
		val body = response.body<ByteArray>()
		file.writeBytes(body)
	}

	private fun String.favIconApi(size: Int = 128): String =
		"https://www.google.com/s2/favicons?domain=${this}&sz=${size}"

	private val imagesDirectory: File
		get() = File(
			context.externalCacheDir,
			"saved_images"
		).apply { isDirectory || mkdirs() || throw RuntimeException() }

}