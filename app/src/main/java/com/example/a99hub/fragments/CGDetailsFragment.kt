package com.example.a99hub.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.a99hub.R
import com.example.a99hub.common.Common
import com.example.a99hub.data.sharedprefrence.Token
import com.example.a99hub.databinding.FragmentCGDetailsBinding
import com.example.a99hub.model.BetsModel
import com.example.a99hub.model.CGBetsModel
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class CGDetailsFragment : Fragment() {

    private var _binding: FragmentCGDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var betsList: ArrayList<CGBetsModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCGDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setProgress()
        betsList = ArrayList()
        compositeDisposable = CompositeDisposable()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun getBets(token: String, eventID: String) {
        Log.d("bets_session", eventID)
        kProgressHUD.show()
        betsList.clear()
        compositeDisposable.add(
            Api().getCompletedBets(token, eventID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->

                    val data = JSONObject(res?.string())
                    if (!Common(requireContext()).checkTokenExpiry(res?.string().toString())) {
                        if (Common(requireContext()).checkJSONObject(data.getString("bets"))) {
                            val betsTemp: JSONObject = data.getJSONObject("bets")
                            val x: Iterator<*> = betsTemp.keys()
                            val jsonBetsArray = JSONArray()
                            while (x.hasNext()) {
                                val key = x.next() as String

                                jsonBetsArray.put(betsTemp[key])
                            }

                            for (i in 1..jsonBetsArray.length()) {
                                val jsonObject = jsonBetsArray.getJSONObject(i - 1)
                                betsList.add(
                                    CGBetsModel(
                                        jsonObject.getString("bet_amount"),
                                        jsonObject.getString("bet_type"),
                                        jsonObject.getString("market_id"),
                                        jsonObject.getString("rate"),
                                        jsonObject.getString("team"),
                                        jsonObject.getString("action"),
                                        jsonObject.getString("created"),
                                        jsonObject.getString("client_id"),
                                        jsonObject.getString("transaction_reference"),
                                        jsonObject.getString("transaction_amount"),
                                        jsonObject.getString("transaction_type"),
                                    )
                                )
                            }
                            Toast.makeText(
                                context,
                                "Success " + jsonBetsArray.length(),
                                Toast.LENGTH_LONG
                            )
                                .show()

                        } else Toast.makeText(context, "No Found Record", Toast.LENGTH_LONG).show()


//                    val x1: Iterator<*> = tempSessionBets.keys()
//                    val jsonSessionBetsArray = JSONArray()
//                    val jsonBetsArray = JSONArray()
//                    while (x.hasNext()) {
//                        val key = x.next() as String
////                        Toast.makeText(context, key + " : " + eventID, Toast.LENGTH_LONG).show()
////                        if (key.contains(eventID)) {
////                            Toast.makeText(context, key + " : " + eventID, Toast.LENGTH_LONG).show()
////                        }
//                        jsonBetsArray.put(tempBets[key])
//                    }
//                    while (x1.hasNext()) {
//                        val key = x1.next() as String
//                        jsonSessionBetsArray.put(tempSessionBets[key])
//                    }
//
//                    Log.d("bets_session", eventID)
//
//                    val jsonBetArray =
//                        jsonBetsArray.getJSONObject(0).getJSONArray("bets") as JSONArray
//                    val jsonSBetsArray =
//                        jsonSessionBetsArray.getJSONObject(0).getJSONArray("bets") as JSONArray
////                    Toast.makeText(context, jsonSBetsArray.toString(), Toast.LENGTH_LONG).show()

//                    tbBets.removeAllViews()
//                    addHeadersBets()
//                    addRowsBets()
//
//                    val name: String =
//                        jsonSessionBetsArray.getJSONObject(0).getString("name")
//                    for (i in 1..jsonSBetsArray.length()) {
//                        val jsonObject = jsonSBetsArray.getJSONObject(i - 1)
//                        sessionBetsList.add(
//                            BetsModel(
//                                jsonObject.getInt("notional_profit"),
//                                jsonObject.getString("ip"),
//                                name,
//                                jsonObject.getString("team"),
//                                jsonObject.getString("size"),
//                                jsonObject.getInt("notional_loss"),
//                                jsonObject.getInt("parent_id"),
//                                jsonObject.getInt("rate"),
//                                jsonObject.getString("action"),
//                                jsonObject.getString("created"),
//                                jsonObject.getString("amount"),
//                                jsonObject.getString("client_id"),
//                                jsonObject.getString("market_id"),
//                                jsonObject.getInt("ledger"),
//                                jsonObject.getInt("type"),
//                            )
//                        )
//                    }
//                    tbSessionBets.removeAllViews()
//                    addHeadersSessionBets()
//                    addRowsSessionBets()

                        kProgressHUD.dismiss()

                    } else {
                        TastyToast.makeText(
                            context,
                            "Your session is expire",
                            TastyToast.LENGTH_LONG,
                            TastyToast.WARNING
                        )
                        lifecycleScope.launch {
                            Common(requireContext()).logout()
                        }
                    }
                }, {
                    kProgressHUD.dismiss()
                    Toast.makeText(context, "" + it.message, Toast.LENGTH_LONG).show()
                })
        )
    }

}