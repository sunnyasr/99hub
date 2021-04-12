package com.example.a99hub.ui.termcondition

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.a99hub.R
import com.example.a99hub.databinding.ActivityTermConditionBinding


class TermConditionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermConditionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_layout)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val mTitle = toolbar.findViewById<View>(R.id.toolbar_title) as TextView
        mTitle.text = getString(R.string.term_con)

    }

}