package com.example.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

data class TtsPlaybackState(
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false,
    val isInitialized: Boolean = false,
    val articleId: Long = 0,
    val articleTitle: String = "",
    val currentParagraphIndex: Int = 0,
    val totalParagraphs: Int = 0,
    val currentText: String = "",
    val speechRate: Float = 1.0f
)

class PocketTtsManager(context: Context) {

    private val _playbackState = MutableStateFlow(TtsPlaybackState())
    val playbackState: StateFlow<TtsPlaybackState> = _playbackState.asStateFlow()

    private var paragraphs: List<String> = emptyList()
    private var currentArticleId: Long = 0
    private var currentTitle: String = ""

    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                _playbackState.value = _playbackState.value.copy(isInitialized = true)
            }
        }

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _playbackState.value = _playbackState.value.copy(isPlaying = true, isPaused = false)
            }

            override fun onDone(utteranceId: String?) {
                val nextIndex = _playbackState.value.currentParagraphIndex + 1
                if (nextIndex < paragraphs.size && _playbackState.value.isPlaying) {
                    _playbackState.value = _playbackState.value.copy(
                        currentParagraphIndex = nextIndex,
                        currentText = paragraphs[nextIndex]
                    )
                    speakCurrentParagraph()
                } else {
                    _playbackState.value = _playbackState.value.copy(
                        isPlaying = false,
                        isPaused = false,
                        currentParagraphIndex = 0
                    )
                }
            }

            override fun onError(utteranceId: String?) {
                _playbackState.value = _playbackState.value.copy(isPlaying = false, isPaused = false)
            }
        })
    }

    fun loadArticle(articleId: Long, title: String, content: String) {
        // Split content into clean non-empty paragraphs
        val rawParagraphs = content.split("\n\n", "\n")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        paragraphs = if (rawParagraphs.isEmpty()) listOf(title) else listOf(title) + rawParagraphs
        currentArticleId = articleId
        currentTitle = title

        _playbackState.value = _playbackState.value.copy(
            articleId = articleId,
            articleTitle = title,
            currentParagraphIndex = 0,
            totalParagraphs = paragraphs.size,
            currentText = paragraphs.getOrNull(0) ?: ""
        )
    }

    fun play() {
        if (paragraphs.isEmpty()) return
        _playbackState.value = _playbackState.value.copy(isPlaying = true, isPaused = false)
        speakCurrentParagraph()
    }

    fun pause() {
        tts?.stop()
        _playbackState.value = _playbackState.value.copy(isPlaying = false, isPaused = true)
    }

    fun togglePlayPause() {
        if (_playbackState.value.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun stop() {
        tts?.stop()
        _playbackState.value = _playbackState.value.copy(
            isPlaying = false,
            isPaused = false,
            currentParagraphIndex = 0
        )
    }

    fun nextParagraph() {
        val nextIndex = _playbackState.value.currentParagraphIndex + 1
        if (nextIndex < paragraphs.size) {
            _playbackState.value = _playbackState.value.copy(
                currentParagraphIndex = nextIndex,
                currentText = paragraphs[nextIndex]
            )
            if (_playbackState.value.isPlaying) {
                speakCurrentParagraph()
            }
        } else {
            stop()
        }
    }

    fun previousParagraph() {
        val prevIndex = (_playbackState.value.currentParagraphIndex - 1).coerceAtLeast(0)
        _playbackState.value = _playbackState.value.copy(
            currentParagraphIndex = prevIndex,
            currentText = paragraphs[prevIndex]
        )
        if (_playbackState.value.isPlaying) {
            speakCurrentParagraph()
        }
    }

    fun setSpeechRate(rate: Float) {
        val clamped = rate.coerceIn(0.5f, 2.5f)
        tts?.setSpeechRate(clamped)
        _playbackState.value = _playbackState.value.copy(speechRate = clamped)
    }

    private fun speakCurrentParagraph() {
        val index = _playbackState.value.currentParagraphIndex
        val text = paragraphs.getOrNull(index) ?: return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utterance_$index")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
