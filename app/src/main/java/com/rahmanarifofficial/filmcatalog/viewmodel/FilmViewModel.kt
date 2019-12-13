package com.rahmanarifofficial.filmcatalog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rahmanarifofficial.filmcatalog.repository.FilmRepository

class FilmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FilmRepository()
    fun getListFilm(keyword: String, page: Int) = repository.getListFilm(keyword, page)
}