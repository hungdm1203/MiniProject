package com.example.easyshop.viewmodel

import androidx.lifecycle.ViewModel
import com.example.easyshop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewmodel : ViewModel(){

    private val auth = Firebase.auth
    private val firestore =Firebase.firestore

    fun login(email: String, pass: String, result :(Boolean, String?) -> Unit){
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    result(true, null)
                } else{
                    result(false, it.exception?.localizedMessage)
                }
            }
    }

    fun signup(email: String, name: String, pass: String, result :(Boolean, String?) -> Unit){
        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    var userId = it.result?.user?.uid

                    val usermodel = UserModel(name, email,userId!!)
                    firestore.collection("users").document(userId)
                        .set(usermodel)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                 result(true, null)
                            } else {
                                result(false,"Wrong")
                            }
                        }
                } else{
                    result(false, it.exception?.localizedMessage)
                }
            }
    }
}