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
import com.example.apigithubuserapp.databinding.FragmentFollowingBinding
import com.example.apigithubuserapp.setadapter.ListFollowAdapter
import com.example.apigithubuserapp.viewmodel.DetailViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        val detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        username?.let { detailViewModel.findFollowing(it) }

        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvFollowing.addItemDecoration(itemDecoration)

        detailViewModel.listFollowing.observe(viewLifecycleOwner, {
            setFollowingData(it)
        })

        detailViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFollowingData(items: List<FollowResponseItem>?) {
        val adapter = items?.let { ListFollowAdapter(it) }
        binding.rvFollowing.adapter = adapter
        Log.d(TAG, "onViewCreated: $items")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "FollowingFragment"
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}