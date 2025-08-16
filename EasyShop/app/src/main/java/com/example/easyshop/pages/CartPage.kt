package com.example.easyshop.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshop.GlobalNavigation
import com.example.easyshop.components.CartItemView
import com.example.easyshop.model.UserModel
import com.google.android.play.integrity.internal.k
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun CartPage(modifier: Modifier = Modifier) {
    var userModel by remember { mutableStateOf(UserModel()) }

    DisposableEffect(Unit) {
        val listener = Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid)
            .addSnapshotListener{ it,_ ->
                if (it!=null){
                    var user = it.toObject(UserModel::class.java)
                    if(user!=null) userModel=user
                }
            }

        onDispose {
            listener.remove()
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your cart",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(10.dp))
        if(userModel.cartItems.isEmpty()){
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No items here.",
                    fontSize = 30.sp,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(userModel.cartItems.toList(), key = {it.first}){(k,v)->
                    CartItemView(productId = k, quantity = v)
                }
            }
        }

        Button(
            onClick = {
                GlobalNavigation.navController.navigate("checkout")
            },
            modifier = Modifier.fillMaxWidth()
                .height(40.dp),
            enabled = userModel.cartItems.isNotEmpty()
        ) {
            Text(
                text = "Checkout",
                fontSize = 16.sp
            )
        }
    }
}


