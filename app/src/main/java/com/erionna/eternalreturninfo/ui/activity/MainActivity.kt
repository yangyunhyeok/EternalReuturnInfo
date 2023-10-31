package com.erionna.eternalreturninfo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.MainActivityBinding
import com.erionna.eternalreturninfo.ui.adapter.MainViewPagerAdapter
import com.erionna.eternalreturninfo.ui.fragment.MyProfileFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding
    private var auth: FirebaseAuth? = null


    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this)
    }

    private val tabIcon = arrayListOf(
        R.drawable.ic_tab_selected_home,
        R.drawable.ic_tab_duo,
        R.drawable.ic_tab_pencil,
        R.drawable.ic_tab_chat,
        R.drawable.ic_tab_user
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var test = intent.getStringExtra("email")
        Log.d("메인페이지","$test")
        var test2 = auth!!.currentUser
        Log.d("메인페이지 유저","$test2")
        var test3 = auth!!.pendingAuthResult
        Log.d("메인페이지 펜딩","$test3")
        var test4 = auth!!.uid
        Log.d("메인페이지 유아이디","$test4")
        var test5 = auth!!.uid
        Log.d("메인페이지 유아이디","$test4")

        var fragment = MyProfileFragment()
        var bundle = Bundle()
        bundle.putString("email",intent.getStringExtra("email"))
        bundle.putString("nickName",intent.getStringExtra("nickName"))
        bundle.putString("character",intent.getStringExtra("character"))
        bundle.putString("profile",intent.getStringExtra("profile"))
        fragment.arguments = bundle
        initView()
    }

    private fun initView() = with(binding) {
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
            tab.setIcon(tabIcon[position])
        }.attach()

        viewPager.run {
            isUserInputEnabled = false
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateTabIconsAndText(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                updateTabIconsAndText(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    private fun updateTabIconsAndText(tab: TabLayout.Tab?, isSelected: Boolean) {
        val position = tab?.position ?: return

        // 선택된 탭의 아이콘 및 텍스트 업데이트
        when (position) {
            0 -> {
                tab.setIcon(if (isSelected) R.drawable.ic_tab_selected_home else R.drawable.ic_tab_home)
            }

            1 -> {
                tab.setIcon(if (isSelected) R.drawable.ic_tab_selected_duo else R.drawable.ic_tab_duo)
            }

            2 -> {
                tab.setIcon(if (isSelected) R.drawable.ic_tab_selected_pencil else R.drawable.ic_tab_pencil)
            }

            3 -> {
                tab.setIcon(if (isSelected) R.drawable.ic_tab_selected_chat else R.drawable.ic_tab_chat)
            }

            4 -> {
                tab.setIcon(if (isSelected) R.drawable.ic_tab_selected_user else R.drawable.ic_tab_user)
            }
        }
    }
}