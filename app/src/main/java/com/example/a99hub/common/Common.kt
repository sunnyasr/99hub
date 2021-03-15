package com.example.a99hub.common

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.widget.*
import androidx.annotation.NonNull
import com.example.a99hub.R
import com.example.a99hub.eventBus.BetEvent
import org.greenrobot.eventbus.EventBus

class Common(context: Context) {
    private var context: Context

    init {
        this.context = context
    }

    @NonNull
    fun getLayoutParams(): TableRow.LayoutParams {
        val params: TableRow.LayoutParams = TableRow.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(1, 0, 0, 1)
        return params
    }

    @NonNull
    fun getTblLayoutParams(): LinearLayout.LayoutParams {
        return TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun getTextView(
        id: Int,
        title: String,
        color: Int,
        typeface: Int,
        bgColor: Int,
        bgStyle: Int,
        fontSize: Float,
        drawable: Int,
        gravity: Int,
        click: Int

    ): TextView {
        val tv = TextView(context)
        tv.id = id
        tv.text = title
        tv.textSize = fontSize
        if (drawable != 0)
            tv.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
        tv.setTextColor(color)
        tv.gravity = gravity
        tv.setPadding(20, 20, 20, 20)
        tv.setTypeface(Typeface.DEFAULT, typeface)
        tv.setBackgroundColor(bgColor)
        if (bgStyle != 0)
            tv.setBackgroundResource(bgStyle)
        tv.layoutParams = getLayoutParams()

        if (click >= 0)
            tv.setOnClickListener {

                EventBus.getDefault().postSticky(BetEvent(click))
            }
        return tv
    }


}