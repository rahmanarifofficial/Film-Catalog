package com.rahmanarifofficial.filmcatalog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmanarifofficial.filmcatalog.adapter.ListFilmAdapter
import com.rahmanarifofficial.filmcatalog.model.Film
import com.rahmanarifofficial.filmcatalog.viewmodel.FilmViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var vm: FilmViewModel
    private lateinit var adapter: ListFilmAdapter
    private var itemList = ArrayList<Film>()

    private var keyword = "america"
    private var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        initObject()
        initUI()
        eventUI()
    }

    fun initObject() {
        vm = ViewModelProvider(this).get(FilmViewModel::class.java)
        adapter = ListFilmAdapter(this, itemList) {
            Toast.makeText(this, it.title, Toast.LENGTH_LONG).show()
        }
    }

    fun initUI() {
        swipeLayout?.post {
            loadData(keyword, page)
        }
        rvListFilm?.layoutManager = LinearLayoutManager(this)
        rvListFilm?.adapter = adapter
        rvListFilm?.setHasFixedSize(true)
        rvListFilm?.itemAnimator = DefaultItemAnimator()
    }

    fun eventUI() {
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
        loadData(keyword, page)
    }

    fun loadData(keyword: String, page: Int) {
        swipeLayout?.isRefreshing = true
        vm.getListFilm(keyword, page).observe(this, Observer {
            it?.let { res ->
                if (res.response) {
                    res.search?.let { data ->
                        swipeLayout?.isRefreshing = false
                        itemList.clear()
                        itemList.addAll(data)
                        adapter.notifyDataSetChanged()
                        errorLyt?.visibility = View.GONE
                        rvListFilm?.visibility = View.VISIBLE
                    }
                } else {
                    swipeLayout?.isRefreshing = false
                    errorLyt?.visibility = View.VISIBLE
                    rvListFilm?.visibility = View.GONE
                } ?: apply {
                    swipeLayout?.isRefreshing = false
                    errorLyt?.visibility = View.VISIBLE
                    rvListFilm?.visibility = View.GONE
                }
            } ?: apply {
                swipeLayout?.isRefreshing = false
                errorLyt?.visibility = View.VISIBLE
                rvListFilm?.visibility = View.GONE
            }
        })
    }
}
