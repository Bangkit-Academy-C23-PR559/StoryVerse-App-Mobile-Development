package com.c32pr559.storyverse.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.c32pr559.storyverse.R
import com.c32pr559.storyverse.ui.theme.Poppins
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val auth = Firebase.auth
    val context = LocalContext.current

    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    val isEmailValid = email.matches(emailRegex)
    val isEmailErrorVisible = email.isNotEmpty() && !isEmailValid

    val isPasswordValid = password.length >= 8 && password[0].isUpperCase()
    val isPasswordErrorVisible = password.isNotEmpty() && !isPasswordValid

    val passwordVisibility = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.storyverse),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.welcome),
            color = Color(0xFFD4A373),
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(start = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier,
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(R.string.email)) },
                textStyle = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                shape = RoundedCornerShape(25.dp),
                isError = isEmailErrorVisible,
                trailingIcon = {
                    if (isEmailErrorVisible) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Email Error"
                        )
                    }
                },
                maxLines = 1
            )
            if (isEmailErrorVisible) {
                Text(
                    text = "Please enter a valid email address.",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier,
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(R.string.password)) },
                maxLines = 1,
                textStyle = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                shape = RoundedCornerShape(25.dp),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                isError = isPasswordErrorVisible,
                trailingIcon = {
                    val image = if (passwordVisibility.value) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    val contentDescription = if (passwordVisibility.value) "Show Password" else "Hide Password"

                    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                        Icon(imageVector = image, contentDescription = contentDescription)
                    }
                }
            )

            if (isPasswordErrorVisible) {
                Text(
                    text = "Please enter a password with at least 8 characters, starting with an uppercase letter.",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty() && isEmailValid && isPasswordValid) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    if (user != null && user.isEmailVerified) {
                                        // Verifikasi email sukses, izinkan login
                                        navController.navigate("recommend")
                                    } else {
                                        // Email belum diverifikasi, tampilkan pesan
                                        Toast.makeText(
                                            context,
                                            "Please verify your email before logging in.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Login failed. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        if (email.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please enter email.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!isEmailValid) {
                            Toast.makeText(
                                context,
                                "Please enter a valid email.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (password.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please enter password.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!isPasswordValid) {
                            Toast.makeText(
                                context,
                                "Password should be at least 8 characters long and start with an uppercase letter.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96AE46))
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFFEFAE0)
                    ),

                )
            }


            TextButton(onClick = { navController.navigate("register")},
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Belum punya akun? Daftar",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 10.sp,
                    )
                )
            }
        }
    }
}

