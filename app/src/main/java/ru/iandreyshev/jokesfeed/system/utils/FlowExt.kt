package ru.iandreyshev.jokesfeed.system.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <TValue> Flow<TValue>.onEachWithViewLifecycle(
    viewLifecycleOwner: LifecycleOwner,
    block: suspend (TValue) -> Unit
) = flowWithLifecycle(viewLifecycleOwner.lifecycle)
    .onEach { block(it) }
    .launchIn(viewLifecycleOwner.lifecycleScope)
