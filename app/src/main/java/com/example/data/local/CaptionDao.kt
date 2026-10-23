package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CaptionDao {
    @Query("SELECT * FROM captions ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<CaptionEntity>>

    @Query("SELECT * FROM captions WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavorites(): Flow<List<CaptionEntity>>

    @Query("SELECT * FROM captions WHERE text LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR platform LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchCaptions(query: String): Flow<List<CaptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(caption: CaptionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(captions: List<CaptionEntity>)

    @Query("UPDATE captions SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE captions SET text = :newText WHERE id = :id")
    suspend fun updateText(id: String, newText: String)

    @Query("DELETE FROM captions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM captions")
    suspend fun clearAll()
}
