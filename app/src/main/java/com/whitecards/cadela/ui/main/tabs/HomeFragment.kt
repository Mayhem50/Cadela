package com.whitecards.cadela.ui.main.tabs

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import com.whitecards.cadela.R
import com.whitecards.cadela.viewModel.HomeViewModel
import com.whitecards.cadela.databinding.*

/**
 * A placeholder fragment containing a simple view.
 */
class HomeFragment : Fragment() {

    private lateinit var _viewModel: HomeViewModel
    var viewModelFactory = ViewModelProvider.NewInstanceFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        var binding = DataBindingUtil.inflate<FragmentMainHomeBinding>(inflater, R.layout.fragment_main_home, container, false)

        binding.homeVm = ViewModelProviders.of(activity!!, viewModelFactory).get(HomeViewModel::class.java)
        binding.lifecycleOwner = this
        return binding.root
    }
}