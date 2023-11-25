package com.example.myempty

import AmiiboItem
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import asyncGetHttpRequest
import com.example.myempty.ui.theme.MyEmptyTheme

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyEmptyTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
//    }
//}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyEmptyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Let's create a composable function named GetAmiiboItems
                    GetAmiiboItems()
                }
            }
        }
    }
}

@Composable
fun GetAmiiboItems(modifier: Modifier = Modifier) {
    val amiiboList = remember { mutableStateOf<List<AmiiboItem>>(listOf()) }
    Column {
        Button(onClick = {
            asyncGetHttpRequest(
                endpoint = "https://www.amiiboapi.com/api/amiibo/",
                onSuccess = {
                    amiiboList.value = it.response.amiibo
                },
                onError = {
                    Log.d("ERROR", it.message.toString())
                }
            )
        }) {
            Text(
                text = "Get Amiibos",
                modifier = modifier
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(amiiboList.value) { item ->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    Column {
                        Text(
                            text = "Amiibo Series: ${item.amiiboSeries}",
                            modifier = modifier
                        )
                        Text(
                            text = "Name: ${item.name}",
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

