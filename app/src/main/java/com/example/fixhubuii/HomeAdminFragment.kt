package com.example.fixhubuii

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fixhubuii.databinding.FragmentHomeAdminBinding

class HomeAdminFragment : Fragment() {

    private var _binding: FragmentHomeAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.cardHistory.setOnClickListener {
           findNavController().navigate(R.id.action_homeAdminFragment_to_riwayatLaporanFragment)
       }

       binding.cardStatistic.setOnClickListener {
           findNavController().navigate(R.id.action_homeAdminFragment_to_statisticFragment)
       }

        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeAdminFragment_to_pengaturanAdminFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
