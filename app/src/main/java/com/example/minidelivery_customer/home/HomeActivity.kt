package com.example.minidelivery_customer.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.minidelivery_customer.R
import com.example.minidelivery_customer.adapter.GridRecyclerViewAdapter
import com.example.minidelivery_customer.adapter.ViewPagerAdapter
import com.example.minidelivery_customer.databinding.ActivityHomeBinding
import com.example.minidelivery_customer.item.BannerItem
import com.example.minidelivery_customer.item.FakeItem
import com.example.minidelivery_customer.model.Interaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), View.OnClickListener, Interaction {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var gridRecyclerViewAdapter: GridRecyclerViewAdapter
    private var isRunning = true

    // FakeItem 인스턴스 생성
    private val fakeItem = FakeItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root) // setContentView(R.layout.activity_home)

        viewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)

        // FakeItem에서 가져온 데이터로 ViewModel 설정
        viewModel.setBannerItems(fakeItem.fakeBannerItemList)
        viewModel.setGridItems(fakeItem.fakeGridItemList)

        initViewPager2()
        subscribeObservers()
        autoScrollViewPager()
    }


    private fun initViewPager2() {
        binding.viewPager2.apply {
            viewPagerAdapter = ViewPagerAdapter(this@HomeActivity)
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    isRunning = true
                    binding.tvPageNumber.text = "${position + 1}"

                    // 직접 유저가 스크롤 했을때
                    viewModel.setCurrentPosition(position)
                }
            })
        }

        binding.gridRecyclerView.apply {
            gridRecyclerViewAdapter = GridRecyclerViewAdapter()
            layoutManager = GridLayoutManager(this@HomeActivity, 4)

            adapter = gridRecyclerViewAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.bannerItemList.observe(this, Observer { bannerItemList ->
            viewPagerAdapter.submitList(bannerItemList)
        })
        viewModel.currentPosition.observe(this, Observer { currentPosition ->
            binding.viewPager2.currentItem = currentPosition
        })
        viewModel.gridItemList.observe(this, Observer { gridItemList ->
            gridRecyclerViewAdapter.submitList(gridItemList)
        })
    }

    private fun autoScrollViewPager() {
        lifecycleScope.launch {
            whenResumed {
                while (isRunning) {
                    delay(3000)
                    viewModel.getcurrentPosition()?.let {
                        viewModel.setCurrentPosition((it.plus(1)) % 5)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
    }

    override fun onBannerItemClicked(bannerItem: BannerItem) {
        TODO("Not yet implemented")
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


}