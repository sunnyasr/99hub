package com.example.a99hub.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.data.network.Resource
import com.example.a99hub.eventBus.BetEvent
import com.example.a99hub.ui.auth.LoginFragment
import com.example.a99hub.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import org.json.JSONTokener

fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}


fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.toast(message: String, type: Int) {
    TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, type)
}

fun Fragment.logout() = lifecycleScope.launch {
    if (activity is HomeActivity) {
        (activity as HomeActivity).performLogout()
    }
}

fun Fragment.progress(yes: Boolean) {
    if (activity is HomeActivity) {
        (activity as HomeActivity).progress(yes)
    }
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> requireView().snackbar(
            "Please check your internet connection",
            retry
        )
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar("You've entered incorrect username or password")
            } else {
                logout()
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()

            requireView().snackbar(error)
        }
    }
}

fun checkJSONObject(str: String): Boolean {

    var result = false
    val json = JSONTokener(str).nextValue()
    result = json is JSONObject

    return result
}

fun checkTokenExpiry(str: String): Boolean {
    var result: Boolean = false
    if (str.length == 40) {
        result = true
    }
    return result
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

fun Fragment.getTextView(
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
    val tv = TextView(requireContext())
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


