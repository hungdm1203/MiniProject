package com.example.easyshop.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.easyshop.AppUtil
import com.example.easyshop.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductDetailPage(
    modifier: Modifier = Modifier,
    productId: String
) {
    var product by remember { mutableStateOf(ProductModel()) }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products").document(productId)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.toObject(ProductModel::class.java)
                    if(result!=null) product = result
                }
            }
        delay(500)
        isLoading = false
        Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    var favorite = it.result.get("favoriteItems") as? List<String>?: emptyList()
                    isFavorite = favorite.contains(productId)
                }
            }
    }

    if(!isLoading) {
        Column(
            modifier = modifier.fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = product.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                var pagerState = rememberPagerState(0) {
                    product.images.size
                }
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 24.dp
                ) {
                    AsyncImage(
                        model = product.images.get(it),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                DotsIndicator(
                    dotCount = product.images.size,
                    type = ShiftIndicatorType(dotsGraphic = DotGraphic(color = MaterialTheme.colorScheme.primary, size = 10.dp),            ),
                    pagerState = pagerState
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = NumberFormat.getInstance(Locale.GERMANY).format(product.price.toLong()) + "đ",
                            fontSize = 18.sp,
                            color = Color.Black,
                            style = TextStyle(textDecoration = TextDecoration.LineThrough)
                        )
                        Text(
                            text = NumberFormat.getInstance(Locale.GERMANY).format(product.actualPrice.toLong())+"đ",
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            AppUtil.updateFavorite(context,productId)
                        }
                    ) {
                        Icon(if(isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,null, tint = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    onClick = {
                        AppUtil.addToCart(context,productId)
                    },
                    modifier = Modifier.fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = "Add to cart",
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Product description:",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = product.description,
                    fontSize = 16.sp,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.size(16.dp))
                if(product.otherDetails.isNotEmpty()){
                    Text(
                        text = "Other product details:",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                    product.otherDetails.forEach { (t, u) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        ) {
                            Text(
                                text = "$t:   ",
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$u",
                                fontSize = 16.sp,
                                color = Color.Black,
                            )
                        }
                    }
                }
            }
        }
    }
}