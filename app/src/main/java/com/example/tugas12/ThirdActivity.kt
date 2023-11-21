package com.example.tugas12

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pertemuan12.Film
import com.example.pertemuan12.FilmDao
import com.example.pertemuan12.FilmRoomDatabase
import com.example.tugas12.databinding.ActivitySecondBinding
import com.example.tugas12.databinding.ActivityThirdBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ThirdActivity : AppCompatActivity() {
    private lateinit var mFilmDao: FilmDao
    private lateinit var executorService: ExecutorService
    private var updateId : Int = 0
    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "AnimeList"
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = FilmRoomDatabase.getDatabase(this)
        mFilmDao = db!!.filmDao()!!
        val title = intent.getStringExtra("judulFilm")
        val url = intent.getStringExtra("urlFilm")
        val rating = intent.getStringExtra("ratingFilm")
        val tag = intent.getStringExtra("tagFilm")
        val id = intent.getIntExtra("idFilm", 0)
        Log.d("ThirdActivity", "Received Intent Data - Title: $title, URL: $url, Rating: $rating, Tag: $tag, ID: $id")
        with(binding){
            updateId = id
            edtJudul.setText(title)
            edtRating.setText(rating)
            edtTag.setText(tag)
            imgCover.setText(url)
            btnUpdate.setOnClickListener {
                update(Film(url = imgCover.text.toString(), id = updateId, title = edtJudul.text.toString(), rating = edtRating.text.toString(), tag = edtTag.text.toString()))
                updateId = 0
                finish()
            }
        }
    }

    private fun update(film: Film){
        executorService.execute {
            mFilmDao.update(film)
        }
    }
}