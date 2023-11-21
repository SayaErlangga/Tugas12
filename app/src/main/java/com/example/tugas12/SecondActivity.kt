package com.example.tugas12

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pertemuan12.Film
import com.example.pertemuan12.FilmDao
import com.example.pertemuan12.FilmRoomDatabase
import com.example.tugas12.databinding.ActivitySecondBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SecondActivity : AppCompatActivity() {
    private lateinit var mFilmDao: FilmDao
    private lateinit var executorService: ExecutorService
    private var updateId : Int = 0
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "AnimeList"
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = FilmRoomDatabase.getDatabase(this)
        mFilmDao = db!!.filmDao()!!

        with(binding){
                btnInsert.setOnClickListener {
                    insert(Film(url = imgCover.text.toString(), title = edtJudul.text.toString(), rating = edtRating.text.toString(), tag = edtTag.text.toString()))
                    finish()
                }
            }
        }

    private fun insert(note: Film) {
        executorService.execute {
            mFilmDao.insert(note)
        }
    }
}