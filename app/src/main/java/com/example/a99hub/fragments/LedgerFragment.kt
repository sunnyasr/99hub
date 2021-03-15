package com.example.a99hub.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.R
import com.example.a99hub.common.Common
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.databinding.FragmentLedgerBinding
import com.example.a99hub.eventBus.BetEvent
import com.example.a99hub.model.EventModel
import com.example.a99hub.model.LedgerModel
import com.example.a99hub.model.MatchMarketsModel
import com.example.a99hub.model.SessionMarketsModel
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class LedgerFragment : Fragment() {

    private var _binding: FragmentLedgerBinding? = null
    private val binding get() = _binding!!
    private lateinit var tl: TableLayout
    private lateinit var userManager: UserManager
    private lateinit var eventList: ArrayList<EventModel>
    private lateinit var matchMarketList: ArrayList<MatchMarketsModel>
    private lateinit var sessionMarketList: ArrayList<SessionMarketsModel>
    private lateinit var arrayList: ArrayList<LedgerModel>
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var balloon: Balloon

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLedgerBinding.inflate(layoutInflater, container, false)
        userManager = UserManager(requireContext())
        lifecycleScope.launch {
            userManager.token.asLiveData().observe(requireActivity(), {
                getData(it.toString())
            })
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setProgress()
        arrayList = ArrayList()
        matchMarketList = ArrayList()
        eventList = ArrayList()
        sessionMarketList = ArrayList()
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        tl = binding.table
//        tooltip()


    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    private fun getData(token: String) {
        kProgressHUD.show()
        arrayList.clear()
        matchMarketList.clear()
        eventList.clear()
        sessionMarketList.clear()
        Api.invoke().getLedger(token).enqueue(object : Callback<ResponseBody> {

            @SuppressLint("NewApi")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.code() == 200) {

                    val data = JSONObject(response.body()?.string())
                    val events: JSONObject = data.getJSONObject("events")
                    val session_markets: JSONObject = data.getJSONObject("session_markets")
                    val match_markets: JSONObject = data.getJSONObject("match_markets")

                    val x: Iterator<*> = events.keys()
                    val _x_session: Iterator<*> = session_markets.keys()
                    val _x_match: Iterator<*> = match_markets.keys()

                    val jsonEventArray = JSONArray()
                    val jsonSessionArray = JSONArray()
                    val jsonMatchArray = JSONArray()
                    val jsonSettlementArray = data.getJSONArray("settlement_list")

                    while (x.hasNext()) {
                        val key = x.next() as String
                        jsonEventArray.put(events[key])
                    }
                    while (_x_session.hasNext()) {
                        val key = _x_session.next() as String
                        jsonSessionArray.put(session_markets[key])
                    }
                    while (_x_match.hasNext()) {
                        val key = _x_match.next() as String
                        jsonMatchArray.put(match_markets[key])
                    }

                    for (s in 1..jsonSettlementArray.length()) {
                        val settelment = jsonSettlementArray.getJSONObject(s - 1)
                        var lost: String = "0"
                        var won: String = "0"
                        if (settelment.getString("type").equals("1")) {
                            won = settelment.getString("amount").replace("-", "").toDouble().toInt()
                                .toString()
                        } else
                            lost =
                                settelment.getString("amount").replace("-", "").toDouble().toInt()
                                    .toString()

                        arrayList.add(
                            LedgerModel(
                                "0",
                                "0",
                                "settlement " + settelment.getString("remark"),
                                "settlement " + settelment.getString("remark"),
                                "",
                                settelment.getString("created"),
                                settelment.getString("amount").replace("-", ""),

                                lost,
                                won,
                                settelment.getString("amount").replace("-", "")
                            )
                        )
                    }



                    for (i in 1..jsonEventArray.length()) {
                        val jsonObject = jsonEventArray.getJSONObject(i - 1)
                        var winner: String = ""
                        if (!jsonObject.getString("winner").equals("null"))
                            winner = jsonObject.getString("winner")
                        eventList.add(
                            EventModel(
                                jsonObject.getString("event_id"),
                                jsonObject.getString("market_id"),
                                jsonObject.getString("long_name"),
                                jsonObject.getString("short_name"),
                                winner,
                                jsonObject.getString("start_time"),
                            )
                        )
                    }

                    for (i in 1..jsonMatchArray.length()) {
                        val jsonObject = jsonMatchArray.getJSONObject(i - 1)

                        matchMarketList.add(
                            MatchMarketsModel(
                                jsonObject.getString("event_id"),
                                jsonObject.getString("market_id"),
                                jsonObject.getString("transaction"),
                                jsonObject.getString("commission"),
                            )
                        )
                    }

                    for (j in 1..jsonSessionArray.length()) {
                        val sessionObject = jsonSessionArray.getJSONObject(j - 1)
                        sessionMarketList.add(
                            SessionMarketsModel(
                                sessionObject.getString("event_id"),
                                sessionObject.getString("transaction"),
                                sessionObject.getString("commission")
                            )
                        )
                    }

                    for (i in 1..eventList.size) {
                        val jsonObject = eventList.get(i - 1)

                        val smarket = sessionMarketList.stream().filter {
                            it.getEventID().contains(jsonObject.getEventID())

                        }.collect(Collectors.toList())
                        val mmarket = matchMarketList.stream().filter {
                            it.getEventID().contains(jsonObject.getEventID())

                        }.collect(Collectors.toList())
                        var winner: String = jsonObject.getWinner()
                        val names = jsonObject.getLongName().split('v')
                        for (name in names) {

                            if (name.replace(" ", "").take(1).equals(winner.take(1))) {
                                winner = name
                            }
                        }
                        var lost: String = "0"
                        var won: String = "0"
                        if (smarket.size != 0)
                            if (smarket.get(0).getTransaction().toDouble() > 0) {
                                won = smarket.get(0).getTransaction().replace("-", "").toDouble()
                                    .toInt().toString()
                            } else {
                                lost = smarket.get(0).getTransaction().replace("-", "").toDouble()
                                    .toInt().toString()
                            }
                        if (mmarket.size != 0) {
                            if (mmarket.get(0).getTransaction().toDouble() > 0) {
                                if (won.toInt() > 0)
                                    won = (won.toDouble() + mmarket.get(0).getTransaction()
                                        .replace("-", "")
                                        .toDouble()).toInt().toString()
                                else {
                                    val temp = (mmarket.get(0).getTransaction()
                                        .replace("-", "")
                                        .toDouble() - lost.toDouble()).toInt()
                                    if (temp > 0) {
                                        won = temp.toString().replace("-", "")
                                        lost = "0"
                                    } else {
                                        lost = temp.toString().replace("-", "")
                                        won = "0"
                                    }
                                }
                            } else {
                                if (won.toInt() > 0) {

                                    won = (won.toDouble() - mmarket.get(0).getTransaction()
                                        .replace("-", "")
                                        .toDouble()).toInt().toString()
                                    if (won > lost) {
                                        lost = "0"
                                    } else {
                                        won = "0"
                                    }
                                } else {
                                    lost = (lost.toDouble() + mmarket.get(0).getTransaction()
                                        .replace("-", "")
                                        .toDouble()).toInt().toString()
                                }
                            }

                        }

                        arrayList.add(
                            LedgerModel(
                                jsonObject.getEventID(),
                                jsonObject.getMarketID(),
                                jsonObject.getLongName(),
                                jsonObject.getShortName(),
                                winner,
                                jsonObject.getStartTime(),
                                (won.toDouble().toInt() + lost.toDouble().toInt()).toString(),
                                lost,
                                won,
                                (won.toDouble().toInt() + lost.toDouble().toInt()).toString(),
                            )
                        )
                    }

                    arrayList.sortBy {
                        it.getStartTime()
                    }
                    tl.removeAllViews()

                    addHeaders()
                    addData()
                    kProgressHUD.dismiss()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "[ERROR]" + t.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun addHeaders() {
        val tr = TableRow(context)
        tr.layoutParams = Common(requireContext()).getLayoutParams()
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "MATCH NAME.",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "WON BY",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "WON",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "LOST",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey,
                0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "BALANCE",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
            )
        )
        tl.addView(tr, Common(requireContext()).getTblLayoutParams())
    }

    @SuppressLint("Range")
    fun addData() {
        var blc: Number
        blc = 0
        for (i in 1..arrayList.size) {
            blc += arrayList.get(i - 1).getWon().toDouble().toInt()
            blc -= arrayList.get(i - 1).getLost().toDouble().toInt()

            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateFormat = SimpleDateFormat("hh:mm a")
            val date = format.parse(arrayList.get(i - 1).getStartTime())
            val time = dateFormat.format(date).toString()

            val dtime = StringBuilder().append(" (").append(DateFormat.format("MMM", date))
                .append(" ")
                .append(DateFormat.format("dd", date))
                .append(", ")
                .append(time)
                .append(")")

            var typeface = Typeface.NORMAL
            if (arrayList.get(i - 1).getEventID().equals("0"))
                typeface = Typeface.BOLD
            var bgColor = ""
            bgColor = if (i % 2 == 0) {
                "#FFFFFF"
            } else "#FFFFFF"
            val tr = TableRow(context)

            tr.orientation = TableRow.VERTICAL
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    StringBuilder().append(arrayList.get(i-1).getLongName()).append(dtime)
                        .toString(),
                    Color.DKGRAY,
                    typeface,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.LEFT, i + 99
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    arrayList.get(i - 1).getWinner(),
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor("#FF471A"),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.CENTER, -1
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    arrayList.get(i - 1).getWon(),
                    Color.parseColor("#2E7D32"),
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
                    StringBuilder().append("-").append(arrayList.get(i - 1).getLost())
                        .toString(),
                    Color.RED,
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
                    blc.toString(),
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor),
                    R.drawable.profile_info_bg_style,
                    12f,
                    0,
                    Gravity.RIGHT, -1
                )
            )
            tl.addView(tr, Common(requireContext()).getTblLayoutParams())
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
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateFormat = SimpleDateFormat("hh:mm a")
        val date = format.parse(arrayList.get(betEvent.betType - 100).getStartTime())
        val time = dateFormat.format(date).toString()

        val dtime =
            StringBuilder().append(arrayList.get(betEvent.betType - 100).getLongName())
               .append(" (")
                .append(DateFormat.format("MMM", date))
                .append(" ")
                .append(DateFormat.format("dd", date))
                .append(", ")
                .append(time)
                .append(")")

        tooltip(dtime.toString())
        balloon.show(requireView())
    }

    fun tooltip(msg: String) {
        balloon = createBalloon(tl.context) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.5f)
            setCornerRadius(5f)
                .setTextSize(15f)
                .setPadding(10)
            setAlpha(0.9f)
                .setTextGravity(Gravity.CENTER)
            setText(msg)
            setTextColorResource(R.color.white)
            setTextIsHtml(true)
            setElevation(4)
            setBackgroundColorResource(R.color.grey_dark)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)

        }
    }
}
