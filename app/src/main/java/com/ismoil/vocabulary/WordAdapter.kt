package com.ismoil.vocabulary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ismoil.vocabulary.databinding.ItemWordBinding
import com.ismoil.vocabulary.room.entities.Word

class WordAdapter( val list: MutableList<Word>,
                  private  val itemClickListener : itemOnClickListener?=null) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater= parent.context.getSystemService(LayoutInflater::class.java) as LayoutInflater
        val binding =  ItemWordBinding.inflate(inflater,parent,false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {

        var word = list[position]
        holder.onBind(word)
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(word)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class WordViewHolder(private val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(word: Word) {
            binding.apply {
                textTextView.text = word.text
                textMean.text = word.mean
                chip.text = word.type
            }
        }
    }

    interface itemOnClickListener{
        fun onClick(word: Word)
    }
}