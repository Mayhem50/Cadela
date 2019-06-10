package com.whitecards.cadela.ui.main.tabs.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.whitecards.cadela.R
import com.whitecards.cadela.ui.adapters.ExerciceAdapter
import com.whitecards.cadela.databinding.FragmentMainHomeBinding
import com.whitecards.cadela.services.AuthService

/**
 * A placeholder fragment containing a simple view.
 */
class HomeFragment : androidx.fragment.app.Fragment() {

    private lateinit var _viewModel: HomeViewModel
    var viewModelFactory = ViewModelProvider.NewInstanceFactory()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding =
            DataBindingUtil.inflate<FragmentMainHomeBinding>(inflater, R.layout.fragment_main_home, container, false)

        _viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(HomeViewModel::class.java)
        binding.homeVm = _viewModel
        binding.lifecycleOwner = this

        var recycler = binding.root.findViewById<View>(R.id.home_exercises) as androidx.recyclerview.widget.RecyclerView
        var layoutManager = FlexboxLayoutManager(binding.root.context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        recycler.layoutManager = layoutManager

        var image = binding.root.findViewById<ImageView>(R.id.user_photo)
        AuthService.user.value?.let {
            Glide.with(binding.root).load(it.photoUrl).into(image)
        }

        _viewModel.session.observe(this, Observer { session ->
            val adapter = ExerciceAdapter(session.program)
            recycler.adapter = adapter
            adapter.submitList(session.program.exercises)
        })

        return binding.root
    }
}