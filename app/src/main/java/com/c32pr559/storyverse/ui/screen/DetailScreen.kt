package com.c32pr559.storyverse.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.c32pr559.storyverse.ui.component.TopBar
import com.c32pr559.storyverse.ui.theme.Poppins
import com.c32pr559.storyverse.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun DetailScreen(
    navController: NavController,
    id: Int,
    moveToSettingPage: () -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel()
    val item = homeViewModel.storyListResponse.find { it.id == id }
    LaunchedEffect(key1 = id) {
        homeViewModel.getStoryList()
    }
    var isChecked by remember { mutableStateOf(false) }

    if (item != null) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {TopBar(moveToSettingPage = moveToSettingPage, navController)},
//                bottomBar = { AdMobBanner() }
            ) {innerPadding ->
                Box(modifier = Modifier.background(Color(0xFFF5F9F0))){
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier
                                    .clip(RoundedCornerShape(30.dp))
                                    .size(40.dp)
                                    .background(Color.White))
                                Text(
                                    text = item.author,
                                    style = TextStyle(
                                        fontFamily = Poppins,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .weight(1f)
                                )
                                LaunchedEffect(Unit) {
                                    val count = withContext(Dispatchers.IO) {
                                        homeViewModel.checkUser(id)
                                    }
                                    if (count != null) {
                                        withContext(Dispatchers.Main) {
                                            isChecked = count > 0
                                        }
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        isChecked = !isChecked
                                        if (isChecked) {
                                            homeViewModel.addToBookmark(item.id, item.title, item.author, item.coverImage)
                                        } else {
                                            homeViewModel.removeFromBookmark(item.id)
                                        }
                                              },
                                    modifier = Modifier.clickable { isChecked = !isChecked }
                                ) {
                                    Icon(
                                        imageVector = if (isChecked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                        contentDescription = if (isChecked) "BookMark" else "Not Bookmark")
                                }
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(data = item.coverImage).apply(block = fun ImageRequest.Builder.() {
                                        placeholder(coil.compose.base.R.drawable.notification_action_background)
                                    }).build()
                                ),
                                contentDescription = item.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(250.dp)
                                    .padding(4.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.title,
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = item.category,
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Light,
                                fontSize = 10.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val convertedText = item.article.replace("<br>", "\n")
                        Text(
                            text = convertedText,
                            softWrap = true,
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

            }
        }
    } else {
        Column {
            Text(
                text = "Item not found",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            Text(text = "$id")
        }
    }
}

//@Composable
//fun AdMobBanner() {
//    val adUnitId = "YOUR_AD_UNIT_ID" // Ganti dengan ID unit iklan Anda
//
//    AndroidView(factory = { context ->
//        val adView = AdView(context)
//        adView.adSize = AdSize.BANNER
//        adView.adUnitId = adUnitId
//        adView.loadAd(AdRequest.Builder().build())
//        adView
//    })
//}