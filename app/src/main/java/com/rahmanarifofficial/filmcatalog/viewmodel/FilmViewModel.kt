package com.rahmanarifofficial.filmcatalog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.rahmanarifofficial.filmcatalog.repository.FilmRepository

class FilmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FilmRepository()
    val searchKeyword = MutableLiveData<String>()

    init {
        this.searchKeyword.value = "america"
    }

    fun getListFilm(page: Int) = Transformations.switchMap(searchKeyword) {
        repository.getListFilm(it, page)
    }

    fun getDetailFilm(id: String) = repository.getDetailFilm(id)
}