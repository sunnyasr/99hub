package com.example.a99hub.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.eventBus.InPLayEvent
import com.example.a99hub.model.UGModel
import com.example.a99hub.model.database.CompleteGame
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class CGAdapter(private val context: Context?, private var arrayList: ArrayList<CompleteGame>) :
    RecyclerView.Adapter<CGAdapter.InPlayViewHolder>() {

    inner class InPlayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team: TextView = itemView.findViewById(R.id.tv_team_name)
        val date: TextView = itemView.findViewById(R.id.tv_time)
        val cardView: CardView = itemView.findViewById(R.id.carviw_inplay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InPlayViewHolder {

        return InPlayViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_completed_game_layout, parent, false)
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: InPlayViewHolder, position: Int) {
        val game = arrayList[position]
        holder.team.text = game.long_name


        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateFormat = SimpleDateFormat("hh:mm a")
        val date = format.parse(game.start_time)
        val time = dateFormat.format(date).toString()

        holder.date.text = StringBuilder().append(DateFormat.format("MMM", date))
            .append(" ")
            .append(DateFormat.format("dd", date))
            .append(", ")
            .append(time)

        holder.cardView.setOnClickListener {
            EventBus.getDefault().postSticky(InPLayEvent(game))
        }

    }

    override fun getItemCount(): Int = arrayList.size

    fun setData(arrayList: ArrayList<CompleteGame>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }
}