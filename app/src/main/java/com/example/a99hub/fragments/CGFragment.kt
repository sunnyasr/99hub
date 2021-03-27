package com.example.a99hub.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.adapters.CGAdapter
import com.example.a99hub.common.Common
import com.example.a99hub.data.sharedprefrence.Token
import com.example.a99hub.databinding.FragmentCompleteGameBinding
import com.example.a99hub.eventBus.InPLayEvent
import com.example.a99hub.model.database.CompleteGame
import com.example.a99hub.network.Api
import com.example.a99hub.viewModel.CompleteGameViewModel
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject

class CGFragment : Fragment() {
    private var _binding: FragmentCompleteGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var recyclerView: RecyclerView
    private lateinit var cgAdapter: CGAdapter
    private lateinit var arraList: ArrayList<CompleteGame>
    private var navController: NavController? = null
    private lateinit var completeGameViewModel: CompleteGameViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteGameBinding.inflate(layoutInflater, container, false)
        completeGameViewModel = ViewModelProvider(this).get(CompleteGameViewModel::class.java)
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
        navController = activity?.let {
            Navigation.findNavController(it, R.id.fragment)
        }
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

        context?.let {
            completeGameViewModel.getCompleteGame(it)
                ?.observe(requireActivity(), Observer {
                    if (it.size > 0) {
                        kProgressHUD.dismiss()
                        cgAdapter.setData(it as ArrayList<CompleteGame>)
                    }
                })
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
                    if (Common(requireContext()).checkJSONObject(data.getString("events"))) {

                        val events: JSONObject = data.getJSONObject("events")
                        val x: Iterator<*> = events.keys()
                        val jsonArray = JSONArray()
                        while (x.hasNext()) {
                            val key = x.next() as String
                            jsonArray.put(events[key])
                        }

                        for (i in 1..jsonArray.length()) {

                            val jsonObject = jsonArray.getJSONObject(i - 1)
                            val ugModel = CompleteGame(
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
                        completeGameViewModel.allDelete(requireContext())
                        completeGameViewModel.insert(requireContext(), arraList)

                        kProgressHUD.dismiss()
                        cgAdapter.setData(arraList)
                    } else {
                        lifecycleScope.launch {
                            Common(requireContext()).logout()
                        }
                    }
                }, {
                    try {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Log.d("CGFragment", e.message.toString())
                    }

                })
        )
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

        val names = inPLayEvent.ugModel.short_name.split("v")
        val bundle = Bundle()
        bundle.putString("eventid", inPLayEvent.ugModel.event_id)
        bundle.putString("team1", names.get(0))
        bundle.putString("team2", names.get(1))
        Log.d("CGDetails", inPLayEvent.ugModel.toString())
        navController?.navigate(R.id.action_completeGameFragment_to_CGDetailsFragment, bundle)
    }
}