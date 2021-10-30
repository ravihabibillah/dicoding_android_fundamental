package com.example.apigithubuserapp.appfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apigithubuserapp.FollowResponseItem
import com.example.apigithubuserapp.databinding.FragmentFollowersBinding
import com.example.apigithubuserapp.setadapter.ListFollowAdapter
import com.example.apigithubuserapp.viewmodel.DetailViewModel

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        val detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        username?.let { detailViewModel.findFollowers(it) }

        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvFollowers.addItemDecoration(itemDecoration)

        detailViewModel.listFollower.observe(viewLifecycleOwner, {
            setFollowerData(it)
        })

        detailViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })


    }

    private fun setFollowerData(items: List<FollowResponseItem>?) {
        val adapter = items?.let { ListFollowAdapter(it) }
        binding.rvFollowers.adapter = adapter
        Log.d(TAG, "onViewCreated: $items")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val TAG = "FollowersFragment"
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

}