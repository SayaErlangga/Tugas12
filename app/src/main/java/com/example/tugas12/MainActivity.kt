package com.example.tugas12

import FilmAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pertemuan12.Film
import com.example.pertemuan12.FilmDao
import com.example.pertemuan12.FilmRoomDatabase
import com.example.tugas12.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var mFilmDao: FilmDao
    private lateinit var executorService: ExecutorService
    private var updateId : Int = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var filmAdapter: FilmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "AnimeList"
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = FilmRoomDatabase.getDatabase(this)
        mFilmDao = db!!.filmDao()!!

        with(binding){
            btnAdd.setOnClickListener {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivity(intent)
            }

            filmAdapter = FilmAdapter { selectedData ->
                // Handle item click here if needed
            }
            filmAdapter.setOnItemClickListener { selectedData ->
                showBottomSheetDialog(selectedData)
            }

            rvFilm.layoutManager = GridLayoutManager(this@MainActivity, 2)
            rvFilm.adapter = filmAdapter

            // Get data and update the adapter
            getAllNotes()
        }
    }
    private fun showBottomSheetDialog(selectedData: Film) {
        val bottomSheetFragment = BottomSheetFragment(
            editCallback = {
                // Intent to edit activity
                val intent = Intent(this@MainActivity, ThirdActivity::class.java)
                intent.putExtra("judulFilm", selectedData.title)
                intent.putExtra("urlFilm", selectedData.url)
                intent.putExtra("ratingFilm", selectedData.rating)
                intent.putExtra("tagFilm", selectedData.tag)
                intent.putExtra("idFilm", selectedData.id)
                startActivity(intent)
            },
            deleteCallback = {
                // Delete the film
                delete(selectedData)
            }
        )
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

    private fun getAllNotes() {
        mFilmDao.getAllFilms().observe(this) { films ->
            filmAdapter.submitList(films)
        }
    }

    private fun delete(film: Film){
        executorService.execute {
            mFilmDao.delete(film)
        }
    }
}