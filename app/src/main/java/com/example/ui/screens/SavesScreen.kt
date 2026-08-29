package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ArticleEntity
import com.example.ui.components.ArticleCard
import com.example.ui.theme.PocketCoral
import com.example.viewmodel.SavesFilter

@Composable
fun SavesScreen(
    articles: List<ArticleEntity>,
    currentFilter: SavesFilter,
    onFilterChange: (SavesFilter) -> Unit,
    isGridView: Boolean,
    onArticleClick: (Long) -> Unit,
    onToggleFavorite: (ArticleEntity) -> Unit,
    onToggleArchive: (ArticleEntity) -> Unit,
    onDeleteArticle: (Long) -> Unit,
    onPlayAudio: (ArticleEntity) -> Unit,
    onTagClick: (String) -> Unit,
    onAddUrlClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Filter Chips Bar (All, Articles, Videos, Favorites, Archive)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Pair(SavesFilter.ALL, "All Items"),
                    Pair(SavesFilter.ARTICLES, "Articles"),
                    Pair(SavesFilter.VIDEOS, "Videos"),
                    Pair(SavesFilter.FAVORITES, "Favorites"),
                    Pair(SavesFilter.ARCHIVE, "Archive")
                ).forEach { (filter, label) ->
                    val isSelected = currentFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterChange(filter) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                when (filter) {
                                    SavesFilter.ALL -> Icon(Icons.Default.MenuBook, null, modifier = Modifier.size(16.dp))
                                    SavesFilter.ARTICLES -> Icon(Icons.Default.BookmarkBorder, null, modifier = Modifier.size(16.dp))
                                    SavesFilter.VIDEOS -> Icon(Icons.Default.VideoLibrary, null, modifier = Modifier.size(16.dp))
                                    SavesFilter.FAVORITES -> Icon(Icons.Default.Favorite, null, modifier = Modifier.size(16.dp))
                                    SavesFilter.ARCHIVE -> Icon(Icons.Default.Archive, null, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PocketCoral.copy(alpha = 0.12f),
                            selectedLabelColor = PocketCoral,
                            selectedLeadingIconColor = PocketCoral
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("filter_chip_${filter.name.lowercase()}")
                    )
                }
            }

            // Article List / Grid or Empty State
            if (articles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(PocketCoral.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (currentFilter) {
                                    SavesFilter.FAVORITES -> Icons.Default.Favorite
                                    SavesFilter.ARCHIVE -> Icons.Default.Archive
                                    else -> Icons.Default.Search
                                },
                                contentDescription = null,
                                tint = PocketCoral,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = when (currentFilter) {
                                SavesFilter.FAVORITES -> "No favorite articles yet"
                                SavesFilter.ARCHIVE -> "Your archive is empty"
                                SavesFilter.VIDEOS -> "No saved videos"
                                else -> "No saved items found"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = when (currentFilter) {
                                SavesFilter.FAVORITES -> "Tap the heart icon on any article to save it to your favorites."
                                SavesFilter.ARCHIVE -> "Articles you finish reading can be archived to keep your list organized."
                                else -> "Tap the '+' button below to paste a URL or explore trending reads in Discover."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                if (isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(articles, key = { it.id }) { article ->
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article.id) },
                                onToggleFavorite = { onToggleFavorite(article) },
                                onToggleArchive = { onToggleArchive(article) },
                                onDelete = { onDeleteArticle(article.id) },
                                onPlayAudio = { onPlayAudio(article) },
                                onTagClick = onTagClick,
                                isGrid = true
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(articles, key = { it.id }) { article ->
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article.id) },
                                onToggleFavorite = { onToggleFavorite(article) },
                                onToggleArchive = { onToggleArchive(article) },
                                onDelete = { onDeleteArticle(article.id) },
                                onPlayAudio = { onPlayAudio(article) },
                                onTagClick = onTagClick,
                                isGrid = false
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button to Add / Save URL
        FloatingActionButton(
            onClick = onAddUrlClick,
            containerColor = PocketCoral,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .testTag("fab_add_url")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Save URL",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
