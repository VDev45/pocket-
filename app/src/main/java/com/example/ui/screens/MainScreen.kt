package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddUrlDialog
import com.example.ui.components.PocketTopBar
import com.example.ui.components.ReaderSettingsSheet
import com.example.ui.components.TagFilterDialog
import com.example.ui.components.TtsMiniPlayer
import com.example.ui.theme.PocketCoral
import com.example.viewmodel.PocketTab
import com.example.viewmodel.PocketViewModel

@Composable
fun MainScreen(
    viewModel: PocketViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val articles by viewModel.displayedArticles.collectAsStateWithLifecycle()
    val rawArticles by viewModel.rawArticles.collectAsStateWithLifecycle()
    val discoverArticles by viewModel.discoverArticles.collectAsStateWithLifecycle()
    val allTags by viewModel.allUniqueTags.collectAsStateWithLifecycle()
    val highlights by viewModel.allHighlights.collectAsStateWithLifecycle()
    val ttsState by viewModel.ttsState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    // Toast message handling
    LaunchedEffect(uiState.showToastMessage) {
        uiState.showToastMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearToast()
        }
    }

    val selectedArticle = rawArticles.find { it.id == uiState.selectedArticleId }

    if (selectedArticle != null) {
        // Reader Mode Full Screen
        val articleHighlights = highlights.filter { it.articleId == selectedArticle.id }

        ReaderScreen(
            article = selectedArticle,
            readerSettings = uiState.readerSettings,
            onReaderSettingsChange = {
                viewModel.updateReaderSettings(
                    fontSizeSp = it.fontSizeSp,
                    themeMode = it.themeMode,
                    fontFamily = it.fontFamily,
                    lineSpacing = it.lineSpacing,
                    justifyText = it.justifyText
                )
            },
            ttsState = ttsState,
            highlights = articleHighlights,
            onPlayTts = { viewModel.playArticleAudio(selectedArticle) },
            onToggleTtsPlayPause = { viewModel.ttsManager.togglePlayPause() },
            onTtsNextParagraph = { viewModel.ttsManager.nextParagraph() },
            onTtsPreviousParagraph = { viewModel.ttsManager.previousParagraph() },
            onSetTtsSpeed = { viewModel.ttsManager.setSpeechRate(it) },
            onCloseTts = { viewModel.ttsManager.stop() },
            onBack = { viewModel.closeArticle() },
            onToggleFavorite = { viewModel.toggleFavorite(selectedArticle) },
            onToggleArchive = { viewModel.toggleArchive(selectedArticle) },
            onDeleteArticle = { viewModel.deleteArticle(selectedArticle.id) },
            onAddHighlight = { text, note, paragraphIndex ->
                viewModel.addHighlight(selectedArticle.id, text, note, paragraphIndex)
            },
            onRemoveHighlight = { highlightId ->
                viewModel.removeHighlight(selectedArticle.id, highlightId)
            },
            onUpdateReadingProgress = { viewModel.updateProgress(selectedArticle.id, it) }
        )
    } else {
        // Primary App Navigation Flow
        Scaffold(
            topBar = {
                if (uiState.currentTab == PocketTab.SAVES) {
                    PocketTopBar(
                        title = "My List",
                        searchQuery = uiState.searchQuery,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        isGridView = uiState.isGridView,
                        onToggleGridView = { viewModel.toggleGridView() },
                        selectedTag = uiState.selectedTag,
                        onOpenTagFilter = { viewModel.setTagFilterDialogOpen(true) },
                        onClearTagFilter = { viewModel.setSelectedTag(null) },
                        sortOption = uiState.sortOption,
                        onSortOptionChange = { viewModel.setSortOption(it) }
                    )
                }
            },
            bottomBar = {
                Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 4.dp,
                        modifier = Modifier.testTag("main_bottom_nav")
                    ) {
                        NavigationBarItem(
                            selected = uiState.currentTab == PocketTab.SAVES,
                            onClick = { viewModel.selectTab(PocketTab.SAVES) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == PocketTab.SAVES) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "My List"
                                )
                            },
                            label = { Text("My List", fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PocketCoral,
                                selectedTextColor = PocketCoral,
                                indicatorColor = PocketCoral.copy(alpha = 0.12f)
                            ),
                            modifier = Modifier.testTag("tab_saves")
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == PocketTab.DISCOVER,
                            onClick = { viewModel.selectTab(PocketTab.DISCOVER) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == PocketTab.DISCOVER) Icons.Default.Explore else Icons.Outlined.Explore,
                                    contentDescription = "Discover"
                                )
                            },
                            label = { Text("Discover", fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PocketCoral,
                                selectedTextColor = PocketCoral,
                                indicatorColor = PocketCoral.copy(alpha = 0.12f)
                            ),
                            modifier = Modifier.testTag("tab_discover")
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == PocketTab.HIGHLIGHTS,
                            onClick = { viewModel.selectTab(PocketTab.HIGHLIGHTS) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == PocketTab.HIGHLIGHTS) Icons.Default.FormatQuote else Icons.Outlined.FormatQuote,
                                    contentDescription = "Highlights"
                                )
                            },
                            label = { Text("Highlights", fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PocketCoral,
                                selectedTextColor = PocketCoral,
                                indicatorColor = PocketCoral.copy(alpha = 0.12f)
                            ),
                            modifier = Modifier.testTag("tab_highlights")
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == PocketTab.PROFILE,
                            onClick = { viewModel.selectTab(PocketTab.PROFILE) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == PocketTab.PROFILE) Icons.Default.Person else Icons.Outlined.Person,
                                    contentDescription = "Profile"
                                )
                            },
                            label = { Text("Profile", fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PocketCoral,
                                selectedTextColor = PocketCoral,
                                indicatorColor = PocketCoral.copy(alpha = 0.12f)
                            ),
                            modifier = Modifier.testTag("tab_profile")
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = uiState.currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        PocketTab.SAVES -> SavesScreen(
                            articles = articles,
                            currentFilter = uiState.savesFilter,
                            onFilterChange = { viewModel.setSavesFilter(it) },
                            isGridView = uiState.isGridView,
                            onArticleClick = { viewModel.openArticle(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onToggleArchive = { viewModel.toggleArchive(it) },
                            onDeleteArticle = { viewModel.deleteArticle(it) },
                            onPlayAudio = { viewModel.playArticleAudio(it) },
                            onTagClick = { viewModel.setSelectedTag(it) },
                            onAddUrlClick = { viewModel.setAddUrlDialogOpen(true) }
                        )

                        PocketTab.DISCOVER -> DiscoverScreen(
                            discoverArticles = discoverArticles,
                            onSaveToPocket = { viewModel.saveDiscoverArticle(it) }
                        )

                        PocketTab.HIGHLIGHTS -> HighlightsScreen(
                            highlights = highlights,
                            articles = rawArticles,
                            onOpenArticle = { viewModel.openArticle(it) },
                            onRemoveHighlight = { articleId, highlightId -> viewModel.removeHighlight(articleId, highlightId) }
                        )

                        PocketTab.PROFILE -> ProfileScreen(
                            articles = rawArticles,
                            totalHighlights = highlights.size
                        )
                    }
                }

                // Floating TTS Mini Player on main screen if active
                if (ttsState.isPlaying || ttsState.isPaused) {
                    TtsMiniPlayer(
                        ttsState = ttsState,
                        onTogglePlayPause = { viewModel.ttsManager.togglePlayPause() },
                        onNextParagraph = { viewModel.ttsManager.nextParagraph() },
                        onPreviousParagraph = { viewModel.ttsManager.previousParagraph() },
                        onSetSpeed = { viewModel.ttsManager.setSpeechRate(it) },
                        onClose = { viewModel.ttsManager.stop() }
                    )
                }
            }
        }

        // Add URL Bottom Sheet Dialog
        if (uiState.isAddUrlDialogOpen) {
            AddUrlDialog(
                onDismiss = { viewModel.setAddUrlDialogOpen(false) },
                onSaveUrl = { url, title, tags -> viewModel.saveUrl(url, title, tags) }
            )
        }

        // Tag Filter Bottom Sheet Dialog
        if (uiState.isTagFilterDialogOpen) {
            TagFilterDialog(
                allTags = allTags,
                selectedTag = uiState.selectedTag,
                onSelectTag = { viewModel.setSelectedTag(it) },
                onDismiss = { viewModel.setTagFilterDialogOpen(false) }
            )
        }
    }
}
