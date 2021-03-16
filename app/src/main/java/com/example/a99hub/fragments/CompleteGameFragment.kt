package com.example.a99hub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.adapters.CGAdapter
import com.example.a99hub.adapters.InPlayAdapter
import com.example.a99hub.common.Common
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.data.sharedprefrence.Token
import com.example.a99hub.databinding.FragmentCompleteGameBinding
import com.example.a99hub.model.UGModel
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject


class CompleteGameFragment : Fragment() {
    private var _binding: FragmentCompleteGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var recyclerView: RecyclerView
    private lateinit var cgAdapter: CGAdapter
    private lateinit var arraList: ArrayList<UGModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        setProgress()
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        arraList = ArrayList()
        cgAdapter = CGAdapter(context, arraList)
        recyclerView = binding.recyclerView


        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = cgAdapter
        }

        getGames(Token(requireContext()).getToken())

    }

    fun getGames(token: String) {
        kProgressHUD.show()
        compositeDisposable.add(
            Api().getCompletedGames(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    kProgressHUD.dismiss()
                    val data = JSONObject(it.string())
                    if (Common(requireContext()).checkJSONObject(data.toString())) {

                        val events: JSONObject = data.getJSONObject("events")
                        val x: Iterator<*> = events.keys()
                        val jsonArray = JSONArray()
                        while (x.hasNext()) {
                            val key = x.next() as String
                            jsonArray.put(events[key])
                        }

                        for (i in 1..jsonArray.length()) {

                            val jsonObject = jsonArray.getJSONObject(i - 1)
                            val ugModel = UGModel(
                                jsonObject.getString("sport_id"),
                                jsonObject.getString("sport_name"),
                                jsonObject.getString("sport_picture"),
                                jsonObject.getString("event_id"),
                                jsonObject.getString("market_id"),
                                jsonObject.getString("long_name"),
                                jsonObject.getString("short_name"),
                                jsonObject.getString("start_time"),
                                jsonObject.getString("competition_name"),
                                jsonObject.getString("display_picture"),
                                jsonObject.getString("inactive")
                            )

                            arraList.add(ugModel)
                        }

                        kProgressHUD.dismiss()
                        cgAdapter.setData(arraList)
                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                    }
                }, {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                })
        )
    }
}