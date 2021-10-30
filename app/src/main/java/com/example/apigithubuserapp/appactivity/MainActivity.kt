package com.example.apigithubuserapp.appactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apigithubuserapp.ItemsItem
import com.example.apigithubuserapp.R
import com.example.apigithubuserapp.databinding.ActivityMainBinding
import com.example.apigithubuserapp.setadapter.ListSearchUserAdapter
import com.example.apigithubuserapp.settingpreferences.SettingPreferences
import com.example.apigithubuserapp.settingpreferences.SettingViewModel
import com.example.apigithubuserapp.settingpreferences.SettingViewModelFactory
import com.example.apigithubuserapp.viewmodel.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingS")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportActionBar?.hide()

        val mainViewModel by viewModels<MainViewModel>()

        val pref = SettingPreferences.getInstance(dataStore)

        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref)).get(SettingViewModel::class.java)

        settingViewModel.getThemeSettings().observe(this, { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.listUser.observe(this, { items ->
            setUserData(items)
        })

        mainViewModel.toastText.observe(this, {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(applicationContext, toastText, Toast.LENGTH_SHORT).show()
            }
        })

        binding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mainViewModel.findUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        val actionBarTitle: String = getString(R.string.search_user_title)
        supportActionBar?.title = actionBarTitle
    }

    private fun setUserData(it: List<ItemsItem>?) {
        val adapter = it?.let { it1 -> ListSearchUserAdapter(it1) }
        binding.rvUser.adapter = adapter

        adapter?.setOnItemClickCallback(object : ListSearchUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                moveToDetailUser(data)
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun moveToDetailUser(user: ItemsItem) {
        val moveWithIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        moveWithIntent.putExtra(DetailUserActivity.EXTRA_USER, user.login)
        Log.d(TAG, "moveToDetailUser: ${user.login}")
        startActivity(moveWithIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                // intent ke activity atau fragment
                val intent = Intent(this@MainActivity, ListFavoriteActivity::class.java)
                startActivity(intent)

                return true
            }
            R.id.settings -> {
                val intent = Intent(this@MainActivity, SettingModeActivity::class.java)
                startActivity(intent)

                return true
            }
            else -> return true
        }

    }


    companion object {
        private const val TAG = "MainActivity"
    }
}