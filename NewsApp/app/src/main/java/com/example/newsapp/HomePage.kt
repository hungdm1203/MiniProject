package com.example.newsapp

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.kwabenaberko.newsapilib.models.Article

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel,
    navController: NavHostController
) {
    val articles = newsViewModel.articles.observeAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CategoriesBar(newsViewModel = newsViewModel)

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(articles.value){it ->
                ArticleItem(aricle = it,navController)
            }
        }
    }
}

@Composable
fun CategoriesBar(newsViewModel: NewsViewModel) {
    val categoryList = listOf(
        "GENERAL", "BUSINESS", "ENTERTAINMENT", "HEALTH", "SCIENCE", "SPORTS", "TECHNOLOGY"
    )

    var searchQuery by remember { mutableStateOf("") }
    var isSearch by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(isSearch){
            OutlinedTextField(
                modifier = Modifier.padding(8.dp)
                    .height(56.dp)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clip(CircleShape),
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isSearch = false
                            if(searchQuery.isNotEmpty()){
                                newsViewModel.fetchNewsEveryThingsbyQuery(searchQuery)
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }
            )
        } else{
            IconButton(
                onClick = { isSearch = true }
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        }

        categoryList.forEach {
            Button(
                onClick = {
                    newsViewModel.fetchNewsTopHeadlines(it)
                },
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = it
                )
            }
        }
    }
}

@Composable
fun ArticleItem(
    aricle: Article,
    navController: NavHostController
) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            navController.navigate(NewArticlePageScreen(aricle.url))
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = aricle.urlToImage?:"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI29NdIe0-9d5F03JNsigmsoaptB4hjMObHB366poHqUhYmvxVwvyjR6f9_0nCTEuNLQA&usqp=CAU",
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = aricle.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3
                )
                Text(
                    text = aricle.source.name,
                    maxLines = 1,
                    fontSize = 14.sp
                )
            }
        }
    }
}