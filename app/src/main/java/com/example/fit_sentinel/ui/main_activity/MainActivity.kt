package com.example.fit_sentinel.ui.main_activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.MainNavGraph
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val mainActivityViewModel: MainActivityViewModel = viewModel()
            Fit_SentinelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    if (mainActivityViewModel.isFirstLaunch())
//                        OnboardingNavGraph(modifier = Modifier.fillMaxSize())
//
//                    else
                    MainNavGraph(Modifier.fillMaxSize())
                }
            }
        }
    }
}