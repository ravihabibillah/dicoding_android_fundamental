package com.example.apigithubuserapp.appactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.apigithubuserapp.R
import com.example.apigithubuserapp.UserDetailResponse
import com.example.apigithubuserapp.database.FavoriteUser
import com.example.apigithubuserapp.databinding.ActivityDetailUserBinding
import com.example.apigithubuserapp.helper.ViewModelFactory
import com.example.apigithubuserapp.setadapter.SectionPagerAdapter
import com.example.apigithubuserapp.viewmodel.DetailViewModel
import com.example.apigithubuserapp.viewmodel.FavoriteAddViewModel
import com.example.apigithubuserapp.viewmodel.FavoriteViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailUserActivity : AppCompatActivity() {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding
    private var favoriteUser: FavoriteUser? = null

    private lateinit var favoriteAddViewModel: FavoriteAddViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var avatarUrl: String = ""
    private var isThere: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val username = intent.getStringExtra(EXTRA_USER)
        val detailViewModel by viewModels<DetailViewModel>()
        favoriteUser = FavoriteUser()


        username?.let { detailViewModel.findUser(it) }

        favoriteAddViewModel = obtainViewModel(this@DetailUserActivity)
        favoriteViewModel = obtainOtherViewModel(this@DetailUserActivity)

        username?.let {
            favoriteViewModel.getByUsername(it).observe(this, { user ->
                isThere = user != null
                setFloatingActionButtonIcon()
            })
        }

        detailViewModel.detailUser.observe(this, {
            setDetailUserData(it)
        })

        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        username?.let { setTabLayout(it) }

        binding?.fabAdd?.setOnClickListener {
            insertDeleteDataToDatabase()
        }

        val actionBarTitle = "Detail $username"
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.btnShare?.setOnClickListener {
            shareUser()
        }

    }

    private fun shareUser(){
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Hii, Let's check this people profile github.com/${binding?.tvUsername}, it's interesting!"
        )
        intent.type = "text/plain"
        startActivity(intent)
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
                val intent = Intent(this@DetailUserActivity, SettingModeActivity::class.java)
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

    private fun setFloatingActionButtonIcon() {
        if (isThere) {
            binding?.fabAdd?.setImageResource(R.drawable.ic_black_favorite_24)
        } else {
            binding?.fabAdd?.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteAddViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteAddViewModel::class.java)
    }

    private fun obtainOtherViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun insertDeleteDataToDatabase() {
        val username = binding?.tvUsername?.text.toString().trim()
        val name = binding?.tvName?.text.toString().trim()


        // check for the same username in database


        favoriteUser.let { favoriteUser ->
            favoriteUser?.username = username
            favoriteUser?.name = name
            favoriteUser?.avatarUrl = avatarUrl
        }

        if (isThere) {
            favoriteAddViewModel.delete(username)
            showToast("$username dihapus dari favorite")
            isThere = false
        } else {
            favoriteAddViewModel.insert(favoriteUser as FavoriteUser)
            showToast("$username ditambahkan ke favorit")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setTabLayout(username: String) {
        // Tab Layout
        val tabLayout: TabLayout = binding!!.tabs
        val viewPager: ViewPager2 = binding!!.viewPager

        val sectionPagerAdapter = SectionPagerAdapter(this@DetailUserActivity, username)
        viewPager.adapter = sectionPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun setDetailUserData(detailUser: UserDetailResponse) {
        Glide.with(this@DetailUserActivity)
            .load(detailUser.avatarUrl)
            .into(binding!!.ivAvatar)

        avatarUrl = detailUser.avatarUrl

        binding?.apply {
            tvName.text = detailUser.name
            tvUsername.text = detailUser.login
            tvLocation.text = detailUser.location
            tvCompany.text = detailUser.company
            tvRepository.text = detailUser.publicRepos.toString()
            tvFollowers.text = detailUser.followers.toString()
            tvFollowing.text = detailUser.following.toString()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }
}