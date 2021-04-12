package com.example.a99hub.ui.termcondition

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.a99hub.R
import com.example.a99hub.databinding.FragmentLoginBinding
import com.example.a99hub.databinding.FragmentTermBinding
import com.example.a99hub.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


class TermFragment : Fragment(R.layout.fragment_term) {
    private lateinit var binding: FragmentTermBinding
    private lateinit var myDialog: Dialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = FragmentTermBinding.bind(view)


        myDialog = Dialog(requireActivity())
        val html = requireActivity().assets.open("terms.html")
        val size = html.available()

        val buffer = ByteArray(size)
        html.read(buffer)
        html.close()

        val str = String(buffer)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.tvTermCond.text =
                Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
        } else binding.tvTermCond.text = Html.fromHtml(str)
//
        popup()

        binding.btnCnt.setOnClickListener {
            val intent = Intent(requireContext(), HomeActivity::class.java)
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
        myDialog.setCancelable(true)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.show()
    }


}