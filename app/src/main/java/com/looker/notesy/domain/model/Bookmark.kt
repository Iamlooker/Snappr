package com.looker.notesy.domain.model

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Stable
data class Bookmark(
    val url: String,
    val name: String,
    val artwork: String,
    val lastModified: Long,
    @PrimaryKey
    val id: Long? = null
)
