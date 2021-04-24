package com.example.a99hub.ui.ugame_detail

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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.R
import com.example.a99hub.common.Common
import com.example.a99hub.data.sharedprefrence.Token
import com.example.a99hub.databinding.FragmentCGDetailsBinding
import com.example.a99hub.model.CGBetsModel
import com.example.a99hub.model.CGResultModel
import com.example.a99hub.data.network.Api
import com.example.a99hub.ui.utils.getLayoutParams
import com.example.a99hub.ui.utils.getTblLayoutParams
import com.example.a99hub.ui.utils.getTextView
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.stream.Collectors
import kotlin.math.roundToInt

@AndroidEntryPoint
class CGDetailsFragment : Fragment(R.layout.fragment_c_g_details) {

    private lateinit var binding: FragmentCGDetailsBinding

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var matchBetsList: ArrayList<CGBetsModel>
    private lateinit var sessionBetsList: ArrayList<CGBetsModel>
    private lateinit var sessionBetsResult: ArrayList<CGResultModel>
    private lateinit var tbBets: TableLayout
    private lateinit var tbSession: TableLayout
    private lateinit var tvMatchLossWin: TextView
    private lateinit var tvSessionLossWin: TextView
    private lateinit var tvMatchHead: TextView
    private lateinit var tvSessionHead: TextView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCGDetailsBinding.bind(view)

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        matchBetsList = ArrayList()
        sessionBetsList = ArrayList()
        sessionBetsResult = ArrayList()
        compositeDisposable = CompositeDisposable()
        tbBets = binding.tableBets
        tvMatchLossWin = binding.tvMatchBetsWl
        tvSessionLossWin = binding.tvMatchSessionWl
        tbSession = binding.tableSession
        tvMatchHead = binding.tvMatchBetsHead
        tvSessionHead = binding.tvSessionBetsHead

        binding.tvTeamFname.text =
            StringBuilder().append(arguments?.getString("eventid").toString()).append("\n")
                .append(arguments?.getString("long_name").toString())
                .append("\n")
                .append(arguments?.getString("start_time").toString())

        getBets(Token(requireContext()).getToken(), arguments?.getString("eventid").toString())
    }


    @SuppressLint("NewApi")
    fun getBets(token: String, eventID: String) {
        Log.d("bets_session", eventID)
        kProgressHUD.show()
        matchBetsList.clear()
        sessionBetsList.clear()
        compositeDisposable.add(
            Api().getCompletedBets(token, eventID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->
                    kProgressHUD.dismiss()
                    val data = JSONObject(res?.string())
                    if (Common(requireContext()).checkTokenExpiry(res?.string().toString())) {
                        lifecycleScope.launch {
                            Common(requireContext()).logout()
                        }
                    } else {
                        if (Common(requireContext()).checkJSONObject(data.getString("bets"))) {
                            val betsTemp: JSONObject = data.getJSONObject("bets")
                            val sessionResult: JSONObject =
                                data.getJSONObject("result").getJSONObject("0")
                            val betsResult: JSONObject =
                                data.getJSONObject("result").getJSONObject("1")
                            val x: Iterator<*> = betsTemp.keys()
                            val sessionX: Iterator<*> = sessionResult.keys()
                            val betsX: Iterator<*> = betsResult.keys()
                            val jsonBetsArray = JSONArray()
                            val betsResultArray = JSONArray()
                            val sessionResultArray = JSONArray()
                            while (x.hasNext()) {
                                val key = x.next() as String
                                jsonBetsArray.put(betsTemp[key])
                            }

                            while (sessionX.hasNext()) {
                                val key = sessionX.next() as String
                                sessionResultArray.put(sessionResult[key])
                            }

                            while (betsX.hasNext()) {
                                val key = betsX.next() as String
                                betsResultArray.put(betsResult[key])
                            }

                            if (jsonBetsArray.length() > 0) {
                                for (i in 1..jsonBetsArray.length()) {
                                    val jsonObject = jsonBetsArray.getJSONObject(i - 1)
                                    if (jsonObject.getString("bet_type").equals("1")) {
                                        matchBetsList.add(
                                            CGBetsModel(
                                                jsonObject.getString("bet_amount"),
                                                jsonObject.getString("bet_type"),
                                                jsonObject.getString("market_id"),
                                                "",
                                                jsonObject.getString("rate"),
                                                jsonObject.getString("team"),
                                                jsonObject.getString("action"),
                                                jsonObject.getString("created"),
                                                jsonObject.getString("client_id"),
                                                jsonObject.getString("transaction_reference"),
                                                jsonObject.getString("transaction_amount"),
                                                jsonObject.getString("transaction_type"),
                                                "",
                                                "",
                                                ""
                                            )
                                        )
                                    } else {
                                        sessionBetsList.add(
                                            CGBetsModel(
                                                jsonObject.getString("bet_amount"),
                                                jsonObject.getString("bet_type"),
                                                jsonObject.getString("market_id"),
                                                jsonObject.getString("size"),
                                                jsonObject.getString("rate"),
                                                jsonObject.getString("team"),
                                                jsonObject.getString("action"),
                                                jsonObject.getString("created"),
                                                jsonObject.getString("client_id"),
                                                jsonObject.getString("transaction_reference"),
                                                jsonObject.getString("transaction_amount"),
                                                jsonObject.getString("transaction_type"),
                                                "",
                                                "",
                                                ""
                                            )
                                        )
                                    }
                                }
                            }

                            /*BETS RESULT ARRAY*/
                            if (betsResultArray.length() > 0) {
                                for (i in 1..betsResultArray.length()) {
                                    val jsonObject = betsResultArray.getJSONObject(i - 1)
                                    sessionBetsResult.add(
                                        CGResultModel(
                                            "1",
                                            jsonObject.getString("result"),
                                            jsonObject.getString("market_id"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("start_time")
                                        )
                                    )

                                }
                            }

                            /*SESSION RESULT ARRAY*/
                            if (sessionResultArray.length() > 0) {
                                for (i in 1..sessionResultArray.length()) {
                                    val jsonObject = sessionResultArray.getJSONObject(i - 1)
                                    sessionBetsResult.add(
                                        CGResultModel(
                                            "0",
                                            jsonObject.getString("result"),
                                            jsonObject.getString("market_id"),
                                            jsonObject.getString("name"),
                                            ""
                                        )
                                    )

                                }
                            }

                            /*BETS MAPPING */
                            if (matchBetsList.size > 0) {
                                for (i in 1..matchBetsList.size) {
                                    val jsonObject = matchBetsList.get(i - 1)
                                    val filtered = sessionBetsResult.stream().filter {
                                        it.market_id.contains(jsonObject.market_id)
                                    }.collect(Collectors.toList())

                                    if (!filtered.isEmpty()) {
                                        if (filtered.get(0).bet_type.equals("1")) {
                                            matchBetsList.set(
                                                i - 1,
                                                CGBetsModel(
                                                    jsonObject.bet_amount,
                                                    jsonObject.bet_type,
                                                    jsonObject.market_id,
                                                    jsonObject.size,
                                                    jsonObject.rate,
                                                    jsonObject.team,
                                                    jsonObject.action,
                                                    jsonObject.created,
                                                    jsonObject.client_id,
                                                    jsonObject.transaction_reference,
                                                    jsonObject.transaction_amount,
                                                    jsonObject.transaction_type,
                                                    filtered.get(0).name,
                                                    filtered.get(0).result,
                                                    filtered.get(0).start_time,
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            /*SESSION MAPPING */
                            if (sessionBetsList.size > 0) {
                                for (i in 1..sessionBetsList.size) {
                                    val jsonObject = sessionBetsList.get(i - 1)
                                    val filtered = sessionBetsResult.stream().filter {

                                        it.market_id.contains(jsonObject.market_id)
                                    }.collect(Collectors.toList())

                                    Log.d("CGDetails", jsonObject.market_id)
                                    if (!filtered.isEmpty()) {
                                        if (filtered.get(0).bet_type.equals("0")) {
                                            sessionBetsList.set(
                                                i - 1,
                                                CGBetsModel(
                                                    jsonObject.bet_amount,
                                                    jsonObject.bet_type,
                                                    jsonObject.market_id,
                                                    jsonObject.size,
                                                    jsonObject.rate,
                                                    jsonObject.team,
                                                    jsonObject.action,
                                                    jsonObject.created,
                                                    jsonObject.client_id,
                                                    jsonObject.transaction_reference,
                                                    jsonObject.transaction_amount,
                                                    jsonObject.transaction_type,
                                                    filtered.get(0).name,
                                                    filtered.get(0).result,
                                                    "",
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            matchBetsList.sortBy {
                                it.name
                            }

                            sessionBetsList.sortBy {
                                it.name
                            }

                            Log.d("CGDetails", sessionBetsList.toString())
                            tbSession.removeAllViews()
                            tbBets.removeAllViews()

                            if (matchBetsList.size > 0) {
                                addHeaderBets()
                                addRowsBets()
                            }

                            if (sessionBetsList.size > 0) {
                                addHeaderSession()
                                addRowsSession()
                            }
                        } else Toast.makeText(context, "No Found Record", Toast.LENGTH_LONG).show()


                    }

                },
                    {
                        kProgressHUD.dismiss()
                        context?.let { ctx ->
                            Toast.makeText(ctx, "" + it.message, Toast.LENGTH_LONG).show()
                        }

                    })
        )
    }


    private fun addHeaderBets() {

        context?.let {
            val tr = TableRow(it)
            tr.layoutParams = getLayoutParams()
            tr.addView(
               getTextView(
                    0,
                    "Rate",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey,
                    0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
               getTextView(
                    0,
                    "Amount",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey,
                    0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
                getTextView(
                    0,
                    "Mode",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
               getTextView(
                    0,
                    "Team",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey,
                    0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
               getTextView(
                    0,
                    arguments?.getString("team1", "").toString(),
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
               getTextView(
                    0,
                    arguments?.getString("team2", "").toString(),
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tbBets.addView(tr,getTblLayoutParams())
            tvMatchHead.text = "Match Bet(s) Won By : ${matchBetsList.get(0).result}"
            tvMatchHead.visibility = View.VISIBLE
        }

    }

    @SuppressLint("Range")
    private fun addRowsBets() {

        context?.let {

            val teamOne: String = arguments?.getString("team1", "").toString().trim()
            val teamTwo: String = arguments?.getString("team2", "").toString().trim()
            var blc: Number
            var team1: Number
            var team2: Number
            team1 = 0
            team2 = 0
            blc = 0
            for (i in 1..matchBetsList.size) {
                val model = matchBetsList.get(i - 1)
                val betTeam = model.team.trim()
                val action = model.action.trim()
                val trans_amt = model.transaction_amount.toDouble().toInt()
                val bet_amount = model.bet_amount.toDouble().toInt()
                val rate = model.rate.toDouble() / 100

                if (model.transaction_type.equals("0")) {
                    blc -= model.transaction_amount.toDouble().toInt()
                } else {
                    blc += model.transaction_amount.toDouble().toInt()
                }

                if (action.equals("1")) {
                    if (betTeam.equals(teamOne)) {
                        team1 = (bet_amount * rate).roundToInt()
                        team2 = bet_amount * -1
                    }
                    if (betTeam.equals(teamTwo)) {
                        team1 = bet_amount * -1
                        team2 = (bet_amount * rate).roundToInt()
                    }
                }

                if (action.equals("0")) {
                    if (betTeam.equals(teamOne)) {
                        team1 = (bet_amount * rate).roundToInt() * -1
                        team2 = bet_amount * 1
                    }
                    if (betTeam.equals(teamTwo)) {
                        team1 = bet_amount * 1
                        team2 = (bet_amount * rate).roundToInt() * -1
                    }
                }

                val bgColor = "#FFFFFF"
                val tr = TableRow(context)

                tr.orientation = TableRow.VERTICAL
                tr.addView(
                  getTextView(
                        i,
                        (matchBetsList.get(i - 1).rate.toDouble() / 100).toDouble().toString(),
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.RIGHT, -1
                    )
                )
                tr.addView(
                   getTextView(
                        i,
                        matchBetsList.get(i - 1).bet_amount,
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor("#FF471A"),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.RIGHT, -1
                    )
                )
                tr.addView(
                    getTextView(
                        i,
                        if (matchBetsList.get(i - 1).action.equals("0")) "K" else "L",
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.CENTER, -1
                    )
                )
                tr.addView(
                    getTextView(
                        i,
                        StringBuilder().append(matchBetsList.get(i - 1).team)
                            .toString(),
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.CENTER, -1
                    )
                )

                tr.addView(
                   getTextView(
                        i,
                        team1.toInt().toString(),
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.CENTER, -1
                    )
                )

                tr.addView(
                    getTextView(
                        i,
                        team2.toString(),
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.RIGHT, -1
                    )
                )
                tbBets.addView(tr, getTblLayoutParams())
            }
            if (blc > 0) {
                tvMatchLossWin.text =
                    StringBuilder().append("You Won : ").append(blc)
                tvMatchLossWin.setTextColor(Color.parseColor("#2E7D32"))
            } else {
                tvMatchLossWin.text =
                    StringBuilder().append("You Lost : ").append(blc)
                tvMatchLossWin.setTextColor(Color.RED)
            }
            tvMatchLossWin.visibility = View.VISIBLE

        }
    }

    @SuppressLint("Range")
    private fun addRowsSession() {

        context?.let {
            var blc: Number
            blc = 0
            for (i in 1..sessionBetsList.size) {
                if (sessionBetsList.get(i - 1).transaction_type.equals("0")) {
                    blc -= sessionBetsList.get(i - 1).transaction_amount.toDouble().toInt()
                } else {
                    blc += sessionBetsList.get(i - 1).transaction_amount.toDouble().toInt()
                }

                val bgColor = "#FFFFFF"
                val tr = TableRow(context)

                tr.orientation = TableRow.VERTICAL
                tr.addView(
                 getTextView(
                        i,
                        StringBuilder().append(sessionBetsList.get(i - 1).name)
                            .toString(),
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.LEFT, -1
                    )
                )
                tr.addView(
                  getTextView(
                        i,
                        (sessionBetsList.get(i - 1).size.toDouble() / 100).toDouble().toString(),
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor("#FF471A"),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.RIGHT, -1
                    )
                )
                tr.addView(
                  getTextView(
                        i,
                        StringBuilder().append(sessionBetsList.get(i - 1).bet_amount)
                            .toString(),
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.RIGHT, -1
                    )
                )
                tr.addView(
                   getTextView(
                        i,
                        StringBuilder().append(sessionBetsList.get(i - 1).rate)
                            .toString(),
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.RIGHT, -1
                    )
                )

                tr.addView(
                  getTextView(
                        i,
                        if (sessionBetsList.get(i - 1).action.equals("0")) "NO" else "YES",
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.CENTER, -1
                    )
                )

                tr.addView(
                 getTextView(
                        i,
                        StringBuilder().append(sessionBetsList.get(i - 1).result)
                            .toString(),
                        Color.DKGRAY,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.RIGHT, -1
                    )
                )
                tbSession.addView(tr, getTblLayoutParams())
            }

            if (blc > 0) {
                tvSessionLossWin.text =
                    StringBuilder().append("You Won : ").append(blc)
                tvSessionLossWin.setTextColor(Color.parseColor("#2E7D32"))
            } else {
                tvSessionLossWin.text =
                    StringBuilder().append("You Lost : ").append(blc)
                tvSessionLossWin.setTextColor(Color.RED)
            }
            tvSessionLossWin.visibility = View.VISIBLE

        }
    }

    private fun addHeaderSession() {

        context?.let {
            val tr = TableRow(it)
            tr.layoutParams = getLayoutParams()
            tr.addView(
            getTextView(
                    0,
                    "Session",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey,
                    0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
                getTextView(
                    0,
                    "Rate",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey,
                    0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
                getTextView(
                    0,
                    "Amount",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
               getTextView(
                    0,
                    "Runs",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey,
                    0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
                getTextView(
                    0,
                    "Mode",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
                getTextView(
                    0,
                    "Dec",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tbSession.addView(tr, getTblLayoutParams())
            tvSessionHead.visibility = View.VISIBLE
        }

    }

}