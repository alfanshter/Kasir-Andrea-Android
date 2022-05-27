package com.kasirandrea.kasirandrea.kasir.owner.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kasirandrea.kasirandrea.R
import com.kasirandrea.kasirandrea.databinding.FragmentTransaksiOwnerBinding

class TransaksiOwnerFragment : Fragment() {

    lateinit var binding : FragmentTransaksiOwnerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_transaksi_owner,container,false)
        binding.lifecycleOwner = this


        return binding.root

    }

}