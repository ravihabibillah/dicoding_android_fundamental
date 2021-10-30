package com.example.apigithubuserapp.appactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apigithubuserapp.R
import com.example.apigithubuserapp.database.FavoriteUser
import com.example.apigithubuserapp.databinding.ActivityListFavoriteBinding
import com.example.apigithubuserapp.helper.ViewModelFactory
import com.example.apigithubuserapp.setadapter.FavoriteAdapter
import com.example.apigithubuserapp.viewmodel.FavoriteViewModel

class ListFavoriteActivity : AppCompatActivity() {

    private var _activityListFavoriteBinding: ActivityListFavoriteBinding? = null
    private val binding get() = _activityListFavoriteBinding

    // adapter initialization
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityListFavoriteBinding = ActivityListFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val favoriteViewModel = obtainViewModel(this@ListFavoriteActivity)

        favoriteViewModel.getAllFavoriteUsers().observe(this, { favoriteList ->
            if (favoriteList != null) {
                adapter.setListFavorite(favoriteList)

            }
        })

        adapter = FavoriteAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvFavorite?.addItemDecoration(itemDecoration)

        binding?.rvFavorite?.setHasFixedSize(true)
        binding?.rvFavorite?.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: FavoriteUser) {
               moveToDetailUser(data)
            }

        })

        val actionBarTitle = getString(R.string.user_favorite_title)
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun moveToDetailUser(user: FavoriteUser) {
        val moveWithIntent = Intent(this@ListFavoriteActivity, DetailUserActivity::class.java)
        moveWithIntent.putExtra(DetailUserActivity.EXTRA_USER, user.username)
        startActivity(moveWithIntent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val item  = menu?.findItem(R.id.menu_favorite)
        item?.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this@ListFavoriteActivity, SettingModeActivity::class.java)
                startActivity(intent)

                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _activityListFavoriteBinding = null
    }


}