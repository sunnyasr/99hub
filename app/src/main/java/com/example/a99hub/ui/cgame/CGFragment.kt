package com.example.a99hub.ui.cgame

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.adapters.CGAdapter
import com.example.a99hub.data.dataStore.UserManager
import com.example.a99hub.databinding.FragmentCompleteGameBinding
import com.example.a99hub.eventBus.InPLayEvent
import com.example.a99hub.model.database.CompleteGame
import com.example.a99hub.data.network.Resource
import com.example.a99hub.ui.utils.checkJSONObject
import com.example.a99hub.ui.utils.logout
import com.example.a99hub.ui.utils.progress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class CGFragment : Fragment(R.layout.fragment_complete_game) {
    private lateinit var binding: FragmentCompleteGameBinding
    private val viewModel by viewModels<CGameViewModel>()

    @Inject
    lateinit var userManager: UserManager

    private lateinit var recyclerView: RecyclerView
    private lateinit var cgAdapter: CGAdapter
    private lateinit var arraList: ArrayList<CompleteGame>
    private var navController: NavController? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCompleteGameBinding.bind(view)

        navController = activity?.let {
            Navigation.findNavController(it, R.id.fragment)
        }

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

        viewModel.cGameResponseResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    progress(true)
                }
                is Resource.Success -> {
                    progress(false)
                    getGames(it.value.string())
                }
                is Resource.Failure -> {
                    progress(false)
                    logout()
                }
            }
        })

        context?.let {
            viewModel.getCompleteGame(it)
                ?.observe(requireActivity(), Observer {
                    if (it.size > 0) {
                        progress(false)
                        cgAdapter.setData(it as ArrayList<CompleteGame>)
                    }
                })
        }
        val token = runBlocking { userManager.token.first() }
        token?.let { viewModel.getUGame(it) }

    }

    fun getGames(str: String) {

        val data = JSONObject(str)
        if (checkJSONObject(data.getString("events"))) {

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
                    jsonObject.getString("inactive"),
                    jsonObject.getString("score_id"),
                    jsonObject.getString("livetv")
                )

                arraList.add(ugModel)
            }
            viewModel.allDelete(requireContext())
            viewModel.insert(requireContext(), arraList)

//            cgAdapter.setData(arraList)
        } else {
            logout()
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

        val names = inPLayEvent.ugModel.short_name.split("v")
        val bundle = Bundle()
        bundle.putString("eventid", inPLayEvent.ugModel.event_id)
        bundle.putString("long_name", inPLayEvent.ugModel.long_name)
        bundle.putString("start_time", inPLayEvent.ugModel.start_time)
        bundle.putString("team1", names.get(0))
        bundle.putString("team2", names.get(1))
        Log.d("CGDetails", inPLayEvent.ugModel.toString())
        navController?.navigate(R.id.action_completeGameFragment_to_CGDetailsFragment, bundle)
    }
}