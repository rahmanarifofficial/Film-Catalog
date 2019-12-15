package com.rahmanarifofficial.filmcatalog.db

data class FilmDB(
    val id: Long?, val kode: String?, val title: String?, val year: String?, val type: String,
    val poster: String?
) {
    companion object {
        const val TABLE_FILM = "TABLE_FILM"
        const val ID = "ID_"
        const val KODE = "KODE"
        const val TITLE = "TITILE"
        const val YEAR = "YEAR"
        const val TYPE = "TYPE"
        const val POSTER = "POSTER"
    }
}