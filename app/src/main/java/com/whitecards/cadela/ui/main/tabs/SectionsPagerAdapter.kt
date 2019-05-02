package com.whitecards.cadela.ui.main.tabs

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import com.whitecards.cadela.R

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        "Tab 3"
    )

    private val TAB_ICONS = arrayOf(
        R.drawable.ic_home_black_24dp,
        R.drawable.ic_library_books_black_24dp,
        R.drawable.ic_settings_black_24dp
    )

    override fun getItem(position: Int): Fragment {
        if(position == 2) return SettingsFragment()
        return HomeFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val image = ContextCompat.getDrawable(context, TAB_ICONS[position])
        image?.let {
            image.setBounds(0, 0, image.intrinsicWidth, image.intrinsicHeight)
        }
        var sb = SpannableString(" ")
        val imageSpan = ImageSpan(image, ImageSpan.ALIGN_BOTTOM)
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return sb
    }

    override fun getCount(): Int {
        return TAB_ICONS.size
    }
}