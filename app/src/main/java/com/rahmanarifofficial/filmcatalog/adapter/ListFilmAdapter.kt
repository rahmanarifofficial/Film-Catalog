package com.rahmanarifofficial.filmcatalog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rahmanarifofficial.filmcatalog.R
import com.rahmanarifofficial.filmcatalog.model.Film
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_film.*

class ListFilmAdapter(
    private val context: Context,
    private val itemList: List<Film>,
    private val onItemClick: ((Film) -> Unit)? = null
) : RecyclerView.Adapter<ListFilmAdapter.DefaultViewHolder>() {

    inner class DefaultViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindItem(
            item: Film,
            onItemClick: ((Film) -> Unit)? = null
        ) {
            Glide.with(context)
                .load(item.poster)
                .apply { RequestOptions().centerCrop().placeholder(R.drawable.ic_video) }
                .into(ivPosterFilm)
            tvTitleFilm.text = item.title
            tvTypeFilm.text = item.type
            tvYearFilm.text = item.year
            onItemClick?.let {
                containerView.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_film, parent, false)
        return DefaultViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        holder.bindItem(itemList[position], onItemClick)
    }

}