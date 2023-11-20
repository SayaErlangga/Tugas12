package com.example.pertemuan12

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(film: Film)

    @Update
    fun update(film: Film)

    @Delete
    fun delete(film: Film)

    @Query("SELECT * from film_table ORDER BY id ASC")
    fun getAllFilms(): LiveData<List<Film>>
}