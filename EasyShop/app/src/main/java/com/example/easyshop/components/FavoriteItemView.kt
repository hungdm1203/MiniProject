package com.example.easyshop.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FavoriteItemView(modifier: Modifier = Modifier,productId: String) {
    var productModel by remember { mutableStateOf(ProductModel()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products").document(productId)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.toObject(ProductModel::class.java)
                    if(result!=null) productModel = result
                }
            }
        delay(500)
        isLoading = false
    }
    val context = LocalContext.current

    if(!isLoading) {
        Card(
            modifier = modifier.padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    GlobalNavigation.navController.navigate("product-detail/"+productId)
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = productModel.images.firstOrNull(),
                    contentDescription = null,
                    modifier = Modifier.width(100.dp)
                        .height(100.dp)
                )
                Column(
                    modifier = Modifier.padding(8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = productModel.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                    Text(
                        text = NumberFormat.getInstance(Locale.GERMANY).format(productModel.price.toLong()) + "đ",
                        fontSize = 14.sp,
                        color = Color.Black,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough)
                    )
                    Text(
                        text = NumberFormat.getInstance(Locale.GERMANY).format(productModel.actualPrice.toLong())+"đ",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(
                    onClick = {
                        AppUtil.updateFavorite(context,productId)
                    }
                ) {
                    Icon(Icons.Default.Delete,null, tint = Color.Black)
                }
            }
        }
    }
}