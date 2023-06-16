package com.c32pr559.storyverse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.c32pr559.storyverse.R
import com.c32pr559.storyverse.ui.viewmodel.RecommendViewModel
import com.c32pr559.storyverse.ui.theme.Poppins

@Composable
fun RecommendScreen(navController: NavController) {
    var selectedTopics by remember { mutableStateOf(emptyList<String>()) }
    val topics = listOf("Kesehatan Mental", "Mistis", "Pengalaman Pribadi", "Profesi", "Percintaan")

    val recommendViewModel: RecommendViewModel = viewModel()

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.recommended_story),
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 16.sp
                    )
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(topics) { topic ->
                        TopicItem(
                            topic = topic,
                            isSelected = selectedTopics.contains(topic),
                            onTopicSelected = { selected ->
                                selectedTopics = if (selected) {
                                    selectedTopics + topic
                                } else {
                                    selectedTopics - topic
                                }
                            }
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(25.dp),
                    onClick = {
                        recommendViewModel.fetchDataFromServer(selectedTopics, navController)
                        navController.navigate("home")
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96AE46))
                ) {
                    Text(text = "Kirim")
                }
            }
        }
    )
}


@Composable
fun TopicItem(
    topic: String,
    isSelected: Boolean,
    onTopicSelected: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onTopicSelected(!isSelected) }
            .clip(RoundedCornerShape(55.dp))
            .border(
                width = 2.dp,
                color = if (isSelected) Color.Green else Color.Gray,
                shape = RoundedCornerShape(55.dp)
            )
            .background(Color(0xFFF5F9F0))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onTopicSelected(it) }
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = topic
            )
        }
    }
}
