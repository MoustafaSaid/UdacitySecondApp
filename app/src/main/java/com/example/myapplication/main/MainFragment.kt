package com.example.myapplication.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.repos.AsteroidRepo

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener {
            viewModel.onAsteroidClicked(it)
        })

        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner) {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.doneNavigating()
            }
        }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        viewModel.updateFilter(
            when (item.itemId) {
                //  R.id.show_all_menu -> viewModel.onViewWeekAsteroidsClicked()
                R.id.show_today_menu -> AsteroidRepo.AsteroidFilter.TODAY
                R.id.show_saved_menu -> AsteroidRepo.AsteroidFilter.SAVED
                else -> AsteroidRepo.AsteroidFilter.WEEK
            }
        )
        return true
    }
}
