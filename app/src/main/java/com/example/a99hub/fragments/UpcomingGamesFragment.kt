package com.example.a99hub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.adapters.UGAdapter
import com.example.a99hub.databinding.FragmentUpcomingGamesBinding
import com.example.a99hub.model.UGModel
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
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
    private lateinit var arraList: ArrayList<UGModel>
    private lateinit var kProgressHUD: KProgressHUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingGamesBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        setProgress()
        arraList = ArrayList()
        ugAdapter = UGAdapter(context, ArrayList<UGModel>())
        recyclerView = binding.recyclerView
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ugAdapter
        }
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun getData() {
        kProgressHUD.show()


        Api.invoke().getAllComingGame().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.code() == 200) {
                    kProgressHUD.dismiss()
                    val res = response.body()?.string()
                    val data: JSONObject = JSONObject(res)
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
                        if (ugModel.getInactive().equals("1"))
                            arraList.add(ugModel)

                    }

                    ugAdapter.setData(arraList)

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