package com.c32pr559.storyverse.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.c32pr559.storyverse.R
import com.c32pr559.storyverse.data.api.model.StoryResponse
import com.c32pr559.storyverse.ui.viewmodel.RecommendViewModel
import com.c32pr559.storyverse.ui.theme.Poppins
import com.c32pr559.storyverse.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    moveToSettingPage: () -> Unit,
    navigateToDetail: (Int) -> Unit,
) {
    val auth = Firebase.auth
    val firestore = Firebase.firestore
    val currentUser by remember {
        mutableStateOf(auth.currentUser)
    }
    val userId by remember { mutableStateOf(currentUser?.uid) }
    val userData by produceState<Map<String, Any>?>(null) {
        userId?.let { uid ->
            val userDocument = firestore.collection("users").document(uid)
            userDocument.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    value = document.data
                }
            }
        }
    }
    val displayName = userData?.get("username") as String?
    val homeViewModel: HomeViewModel = viewModel()
    val imageList = listOf(R.drawable.home_image1, R.drawable.welcome2)
    var currentImageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(10000)
            currentImageIndex = (currentImageIndex + 1) % imageList.size
        }
    }
    val currentImage = imageList[currentImageIndex]
    val recommendViewModel: RecommendViewModel = viewModel()
    val recommendedTitles by recommendViewModel.recommendedTitles.observeAsState(emptyList())
    val recommendedTitlesString = recommendedTitles.map { it.title }


    Surface(modifier = Modifier.background(Color(0xFFF5F9F0))) {
        Column(modifier = Modifier
            .fillMaxSize()
        )
        {
            if (currentUser?.isEmailVerified == false) {
                Text(
                    text = "Please verify your email before accessing the home page.",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
                auth.signOut()
                navController.navigate("login") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            } else {
                Scaffold(
                    topBar = { TopBarContent(
                        moveToSettingPage = moveToSettingPage
                    )}
                ) {innerPadding ->
                    Box(modifier = Modifier.background(Color(0xFFF5F9F0))) {
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .verticalScroll(rememberScrollState())
                        ) {

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(170.dp)
                                ) {
                                    AnimatedVisibility(
                                        visible = true,
                                        enter = fadeIn(),
                                        exit = fadeOut()
                                    ) {
                                        Image(
                                            painter = painterResource(id = currentImage),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.Transparent)
                                        .background(Color.Black.copy(alpha = 0.7f))
                                        .padding(8.dp),
                                ) {
                                    Text(
                                        text = "Hello, $displayName!",
                                        style = TextStyle(
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                    Text(
                                        text = stringResource(R.string.hello_user),
                                        style = TextStyle(
                                            fontFamily = Poppins,
                                            color = Color.White
                                        )
                                    )
                                }
                            }

                            TextItem(stringResource(R.string.categories))

                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .horizontalScroll(
                                        rememberScrollState()
                                    ),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                CategoryButton("Profesi") {
                                    navController.navigate("category/Profesi")
                                }
                                CategoryButton("Percintaan") {
                                    navController.navigate("category/Percintaan")
                                }
                                CategoryButton("Kesehatan Mental") {
                                    navController.navigate("category/Kesehatan Mental")
                                }
                                CategoryButton("Pengalaman Pribadi") {
                                    navController.navigate("category/Pengalaman Pribadi")
                                }
                                CategoryButton("Mistis") {
                                    navController.navigate("category/Mistis")
                                }
                            }

                            TextItem(stringResource(R.string.new_Story))
                            StoryList(storyList = homeViewModel.storyListResponse, navigateToDetail)
                            TextItem(stringResource(R.string.story_recommed))
                            VerticalGridList(gridList = homeViewModel.storyListResponse, navController, recommendedTitlesString)
                            homeViewModel.getStoryList()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalGridList(gridList: List<StoryResponse>, navController: NavController, recommendedTitles: List<String>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        val recommendList = gridList.filter { it.title in recommendedTitles}
        for (index in 0 until recommendList.size step 2) {
            val item = recommendList[index]
            val nextItem = recommendList.getOrNull(index + 1)
            GridItems(story = item, nextItem, navController)
        }
    }
}

@Composable
fun GridItems(story: StoryResponse, nextStory: StoryResponse?, navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        StoryItemGrid(storyBook = story, navController)
        if (nextStory != null) {
            StoryItemGrid(storyBook = nextStory, navController)
        }
    }
}

@Composable
fun StoryList(storyList: List<StoryResponse>, navigateToDetail: (Int) -> Unit) {
    val sortedList = storyList.sortedByDescending { it.createdDate }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
    ) {
        itemsIndexed(items = sortedList) { _, item ->
            StoryItem(storyBook = item, navigateToDetail)
        }
    }
}

@Composable
fun StoryItem(storyBook: StoryResponse, navigateToDetail: (Int) -> Unit){
    val id = storyBook.id
    Card(modifier = Modifier
        .width(149.dp)
        .height(170.dp)
        .clickable { navigateToDetail(id) },
    ) {
        Surface() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = storyBook.coverImage)
                            .apply(block = fun ImageRequest.Builder.() {
                                placeholder(coil.compose.base.R.drawable.notification_action_background)
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

@Composable
fun StoryItemGrid(storyBook: StoryResponse, navController: NavController){
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
                                placeholder(coil.compose.base.R.drawable.notification_action_background)
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

@Composable
fun TopBarContent(
    moveToSettingPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        elevation = 0.dp,
        actions = {
            IconButton(
                onClick = moveToSettingPage) {
                Icon(Icons.Default.Settings, contentDescription = "Setting")
            }
        },
        backgroundColor = Color(0xFFA5C67C)
    )
}

@Composable
fun TextItem(item: String, modifier: Modifier = Modifier) {
    Text(
        text = "$item",
        modifier = modifier
            .padding(start = 16.dp, top = 16.dp),
        style = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
}

@Composable
fun CategoryButton(
    category: String,
    color: Color = Color.Black,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .width(185.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        )
    ) {
        Text(
            text = category,
            color = color,
            fontSize = 16.sp,
            style = TextStyle(
                fontFamily = Poppins
            )
        )
    }
}
