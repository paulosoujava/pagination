package br.com.study.view.characterslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.study.R
import br.com.study.extensions.load
import br.com.study.model.entity.Character
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterAdapter(): RecyclerView.Adapter<CharacterAdapter.VH>(){

    private val items=  mutableListOf<Character>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CharacterAdapter.VH {
        val view = LayoutInflater.from(parent.context). inflate(R.layout.item_character, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, pos: Int) {
        val character = items[pos]
        holder.txtName.text = character.name
        holder.imgThumnail.load( "${character.thumbnails.path}/standard_medium.${character.thumbnails.extension}")
    }

    override fun getItemCount(): Int = items.size

    class VH( itemView: View): RecyclerView.ViewHolder(itemView){
        val imgThumnail = itemView.imgThumbnail
        val txtName = itemView.txtName
    }

    fun add( character: Character ){
        items.add(character)
        notifyItemInserted(items.lastIndex)
    }
}
