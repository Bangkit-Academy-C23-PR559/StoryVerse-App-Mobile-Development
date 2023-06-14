package com.c32pr559.storyverse.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.base.R
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.c32pr559.storyverse.data.api.model.StoryResponse
import com.c32pr559.storyverse.ui.theme.Poppins
import com.c32pr559.storyverse.ui.viewmodel.HomeViewModel

@Composable
fun ExploreScreen(
    navController : NavController,
    moveToSettingPage: () -> Unit
) {
    Scaffold(
        topBar = { TopBarContent(moveToSettingPage = moveToSettingPage) }
    ) {innerPadding ->
        Box(modifier = Modifier.background(Color(0xFFF5F9F0))) {
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                ExploreContent(navController)
            }
        }
    }

}

@Composable
fun ExploreContent(
    navController: NavController
){
    val homeViewModel: HomeViewModel = viewModel()
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ){
        VerticalGridListExplore(gridList = homeViewModel.storyListResponse, navController)
        homeViewModel.getStoryList()
    }
}

@Composable
fun VerticalGridListExplore(gridList: List<StoryResponse>, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        gridList.forEachIndexed { index, item ->
            if (index % 2 == 0) {
                GridItemsExplore(story = item, gridList.getOrNull(index + 1), navController)
            }
        }
    }
}

@Composable
fun GridItemsExplore(story: StoryResponse, nextStory: StoryResponse?, navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        StoryItemGridExplore(storyBook = story, navController)
        if (nextStory != null) {
            StoryItemGridExplore(storyBook = nextStory, navController)
        }
    }
}


@Composable
fun StoryItemGridExplore(storyBook: StoryResponse, navController: NavController){
    Card(modifier = Modifier
        .clickable {
            navController.navigate("detail/${storyBook.id}")
        }
        .width(170.dp)
        .height(170.dp),
    ) {
        Surface {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = storyBook.coverImage)
                            .apply(block = fun ImageRequest.Builder.() {
                                placeholder(R.drawable.notification_action_background)
                            }).build()
                    ),
                    contentDescription = storyBook.title,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(4.dp),
                )
                Text(
                    text = storyBook.title,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    ),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.BottomCenter)
                ) {
                    Text(
                        text = storyBook.category,
                        modifier = Modifier.padding(8.dp),
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 9.sp
                        ),
                    )
                }

            }
        }
    }
}

