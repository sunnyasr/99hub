package com.example.a99hub.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.adapters.InPlayAdapter
import com.example.a99hub.common.Common
import com.example.a99hub.databinding.FragmentInPlayBinding
import com.example.a99hub.eventBus.InPLayEvent
import com.example.a99hub.model.database.CompleteGame
import com.example.a99hub.model.database.InPlayGame
import com.example.a99hub.network.Api
import com.example.a99hub.viewModel.InPlayGameViewModel
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

class InPlayFragment : Fragment() {
    private var _binding: FragmentInPlayBinding? = null
    private val binding get() = _binding!!
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var recyclerView: RecyclerView
    private lateinit var inPLayAdapter: InPlayAdapter
    private lateinit var arrayList: ArrayList<InPlayGame>
    private var navController: NavController? = null
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var inPlayGameViewModel: InPlayGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInPlayBinding.inflate(layoutInflater, container, false)
        inPlayGameViewModel = ViewModelProvider(this).get(InPlayGameViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        compositeDisposable = CompositeDisposable()
        navController = activity?.let {
            Navigation.findNavController(it, R.id.fragment)
        }
        setProgress()
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

        context?.let {
            inPlayGameViewModel.getInPlayGame(it)
                ?.observe(requireActivity(), Observer {
                    if (it.size > 0) {
                        kProgressHUD.dismiss()
                        inPLayAdapter.setData(it as ArrayList<InPlayGame>)
                    }
                })
        }
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getData() {
        kProgressHUD.show()
        compositeDisposable.add(
            Api().getInPlay().subscribeOn(Schedulers.io())
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
                        inPlayGameViewModel.allDelete(requireActivity())

                        inPlayGameViewModel.insert(requireActivity(), arrayList)
                        inPLayAdapter.setData(arrayList)

                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                    }
                }, {

//                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                })
        )


    }

    fun setProgress() {
        kProgressHUD = KProgressHUD(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
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
        bundle.putString("eventid", inPLayEvent.ugModel.event_id.toString())
        navController?.navigate(R.id.action_inPlayFragment_to_inplayDetailFragment, bundle)
    }
}