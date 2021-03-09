package com.example.a99hub.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.a99hub.R
import com.example.a99hub.common.Common
import com.example.a99hub.databinding.FragmentLedgerBinding
import org.json.JSONArray
import org.json.JSONException


class LedgerFragment : Fragment() {

    private var _binding: FragmentLedgerBinding? = null
    private val binding get() = _binding!!
    private lateinit var tl: TableLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLedgerBinding.inflate(layoutInflater, container, false)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        tl = binding.table
        addHeaders()
        addData()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


//    private fun getData() {
//        try {
//            val array: JSONArray =
//                JSONArray(AssetsJson().loadJSONFromAsset(context, "dossierSingle.json"))
//            val jsonObject = array.getJSONObject(0)
//            val familyArray = jsonObject.getJSONArray("family")
//            if (array.length() == 0) {
////                tvNoData.setVisibility(View.VISIBLE);
//                Toast.makeText(context, "No Image", Toast.LENGTH_SHORT).show()
//            } else {
////                tvNoData.setVisibility(View.GONE);
//                for (i in 0 until familyArray.length()) {
//                    val model = FamilyModel()
//                    val `object` = familyArray.getJSONObject(i)
//                    model.setLevel(`object`.getString("level"))
//                    model.setName(`object`.getString("name"))
//                    model.setFname(`object`.getString("fname"))
//                    model.setRelation(`object`.getString("relation"))
//                    val arrayPhoto = `object`.getJSONArray("dphoto")
//                    val photoModelList: MutableList<PhotoModel> = ArrayList()
//                    for (j in 0 until arrayPhoto.length()) {
//                        val photoModel = PhotoModel()
//                        val objectPhoto = arrayPhoto.getJSONObject(j)
//                        photoModel.setPhoto(objectPhoto.getString("photo"))
//                        photoModelList.add(photoModel)
//                    }
//                    model.setPhotoModelList(photoModelList)
//                    list.add(model)
//                }
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//    }


    fun addHeaders() {
        val tr = TableRow(context)
        tr.setLayoutParams(Common(requireContext()).getLayoutParams())
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "MATCH NAME.",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey,
                0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "WON BY",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey,
                0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "WON",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey, 0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "LOST",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey,
                0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "BALANCE",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey, 0
            )
        )
        tl.addView(tr, Common(requireContext()).getTblLayoutParams())
    }


    @SuppressLint("Range")
    fun addData() {
//        val numCompanies: Int = list.size()
        //
//        for (i in 0 until list.size()) {
        for (i in 1..10) {
            var bgColor = ""
            bgColor = if (i % 2 == 0) {
                "#FFFFFF"
            } else "#FFFFFF"
            val tr = TableRow(context)

            tr.setOrientation(TableRow.VERTICAL)
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "TeamA v TeamB (Jan 19,19:00PM)",
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "LION",
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0",
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0",
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0",
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style
                )
            )
            tl.addView(tr, Common(requireContext()).getTblLayoutParams())
        }
    }
}
