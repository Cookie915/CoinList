package com.example.coinlist.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinlist.models.CoinData
import com.example.coinlist.models.NetworkResult
import com.example.coinlist.models.toCoinData
import com.example.coinlist.repositories.CoinListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
class CoinListViewmodel @Inject constructor(
    private val coinListRepo: CoinListRepo
): ViewModel(){
    val coinDataList: MutableStateFlow<NetworkResult<List<CoinData>>> = MutableStateFlow(NetworkResult.Loading())

    val showSplashScreen: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val errorState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val errorMessage: MutableStateFlow<String> = MutableStateFlow("")

    fun refreshCoinData (page: Int) {
        viewModelScope.launch {
            coinListRepo.refreshCoinData(page).collect{
                when(it) {
                    is NetworkResult.Loading -> {
                        coinDataList.emit(NetworkResult.Loading())
                    }
                    is NetworkResult.Error -> {
                        emitError(it.message ?: "An unknown error occurred")
                    }
                    is NetworkResult.Success -> {
                        resetErrorState()
                        val coinList = coinDataList.value.data?.toMutableList() ?: mutableListOf()
                        it.data?.markets?.forEach { coin ->
                            coinList.add(coin.toCoinData())
                        }
                        coinDataList.emit(
                            NetworkResult.Success(coinList.toImmutableList())
                        )
                    }
                }
            }
        }
    }

    suspend fun hideSplash(){
        showSplashScreen.emit(false)
    }

    private fun emitError(message: String) {
        viewModelScope.launch {
            errorState.emit(true)
            errorMessage.emit(message)
        }
    }

    private fun resetErrorState() {
        viewModelScope.launch {
            errorState.emit(false)
            errorMessage.emit("")
        }
    }

}