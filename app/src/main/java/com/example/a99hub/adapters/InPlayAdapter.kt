package com.example.a99hub.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.model.UGModel
import java.text.SimpleDateFormat
import java.util.*

class InPlayAdapter(private val context: Context?, private var arrayList: ArrayList<UGModel>) :
    RecyclerView.Adapter<InPlayAdapter.InPlayViewHolder>() {

    inner class InPlayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team: TextView = itemView.findViewById(R.id.tv_team_name)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val declared: TextView = itemView.findViewById(R.id.tv_declared)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InPlayViewHolder {

        return InPlayViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_in_play_layout, parent, false)
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: InPlayViewHolder, position: Int) {
        val game = arrayList[position]
        holder.team.text = game.getLongName()
        holder.date.text = game.getStartTime()
        holder.declared.text = StringBuilder().append( "Declared").append(" : ").append("NO")

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateFormat = SimpleDateFormat("hh:mm a")
        val date = format.parse(game.getStartTime())
        val time = dateFormat.format(date).toString()

        holder.date.text = StringBuilder().append(DateFormat.format("MMM", date))
            .append(" ")
            .append(DateFormat.format("dd", date))
            .append(", ")
            .append(time)


    }

    override fun getItemCount(): Int = arrayList.size

    fun setData(arrayList: ArrayList<UGModel>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }
}