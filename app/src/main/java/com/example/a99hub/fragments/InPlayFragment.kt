package com.example.a99hub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99hub.R
import com.example.a99hub.adapters.InPlayAdapter
import com.example.a99hub.adapters.UGAdapter
import com.example.a99hub.databinding.FragmentInPlayBinding
import com.example.a99hub.model.UGModel
import com.example.a99hub.network.Api
import com.kaopiz.kprogresshud.KProgressHUD
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InPlayFragment : Fragment() {
    private var _binding: FragmentInPlayBinding? = null
    private val binding get() = _binding!!
    private lateinit var kProgressHUD: KProgressHUD
    private lateinit var recyclerView: RecyclerView
    private lateinit var inPLayAdapter: InPlayAdapter
    private lateinit var arraList: ArrayList<UGModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInPlayBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navController = activity?.let {
            Navigation.findNavController(it, R.id.fragment)
        }
        setProgress()
        recyclerView = binding.recyclerView
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        arraList = ArrayList()
        inPLayAdapter = InPlayAdapter(context, ArrayList<UGModel>())
        recyclerView = binding.recyclerView
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = inPLayAdapter
        }

        getData()

//        binding.carviwInplay.setOnClickListener {
//            navController?.navigate(R.id.action_inPlayFragment_to_inplayDetailFragment)
//        }

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
                    kProgressHUD.dismiss()
                    inPLayAdapter.setData(arraList)

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