package com.ismoil.vocabulary.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ismoil.vocabulary.room.entities.Word

@Dao //데이터 액세스 객체
interface WordDao {


    @Query("select * from word order by id desc")
    fun getAll(): List<Word>

    @Query("select * from word order by id desc limit 1")
    fun getLatestWord(): Word

    @Insert
    fun insert(word: Word)

    @Update
    fun update(word: Word)

    @Delete
    fun delete(word: Word)

    @Query("delete from word")
    fun deleteAll()

    @Query("select * from word where type = :type")
    fun getByType(type: String): List<Word>

    @Query("select * from word where text = :text")
    fun getByText(text: String): List<Word>
}