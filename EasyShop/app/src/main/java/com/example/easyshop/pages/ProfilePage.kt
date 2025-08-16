package com.example.easyshop.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshop.AppUtil
import com.example.easyshop.GlobalNavigation
import com.example.easyshop.R
import com.example.easyshop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    var userModel by remember { mutableStateOf(UserModel()) }
    var address by remember { mutableStateOf(userModel.address) }
    var phone by remember { mutableStateOf(userModel.phone) }
    var context = LocalContext.current
    var keybords = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.uid)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val user = it.result.toObject(UserModel::class.java)
                    if(user!=null) {
                        userModel = user
                        address = userModel.address
                        phone = userModel.phone
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
            text = "Your Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
        Text(
            text=userModel.name,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "Address:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = address,
            onValueChange = {
                address = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if(address.isNotEmpty()){
                    Firebase.firestore.collection("users")
                        .document(Firebase.auth.currentUser!!.uid)
                        .update("address",address)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                AppUtil.showToast(context,"Update address success")
                                keybords?.hide()
                            }
                        }
                } else {
                    AppUtil.showToast(context,"Address cannot be empty")
                }
            })
        )

        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "Phone number:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = {
                if(address.isNotEmpty()){
                    Firebase.firestore.collection("users")
                        .document(Firebase.auth.currentUser!!.uid)
                        .update("phone",phone)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                AppUtil.showToast(context,"Update phone number success")
                                keybords?.hide()
                            }
                        }
                } else {
                    AppUtil.showToast(context,"Phone number cannot be empty")
                }
            })
        )

        Spacer(modifier = Modifier.size(20.dp))
        TextButton (
            onClick = {
                GlobalNavigation.navController.navigate("orders")
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text (
                text = "View my orders",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        TextButton (
            onClick = {
                Firebase.auth.signOut()
                val navController = GlobalNavigation.navController
                navController.popBackStack()
                navController.navigate("auth")
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text (
                text = "Sign out",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}