package com.example.fixhubuii

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class HomeUserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardLaporkan = view.findViewById<View>(R.id.cardLaporkan)
        val cardRiwayat = view.findViewById<View>(R.id.cardRiwayat)
        val btnSettings = view.findViewById<View>(R.id.btnSettings)

        cardLaporkan.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_laporFragment)
        }

        cardRiwayat.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_riwayatLaporanFragment)
        }

        btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pengaturanFragment)
        }

    }
}
