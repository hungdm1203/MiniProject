package com.example.easyshop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easyshop.components.CategoriesView
import com.example.easyshop.pages.CategoryProductsPage
import com.example.easyshop.pages.CheckoutPage
import com.example.easyshop.pages.OrderPage
import com.example.easyshop.pages.ProductDetailPage
import com.example.easyshop.screen.AuthScreen
import com.example.easyshop.screen.HomeScreen
import com.example.easyshop.screen.LoginScreen
import com.example.easyshop.screen.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    val islogin = Firebase.auth.currentUser!=null
    val firstPage = if(islogin) "home" else "auth"

    NavHost(navController = navController, startDestination = firstPage){
        composable(route = "auth"){
            AuthScreen(modifier, navController)
        }

        composable(route = "login"){
            LoginScreen(modifier,navController)
        }

        composable(route = "signup"){
            SignupScreen(modifier, navController)
        }

        composable(route = "home"){
            HomeScreen(modifier)
        }

        composable(route = "category-products/{categoryId}"){
            val categoryId = it.arguments?.getString("categoryId")
            CategoryProductsPage(modifier,categoryId?:"")
        }
        composable(route = "product-detail/{productId}"){
            val productId = it.arguments?.getString("productId")
            ProductDetailPage(modifier,productId?:"")
        }
        composable(route = "checkout"){
            CheckoutPage(modifier)
        }
        composable(route = "orders"){
            OrderPage(modifier)
        }
    }
}

object GlobalNavigation{
    lateinit var navController: NavController
}