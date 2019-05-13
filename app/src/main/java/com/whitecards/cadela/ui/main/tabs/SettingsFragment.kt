package com.whitecards.cadela.ui.main.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.whitecards.cadela.R
import com.whitecards.cadela.databinding.FragmentMainSettingsBinding
import com.whitecards.cadela.viewModel.SettingsViewModel

/**
 * A placeholder fragment containing a simple view.
 */
class SettingsFragment : androidx.fragment.app.Fragment() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMainSettingsBinding>(
            inflater,
            R.layout.fragment_main_settings,
            container,
            false
        )
        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}