package com.example.easyshop.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.easyshop.AppUtil
import com.example.easyshop.GlobalNavigation
import com.example.easyshop.model.ProductModel
import java.text.NumberFormat
import java.util.Locale


@Composable
fun ProductItemView(modifier: Modifier = Modifier, productModel: ProductModel) {

    val context = LocalContext.current

    Card(
        modifier = modifier.padding(8.dp)
            .clickable {
                GlobalNavigation.navController.navigate("product-detail/"+productModel.id)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = productModel.images.firstOrNull(),
                contentDescription = null,
                modifier = Modifier.height(120.dp)
                    .fillMaxWidth()
            )
            Text(
                text = productModel.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp),
                color = Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = NumberFormat.getInstance(Locale.GERMANY).format(productModel.price.toLong()) + " đ",
                        fontSize = 14.sp,
                        color = Color.Black,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough)
                    )
                    Text(
                        text = NumberFormat.getInstance(Locale.GERMANY).format(productModel.actualPrice.toLong())+" đ",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        AppUtil.addToCart(context,productModel.id)
                    }
                ) {
                    Icon(Icons.Default.ShoppingCart,null, tint = Color.Black)
                }
            }
        }
    }
}