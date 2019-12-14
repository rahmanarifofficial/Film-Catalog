package com.rahmanarifofficial.filmcatalog;

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rahmanarifofficial.filmcatalog.viewmodel.FilmViewModel
import kotlinx.android.synthetic.main.activity_detail_film.*

class DetailFilmActivity : AppCompatActivity() {

    private lateinit var vm: FilmViewModel
    private var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
        init()
    }

    private fun init() {
        initObject()
        initUI()
    }

    private fun initObject() {
        vm = ViewModelProvider(this).get(FilmViewModel::class.java)
        id = intent.getStringExtra(Constant.ID_FILM) ?: ""
    }

    private fun initUI() {
        setSupportActionBar(toolbar_detail_film);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        vm.getDetailFilm(id).observe(this, Observer {
            Glide.with(this)
                .load(it.poster)
                .apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_video))
                .into(ivPosterFilm)
            collapsing_toolbar_detail_film?.title = it.title
            tvTitleFilm?.text = it.title
            tvTypeFilm?.text = "${it.genre} -  ${it.runtime}"
            tvYearFilm?.text = "${it.year}  ${it.rating}"
            tvPlotFilm?.text = it.plot
            tvProductionFilm?.text = it.production
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}