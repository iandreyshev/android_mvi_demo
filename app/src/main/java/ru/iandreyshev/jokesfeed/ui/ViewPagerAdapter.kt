package ru.iandreyshev.jokesfeed.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.iandreyshev.jokesfeed.presentation.State
import ru.iandreyshev.jokesfeed.ui.feed.FeedFragment
import ru.iandreyshev.jokesfeed.ui.filter.FilterFragment

class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount() = State.Screen.values().count()

    override fun createFragment(position: Int): Fragment {
        return when (State.Screen.values()[position]) {
            State.Screen.FEED -> FeedFragment()
            State.Screen.FILTER -> FilterFragment()
        }
    }

}