package com.example.demo

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    companion object {
        const val LEVEL_COUNT = 9
    }

    private var progressBarLeftViewWidth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        //showSystemParameter()
    }

    private fun showSystemParameter() {
        val TAG = "系统参数："
        Log.e(TAG, "手机型号名：" + DeviceManager.getDeviceName(this))
        Log.e(TAG, "---" + android.os.Build.DEVICE)
        Log.e(TAG, "---" + android.os.Build.BOARD)
        Log.e(TAG, "---" + android.os.Build.BRAND)
        Log.e(TAG, "---" + android.os.Build.MODEL)
    }

    private fun initView() {
        view_page.layoutParams.apply {
            this.width = getScreenWidth() - getDip(32)
            this.height = getDip(200)
        }

        val fragments: MutableList<Fragment> = mutableListOf()
        for (i in 1..LEVEL_COUNT) {
            fragments.add(MyFragment())
        }
        val myAdapter = BaseTabPagerFragmentAdapter(supportFragmentManager, fragments)
        view_page.apply {
            adapter = myAdapter
            setPageTransformer(false, ScalePageTransformer(false))
            offscreenPageLimit = fragments.size
            pageMargin = getDip(8)
        }
        tab_layout.setupWithViewPager(view_page)

        tab_layout.removeAllTabs()
        val level = 3
        for (i in 1..LEVEL_COUNT) {
            val tv = TextView(this)
            tv.text = "LV$i"
            tv.textSize = 15f
            tv.maxLines = 1
            tv.gravity = Gravity.CENTER
            tv.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (i <= level) {
                tv.typeface = Typeface.DEFAULT_BOLD
            }
            tab_layout.addTab(tab_layout.newTab().apply {
                customView = tv
            })
        }

        // 滑动offset需要依赖每个tab的宽度
        val halfScreenWidth = getScreenWidth() / 2
        tab_layout.post {
            val tabItemWidth = tab_layout.getTabAt(0)?.view?.width ?: 0

            progressBarLeftViewWidth = tabItemWidth / 2 - getDip(4)

            var params = progress_bar_left.layoutParams
            params.width = progressBarLeftViewWidth
            progress_bar_left.layoutParams = params

            params = progress_bar_right.layoutParams
            params.width = progressBarLeftViewWidth
            progress_bar_right.layoutParams = params

            params = progress_bar.layoutParams
            params.width = LEVEL_COUNT * tabItemWidth - 2 * progressBarLeftViewWidth
            progress_bar.layoutParams = params

            progress_bar.setProgress(tabItemWidth * (level - 1) + getDip(4))

            var offset = tabItemWidth * (level - 1)
            progress_bar.moveThumbTo(offset)
            var allOffset = offset + progressBarLeftViewWidth + getDip(4)
            if (allOffset > halfScreenWidth) {
                hsv_progress.smoothScrollTo(allOffset - halfScreenWidth, 0)
            } else {
                hsv_progress.smoothScrollTo(0 , 0)
            }

            view_page.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    Log.d("quincy", "onPageScrolled-- position: $position   positionOffset: $positionOffset   positionOffsetPixels: $positionOffsetPixels")
                    offset = (tabItemWidth * position + tabItemWidth * positionOffset).toInt()
                    progress_bar.moveThumbTo(offset)

                    allOffset = offset + progressBarLeftViewWidth + getDip(4)
                    if (allOffset > halfScreenWidth) {
                        hsv_progress.smoothScrollTo(allOffset - halfScreenWidth, 0)
                    } else {
                        hsv_progress.smoothScrollTo(0 , 0)
                    }
                }

                override fun onPageSelected(position: Int) {
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
        }
        hsv_progress.setOnTouchListener { _, _ -> true }
        view_page.currentItem = level - 1
    }

    private fun getScreenWidth(): Int {
        val dm = applicationContext.resources.displayMetrics
        return dm?.widthPixels ?: 0
    }

    private fun getDip(x: Int): Int {
        val dm = applicationContext.resources.displayMetrics
        return (x * dm.density + 0.5f).toInt()
    }


}

class BaseTabPagerFragmentAdapter(fm: FragmentManager, private val fragments: List<Fragment>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}