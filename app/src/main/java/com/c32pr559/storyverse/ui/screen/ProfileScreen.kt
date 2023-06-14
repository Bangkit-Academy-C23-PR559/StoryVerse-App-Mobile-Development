package com.c32pr559.storyverse.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(){
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
    val email = userData?.get("email") as String?

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column( modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Username: ${displayName}")
            Text(text = "Id: ${id}")
            Text(text = "Email: ${email}")
        }
    }
}
