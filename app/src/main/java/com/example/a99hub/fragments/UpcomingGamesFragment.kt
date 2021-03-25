package com.example.a99hub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.adapters.UGAdapter
import com.example.a99hub.common.Common
import com.example.a99hub.databinding.FragmentUpcomingGamesBinding
import com.example.a99hub.model.database.CompleteGame
import com.example.a99hub.model.database.InPlayGame
import com.example.a99hub.model.database.UpcomingGame
import com.example.a99hub.network.Api
import com.example.a99hub.viewModel.CompleteGameViewModel
import com.example.a99hub.viewModel.UpcomingGameViewModel
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import okhttp3.ResponseBody


import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpcomingGamesFragment : Fragment() {
    private var _binding: FragmentUpcomingGamesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var ugAdapter: UGAdapter
    private lateinit var arrayList: ArrayList<UpcomingGame>
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var upcomginGameViewModel: UpcomingGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingGamesBinding.inflate(layoutInflater, container, false)
        upcomginGameViewModel = ViewModelProvider(this).get(UpcomingGameViewModel::class.java)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        setProgress()
        arrayList = ArrayList()
        ugAdapter = UGAdapter(context, ArrayList<UpcomingGame>())
        recyclerView = binding.recyclerView
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ugAdapter
        }

        context?.let {
            upcomginGameViewModel.getUpcomingGame(it)
                ?.observe(requireActivity(), Observer {
                    if (it.size > 0) {
                        kProgressHUD.dismiss()
                        ugAdapter.setData(it as ArrayList<UpcomingGame>)
                    }
                })
        }
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun getData() {
        kProgressHUD.show()
        Api().getAllComingGame().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.code() == 200) {

                    kProgressHUD.dismiss()
                    val res = response.body()?.string()

                    val data = JSONObject(res)

                    if (!Common(requireActivity()).checkJSONObject(data.getString("events"))) {
                        TastyToast.makeText(
                            requireActivity(),
                            "No found records",
                            TastyToast.LENGTH_LONG,
                            TastyToast.CONFUSING
                        )
                    } else {
                        val events: JSONObject = data.getJSONObject("events")
                        val x: Iterator<*> = events.keys()
                        val jsonArray = JSONArray()

                        while (x.hasNext()) {
                            val key = x.next() as String
                            jsonArray.put(events[key])
                        }
                        for (i in 1..jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i - 1)
                            val ugModel = UpcomingGame(
                                jsonObject.getString("sport_id"),
                                jsonObject.getString("sport_name"),
                                jsonObject.getString("event_id"),
                                jsonObject.getString("market_id"),
                                jsonObject.getString("long_name"),
                                jsonObject.getString("start_time"),
                                jsonObject.getString("inactive")
                            )
//                            if (ugModel.inactive.equals("1"))

                            arrayList.add(ugModel)
                        }

                        upcomginGameViewModel.allDelete(requireContext())
                        upcomginGameViewModel.insert(requireContext(), arrayList)

//                        ugAdapter.setData(arrayList)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }


}