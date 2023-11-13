package ru.iandreyshev.jokesfeed.system.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.viewbinding.ViewBinding

inline fun <T : ViewBinding> Fragment.viewBindings(
    crossinline bind: (View) -> T
) = object : Lazy<T> {

    private var mCached: T? = null

    private val mObserver = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            mCached = null
        }
    }

    override val value: T
        get() = mCached ?: bind(requireView()).also {
            viewLifecycleOwner.lifecycle.addObserver(mObserver)
            mCached = it
        }

    override fun isInitialized(): Boolean = mCached != null

}
