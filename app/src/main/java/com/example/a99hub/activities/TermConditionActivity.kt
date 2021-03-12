package com.example.a99hub.activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var myDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_layout)
        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val mTitle = toolbar.findViewById<View>(R.id.toolbar_title) as TextView
        mTitle.text = getString(R.string.term_con)
        myDialog = Dialog(this)

        val html = assets.open("terms.html")
        val size = html.available()

        val buffer = ByteArray(size)
        html.read(buffer)
        html.close()

        val str = String(buffer)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.tvTermCond.text =
                Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
        } else binding.tvTermCond.text = Html.fromHtml(str)

        popup()

        binding.btnCnt.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }


    private fun popup() {
        val txtclose: ImageButton

        myDialog.setContentView(R.layout.terms_popup)
        txtclose = myDialog.findViewById<View>(R.id.txtclose) as ImageButton

        txtclose.setOnClickListener {
            myDialog.dismiss()
        }
        //        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false)
        myDialog.setCanceledOnTouchOutside(false)
        myDialog.show()
    }
}