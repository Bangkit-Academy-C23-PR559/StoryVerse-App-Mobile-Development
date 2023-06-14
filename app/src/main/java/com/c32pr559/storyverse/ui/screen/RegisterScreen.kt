package com.c32pr559.storyverse.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.c32pr559.storyverse.R
import com.c32pr559.storyverse.ui.theme.Poppins

@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = Firebase.firestore
    val context: Context = LocalContext.current
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    val isEmailValid = email.matches(emailRegex)
    val isEmailErrorVisible = email.isNotEmpty() && !isEmailValid
    val isPasswordValid = password.length >= 8 && password[0].isUpperCase()
    val isPasswordErrorVisible = password.isNotEmpty() && !isPasswordValid
    val isUsernameValid = !username.contains(" ")
    val isUsernameErrorVisible = username.isNotEmpty() && !isUsernameValid
    val passwordVisibility = remember { mutableStateOf(false) }
    var agreementChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.daftar),
            color = Color(0xFFD4A373),
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            OutlinedTextField(
                modifier = Modifier.width(280.dp),
                value = fullName,
                maxLines = 1,
                onValueChange = { fullName = it },
                label = { Text(text = stringResource(R.string.nama_lengkap)) },
                textStyle = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                shape = RoundedCornerShape(25.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.width(280.dp),
                value = username,
                maxLines = 1,
                onValueChange = { username = it },
                label = { Text(text = stringResource(R.string.nama_pengguna)) },
                textStyle = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                shape = RoundedCornerShape(25.dp),
                isError = isUsernameErrorVisible,
                trailingIcon = {
                    if (isUsernameErrorVisible) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Username Error"
                        )
                    }
                }
            )
            if (isUsernameErrorVisible) {
                Text(
                    text = "Please enter a username without spaces.",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.width(280.dp),
                value = email,
                maxLines = 1,
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
                }
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
                modifier = Modifier.width(280.dp),
                maxLines = 1,
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(R.string.password)) },
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
            val agreementText = buildAnnotatedString {
                append("Saya menyetujui ")
                pushStringAnnotation("URL", "https://example.com/syarat-ketentuan")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Syarat & Ketentuan")
                }
                pop()
                append(" dan ")
                pushStringAnnotation("URL", "https://example.com/kebijakan-privasi")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Kebijakan Privasi")
                }
                pop()
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(250.dp)
            ) {
                Checkbox(
                    checked = agreementChecked,
                    onCheckedChange = { agreementChecked = it },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = agreementText,
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick =
                {
                    if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty() && fullName.isNotEmpty() && isEmailValid && isPasswordValid && isUsernameValid) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                                        if (verificationTask.isSuccessful) {
                                            val userData = hashMapOf(
                                                "email" to email,
                                                "username" to username,
                                                "fullName" to fullName,
                                                "createdAt" to FieldValue.serverTimestamp()
                                            )
                                            user.uid.let { userId ->
                                                firestore.collection("users")
                                                    .document(userId)
                                                    .set(userData)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(
                                                            context,
                                                            "Registration successful. Please check your email for verification.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        navController.navigate("login")
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(
                                                            context,
                                                            "Failed to save user data. Please try again.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Failed to send email verification. Please try again.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Registration failed. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter all required information correctly.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(25.dp),
                enabled = agreementChecked,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96AE46))
            ) {
                Text(
                    text = "Register",
                    style = TextStyle(
                        fontFamily = Poppins
                    )
                )
            }
            TextButton(onClick = { navController.navigate("login")},
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Sudah punya akun? Login",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 10.sp,
                    )
                )
            }
        }
    }
}