package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ArticleEntity
import com.example.model.Highlight
import com.example.model.ReaderFontFamily
import com.example.model.ReaderLineSpacing
import com.example.model.ReaderSettings
import com.example.model.ReaderThemeMode
import com.example.tts.TtsPlaybackState
import com.example.ui.components.ReaderSettingsSheet
import com.example.ui.components.TtsMiniPlayer
import com.example.ui.theme.PocketAmber
import com.example.ui.theme.PocketCoral
import com.example.ui.theme.PocketTeal
import com.example.ui.theme.ReaderBlackBg
import com.example.ui.theme.ReaderBlackText
import com.example.ui.theme.ReaderDarkBg
import com.example.ui.theme.ReaderDarkText
import com.example.ui.theme.ReaderPaperBg
import com.example.ui.theme.ReaderPaperText
import com.example.ui.theme.ReaderSepiaBg
import com.example.ui.theme.ReaderSepiaText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReaderScreen(
    article: ArticleEntity,
    readerSettings: ReaderSettings,
    onReaderSettingsChange: (ReaderSettings) -> Unit,
    ttsState: TtsPlaybackState,
    highlights: List<Highlight>,
    onPlayTts: () -> Unit,
    onToggleTtsPlayPause: () -> Unit,
    onTtsNextParagraph: () -> Unit,
    onTtsPreviousParagraph: () -> Unit,
    onSetTtsSpeed: (Float) -> Unit,
    onCloseTts: () -> Unit,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleArchive: () -> Unit,
    onDeleteArticle: () -> Unit,
    onAddHighlight: (text: String, note: String, paragraphIndex: Int) -> Unit,
    onRemoveHighlight: (highlightId: String) -> Unit,
    onUpdateReadingProgress: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showAddNoteDialogForParagraph by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var noteInputText by remember { mutableStateOf("") }

    // Color tokens based on Reader Theme Mode
    val (backgroundColor, textColor, secondaryTextColor) = when (readerSettings.themeMode) {
        ReaderThemeMode.LIGHT -> Triple(ReaderPaperBg, ReaderPaperText, Color(0xFF555555))
        ReaderThemeMode.SEPIA -> Triple(ReaderSepiaBg, ReaderSepiaText, Color(0xFF6E5E4E))
        ReaderThemeMode.DARK -> Triple(ReaderDarkBg, ReaderDarkText, Color(0xFFAAAAAA))
        ReaderThemeMode.BLACK -> Triple(ReaderBlackBg, ReaderBlackText, Color(0xFF888888))
    }

    val selectedFontFamily = when (readerSettings.fontFamily) {
        ReaderFontFamily.SERIF -> FontFamily.Serif
        ReaderFontFamily.SANS -> FontFamily.SansSerif
        ReaderFontFamily.MONO -> FontFamily.Monospace
    }

    val lineHeightSp = when (readerSettings.lineSpacing) {
        ReaderLineSpacing.COMPACT -> (readerSettings.fontSizeSp * 1.35f).sp
        ReaderLineSpacing.NORMAL -> (readerSettings.fontSizeSp * 1.6f).sp
        ReaderLineSpacing.RELAXED -> (readerSettings.fontSizeSp * 1.9f).sp
    }

    // Split article content into paragraphs
    val paragraphs = remember(article.content) {
        article.content.split("\n\n")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

    // Reading progress calculation
    val scrollProgress by remember {
        derivedStateOf {
            if (paragraphs.isEmpty()) 1f
            else {
                val totalItems = paragraphs.size + 2
                val currentItem = listState.firstVisibleItemIndex + 1
                (currentItem.toFloat() / totalItems.toFloat()).coerceIn(0f, 1f)
            }
        }
    }

    LaunchedEffect(scrollProgress) {
        if (scrollProgress > article.readingProgress) {
            onUpdateReadingProgress(scrollProgress)
        }
    }

    // Map highlights to find by text or paragraph index
    val highlightByParagraph = remember(highlights) {
        highlights.associateBy { it.paragraphIndex }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = article.domain,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                        maxLines = 1,
                        color = textColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("reader_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                },
                actions = {
                    // TTS Listen button
                    IconButton(
                        onClick = onPlayTts,
                        modifier = Modifier.testTag("reader_tts_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = "Listen to Article",
                            tint = if (ttsState.isPlaying) PocketTeal else textColor
                        )
                    }

                    // Display Options button
                    IconButton(
                        onClick = { showSettingsSheet = true },
                        modifier = Modifier.testTag("reader_display_options_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Display Settings",
                            tint = textColor
                        )
                    }

                    // Favorite button
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.testTag("reader_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (article.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (article.isFavorite) PocketCoral else textColor
                        )
                    }

                    // Archive button
                    IconButton(
                        onClick = onToggleArchive,
                        modifier = Modifier.testTag("reader_archive_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Archive,
                            contentDescription = "Archive",
                            tint = if (article.isArchived) PocketTeal else textColor
                        )
                    }

                    // Overflow Menu
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = textColor
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Share Article") },
                                leadingIcon = { Icon(Icons.Default.Share, null) },
                                onClick = {
                                    showMenu = false
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "${article.title}\n${article.url}")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Article"))
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Open in Browser") },
                                leadingIcon = { Icon(Icons.Default.OpenInBrowser, null) },
                                onClick = {
                                    showMenu = false
                                    if (article.url.startsWith("http")) {
                                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                        context.startActivity(browserIntent)
                                    }
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Delete Article", color = MaterialTheme.colorScheme.error) },
                                leadingIcon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    showMenu = false
                                    onDeleteArticle()
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor,
        modifier = modifier.fillMaxSize().testTag("reader_screen")
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Reading Progress Line
                LinearProgressIndicator(
                    progress = { scrollProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.5.dp),
                    color = PocketCoral,
                    trackColor = backgroundColor
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Article Title
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = selectedFontFamily,
                                lineHeight = 36.sp
                            ),
                            color = textColor,
                            modifier = Modifier.testTag("reader_article_title")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Author & Meta Info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "By ${article.author}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = textColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "• ${article.timeToReadMinutes} min read",
                                style = MaterialTheme.typography.bodySmall,
                                color = secondaryTextColor
                            )
                        }

                        // Tags flow
                        if (article.tags.isNotEmpty()) {
                            val tagList = article.tags.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            if (tagList.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    tagList.forEach { tag ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = PocketCoral.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = "#$tag",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = PocketCoral,
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Hero Image if available
                        if (article.thumbnailResId != null) {
                            Image(
                                painter = painterResource(id = article.thumbnailResId),
                                contentDescription = article.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // Paragraphs with Distraction-Free Typography & Highlights
                    itemsIndexed(paragraphs) { index, paragraph ->
                        val highlight = highlightByParagraph[index] ?: highlights.find { it.text == paragraph }
                        val isTtsCurrent = (ttsState.isPlaying || ttsState.isPaused) &&
                                ttsState.articleId == article.id &&
                                ttsState.currentParagraphIndex == index

                        val isHighlighted = highlight != null

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        isTtsCurrent -> PocketTeal.copy(alpha = 0.18f)
                                        isHighlighted -> PocketAmber.copy(alpha = 0.22f)
                                        else -> Color.Transparent
                                    }
                                )
                                .then(
                                    if (isTtsCurrent) Modifier.border(1.5.dp, PocketTeal, RoundedCornerShape(8.dp))
                                    else if (isHighlighted) Modifier.border(1.dp, PocketAmber, RoundedCornerShape(8.dp))
                                    else Modifier
                                )
                                .clickable {
                                    // Tap paragraph to toggle highlight / add note
                                    if (highlight != null) {
                                        onRemoveHighlight(highlight.id)
                                    } else {
                                        showAddNoteDialogForParagraph = Pair(index, paragraph)
                                        noteInputText = ""
                                    }
                                }
                                .padding(horizontal = if (isTtsCurrent || isHighlighted) 12.dp else 0.dp, vertical = 6.dp)
                                .testTag("reader_paragraph_$index")
                        ) {
                            Column {
                                if (isTtsCurrent) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Headphones,
                                            contentDescription = null,
                                            tint = PocketTeal,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "NOW PLAYING",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = PocketTeal,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp,
                                                letterSpacing = 1.sp
                                            )
                                        )
                                    }
                                }

                                if (isHighlighted) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FormatQuote,
                                            contentDescription = null,
                                            tint = PocketAmber,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "HIGHLIGHTED PASSAGE (TAP TO REMOVE)",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = PocketAmber,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = paragraph,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = readerSettings.fontSizeSp.sp,
                                        lineHeight = lineHeightSp,
                                        fontFamily = selectedFontFamily,
                                        textAlign = if (readerSettings.justifyText) TextAlign.Justify else TextAlign.Start
                                    ),
                                    color = textColor
                                )

                                if (highlight?.note?.isNotBlank() == true) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(PocketAmber.copy(alpha = 0.25f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Note: ${highlight.note}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontStyle = FontStyle.Italic
                                            ),
                                            color = textColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Article Finish Footer
                    item {
                        Spacer(modifier = Modifier.height(36.dp))
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = PocketTeal,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (article.isArchived) "Article is Archived" else "You've finished this story!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = selectedFontFamily
                                    ),
                                    color = textColor
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Button(
                                        onClick = onToggleArchive,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (article.isArchived) PocketTeal else PocketCoral
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.testTag("reader_finish_archive_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Archive,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(if (article.isArchived) "In Archive" else "Archive Article")
                                    }

                                    TextButton(onClick = onBack) {
                                        Text("Back to Saves", color = textColor)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Floating TTS Mini Player if listening
            if (ttsState.isPlaying || ttsState.isPaused) {
                TtsMiniPlayer(
                    ttsState = ttsState,
                    onTogglePlayPause = onToggleTtsPlayPause,
                    onNextParagraph = onTtsNextParagraph,
                    onPreviousParagraph = onTtsPreviousParagraph,
                    onSetSpeed = onSetTtsSpeed,
                    onClose = onCloseTts,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }

    // Display Options Sheet
    if (showSettingsSheet) {
        ReaderSettingsSheet(
            settings = readerSettings,
            onSettingsChange = onReaderSettingsChange,
            onDismiss = { showSettingsSheet = false }
        )
    }

    // Add Highlight & Note Dialog
    showAddNoteDialogForParagraph?.let { (pIndex, pText) ->
        AlertDialog(
            onDismissRequest = { showAddNoteDialogForParagraph = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Highlight,
                        contentDescription = null,
                        tint = PocketAmber
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Highlight Passage", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "“${pText.take(120)}...”",
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = noteInputText,
                        onValueChange = { noteInputText = it },
                        label = { Text("Add personal note (optional)") },
                        placeholder = { Text("Key insight, thought, or reference...") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PocketCoral,
                            focusedLabelColor = PocketCoral
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddHighlight(pText, noteInputText, pIndex)
                        showAddNoteDialogForParagraph = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PocketCoral),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Highlight")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddNoteDialogForParagraph = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
