package com.example.coinlist

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.coinlist.composables.CryptoLazyColumn
import com.example.coinlist.models.NetworkResult
import com.example.coinlist.viewModels.CoinListViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val coinListViewmodel: CoinListViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        setupSplash(splashScreen)
        super.onCreate(savedInstanceState)
        setupObservables()
        setContent {
            val errorState = coinListViewmodel.errorState.collectAsState()
            val errorMessage = coinListViewmodel.errorMessage.collectAsState()
            MaterialTheme() {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.TopCenter
                ){
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_crypto_man),
                            contentDescription = "List Icon",
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .fillMaxHeight(0.3f)
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        if (errorState.value) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = errorMessage.value, color = colorResource(id = R.color.red))
                            }
                        } else {
                            CryptoLazyColumn(
                                modifier = Modifier.fillMaxWidth(0.95f),
                                viewModel = coinListViewmodel
                            )
                        }

                    }
                }
            }
        }
    }


    private fun setupObservables() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    coinListViewmodel.refreshCoinData(1)
                }
                launch {
                    coinListViewmodel.coinDataList.collect{ coinData ->
                        when(coinData) {
                            is NetworkResult.Loading -> {
                                Toast.makeText(this@MainActivity, "Loading coins...", Toast.LENGTH_SHORT).show()
                            }
                            is NetworkResult.Error -> {

                            }
                            is NetworkResult.Success -> {
                                coinListViewmodel.hideSplash()
                                coinData.data?.forEach {
                                    Log.i("tester", it.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupSplash(splashScreen: SplashScreen) {
        splashScreen.setKeepOnScreenCondition{coinListViewmodel.showSplashScreen.value}
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.TRANSLATION_X,
                0f,
                -splashScreenView.view.width.toFloat()

            ).apply {
                interpolator = DecelerateInterpolator()
                duration = 300L
                doOnEnd {
                    splashScreenView.remove()
                }
            }.start()
        }
    }
}