package com.example.coinlist.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.coinlist.models.CoinData
import com.example.coinlist.models.NetworkResult
import com.example.coinlist.viewModels.CoinListViewmodel

@Composable
fun CryptoLazyColumn(modifier: Modifier, viewModel: CoinListViewmodel) {
    val items = viewModel.coinDataList.collectAsState()
    var pageTotal by remember {
        mutableStateOf(1)
    }
    var lastItemIndex by remember {
        mutableStateOf(99)
    }
    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState(),
        contentPadding = PaddingValues(1.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        when (items.value) {
            is NetworkResult.Loading -> {
                item { }
            }
            is NetworkResult.Error -> {
                item {  }
            }
            is NetworkResult.Success -> {
                return@LazyColumn itemsIndexed(
                    items = items.value.data!!
                ) { index: Int, coin: CoinData ->
                    if (index == lastItemIndex) {
                        pageTotal += 1
                        lastItemIndex += 100
                        viewModel.refreshCoinData(pageTotal)
                    }
                    CryptoRow(modifier = Modifier.fillMaxWidth(), coinData = coin)
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}