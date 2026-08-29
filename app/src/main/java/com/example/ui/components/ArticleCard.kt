package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ArticleEntity
import com.example.ui.theme.PocketCoral
import com.example.ui.theme.PocketTeal

@Composable
fun ArticleCard(
    article: ArticleEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleArchive: () -> Unit,
    onDelete: () -> Unit,
    onPlayAudio: () -> Unit,
    onTagClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isGrid: Boolean = false
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("article_card_${article.id}")
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (isGrid) {
            // Grid Layout
            Column {
                if (article.thumbnailResId != null) {
                    Image(
                        painter = painterResource(id = article.thumbnailResId),
                        contentDescription = article.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(PocketCoral.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = PocketCoral.copy(alpha = 0.6f),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                if (article.readingProgress > 0f && article.readingProgress < 1f) {
                    LinearProgressIndicator(
                        progress = { article.readingProgress },
                        modifier = Modifier.fillMaxWidth().height(3.dp),
                        color = PocketCoral,
                        trackColor = PocketCoral.copy(alpha = 0.2f)
                    )
                }

                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${article.domain} • ${article.timeToReadMinutes}m",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier.size(28.dp).testTag("fav_btn_${article.id}")
                        ) {
                            Icon(
                                imageVector = if (article.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (article.isFavorite) PocketCoral else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // List Row Layout
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Left Text content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    ) {
                        // Domain & reading time metadata
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = article.domain,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${article.timeToReadMinutes} min read",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Title
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                lineHeight = 22.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Excerpt
                        Text(
                            text = article.excerpt,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Right Thumbnail Image
                    if (article.thumbnailResId != null) {
                        Image(
                            painter = painterResource(id = article.thumbnailResId),
                            contentDescription = article.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }

                // Progress indicator if reading has started
                if (article.readingProgress > 0f && article.readingProgress < 1f) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LinearProgressIndicator(
                            progress = { article.readingProgress },
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(CircleShape),
                            color = PocketCoral,
                            trackColor = PocketCoral.copy(alpha = 0.15f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${(article.readingProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PocketCoral,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom row: Tags and quick action icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Tag badges
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        article.tags.split(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                            .take(2)
                            .forEach { tag ->
                                Surface(
                                    shape = CircleShape,
                                    color = PocketTeal.copy(alpha = 0.1f),
                                    modifier = Modifier.clickable { onTagClick(tag) }
                                ) {
                                    Text(
                                        text = "#$tag",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = PocketTeal,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 11.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                    }

                    // Action buttons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Listen button
                        IconButton(
                            onClick = onPlayAudio,
                            modifier = Modifier.size(32.dp).testTag("listen_btn_${article.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = "Listen",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Favorite button
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier.size(32.dp).testTag("favorite_btn_${article.id}")
                        ) {
                            Icon(
                                imageVector = if (article.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (article.isFavorite) PocketCoral else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Archive button
                        IconButton(
                            onClick = onToggleArchive,
                            modifier = Modifier.size(32.dp).testTag("archive_btn_${article.id}")
                        ) {
                            Icon(
                                imageVector = if (article.isArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                                contentDescription = if (article.isArchived) "Unarchive" else "Archive",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // More overflow menu
                        Box {
                            IconButton(
                                onClick = { isMenuExpanded = true },
                                modifier = Modifier.size(32.dp).testTag("more_btn_${article.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Listen to Article") },
                                    leadingIcon = { Icon(Icons.Default.Headphones, contentDescription = null) },
                                    onClick = {
                                        onPlayAudio()
                                        isMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(if (article.isFavorite) "Remove from Favorites" else "Add to Favorites") },
                                    leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                                    onClick = {
                                        onToggleFavorite()
                                        isMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(if (article.isArchived) "Move to My List" else "Archive Article") },
                                    leadingIcon = { Icon(Icons.Default.Archive, contentDescription = null) },
                                    onClick = {
                                        onToggleArchive()
                                        isMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete Article", color = PocketCoral) },
                                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = PocketCoral) },
                                    onClick = {
                                        onDelete()
                                        isMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
