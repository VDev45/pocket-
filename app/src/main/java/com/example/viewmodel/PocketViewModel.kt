package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ArticleRepository
import com.example.data.SampleArticles
import com.example.model.ArticleEntity
import com.example.model.Highlight
import com.example.model.ReaderFontFamily
import com.example.model.ReaderLineSpacing
import com.example.model.ReaderSettings
import com.example.model.ReaderThemeMode
import com.example.tts.PocketTtsManager
import com.example.tts.TtsPlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class PocketTab {
    SAVES, DISCOVER, HIGHLIGHTS, PROFILE
}

enum class SavesFilter {
    ALL, ARTICLES, VIDEOS, FAVORITES, ARCHIVE
}

enum class SortOption {
    NEWEST, OLDEST, SHORTEST_READ, LONGEST_READ
}

data class PocketUiState(
    val currentTab: PocketTab = PocketTab.SAVES,
    val savesFilter: SavesFilter = SavesFilter.ALL,
    val selectedTag: String? = null,
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.NEWEST,
    val isGridView: Boolean = false,
    val selectedArticleId: Long? = null,
    val readerSettings: ReaderSettings = ReaderSettings(),
    val isAddUrlDialogOpen: Boolean = false,
    val isReaderSettingsOpen: Boolean = false,
    val isTagFilterDialogOpen: Boolean = false,
    val showToastMessage: String? = null
)

class PocketViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ArticleRepository
    val ttsManager: PocketTtsManager = PocketTtsManager(application)

    private val _uiState = MutableStateFlow(PocketUiState())
    val uiState: StateFlow<PocketUiState> = _uiState.asStateFlow()

    val ttsState: StateFlow<TtsPlaybackState> = ttsManager.playbackState

    private val _discoverArticles = MutableStateFlow(SampleArticles.getDiscoverArticles())
    val discoverArticles: StateFlow<List<ArticleEntity>> = _discoverArticles.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = ArticleRepository(database.articleDao())
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    val rawArticles: StateFlow<List<ArticleEntity>> = repository.allArticles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val displayedArticles: StateFlow<List<ArticleEntity>> = combine(
        rawArticles,
        _uiState
    ) { articles, uiState ->
        var filtered = articles.filter { article ->
            when (uiState.savesFilter) {
                SavesFilter.ALL -> !article.isArchived
                SavesFilter.ARTICLES -> !article.isArchived && !article.isVideo
                SavesFilter.VIDEOS -> !article.isArchived && article.isVideo
                SavesFilter.FAVORITES -> !article.isArchived && article.isFavorite
                SavesFilter.ARCHIVE -> article.isArchived
            }
        }

        // Tag filter
        if (!uiState.selectedTag.isNullOrBlank()) {
            filtered = filtered.filter { article ->
                article.tags.split(",")
                    .map { it.trim().lowercase() }
                    .contains(uiState.selectedTag.lowercase())
            }
        }

        // Search query
        if (uiState.searchQuery.isNotBlank()) {
            val query = uiState.searchQuery.trim().lowercase()
            filtered = filtered.filter {
                it.title.lowercase().contains(query) ||
                it.excerpt.lowercase().contains(query) ||
                it.domain.lowercase().contains(query) ||
                it.author.lowercase().contains(query) ||
                it.tags.lowercase().contains(query)
            }
        }

        // Sorting
        when (uiState.sortOption) {
            SortOption.NEWEST -> filtered.sortedByDescending { it.savedAt }
            SortOption.OLDEST -> filtered.sortedBy { it.savedAt }
            SortOption.SHORTEST_READ -> filtered.sortedBy { it.timeToReadMinutes }
            SortOption.LONGEST_READ -> filtered.sortedByDescending { it.timeToReadMinutes }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allUniqueTags: StateFlow<List<String>> = rawArticles.combine(_uiState) { articles, _ ->
        val tagsSet = mutableSetOf<String>()
        articles.forEach { article ->
            article.tags.split(",").forEach { tag ->
                val trimmed = tag.trim()
                if (trimmed.isNotBlank()) {
                    tagsSet.add(trimmed)
                }
            }
        }
        tagsSet.sorted()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allHighlights: StateFlow<List<Highlight>> = rawArticles.combine(_uiState) { articles, _ ->
        val list = mutableListOf<Highlight>()
        articles.forEach { article ->
            list.addAll(repository.parseHighlights(article.highlights))
        }
        list.sortedByDescending { it.createdAt }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun selectTab(tab: PocketTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab, selectedArticleId = null)
    }

    fun setSavesFilter(filter: SavesFilter) {
        _uiState.value = _uiState.value.copy(savesFilter = filter)
    }

    fun setSelectedTag(tag: String?) {
        _uiState.value = _uiState.value.copy(selectedTag = tag)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setSortOption(sortOption: SortOption) {
        _uiState.value = _uiState.value.copy(sortOption = sortOption)
    }

    fun toggleGridView() {
        _uiState.value = _uiState.value.copy(isGridView = !_uiState.value.isGridView)
    }

    fun openArticle(articleId: Long) {
        _uiState.value = _uiState.value.copy(selectedArticleId = articleId)
    }

    fun closeArticle() {
        _uiState.value = _uiState.value.copy(selectedArticleId = null)
    }

    fun toggleFavorite(article: ArticleEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(article.id, article.isFavorite)
            showToast(if (article.isFavorite) "Removed from Favorites" else "Added to Favorites")
        }
    }

    fun toggleArchive(article: ArticleEntity) {
        viewModelScope.launch {
            repository.toggleArchived(article.id, article.isArchived)
            showToast(if (article.isArchived) "Moved to My List" else "Archived article")
        }
    }

    fun deleteArticle(articleId: Long) {
        viewModelScope.launch {
            repository.deleteArticle(articleId)
            if (_uiState.value.selectedArticleId == articleId) {
                closeArticle()
            }
            showToast("Item deleted")
        }
    }

    fun updateProgress(articleId: Long, progress: Float) {
        viewModelScope.launch {
            repository.updateProgress(articleId, progress)
        }
    }

    fun saveUrl(url: String, customTitle: String = "", customTags: String = "") {
        if (url.isBlank()) return
        viewModelScope.launch {
            val domain = extractDomain(url)
            val cleanTitle = if (customTitle.isNotBlank()) customTitle else "Saved from $domain"
            val wordCount = 450
            val readTime = (wordCount / 200).coerceAtLeast(2)

            val newArticle = ArticleEntity(
                title = cleanTitle,
                url = url,
                domain = domain,
                author = "Web Author",
                publishedDate = "Just now",
                timeToReadMinutes = readTime,
                excerpt = "Article content saved from $domain to read anytime offline without distractions.",
                content = """
                    $cleanTitle
                    
                    Saved from $url
                    
                    This article has been clipped to your Pocket library. You can now read it distraction-free, customize font size, switch between light, sepia, and dark themes, or use the Listen feature to have it read aloud.
                    
                    Pocket automatically cleans away ads, pop-ups, and extraneous formatting so you can focus entirely on ideas.
                """.trimIndent(),
                thumbnailResId = null,
                isFavorite = false,
                isArchived = false,
                readingProgress = 0f,
                savedAt = System.currentTimeMillis(),
                category = "General",
                tags = customTags.ifBlank { "SavedLink" }
            )

            repository.saveArticle(newArticle)
            _uiState.value = _uiState.value.copy(isAddUrlDialogOpen = false)
            showToast("Saved to Pocket!")
        }
    }

    fun saveDiscoverArticle(discoverArticle: ArticleEntity) {
        viewModelScope.launch {
            val newArticle = discoverArticle.copy(
                id = 0, // Auto-generate new DB ID
                savedAt = System.currentTimeMillis(),
                isArchived = false,
                isFavorite = false,
                readingProgress = 0f
            )
            repository.saveArticle(newArticle)
            showToast("Saved '${discoverArticle.title}' to My List")
        }
    }

    fun addHighlight(articleId: Long, text: String, note: String = "", paragraphIndex: Int = -1, colorHex: String = "#FFF59D") {
        viewModelScope.launch {
            repository.addHighlight(articleId, text, note, colorHex, paragraphIndex)
            showToast("Highlight saved")
        }
    }

    fun removeHighlight(articleId: Long, highlightId: String) {
        viewModelScope.launch {
            repository.removeHighlight(articleId, highlightId)
            showToast("Highlight removed")
        }
    }

    fun updateTags(articleId: Long, tags: String) {
        viewModelScope.launch {
            repository.updateTags(articleId, tags)
            showToast("Tags updated")
        }
    }

    fun setAddUrlDialogOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isAddUrlDialogOpen = open)
    }

    fun setReaderSettingsOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isReaderSettingsOpen = open)
    }

    fun setTagFilterDialogOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isTagFilterDialogOpen = open)
    }

    fun updateReaderSettings(
        fontSizeSp: Int = _uiState.value.readerSettings.fontSizeSp,
        themeMode: ReaderThemeMode = _uiState.value.readerSettings.themeMode,
        fontFamily: ReaderFontFamily = _uiState.value.readerSettings.fontFamily,
        lineSpacing: ReaderLineSpacing = _uiState.value.readerSettings.lineSpacing,
        justifyText: Boolean = _uiState.value.readerSettings.justifyText
    ) {
        _uiState.value = _uiState.value.copy(
            readerSettings = _uiState.value.readerSettings.copy(
                fontSizeSp = fontSizeSp,
                themeMode = themeMode,
                fontFamily = fontFamily,
                lineSpacing = lineSpacing,
                justifyText = justifyText
            )
        )
    }

    fun playArticleAudio(article: ArticleEntity) {
        ttsManager.loadArticle(article.id, article.title, article.content)
        ttsManager.play()
    }

    fun parseHighlights(json: String): List<Highlight> {
        return repository.parseHighlights(json)
    }

    fun showToast(message: String) {
        _uiState.value = _uiState.value.copy(showToastMessage = message)
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(showToastMessage = null)
    }

    private fun extractDomain(url: String): String {
        return try {
            val clean = url.removePrefix("https://").removePrefix("http://").removePrefix("www.")
            clean.substringBefore("/").substringBefore("?")
        } catch (e: Exception) {
            "web.page"
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
