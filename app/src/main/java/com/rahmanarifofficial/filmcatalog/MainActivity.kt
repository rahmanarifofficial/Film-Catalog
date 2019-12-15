package com.rahmanarifofficial.filmcatalog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rahmanarifofficial.filmcatalog.adapter.ListFilmAdapter
import com.rahmanarifofficial.filmcatalog.helper.EndlessOnScrollListener
import com.rahmanarifofficial.filmcatalog.model.Film
import com.rahmanarifofficial.filmcatalog.viewmodel.FilmViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    private lateinit var vm: FilmViewModel
    private lateinit var adapter: ListFilmAdapter
    private var itemList = ArrayList<Film>()

    //==== SEARCHING VARIABLE =====//
    private var q: String = ""
    private var lastTextEdit = 0L
    private val idleMin = 1000L
    private var alreadyQueried = false
    private var lastKeywords = ""

    private var keyword = "america"
    private var currentPage = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorites_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_to_favorite -> {
                startActivity<FavoritesActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
    }

    private fun initObject() {
        vm = ViewModelProvider(this).get(FilmViewModel::class.java)
        adapter = ListFilmAdapter(this, itemList) { item ->
            startActivity<DetailFilmActivity>(Constant.ID_FILM to item.imdbID)
        }
    }

    private fun initUI() {
        swipeLayout?.post {
            loadData()
        }
        rvListFilm?.layoutManager = LinearLayoutManager(this)
        rvListFilm?.adapter = adapter
        rvListFilm?.setHasFixedSize(true)
        rvListFilm?.itemAnimator = DefaultItemAnimator()
        rvListFilm?.addOnScrollListener(object : EndlessOnScrollListener() {
            override fun onLoadMore() {
                currentPage++
                loadData()
            }
        })
    }

    private fun eventUI() {
        swipeLayout?.setOnRefreshListener {
            refresh()
        }
        btnRefresh?.setOnClickListener {
            refresh()
        }
        //SEARCH KEYWORDS
        searchEdt?.addTextChangedListener {
            it?.let { d ->
                q = d.toString()
                if (q.isNotEmpty()) {
                    lastTextEdit = System.currentTimeMillis()
                    val inputFinishChecker = Runnable {
                        if (System.currentTimeMillis() > lastTextEdit + idleMin - 500) {
                            if (searchEdt != null) {
                                keyword = searchEdt.text.toString().trim()

                                if (keyword != lastKeywords && keyword.isNotEmpty()) {
                                    if (!alreadyQueried) {
                                        alreadyQueried = true
                                        lastKeywords = keyword
                                        vm.searchKeyword.value = keyword
                                    } else {
                                        alreadyQueried = false
                                    }
                                } else {
                                    alreadyQueried = false
                                }
                            }
                        } else {
                            alreadyQueried = false
                        }
                    }

                    Handler().postDelayed(inputFinishChecker, idleMin)
                    searchDrawable?.setImageResource(R.drawable.ic_close)
                } else {
                    vm.searchKeyword.value = "america"
                    searchDrawable?.setImageResource(R.drawable.ic_search)
                    alreadyQueried = false
                }
            }
        }

        //CLOSE SEARCH FORM
        searchDrawableLyt?.setOnClickListener {
            if (q.isNotEmpty()) {
                searchEdt?.setText("")
                searchEdt?.clearFocus()
                hideKeyboard()
            }
        }
    }

    private fun hideKeyboard() {
        try {
            val windowToken = currentFocus?.windowToken
            if (windowToken != null) {
                val inputManager =
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)

                inputManager.hideSoftInputFromWindow(
                    windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun refresh() {
        currentPage = 1
        itemList.clear()
        adapter.notifyDataSetChanged()
        loadData()
    }

    fun loadData() {
        swipeLayout?.isRefreshing = true
        shimmerLyt?.startShimmer()
        vm.getListFilm(currentPage).observe(this, Observer {
            it?.let { res ->
                if (res.response) {
                    res.search?.let { data ->
                        shimmerLyt?.stopShimmer()
                        shimmerLyt?.visibility = View.GONE
                        successLyt?.visibility = View.VISIBLE
                        swipeLayout?.isRefreshing = false
//                        itemList.clear()
                        itemList.addAll(data)
                        adapter.notifyDataSetChanged()
                        errorLyt?.visibility = View.GONE
                        rvListFilm?.visibility = View.VISIBLE
                    }
                } else {
                    shimmerLyt?.stopShimmer()
                    shimmerLyt?.visibility = View.GONE
                    successLyt?.visibility = View.VISIBLE
                    swipeLayout?.isRefreshing = false
                    tvError?.text = res.error
                    errorLyt?.visibility = View.VISIBLE
                    rvListFilm?.visibility = View.GONE
                } ?: apply {
                    shimmerLyt?.stopShimmer()
                    shimmerLyt?.visibility = View.GONE
                    successLyt?.visibility = View.VISIBLE
                    swipeLayout?.isRefreshing = false
                    tvError?.text = res.error
                    errorLyt?.visibility = View.VISIBLE
                    rvListFilm?.visibility = View.GONE
                }
            } ?: apply {
                shimmerLyt?.stopShimmer()
                shimmerLyt?.visibility = View.GONE
                successLyt?.visibility = View.VISIBLE
                swipeLayout?.isRefreshing = false
                errorLyt?.visibility = View.VISIBLE
                rvListFilm?.visibility = View.GONE
            }
        })
    }
}