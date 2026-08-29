package com.example.data

import com.example.model.ArticleEntity
import com.example.model.Highlight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class ArticleRepository(private val articleDao: ArticleDao) {

    val allArticles: Flow<List<ArticleEntity>> = articleDao.getAllArticles()
    val savedArticles: Flow<List<ArticleEntity>> = articleDao.getArticlesByArchived(false)
    val archivedArticles: Flow<List<ArticleEntity>> = articleDao.getArticlesByArchived(true)
    val favoriteArticles: Flow<List<ArticleEntity>> = articleDao.getFavoriteArticles()

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        if (articleDao.countArticles() == 0) {
            articleDao.insertAll(SampleArticles.getInitialArticles())
        }
    }

    fun getArticleById(id: Long): Flow<ArticleEntity?> {
        return articleDao.getArticleById(id)
    }

    suspend fun getArticleByIdDirect(id: Long): ArticleEntity? = withContext(Dispatchers.IO) {
        articleDao.getArticleByIdDirect(id)
    }

    suspend fun saveArticle(article: ArticleEntity): Long = withContext(Dispatchers.IO) {
        articleDao.insert(article)
    }

    suspend fun toggleFavorite(id: Long, current: Boolean) = withContext(Dispatchers.IO) {
        articleDao.updateFavorite(id, !current)
    }

    suspend fun toggleArchived(id: Long, current: Boolean) = withContext(Dispatchers.IO) {
        articleDao.updateArchived(id, !current)
    }

    suspend fun updateProgress(id: Long, progress: Float) = withContext(Dispatchers.IO) {
        articleDao.updateReadingProgress(id, progress)
    }

    suspend fun updateTags(id: Long, tags: String) = withContext(Dispatchers.IO) {
        articleDao.updateTags(id, tags)
    }

    suspend fun deleteArticle(id: Long) = withContext(Dispatchers.IO) {
        articleDao.deleteById(id)
    }

    suspend fun addHighlight(articleId: Long, text: String, note: String = "", colorHex: String = "#FFF59D", paragraphIndex: Int = -1) = withContext(Dispatchers.IO) {
        val article = articleDao.getArticleByIdDirect(articleId) ?: return@withContext
        val highlightsList = parseHighlights(article.highlights).toMutableList()
        val newHighlight = Highlight(
            id = System.currentTimeMillis().toString(),
            articleId = articleId,
            text = text,
            note = note,
            colorHex = colorHex,
            paragraphIndex = paragraphIndex,
            createdAt = System.currentTimeMillis()
        )
        highlightsList.add(newHighlight)
        articleDao.updateHighlights(articleId, serializeHighlights(highlightsList))
    }

    suspend fun removeHighlight(articleId: Long, highlightId: String) = withContext(Dispatchers.IO) {
        val article = articleDao.getArticleByIdDirect(articleId) ?: return@withContext
        val highlightsList = parseHighlights(article.highlights).filterNot { it.id == highlightId }
        articleDao.updateHighlights(articleId, serializeHighlights(highlightsList))
    }

    fun parseHighlights(jsonStr: String): List<Highlight> {
        if (jsonStr.isBlank() || jsonStr == "[]") return emptyList()
        return try {
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<Highlight>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Highlight(
                        id = obj.optString("id", System.currentTimeMillis().toString()),
                        articleId = obj.optLong("articleId", 0L),
                        text = obj.optString("text", ""),
                        note = obj.optString("note", ""),
                        colorHex = obj.optString("colorHex", "#FFF59D"),
                        paragraphIndex = obj.optInt("paragraphIndex", -1),
                        createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun serializeHighlights(list: List<Highlight>): String {
        val jsonArray = JSONArray()
        for (item in list) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("articleId", item.articleId)
                put("text", item.text)
                put("note", item.note)
                put("colorHex", item.colorHex)
                put("paragraphIndex", item.paragraphIndex)
                put("createdAt", item.createdAt)
            }
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }
}
