package com.example.a99hub.ui.ledger

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.a99hub.R
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.eventBus.BetEvent
import com.example.a99hub.model.EventModel
import com.example.a99hub.model.database.Ledger
import com.example.a99hub.model.MatchMarketsModel
import com.example.a99hub.model.SessionMarketsModel
import com.example.a99hub.data.network.Resource
import com.example.a99hub.databinding.FragmentLedgerBinding
import com.example.a99hub.ui.utils.*
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.stream.Collectors
import javax.inject.Inject

@AndroidEntryPoint
class LedgerFragment : Fragment(R.layout.fragment_ledger) {

    private lateinit var binding: FragmentLedgerBinding
    private val viewModel by viewModels<LedgerViewModel>()
    private lateinit var tl: TableLayout

    @Inject
    lateinit var userManager: UserManager
    private lateinit var eventList: ArrayList<EventModel>
    private lateinit var matchMarketList: ArrayList<MatchMarketsModel>
    private lateinit var sessionMarketList: ArrayList<SessionMarketsModel>
    private lateinit var arrayList: ArrayList<Ledger>
    private lateinit var tempList: ArrayList<Ledger>
    private lateinit var balloon: Balloon


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLedgerBinding.bind(view)

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        tl = binding.table
        arrayList = ArrayList()
        tempList = ArrayList()
        matchMarketList = ArrayList()
        eventList = ArrayList()
        sessionMarketList = ArrayList()

        viewModel.ledgerResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    progress(true)
                }
                is Resource.Success -> {
                    progress(false)
                    getData(it.value.string())
                }
                is Resource.Failure -> {
                    progress(false)
                    logout()
                }
            }
        })

        context?.let {
            viewModel.getLedger(it)
                ?.observe(requireActivity(), Observer {
                    if (it.size > 0) {
                        arrayList = it as ArrayList<Ledger>
                        progress(false)
                        tl.removeAllViews()

                        if (arrayList.size > 0) {
                            addHeaders()
                            addData()
                            binding.tvEmpty.visibility = GONE
                        } else {
                            binding.tvEmpty.visibility = VISIBLE
                        }
                    }

                })
        }

        val token = runBlocking { userManager.token.first() }
        token?.let { viewModel.getInPlay(it) }

    }


    @SuppressLint("NewApi")
    private fun getData(str: String) {

        arrayList.clear()
        matchMarketList.clear()
        eventList.clear()
        sessionMarketList.clear()

        val data = JSONObject(str)
        context?.let {
            if (checkTokenExpiry(data.toString())) {
                logout()
            } else {
                /*EVENTS*/
                if (checkJSONObject(data.getString("events"))) {
                    val events: JSONObject = data.getJSONObject("events")
                    val x: Iterator<*> = events.keys()
                    val jsonEventArray = JSONArray()
                    while (x.hasNext()) {
                        val key = x.next() as String
                        jsonEventArray.put(events[key])
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
                }

                /*SESSION MARKET*/
                if (checkJSONObject(data.getString("session_markets"))) {
                    val session_markets: JSONObject =
                        data.getJSONObject("session_markets")
                    val _x_session: Iterator<*> = session_markets.keys()
                    val jsonSessionArray = JSONArray()
                    while (_x_session.hasNext()) {
                        val key = _x_session.next() as String
                        jsonSessionArray.put(session_markets[key])
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
                }

                /*MATCH MARKET*/
                if (checkJSONObject(data.getString("match_markets"))) {
                    val match_markets: JSONObject = data.getJSONObject("match_markets")
                    val _x_match: Iterator<*> = match_markets.keys()
                    val jsonMatchArray = JSONArray()
                    while (_x_match.hasNext()) {
                        val key = _x_match.next() as String
                        jsonMatchArray.put(match_markets[key])
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
                }

                /*Settlement*/
                val jsonSettlementArray = data.getJSONArray("settlement_list")
                for (s in 1..jsonSettlementArray.length()) {
                    val settelment = jsonSettlementArray.getJSONObject(s - 1)
                    var lost: String = "0"
                    var won: String = "0"
                    if (settelment.getString("type").equals("1")) {
                        won = settelment.getString("amount").replace("-", "").toDouble()
                            .toInt()
                            .toString()
                    } else
                        lost =
                            settelment.getString("amount").replace("-", "").toDouble()
                                .toInt()
                                .toString()

                    arrayList.add(
                        Ledger(
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

                /*Final Ledger*/
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
                            won =
                                smarket.get(0).getTransaction().replace("-", "")
                                    .toDouble()
                                    .toInt().toString()
                        } else {
                            lost =
                                smarket.get(0).getTransaction().replace("-", "")
                                    .toDouble()
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
                                lost =
                                    (lost.toDouble() + mmarket.get(0).getTransaction()
                                        .replace("-", "")
                                        .toDouble()).toInt().toString()
                            }
                        }

                    }

                    arrayList.add(
                        Ledger(
                            jsonObject.getEventID(),
                            jsonObject.getMarketID(),
                            jsonObject.getLongName(),
                            jsonObject.getShortName(),
                            winner,
                            jsonObject.getStartTime(),
                            (won.toDouble().toInt() + lost.toDouble()
                                .toInt()).toString(),
                            lost,
                            won,
                            (won.toDouble().toInt() + lost.toDouble()
                                .toInt()).toString(),
                        )
                    )
                }

                /*Final List date sort*/
                arrayList.sortBy {
                    it.start_time
                }
                viewModel.allDelete(requireContext())

                viewModel.insert(requireActivity(), arrayList)


            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()

        tl.removeAllViews()
    }

    private fun addHeaders() {

        context?.let {
            val tr = TableRow(it)
            tr.layoutParams = getLayoutParams()
            tr.addView(
              getTextView(
                    0,
                    "MATCH NAME.",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey,
                    0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
             getTextView(
                    0,
                    "WON BY",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey,
                    0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
               getTextView(
                    0,
                    "WON",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
                getTextView(
                    0,
                    "LOST",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey,
                    0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tr.addView(
                getTextView(
                    0,
                    "BALANCE",
                    Color.WHITE,
                    Typeface.NORMAL,
                    R.color.grey, 0, 12f, 0, Gravity.CENTER, -1
                )
            )
            tl.addView(tr,getTblLayoutParams())
        }

    }

    @SuppressLint("Range")
    fun addData() {

        context?.let {
            var blc: Number
            blc = 0
            for (i in 1..arrayList.size) {
                blc += arrayList.get(i - 1).won.toDouble().toInt()
                blc -= arrayList.get(i - 1).lost.toDouble().toInt()

//                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                val dateFormat = SimpleDateFormat("hh:mm a")
//                val date = format.parse(arrayList.get(i - 1).start_time)
//                val time = dateFormat.format(date).toString()

//                val dtime = StringBuilder().append(" (").append(DateFormat.format("MMM", date))
//                    .append(" ")
//                    .append(DateFormat.format("dd", date))
//                    .append(", ")
//                    .append(time)
//                    .append(")")

                var typeface = Typeface.NORMAL
                if (arrayList.get(i - 1).event_id.equals("0"))
                    typeface = Typeface.BOLD
                var bgColor = ""
                bgColor =
                    if (i % 2 == 0) {
                        "#FFFFFF"
                    } else "#FFFFFF"
                val tr = TableRow(context)

                tr.orientation = TableRow.VERTICAL
                tr.addView(
                  getTextView(
                        i,
                        StringBuilder().append(arrayList.get(i - 1).short_name)
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
                    getTextView(
                        i,
                        arrayList.get(i - 1).winner,
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
                   getTextView(
                        i,
                        arrayList.get(i - 1).won,
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
                   getTextView(
                        i,
                        StringBuilder().append("-").append(arrayList.get(i - 1).lost)
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

                var colorBalance: Int = 0

                if (blc > 0) {
                    colorBalance = Color.parseColor("#2E7D32")
                } else
                    colorBalance = Color.RED


                tr.addView(
                   getTextView(
                        i,
                        blc.toString(),
                        colorBalance,
                        Typeface.NORMAL,
                        Color.parseColor(bgColor),
                        R.drawable.profile_info_bg_style,
                        12f,
                        0,
                        Gravity.RIGHT, -1
                    )
                )
                tl.addView(tr,getTblLayoutParams())
            }

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
        val date = format.parse(arrayList.get(betEvent.betType - 100).start_time)
        val time = dateFormat.format(date).toString()

        val dtime =
            StringBuilder().append(arrayList.get(betEvent.betType - 100).long_name)
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
