package com.c32pr559.storyverse.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.c32pr559.storyverse.R
import com.c32pr559.storyverse.navigation.Screen
import com.c32pr559.storyverse.ui.theme.Poppins


@Composable
fun WelcomeScreen(navController: NavController) {
    val images = remember {
        mutableStateListOf(
            R.drawable.welcome2,
            R.drawable.storyverse
        )
    }
    val currentIndex = remember { mutableStateOf(0) }

    Surface(color = MaterialTheme.colors.background) {
        Box(
            modifier = Modifier.fillMaxSize(),
            Alignment.Center
        ) {
            Crossfade(
                targetState = currentIndex.value,
                animationSpec = tween(durationMillis = 1000)
            ) { index ->
                val imageResource = images[index % images.size]
                val alpha = if (index == currentIndex.value) 1f else 0f

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(350.dp)
                            .padding(16.dp)
                            .alpha(alpha)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(Screen.Login.route) },
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96AE46)),
                        modifier = Modifier.width(300.dp)
                    ) {
                        Text(text = "Sign In", style = TextStyle(fontFamily = Poppins, color = Color.White),
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("register")},
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        modifier = Modifier.width(300.dp)
                    ) {
                        Text(text = "Sign Up", style = TextStyle(fontFamily = Poppins, color = Color.Black),
                        )
                    }
                }
            }
        }
    }
}
