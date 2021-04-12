package com.example.a99hub.ui.inplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.adapters.InPlayAdapter
import com.example.a99hub.common.Common
import com.example.a99hub.databinding.FragmentInPlayBinding
import com.example.a99hub.eventBus.InPLayEvent
import com.example.a99hub.model.database.InPlayGame
import com.example.a99hub.data.network.Api
import com.example.a99hub.data.network.Resource
import com.example.a99hub.ui.utils.checkJSONObject
import com.example.a99hub.ui.utils.logout
import com.example.a99hub.ui.utils.progress
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class InPlayFragment : Fragment(R.layout.fragment_in_play) {

    private lateinit var binding: FragmentInPlayBinding
    private val viewModel by viewModels<InPlayGameViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var inPLayAdapter: InPlayAdapter
    private lateinit var arrayList: ArrayList<InPlayGame>
    private var navController: NavController? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = FragmentInPlayBinding.bind(view)
        navController = activity?.let {
            Navigation.findNavController(it, R.id.fragment)
        }
        recyclerView = binding.recyclerView
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        arrayList = ArrayList()
        inPLayAdapter = InPlayAdapter(context, ArrayList<InPlayGame>())
        recyclerView = binding.recyclerView
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = inPLayAdapter
        }
        viewModel.inPlayResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    progress(true)
                }
                is Resource.Success -> {
                    lifecycleScope.launch {
                        progress(false)
                        getData(it.value.string())

                    }
                }
                is Resource.Failure -> {
                    progress(false)
                    logout()
                }
            }
        })

        viewModel.getInPlay()

        context?.let {
            viewModel.getInPlayGame(it)
                ?.observe(requireActivity(), Observer {
                    if (it.size > 0) {
                        progress(false)
                        inPLayAdapter.setData(it as ArrayList<InPlayGame>)
                    }
                })
        }

    }


    private fun getData(str: String) {

        val data = JSONObject(str)
        if (checkJSONObject(data.getString("events"))){

                val events: JSONObject = data.getJSONObject("events")
                val x: Iterator<*> = events.keys()
                val jsonArray = JSONArray()
                while (x.hasNext()) {
                    val key = x.next() as String
                    jsonArray.put(events[key])
                }

                for (i in 1..jsonArray.length()) {

                    val jsonObject = jsonArray.getJSONObject(i - 1)
                    val ugModel = InPlayGame(
                        jsonObject.getString("sport_id"),
                        jsonObject.getString("sport_name"),
                        jsonObject.getString("event_id"),
                        jsonObject.getString("market_id"),
                        jsonObject.getString("long_name"),
                        jsonObject.getString("short_name"),
                        jsonObject.getString("start_time"),
                        jsonObject.getString("inactive")
                    )

                    arrayList.add(ugModel)
                }
                viewModel.allDelete(requireActivity())

                viewModel.insert(requireActivity(), arrayList)


            } else {
            Toast.makeText(context, "No Record", Toast.LENGTH_LONG).show()
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
    fun onEventClicked(inPLayEvent: InPLayEvent) {
        val bundle = Bundle()
        bundle.putString("eventid", inPLayEvent.ugModel.event_id)
        navController?.navigate(R.id.action_inPlayFragment_to_inplayDetailFragment, bundle)
    }
}