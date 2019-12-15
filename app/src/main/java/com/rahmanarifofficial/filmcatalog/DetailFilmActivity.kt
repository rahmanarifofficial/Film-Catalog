package com.rahmanarifofficial.filmcatalog;

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rahmanarifofficial.filmcatalog.db.FilmDB
import com.rahmanarifofficial.filmcatalog.db.database
import com.rahmanarifofficial.filmcatalog.viewmodel.FilmViewModel
import kotlinx.android.synthetic.main.activity_detail_film.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast

class DetailFilmActivity : AppCompatActivity() {

    private lateinit var vm: FilmViewModel
    private var id = ""
    private var isFavorite = false

    //PARAMS DB
    private var title = ""
    private var year = ""
    private var type = ""
    private var poster = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
        init()
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
        favoriteState()
        setFavorite()
    }

    private fun initObject() {
        vm = ViewModelProvider(this).get(FilmViewModel::class.java)
        id = intent.getStringExtra(Constant.ID_FILM) ?: ""
    }

    private fun initUI() {
        setSupportActionBar(toolbar_detail_film);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        vm.getDetailFilm(id).observe(this, Observer {
            it?.let {
                if (it.response) {
                    collapsingToolbarDetailFilm?.visibility = View.VISIBLE
                    ivPosterFilm?.visibility = View.VISIBLE
                    successLyt?.visibility = View.VISIBLE
                    errorLyt?.visibility = View.GONE
                    Glide.with(this)
                        .load(it.poster)
                        .apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_video))
                        .into(ivPosterFilm)
                    collapsingToolbarDetailFilm?.title = it.title
                    tvTitleFilm?.text = it.title
                    tvTypeFilm?.text = it.genre
                    tvRuntimeFilm?.text = it.runtime
                    tvReleaseFilm?.text = it.released
                    tvRatingFilm?.text = it.rating
                    tvPlotFilm?.text = it.plot
                    tvProductionFilm?.text = it.production

                    title = it.title.toString()
                    year = it.year.toString()
                    poster = it.poster.toString()
                    type = it.type.toString()
                } else {
                    collapsingToolbarDetailFilm?.visibility = View.GONE
                    errorLyt?.visibility = View.VISIBLE
                    ivPosterFilm?.visibility = View.GONE
                    successLyt?.visibility = View.GONE
                    tvError?.text = it.error
                }
            } ?: apply {
                collapsingToolbarDetailFilm?.visibility = View.GONE
                errorLyt?.visibility = View.VISIBLE
                ivPosterFilm?.visibility = View.GONE
                successLyt?.visibility = View.GONE
            }
        })
    }

    private fun eventUI() {
        btnRefresh?.setOnClickListener {
            initUI()
        }
        btnFavorite?.setOnClickListener {
            if (isFavorite) {
                removeFromFavorite()
            } else {
                addToFavorite()
            }
            isFavorite = !isFavorite
            setFavorite()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            btnFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_full
                )
            )
        else
            btnFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite))
    }

    private fun favoriteState() {
        database.use {
            val result = select(FilmDB.TABLE_FILM).whereArgs(
                "(KODE = {kode})",
                "kode" to id
            )
            val favorite = result.parseList(classParser<FilmDB>())
            if (!favorite.isEmpty()) {
                isFavorite = true
            }

        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(
                    FilmDB.TABLE_FILM,
                    FilmDB.KODE to id,
                    FilmDB.TITLE to title,
                    FilmDB.YEAR to year,
                    FilmDB.TYPE to type,
                    FilmDB.POSTER to poster
                )
            }
            toast(getString(R.string.tambah_favorite))
        } catch (e: SQLiteException) {
            Log.d(Constant.TAG, e.message.toString())
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(FilmDB.TABLE_FILM, "(KODE = {kode})", "kode" to id)
            }
            toast(getString(R.string.hapus_favorite))
        } catch (e: SQLiteException) {
            Log.d(Constant.TAG, e.message.toString())
        }
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