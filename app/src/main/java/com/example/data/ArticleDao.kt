package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY savedAt DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE isArchived = :isArchived ORDER BY savedAt DESC")
    fun getArticlesByArchived(isArchived: Boolean): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE isFavorite = 1 AND isArchived = 0 ORDER BY savedAt DESC")
    fun getFavoriteArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticleById(id: Long): Flow<ArticleEntity?>

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getArticleByIdDirect(id: Long): ArticleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: ArticleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)

    @Update
    suspend fun update(article: ArticleEntity)

    @Query("DELETE FROM articles WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE articles SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE articles SET isArchived = :isArchived WHERE id = :id")
    suspend fun updateArchived(id: Long, isArchived: Boolean)

    @Query("UPDATE articles SET readingProgress = :progress WHERE id = :id")
    suspend fun updateReadingProgress(id: Long, progress: Float)

    @Query("UPDATE articles SET tags = :tags WHERE id = :id")
    suspend fun updateTags(id: Long, tags: String)

    @Query("UPDATE articles SET highlights = :highlightsJson WHERE id = :id")
    suspend fun updateHighlights(id: Long, highlightsJson: String)

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun countArticles(): Int
}
