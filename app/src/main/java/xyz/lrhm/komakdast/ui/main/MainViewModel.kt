package xyz.lrhm.komakdast.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import timber.log.Timber
import xyz.lrhm.komakdast.core.data.source.AppRepository
import xyz.lrhm.komakdast.core.data.source.PreferenceRepository
import xyz.lrhm.komakdast.core.util.LocalUtil
import xyz.lrhm.komakdast.core.util.SizeManager

class MainViewModel @ViewModelInject constructor(
    val appRepository: AppRepository,
    val preferenceRepository: PreferenceRepository,
    val localUtil: LocalUtil, val sizeManager: SizeManager
) : ViewModel() {

    val config = preferenceRepository.livePreferences

    init {
        Timber.d("init main view model")
    }


}