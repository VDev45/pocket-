package com.example.model

data class Highlight(
    val id: String,
    val articleId: Long,
    val text: String,
    val note: String = "",
    val colorHex: String = "#FFF59D", // Light yellow highlight default
    val paragraphIndex: Int = -1,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ReaderThemeMode {
    LIGHT, SEPIA, DARK, BLACK
}

enum class ReaderFontFamily {
    SERIF, SANS, MONO
}

enum class ReaderLineSpacing {
    COMPACT, NORMAL, RELAXED
}

data class ReaderSettings(
    val fontSizeSp: Int = 18,
    val themeMode: ReaderThemeMode = ReaderThemeMode.LIGHT,
    val fontFamily: ReaderFontFamily = ReaderFontFamily.SERIF,
    val lineSpacing: ReaderLineSpacing = ReaderLineSpacing.NORMAL,
    val justifyText: Boolean = false,
    val keepScreenOn: Boolean = true
)
