package com.example.coinlist.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.request.RequestOptions
import com.example.coinlist.R
import com.example.coinlist.models.CoinData
import com.skydoves.landscapist.glide.GlideImage
import java.util.*

@Composable
fun CryptoRow(modifier: Modifier, coinData: CoinData) {
    var price = coinData.price.toString().take(6)
    if (price.last() == '.') {
        price = price.dropLast(1)
    }
    val tint = if (coinData.priceChangePercentage > 0) colorResource(id = R.color.light_green)
        else colorResource(id = R.color.red)
    val rotation = if (coinData.priceChangePercentage > 0) 180f else 0f
    Row(
        modifier = modifier
            .border(width = 0.1.dp, Color.Black, shape = RoundedCornerShape(40.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            imageModel = coinData.imageUrl ?: R.drawable.ic_crypto_lady,
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Crop,
            requestOptions = {RequestOptions.circleCropTransform()}
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = coinData.symbol.uppercase(Locale.getDefault()),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(modifier = Modifier.weight(2f), text = "$$price")
        Text(
            modifier = Modifier.weight(2f),
            text = coinData.priceChangePercentage.toString().take(6) + '%',
            color = tint
        )
        Icon(
            modifier = Modifier.weight(1f).rotate(rotation),
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "Price Percent Change Icon",
            tint = tint
        )
    }
}

@Preview
@Composable
fun previewCryptoRow(){
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    
        ) {
        CryptoRow(
            modifier = Modifier.fillMaxWidth(0.9f),
            coinData = CoinData(
                "Bitcoin",
                "BTC",
                price = 1232.12,
                imageUrl = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                1,
                -1.078)
        )
    }
}