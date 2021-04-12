package com.example.a99hub.ui.oldledger

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a99hub.R
import com.example.a99hub.databinding.FragmentOldLedgerBinding


class OldLedgerFragment : Fragment(R.layout.fragment_old_ledger) {
    private lateinit var binding: FragmentOldLedgerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOldLedgerBinding.bind(view)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }


}