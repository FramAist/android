package com.zss.base.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


fun <T> Flow<T>.collectResumed(
    lifecycleOwner: LifecycleOwner,
    action: (T) -> Unit
): Job = lifecycleOwner.lifecycleScope.launchWhenResumed {
    collect {
        action(it)
    }
}


fun <T> Flow<T>.collectDefault(
    lifecycleOwner: LifecycleOwner,
    action: (T) -> Unit
): Job = lifecycleOwner.lifecycleScope.launch {
    collect {
        action(it)
    }
}