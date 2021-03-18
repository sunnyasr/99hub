package com.example.a99hub.common

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.widget.*
import androidx.annotation.NonNull
import com.example.a99hub.activities.LoginActivity
import com.example.a99hub.data.dataStore.LoginManager
import com.example.a99hub.eventBus.BetEvent
import com.sdsmdg.tastytoast.TastyToast
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class Common(context: Context) {
    private var context: Context

    val sessionError: String =
        "Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path \$"

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

    fun checkJSONObject(str: String): Boolean {

        var result: Boolean = false
        val json: Object = JSONTokener(str).nextValue() as Object;
        if (json is JSONObject)
            result = true
        else if (json is JSONArray)
            result = false

        return result
    }


    fun checkTokenExpiry(str: String): Boolean {
        var result: Boolean = false
        if (str.length == 40) {
            result = true
        }
        return result
    }

    suspend fun logout() {
        TastyToast.makeText(
            context,
            "Your session is expire",
            TastyToast.LENGTH_LONG,
            TastyToast.WARNING
        )

        LoginManager(context).setLogged(false)
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

    }

}