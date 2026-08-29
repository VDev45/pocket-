package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val url: String,
    val domain: String,
    val author: String,
    val publishedDate: String,
    val timeToReadMinutes: Int,
    val excerpt: String,
    val content: String,
    val thumbnailResId: Int? = null,
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val readingProgress: Float = 0f, // 0.0 to 1.0
    val savedAt: Long = System.currentTimeMillis(),
    val category: String = "General",
    val tags: String = "", // Comma-separated tags
    val isVideo: Boolean = false,
    val highlights: String = "[]" // JSON string of highlights
)
