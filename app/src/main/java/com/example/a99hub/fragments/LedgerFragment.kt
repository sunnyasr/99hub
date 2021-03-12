package com.example.a99hub.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.example.a99hub.R
import com.example.a99hub.common.Common
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.databinding.FragmentLedgerBinding
import com.example.a99hub.model.EventModel
import com.example.a99hub.model.LedgerModel
import com.example.a99hub.model.SessionMarketsModel
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.Collator
import java.util.stream.Collectors
import java.util.stream.Collectors.toList


class LedgerFragment : Fragment() {

    private var _binding: FragmentLedgerBinding? = null
    private val binding get() = _binding!!
    private lateinit var tl: TableLayout
    private lateinit var userManager: UserManager
    private lateinit var eventList: ArrayList<EventModel>
    private lateinit var sessionMarketList: ArrayList<SessionMarketsModel>
    private lateinit var arrayList: ArrayList<LedgerModel>
    private lateinit var kProgressHUD: KProgressHUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLedgerBinding.inflate(layoutInflater, container, false)
        userManager = UserManager(requireContext())
        userManager.token.asLiveData().observe(requireActivity(), {
            getData(it.toString(), "30327245")
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setProgress()
        arrayList = ArrayList()
        eventList = ArrayList()
        sessionMarketList = ArrayList()
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        tl = binding.table


    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    private fun getData(token: String, evtid: String) {
        kProgressHUD.show()
        arrayList.clear()
        eventList.clear()
        sessionMarketList.clear()
        Api.invoke().getLedger(evtid, token).enqueue(object : Callback<ResponseBody> {

            @SuppressLint("NewApi")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.code() == 200) {
                    val res = response.body()?.string()
                    val data: JSONObject = JSONObject(res)
                    val events: JSONObject = data.getJSONObject("events")
                    val session_markets: JSONObject = data.getJSONObject("session_markets")
                    val x: Iterator<*> = events.keys()
                    val _x_session: Iterator<*> = session_markets.keys()
                    val jsonEventArray = JSONArray()
                    val jsonSessionArray = JSONArray()

                    while (x.hasNext()) {
                        val key = x.next() as String
                        jsonEventArray.put(events[key])
                    }
                    while (_x_session.hasNext()) {
                        val key = _x_session.next() as String
                        jsonSessionArray.put(session_markets[key])
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


                        val filteredList = sessionMarketList.stream().filter {
                            it.getEventID().contains(jsonObject.getEventID())

                        }.collect(Collectors.toList());
                        if (filteredList.size != 0) {
                            var lost: String = "0"
                            var won: String = "0"
                            val long_name: String = jsonObject.getLongName()
                            val names = long_name.split('v')
                            val winner: String = jsonObject.getWinner()


                            if (filteredList.get(0).getTransaction().toDouble().toInt() > 0) {
                                won = filteredList.get(0).getTransaction()
                            } else {
                                lost = filteredList.get(0).getTransaction()
                            }


                            arrayList.add(
                                LedgerModel(
                                    jsonObject.getEventID(),
                                    jsonObject.getMarketID(),
                                    jsonObject.getLongName(),
                                    jsonObject.getShortName(),
                                    jsonObject.getWinner(),
                                    jsonObject.getStartTime(),
                                    filteredList.get(0).getTransaction(),
                                    lost,
                                    won,
                                    filteredList.get(0).getTransaction()
                                )
                            )
                        } else {
                            arrayList.add(
                                LedgerModel(
                                    jsonObject.getEventID(),
                                    jsonObject.getMarketID(),
                                    jsonObject.getLongName(),
                                    jsonObject.getShortName(),
                                    jsonObject.getWinner(),
                                    jsonObject.getStartTime(),
                                    "0",
                                    "0",
                                    "0",
                                    "0"
                                )
                            )
                        }

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
        tr.setLayoutParams(Common(requireContext()).getLayoutParams())
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "MATCH NAME.",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey,
                0, 14f, 0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "WON BY",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey,
                0, 14f, 0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "WON",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey, 0, 14f, 0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "LOST",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey,
                0, 14f, 0
            )
        )
        tr.addView(
            Common(requireContext()).getTextView(
                0,
                "BALANCE",
                Color.WHITE,
                Typeface.NORMAL,
                R.color.grey, 0, 14f, 0,
            )
        )
        tl.addView(tr, Common(requireContext()).getTblLayoutParams())
    }

    @SuppressLint("Range")
    fun addData() {
//        val numCompanies: Int = list.size()
        //
//        for (i in 0 until list.size()) {
        for (i in 1..eventList.size) {

            var bgColor = ""
            bgColor = if (i % 2 == 0) {
                "#FFFFFF"
            } else "#FFFFFF"
            val tr = TableRow(context)

            tr.setOrientation(TableRow.VERTICAL)
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    StringBuilder().append(arrayList.get(i - 1).getLongName()).toString(),
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style, 14f, 0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    arrayList.get(i - 1).getWinner(),
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor("#FF471A"), R.drawable.profile_info_bg_style, 14f, 0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    arrayList.get(i - 1).getWon(),
                    Color.GREEN,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style, 14f, 0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    arrayList[i - 1].getLost(),
                    Color.RED,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style, 14f, 0
                )
            )
            tr.addView(
                Common(requireContext()).getTextView(
                    i,
                    arrayList[i - 1].getBalance(),
                    Color.DKGRAY,
                    Typeface.NORMAL,
                    Color.parseColor(bgColor), R.drawable.profile_info_bg_style, 14f, 0
                )
            )
            tl.addView(tr, Common(requireContext()).getTblLayoutParams())
        }
    }
}
