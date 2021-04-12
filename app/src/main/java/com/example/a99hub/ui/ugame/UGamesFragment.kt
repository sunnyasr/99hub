package com.example.a99hub.ui.ugame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.adapters.UGAdapter
import com.example.a99hub.common.Common
import com.example.a99hub.databinding.FragmentUpcomingGamesBinding
import com.example.a99hub.model.database.UpcomingGame
import com.example.a99hub.data.network.Api
import com.example.a99hub.data.network.Resource
import com.example.a99hub.databinding.FragmentInPlayBinding
import com.example.a99hub.ui.inplay.InPlayGameViewModel
import com.example.a99hub.ui.utils.checkJSONObject
import com.example.a99hub.ui.utils.logout
import com.example.a99hub.ui.utils.progress
import com.example.a99hub.ui.utils.toast
import com.kaopiz.kprogresshud.KProgressHUD
import com.sdsmdg.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody


import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class UGamesFragment : Fragment(R.layout.fragment_upcoming_games) {
    private lateinit var binding: FragmentUpcomingGamesBinding
    private val viewModel by viewModels<UGameViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var ugAdapter: UGAdapter
    private lateinit var arrayList: ArrayList<UpcomingGame>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = FragmentUpcomingGamesBinding.bind(view)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        arrayList = ArrayList()
        ugAdapter = UGAdapter(context, ArrayList<UpcomingGame>())
        recyclerView = binding.recyclerView
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ugAdapter
        }

        viewModel.uGameResponseResponse.observe(viewLifecycleOwner, {
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
            viewModel.getUpcomingGame(it)
                ?.observe(requireActivity(), Observer {
                    if (it.size > 0) {
                        progress(false)
                        ugAdapter.setData(it as ArrayList<UpcomingGame>)
                    }
                })
        }
        viewModel.getUGame()


    }


    fun getData(str: String) {


        val data = JSONObject(str)

        if (!checkJSONObject(data.getString("events"))) {
            toast("No found records", TastyToast.CONFUSING)
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
                arrayList.add(ugModel)
            }

            viewModel.allDelete(requireContext())
            viewModel.insert(requireContext(), arrayList)

        }
    }
}







