package com.example.a99hub.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.model.UGModel
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UGAdapter(private val context: Context?, private var arrayList: ArrayList<UGModel>) :
    RecyclerView.Adapter<UGAdapter.UGViewHolder>() {

    inner class UGViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team: TextView = itemView.findViewById(R.id.tv_team_name)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val declared: TextView = itemView.findViewById(R.id.tv_declared)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UGViewHolder {

        return UGViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_upcomgin_game_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UGViewHolder, position: Int) {
        val game = arrayList[position]
        holder.team.text = game.getLongName()
        holder.date.text =  game.getStartTime()
        holder.declared.text = "Declared : No"

        var time: Long = 0
        var formatter: DateFormat? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            formatter = SimpleDateFormat("MM-dd, HH:mm")
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var date: Date? = null
                date = formatter!!.parse(game.getStartTime())
                time = date!!.time
            }
            holder.date.text=getDate(time)?.getTime().toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }


    }

    override fun getItemCount(): Int = arrayList.size

    fun setData(arrayList: ArrayList<UGModel>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    private fun getDate(time: Long): Calendar? {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.timeInMillis = time
        return cal
    }

}