package com.example.easyshop.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshop.AppUtil
import com.example.easyshop.GlobalNavigation
import com.example.easyshop.model.ProductModel
import com.example.easyshop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CheckoutPage(modifier: Modifier = Modifier) {
    var userModel by remember { mutableStateOf(UserModel()) }
    var productList = remember { mutableStateListOf(ProductModel()) }
    var total by remember { mutableLongStateOf(0L) }
    var shipping by remember { mutableStateOf(30000) }
    var tax by remember { mutableStateOf(0L) }
    var finalTotal by remember { mutableStateOf(0L) }

    val context = LocalContext.current

    fun calculateTotal() {
        productList.forEach{
            var quantity = userModel.cartItems[it.id] ?:0
            total+=it.actualPrice.toLong()*quantity
        }
        tax=(total*0.05).toLong()
        finalTotal = total+shipping+tax
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    var user = it.result.toObject(UserModel::class.java)
                    if(user!=null) {
                        userModel = user
                        Firebase.firestore.collection("data")
                            .document("stock").collection("products")
                            .whereIn("id", user.cartItems.keys.toList())
                            .get().addOnCompleteListener { i->
                                if(i.isSuccessful){
                                    val products = i.result.toObjects(ProductModel::class.java)
                                    productList.clear()
                                    productList.addAll(products)
                                    calculateTotal()
                                }
                            }
                    }
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Checkout",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "Delivery to:",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = userModel.address,
            fontSize = 18.sp,
        )
        Spacer(Modifier.size(10.dp))
        Text(
            text = "Phone number:",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = userModel.phone,
            fontSize = 18.sp,
        )
        Spacer(Modifier.size(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.size(5.dp))
        RowCheckoutItem(title = "Total:", value = total.toString())
        Spacer(modifier = Modifier.size(5.dp))
        RowCheckoutItem(title = "Tax:", value = tax.toString())
        Spacer(modifier = Modifier.size(5.dp))
        RowCheckoutItem(title = "Shipping:", value = shipping.toString())
        Spacer(modifier = Modifier.size(5.dp))
        HorizontalDivider()
        Spacer(Modifier.size(20.dp))
        Text(
            text = "To pay",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = NumberFormat.getInstance(Locale.GERMANY).format(finalTotal)+" đ",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(20.dp))
        Button(
            onClick = {
                val navController = GlobalNavigation.navController
                AppUtil.orderProducts(context)
                navController.popBackStack()
                navController.navigate("home")
            },
            modifier = Modifier.fillMaxWidth()
                .height(40.dp),
            enabled = userModel.cartItems.isNotEmpty()
        ) {
            Text(
                text = "Pay now",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun RowCheckoutItem(modifier: Modifier = Modifier, title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(
            text = NumberFormat.getInstance(Locale.GERMANY).format(value.toLong())+" đ",
            fontSize = 18.sp
        )
    }
}