package com.example.a99hub.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.R
import com.example.a99hub.common.Common
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.data.sharedprefrence.Token
import com.example.a99hub.databinding.FragmentInplayDetailBinding
import com.example.a99hub.eventBus.BetEvent
import com.example.a99hub.model.BetsModel
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject


class InplayDetailFragment : Fragment() {

    private var _binding: FragmentInplayDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var tb1: TableLayout
    private lateinit var tb2: TableLayout
    private lateinit var tb3: TableLayout
    private lateinit var tbBets: TableLayout
    private lateinit var tbSessionBets: TableLayout
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var betsList: ArrayList<BetsModel>
    private lateinit var sessionBetsList: ArrayList<BetsModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInplayDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        compositeDisposable = CompositeDisposable()
        setProgress()
        betsList = ArrayList()
        sessionBetsList = ArrayList()
        tb1 = binding.table1
        tb2 = binding.table2
        tb3 = binding.table3
        tbBets = binding.tbBets
        tbSessionBets = binding.tbSessionBets
        addHeaders()
        addHeaders2()
        addHeaders3()
        addData()
        addData2()
        addData3()

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        getBets(Token(requireContext()).getToken(), arguments?.getString("eventid").toString())

    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    fun getBets(token: String, eventID: String) {
        Log.d("bets_session", eventID)
        kProgressHUD.show()
        betsList.clear()
        sessionBetsList.clear()

        compositeDisposable.add(
            Api().getBets(token, eventID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->
                    kProgressHUD.dismiss()
                    if (Common(requireContext()).checkTokenExpiry(res?.string().toString())) {
                        lifecycleScope.launch {
                            Common(requireContext()).logout()
                        }
                    } else if (res?.string().toString().length > 51) {
                        val data = JSONObject(res?.string())
                        val tempBets: JSONObject = data.getJSONObject("0")
                        val tempSessionBets: JSONObject = data.getJSONObject("1")
                        val x: Iterator<*> = tempBets.keys()
                        val x1: Iterator<*> = tempSessionBets.keys()
                        val jsonSessionBetsArray = JSONArray()
                        val jsonBetsArray = JSONArray()
                        while (x.hasNext()) {
                            val key = x.next() as String
//                        Toast.makeText(context, key + " : " + eventID, Toast.LENGTH_LONG).show()
//                        if (key.contains(eventID)) {
//                            Toast.makeText(context, key + " : " + eventID, Toast.LENGTH_LONG).show()
//                        }
                            jsonBetsArray.put(tempBets[key])
                        }
                        while (x1.hasNext()) {
                            val key = x1.next() as String
                            jsonSessionBetsArray.put(tempSessionBets[key])
                        }

                        Log.d("bets_session", eventID)

                        val jsonBetArray =
                            jsonBetsArray.getJSONObject(0).getJSONArray("bets") as JSONArray
                        val jsonSBetsArray =
                            jsonSessionBetsArray.getJSONObject(0).getJSONArray("bets") as JSONArray
//                    Toast.makeText(context, jsonSBetsArray.toString(), Toast.LENGTH_LONG).show()
                        for (i in 1..jsonBetArray.length()) {
                            val jsonObject = jsonBetArray.getJSONObject(i - 1)
                            betsList.add(
                                BetsModel(
                                    jsonObject.getInt("notional_profit"),
                                    jsonObject.getString("ip"),
                                    "",
                                    jsonObject.getString("team"),
                                    "",
                                    jsonObject.getInt("notional_loss"),
                                    jsonObject.getInt("parent_id"),
                                    jsonObject.getInt("rate"),
                                    jsonObject.getString("action"),
                                    jsonObject.getString("created"),
                                    jsonObject.getString("amount"),
                                    jsonObject.getString("client_id"),
                                    jsonObject.getString("market_id"),
                                    jsonObject.getInt("ledger"),
                                    jsonObject.getInt("type"),
                                )
                            )
                        }

                        tbBets.removeAllViews()
                        addHeadersBets()
                        addRowsBets()

                        val name: String =
                            jsonSessionBetsArray.getJSONObject(0).getString("name")
                        for (i in 1..jsonSBetsArray.length()) {
                            val jsonObject = jsonSBetsArray.getJSONObject(i - 1)
                            sessionBetsList.add(
                                BetsModel(
                                    jsonObject.getInt("notional_profit"),
                                    jsonObject.getString("ip"),
                                    name,
                                    jsonObject.getString("team"),
                                    jsonObject.getString("size"),
                                    jsonObject.getInt("notional_loss"),
                                    jsonObject.getInt("parent_id"),
                                    jsonObject.getInt("rate"),
                                    jsonObject.getString("action"),
                                    jsonObject.getString("created"),
                                    jsonObject.getString("amount"),
                                    jsonObject.getString("client_id"),
                                    jsonObject.getString("market_id"),
                                    jsonObject.getInt("ledger"),
                                    jsonObject.getInt("type"),
                                )
                            )
                        }
                        tbSessionBets.removeAllViews()
                        addHeadersSessionBets()
                        addRowsSessionBets()


                    }
                }, {
                    kProgressHUD.dismiss()
                    Toast.makeText(context, "" + it.message, Toast.LENGTH_LONG).show()
                })
        )

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
                12f, 0, Gravity.CENTER, -1
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
                12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "KHAI\n",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0,
                12f, 0, Gravity.CENTER, -1
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
                12f, 0, Gravity.CENTER, -1
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
                12f, 0, Gravity.CENTER, -1
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
                12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "YES",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0, 12f, 0, Gravity.CENTER, -1
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
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "NOT",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "YES",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0, 12f, 0, Gravity.CENTER, -1
            )
        )


        tb3.addView(tr, Common(requireContext()).getTblLayoutParams())
    }

    fun addHeadersBets() {
        val tr = TableRow(context)
        tr.setLayoutParams(Common(requireContext()).getLayoutParams())
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Sr.",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Rate",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Amount",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Mode",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Team",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0, 12f, 0, Gravity.CENTER, -1
            )
        )



        tbBets.addView(tr, Common(requireContext()).getTblLayoutParams())
    }

    fun addHeadersSessionBets() {
        val tr = TableRow(context)
        tr.setLayoutParams(Common(requireContext()).getLayoutParams())
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Sr.",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Name",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Rate",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Amount",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Run",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Mode",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "Dec",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.red, 0, 12f, 0, Gravity.CENTER, -1
            )
        )



        tbSessionBets.addView(tr, Common(requireContext()).getTblLayoutParams())
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
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.RED,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.BLUE,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
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
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.RED,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.BLUE,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
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
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.BLUE,
                    Typeface.BOLD,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, 1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0.00",
                    Color.RED,
                    Typeface.BOLD,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, 0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "0",
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )

            tb1.addView(tr, Common(requireContext()).getTblLayoutParams())
        }
    }

    @SuppressLint("Range")
    fun addRowsBets() {
//        val numCompanies: Int = list.size()
        //
//        for (i in 0 until list.size()) {

        for (i in 1..betsList.size) {

            var bgColor = ""
            bgColor = if (i % 2 == 0) {
                "#FFFFFF"
            } else "#FFFFFF"
            val tr = TableRow(context)

            tr.setOrientation(TableRow.VERTICAL)
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    i.toString(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    betsList.get(i - 1).getRate().toString(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.RIGHT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    betsList.get(i - 1).getAmount(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.RIGHT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    betsList.get(i - 1).getAction(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    betsList.get(i - 1).getTeam(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )


            tbBets.addView(tr, Common(requireContext()).getTblLayoutParams())
        }
    }

    @SuppressLint("Range")
    fun addRowsSessionBets() {
//        val numCompanies: Int = list.size()
        //
//        for (i in 0 until list.size()) {

        for (i in 1..sessionBetsList.size) {

            var bgColor = ""
            bgColor = if (i % 2 == 0) {
                "#FFFFFF"
            } else "#FFFFFF"
            val tr = TableRow(context)

            tr.setOrientation(TableRow.VERTICAL)
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    i.toString(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    sessionBetsList.get(i - 1).getName(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    (sessionBetsList.get(i - 1).getSize().toInt() / 100).toString(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.RIGHT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    sessionBetsList.get(i - 1).getAmount(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.RIGHT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    sessionBetsList.get(i - 1).getRate().toString(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.RIGHT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    sessionBetsList.get(i - 1).getMode(),
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    "NO",
                    Color.BLACK,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, -1
                )
            )



            tbSessionBets.addView(tr, Common(requireContext()).getTblLayoutParams())
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClickBet(betEvent: BetEvent) {
        if (betEvent.betType == 0)
            Toast.makeText(context, "Clicked KHAI", Toast.LENGTH_LONG).show()
        if (betEvent.betType == 1)
            Toast.makeText(context, "Clicked LAGAI", Toast.LENGTH_LONG).show()
    }
}
