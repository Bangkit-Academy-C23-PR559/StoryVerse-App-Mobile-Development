package com.c32pr559.storyverse.ui.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.c32pr559.storyverse.R
import com.c32pr559.storyverse.ui.theme.Poppins
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SettingScreen(navController: NavController) {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
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
    val id = userId

    Scaffold(
        modifier = Modifier,
        topBar = { TopBarSetting(navController) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Box(modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(25.dp))
                .background(Color(0xFFCDDFB6))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Image(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .size(77.dp)
                    )
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$displayName",
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "ID: $id",
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            val customButtonColors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF90B95F)
            )

            Button(
                onClick = { performLogout(context, firebaseAuth, navController) },
                colors = customButtonColors,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Logout")
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "logout")
                }
            }
        }
    }
}

private fun performLogout(context: Context, firebaseAuth: FirebaseAuth, navController: NavController) {
    firebaseAuth.signOut()
    navController.navigate("login")
}

@Composable
fun TopBarSetting(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { navController.navigateUp() }
            )
        },
        title = {
            Text(text = "Pegaturan", style = TextStyle(fontFamily = Poppins, fontSize = 16.sp))
        },
        elevation = 0.dp,
        backgroundColor = Color(0xFFA5C67C)
    )
}