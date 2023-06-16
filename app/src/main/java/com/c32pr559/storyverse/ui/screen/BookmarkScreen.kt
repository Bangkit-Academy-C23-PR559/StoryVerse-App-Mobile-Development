package com.c32pr559.storyverse.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.c32pr559.storyverse.local.entity.BookmarkStory
import com.c32pr559.storyverse.ui.viewmodel.BookmarkViewModel

@Composable
fun BookmarkScreen(viewModel: BookmarkViewModel, navigateToDetail: (Int) -> Unit, moveToSettingPage: () -> Unit) {
    val stories by viewModel.getFavoriteUser().observeAsState()
    val list = mapList(stories ?: emptyList())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9F0)),
        topBar = { TopBarContent(moveToSettingPage = moveToSettingPage) }
    ) {
        Column(Modifier.padding(it)) {
            FavoriteList(list, navigateToDetail)
        }
    }
}

@Composable
fun FavoriteList(stories: List<BookmarkStory>, navigateToDetail: (Int) -> Unit) {
    LazyColumn {
        items(stories.size) { index ->
            BookmarkItem(story = stories[index], navigateToDetail)
        }
    }
}

private fun mapList(users: List<BookmarkStory>): List<BookmarkStory> {
    return users.map { user ->
        BookmarkStory(
            user.id,
            user.title,
            user.author,
            user.cover_image
        )
    }
}


@Composable
fun BookmarkItem(story: BookmarkStory, navigateToDetail: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp, end = 8.dp)
            .clickable { navigateToDetail(story.id) },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = story.cover_image),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .padding(end = 4.dp)
            )
            Column(modifier = Modifier) {
                Text(
                    text = story.title,
                    style = TextStyle(fontSize = 13.sp),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Oleh: ${story.author}",
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}