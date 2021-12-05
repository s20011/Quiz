package com.example.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val choices: MutableList<String>)
    : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val choiceView: TextView = view.findViewById(R.id.choices)
        val row: ConstraintLayout = view.findViewById(R.id.row_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener {
            itemClickListener?.onItemClick(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.choiceView.text = choices[position]

    }

    override fun getItemCount() = choices.size

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder)
    }
}