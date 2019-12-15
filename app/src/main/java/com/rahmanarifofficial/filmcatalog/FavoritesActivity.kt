package com.rahmanarifofficial.filmcatalog

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmanarifofficial.filmcatalog.adapter.ListFilmDBAdapter
import com.rahmanarifofficial.filmcatalog.db.FilmDB
import com.rahmanarifofficial.filmcatalog.db.database
import kotlinx.android.synthetic.main.activity_favorites.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity

class FavoritesActivity : AppCompatActivity() {

    private var itemList = mutableListOf<FilmDB>()
    private lateinit var adapter: ListFilmDBAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        init()
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
    }

    private fun initObject() {
        adapter = ListFilmDBAdapter(this, itemList) { item ->
            startActivity<DetailFilmActivity>(Constant.ID_FILM to item.kode)
        }
    }

    private fun initUI() {
        supportActionBar?.title = "Film Favorite"
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        swipeLayout?.post {
            showFavorite()
        }
        rvListFilm?.layoutManager = LinearLayoutManager(this)
        rvListFilm?.adapter = adapter
        rvListFilm?.setHasFixedSize(true)
        rvListFilm?.itemAnimator = DefaultItemAnimator()
    }

    private fun eventUI() {
        swipeLayout?.setOnRefreshListener {
            refresh()
        }
        btnRefresh?.setOnClickListener {
            refresh()
        }
    }

    private fun refresh() {
        itemList.clear()
        adapter.notifyDataSetChanged()
        showFavorite()
    }

    private fun showFavorite() {
        swipeLayout?.isRefreshing = true
        database.use {
            val result = select(FilmDB.TABLE_FILM)
            val favorite = result.parseList(classParser<FilmDB>())
            if (favorite.isNotEmpty()) {
                swipeLayout?.isRefreshing = false
                errorLyt?.visibility = View.GONE
                rvListFilm.visibility = View.VISIBLE
                itemList.addAll(favorite)
                adapter.notifyDataSetChanged()
            } else {
                swipeLayout?.isRefreshing = false
                errorLyt?.visibility = View.VISIBLE
                rvListFilm?.visibility = View.GONE
            }
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
