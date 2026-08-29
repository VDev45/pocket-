package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatAlignJustify
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ReaderFontFamily
import com.example.model.ReaderLineSpacing
import com.example.model.ReaderSettings
import com.example.model.ReaderThemeMode
import com.example.ui.theme.PocketCoral
import com.example.ui.theme.ReaderBlackBg
import com.example.ui.theme.ReaderDarkBg
import com.example.ui.theme.ReaderPaperBg
import com.example.ui.theme.ReaderSepiaBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsSheet(
    settings: ReaderSettings,
    onSettingsChange: (ReaderSettings) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Display Options",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Reader Themes (Light, Sepia, Dark, Black)
            Text(
                text = "THEME",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val themes = listOf(
                    Triple(ReaderThemeMode.LIGHT, "Light", ReaderPaperBg),
                    Triple(ReaderThemeMode.SEPIA, "Sepia", ReaderSepiaBg),
                    Triple(ReaderThemeMode.DARK, "Dark", ReaderDarkBg),
                    Triple(ReaderThemeMode.BLACK, "Black", ReaderBlackBg)
                )

                themes.forEach { (mode, label, bgColor) ->
                    val isSelected = settings.themeMode == mode
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onSettingsChange(settings.copy(themeMode = mode)) }
                            .testTag("theme_btn_${mode.name.lowercase()}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(bgColor)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) PocketCoral else Color.Gray.copy(alpha = 0.4f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (mode == ReaderThemeMode.DARK || mode == ReaderThemeMode.BLACK) Color.White else PocketCoral,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) PocketCoral else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Font Size Controls
            Text(
                text = "FONT SIZE",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            val newSize = (settings.fontSizeSp - 2).coerceAtLeast(14)
                            onSettingsChange(settings.copy(fontSizeSp = newSize))
                        },
                        modifier = Modifier.testTag("font_size_decrease")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Smaller font",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = null,
                            tint = PocketCoral,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${settings.fontSizeSp} pt",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    IconButton(
                        onClick = {
                            val newSize = (settings.fontSizeSp + 2).coerceAtMost(28)
                            onSettingsChange(settings.copy(fontSizeSp = newSize))
                        },
                        modifier = Modifier.testTag("font_size_increase")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Larger font",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Typeface Selection (Serif, Sans, Mono)
            Text(
                text = "TYPEFACE",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Pair(ReaderFontFamily.SERIF, "Serif (Editorial)"),
                    Pair(ReaderFontFamily.SANS, "Sans-Serif"),
                    Pair(ReaderFontFamily.MONO, "Monospace")
                ).forEach { (font, label) ->
                    val isSelected = settings.fontFamily == font
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSettingsChange(settings.copy(fontFamily = font)) },
                        label = {
                            Text(
                                text = label,
                                fontFamily = when (font) {
                                    ReaderFontFamily.SERIF -> FontFamily.Serif
                                    ReaderFontFamily.SANS -> FontFamily.SansSerif
                                    ReaderFontFamily.MONO -> FontFamily.Monospace
                                },
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PocketCoral.copy(alpha = 0.15f),
                            selectedLabelColor = PocketCoral
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("font_family_${font.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Line Spacing & Alignment
            Text(
                text = "LINE SPACING & ALIGNMENT",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(
                    Pair(ReaderLineSpacing.COMPACT, "Compact"),
                    Pair(ReaderLineSpacing.NORMAL, "Normal"),
                    Pair(ReaderLineSpacing.RELAXED, "Relaxed")
                ).forEach { (spacing, label) ->
                    val isSelected = settings.lineSpacing == spacing
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSettingsChange(settings.copy(lineSpacing = spacing)) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PocketCoral.copy(alpha = 0.15f),
                            selectedLabelColor = PocketCoral
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Justify alignment toggle
                IconButton(
                    onClick = { onSettingsChange(settings.copy(justifyText = !settings.justifyText)) },
                    modifier = Modifier.testTag("text_justify_toggle")
                ) {
                    Icon(
                        imageVector = if (settings.justifyText) Icons.Default.FormatAlignJustify else Icons.Default.FormatAlignLeft,
                        contentDescription = "Text Alignment",
                        tint = if (settings.justifyText) PocketCoral else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
