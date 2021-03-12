package com.example.a99hub.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.a99hub.R
import com.example.a99hub.common.Common
import com.example.a99hub.databinding.FragmentInplayDetailBinding


class InplayDetailFragment : Fragment() {

    private var _binding: FragmentInplayDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var tb1: TableLayout
    private lateinit var tb2: TableLayout
    private lateinit var tb3: TableLayout
    private lateinit var tb4: TableLayout
    private lateinit var tb5: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentInplayDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tb1 = binding.table1
        tb2 = binding.table2
        tb3 = binding.table3
        tb4 = binding.table4
        tb5 = binding.table5
        addHeaders()
        addHeaders2()
        addHeaders3()
        addHeaders4()
        addHeaders5()
        addData()
        addData2()
        addData3()

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    fun addHeaders() {
        val tr = TableRow(context)
        tr.setLayoutParams(Common(requireContext()).getLayoutParams())
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "TEAM\nMax-1,00,000  Min-1,000",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,
                16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "LAGAI\n",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,
                16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "KHAI\n",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,
                16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "POSITION\n",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,
                16f,0
            )
        )

        tb1.addView(tr, Common(requireContext()).getTblLayoutParams())
    }

    fun addHeaders2() {
        val tr = TableRow(context)
        tr.setLayoutParams(Common(requireContext()).getLayoutParams())
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "SESSION",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,
                16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "NOT",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,
                16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "YES",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,16f,0
            )
        )


        tb2.addView(tr, Common(requireContext()).getTblLayoutParams())
    }

    fun addHeaders3() {
        val tr = TableRow(context)
        tr.setLayoutParams(Common(requireContext()).getLayoutParams())
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "EXTRA SESSION",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "NOT",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "YES",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,16f,0
            )
        )


        tb3.addView(tr, Common(requireContext()).getTblLayoutParams())
    }

    fun addHeaders4() {
        val tr = TableRow(context)
        tr.setLayoutParams(Common(requireContext()).getLayoutParams())
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Sr.",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Rate",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Amount",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Mode",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Team",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,16f,0
            )
        )



        tb4.addView(tr, Common(requireContext()).getTblLayoutParams())
    }

    fun addHeaders5() {
        val tr = TableRow(context)
        tr.setLayoutParams(Common(requireContext()).getLayoutParams())
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Sr.",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "L Good all run",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Rate",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Amount",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Run",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Mode",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,16f,0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Dec",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,16f,0
            )
        )



        tb5.addView(tr, Common(requireContext()).getTblLayoutParams())
    }

    @SuppressLint("Range")
    fun addData2() {
//        val numCompanies: Int = list.size()
        //
//        for (i in 0 until list.size()) {
        for (i in 1..1) {
            var bgColor = ""
            bgColor = if (i % 2 == 0) {
                "#FFFFFF"
            } else "#FFFFFF"
            val tr = TableRow(context)

            tr.setOrientation(TableRow.VERTICAL)
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "35 over run SAW",
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.RED,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.BLUE,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )


            tb2.addView(tr, Common(requireContext()).getTblLayoutParams())
        }
    }

    @SuppressLint("Range")
    fun addData3() {
//        val numCompanies: Int = list.size()
        //
//        for (i in 0 until list.size()) {
        for (i in 1..2) {
            var bgColor = ""
            bgColor = if (i % 2 == 0) {
                "#FFFFFF"
            } else "#FFFFFF"
            val tr = TableRow(context)

            tr.setOrientation(TableRow.VERTICAL)
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "35 over run SAW",
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.RED,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.BLUE,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )


            tb3.addView(tr, Common(requireContext()).getTblLayoutParams())
        }
    }


    @SuppressLint("Range")
    fun addData() {
//        val numCompanies: Int = list.size()
        //
//        for (i in 0 until list.size()) {
        for (i in 1..2) {
            var bgColor = ""
            bgColor = if (i % 2 == 0) {
                "#FFFFFF"
            } else "#FFFFFF"
            val tr = TableRow(context)

            tr.setOrientation(TableRow.VERTICAL)
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "Indian Women",
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.BLUE,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.RED,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0",
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style,16f,0
                )
            )

            tb1.addView(tr, Common(requireContext()).getTblLayoutParams())
        }
    }

}