package com.aod.aivoicechat.activities

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.savedstate.SavedState
import com.aod.aivoicechat.R
import com.aod.aivoicechat.databinding.ActivityMainBinding
import com.aod.aivoicechat.utils.ext.hideSystemUI
import com.aod.aivoicechat.utils.ext.showDarkTextInStatusBar

class MainActivity : BaseActivity<ActivityMainBinding>(layoutResId = R.layout.activity_main),
    NavController.OnDestinationChangedListener {

    private var fragmentId = -1

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController.addOnDestinationChangedListener(this)

        hideSystemUI()
        showDarkTextInStatusBar(true)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: SavedState?
    ) {
        fragmentId = destination.id
    }
}