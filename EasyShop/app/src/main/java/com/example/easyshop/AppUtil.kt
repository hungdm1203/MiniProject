package com.example.easyshop

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import com.example.easyshop.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.util.UUID

object AppUtil {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun addToCart(context: Context,productId: String) {
        val user = Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid)

        user.get().addOnCompleteListener{
            if(it.isSuccessful){
                var cart = it.result.get("cartItems") as? Map<String, Long>?: emptyMap()
                var quantity = cart.get(productId)?:0
                var updateQuantity = quantity+1

                var updateCart = mapOf("cartItems.$productId" to updateQuantity)

                user.update(updateCart).addOnCompleteListener {
                    if(it.isSuccessful){
                        showToast(context, "Add to cart success")
                    } else {
                        showToast(context, "Add to cart failed")
                    }
                }
            }
        }
    }

    fun removeFromCart(context: Context,productId: String, removeAll: Boolean = false) {
        val user = Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid)

        user.get().addOnCompleteListener{
            if(it.isSuccessful){
                var cart = it.result.get("cartItems") as? Map<String, Long>?: emptyMap()
                var quantity = cart.get(productId)?:0
                var updateQuantity = quantity-1

                var updateCart = if(removeAll || updateQuantity<=0){
                    mapOf("cartItems.$productId" to FieldValue.delete())
                } else {
                    mapOf("cartItems.$productId" to updateQuantity)
                }

                user.update(updateCart).addOnCompleteListener {
                    if(it.isSuccessful){
                        showToast(context, "Remove to cart success")
                    } else {
                        showToast(context, "Remove to cart failed")
                    }
                }
            }
        }
    }

    fun updateFavorite(context: Context,productId: String) {
        val user = Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid)

        user.get().addOnCompleteListener{
            if(it.isSuccessful){
                val favorite = it.result.get("favoriteItems") as? List<String>?: emptyList()
                var updateFavorite = favorite.toMutableList()
                if(updateFavorite.contains(productId)){
                    updateFavorite.remove(productId)
                    user.update("favoriteItems",updateFavorite).addOnCompleteListener {
                        if(it.isSuccessful){
                            showToast(context, "Remove from favorite success")
                        } else {
                            showToast(context, "Remove from favorite failed")
                        }
                    }
                } else {
                    updateFavorite.add(productId)
                    user.update("favoriteItems",updateFavorite).addOnCompleteListener {
                        if(it.isSuccessful){
                            showToast(context, "Add to favorite success")
                        } else {
                            showToast(context, "Add to favorite failed")
                        }
                    }
                }

            }
        }
    }


    fun orderProducts(context: Context){
        val user = Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid)

        user.get().addOnCompleteListener{
            if(it.isSuccessful){
                var cart = it.result.get("cartItems") as? Map<String, Long>?: emptyMap()
                var order = OrderModel(
                    id=UUID.randomUUID().toString(),
                    date = Timestamp.now(),
                    userId = Firebase.auth.currentUser!!.uid,
                    items = cart,
                    status = "order",
                    address = it.result.get("address").toString(),
                    phone = it.result.get("phone").toString()
                )
                Firebase.firestore.collection("orders").document(order.id)
                    .set(order).addOnCompleteListener {
                        if(it.isSuccessful){
                            user.update("cartItems", FieldValue.delete())
                            showToast(context,"Order success")
                        }
                    }
            }
        }
    }

}