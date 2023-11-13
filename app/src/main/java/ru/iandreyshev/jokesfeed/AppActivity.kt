package ru.iandreyshev.jokesfeed

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.iandreyshev.jokesfeed.presentation.MainStore
import ru.iandreyshev.jokesfeed.presentation.State
import ru.iandreyshev.jokesfeed.system.utils.uiLazy
import ru.iandreyshev.jokesfeed.ui.ViewPagerAdapter
import timber.log.Timber

class AppActivity : AppCompatActivity() {

    private val mStore by viewModels<MainStore>()
    private val mViewPager by uiLazy { findViewById<ViewPager2>(R.id.viewPager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        mViewPager.adapter = ViewPagerAdapter(this)
        mViewPager.isUserInputEnabled = false

        lifecycleScope.launchWhenStarted {
            mStore.state
                .onEach(::render)
                .launchIn(lifecycleScope)
        }
    }

    private fun render(state: State) {
        val screenIndex = State.Screen.values().indexOf(state.screen)
        mViewPager.setCurrentItem(screenIndex, false)
    }

}
