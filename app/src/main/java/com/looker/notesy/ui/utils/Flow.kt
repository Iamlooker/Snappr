package com.looker.notesy.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val DEFAULT_SUBSCRIBE_MILLIS = 5_000L

context(ViewModel)
fun <T> Flow<T>.asStateFlow(
    initial: T,
    sharingStarted: SharingStarted = SharingStarted.WhileSubscribed(DEFAULT_SUBSCRIBE_MILLIS),
    scope: CoroutineScope = viewModelScope,
): StateFlow<T> = stateIn(
    scope = scope,
    started = sharingStarted,
    initialValue = initial,
)
