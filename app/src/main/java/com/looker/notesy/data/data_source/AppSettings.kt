package com.looker.notesy.data.data_source

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.looker.notesy.domain.utils.NoteOrder
import com.looker.notesy.domain.utils.OrderType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.IOException

data class AppSettings(
	val order: OrderType,
	val noteOrder: NoteOrder
)

class AppSettingsRepository(
	private val store: DataStore<Preferences>
) {

	private object Keys {
		val ORDER_TYPE = stringPreferencesKey("key_order_type")
		val NOTE_ORDER = intPreferencesKey("key_note_order")
	}

	val stream: Flow<AppSettings> = store.data
		.catch { e ->
			if (e is IOException) Log.e("SettingsRepository", "Error", e)
			else throw e
		}
		.map(::mapSettings)

	suspend fun initialSettings() =
		mapSettings(store.data.first().toPreferences())

	suspend fun setNoteOrder(order: NoteOrder) {
		Keys.NOTE_ORDER.update(order.toInt())
		Keys.ORDER_TYPE.update(order.orderType.name)
	}

	private fun mapSettings(preferences: Preferences): AppSettings {
		val orderType = OrderType.valueOf(preferences[Keys.ORDER_TYPE] ?: OrderType.Descending.name)
		val noteOrder = preferences[Keys.NOTE_ORDER] ?: 2
		return AppSettings(
			order = orderType,
			noteOrder = noteOrder.toNoteOrder(orderType)
		)
	}

	private suspend inline fun <T> Preferences.Key<T>.update(
		newValue: T
	) = withContext(Dispatchers.IO) {
		store.edit { preferences ->
			preferences[this@update] = newValue
		}
	}
}

private fun NoteOrder.toInt(): Int = when (this) {
	is NoteOrder.Date -> 1
	is NoteOrder.Id -> 2
	is NoteOrder.Title -> 3
}

private fun Int.toNoteOrder(type: OrderType): NoteOrder = when (this) {
	1 -> NoteOrder.Date(type)
	2 -> NoteOrder.Id(type)
	3 -> NoteOrder.Title(type)
	else -> throw IllegalArgumentException("Invalid Note Order")
}
