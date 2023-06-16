package com.c32pr559.storyverse.ui.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Upload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.c32pr559.storyverse.R
import com.c32pr559.storyverse.ui.theme.Poppins
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


@Composable
fun UploadScreen(navController: NavController) {
    val context = LocalContext.current
    var description by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImage = uri?.let { uri ->
            val inputStream = context.contentResolver.openInputStream(uri)
            val selectedImageFile = File.createTempFile("selected_image", null, context.cacheDir)
            selectedImageFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            Uri.fromFile(selectedImageFile)
        }
    }
    var snackbarVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(false) }
    var isHintVisible by remember { mutableStateOf(true) }
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


    Scaffold(
        topBar = {
            TopBarUpload(navController)
        },
        content = {
            Column(
                modifier = Modifier.padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.upload_story),
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isHintVisible) {
                        val imagePainter: Painter = rememberVectorPainter(Icons.Default.Image)
                        Image(
                            painter = imagePainter,
                            contentDescription = "Gambar Sampel",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }

                    selectedImage?.let { uri ->
                        Crossfade(targetState = uri) { targetUri ->
                            Image(
                                painter = rememberAsyncImagePainter(targetUri),
                                contentDescription = "Gambar",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clickable {
                                        isHintVisible = false
                                        launcher.launch("image/jpeg")
                                    }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.upload_image_story),
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 12.sp,
                    )
                )
                Button(
                    onClick = { launcher.launch("image/jpeg") },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96AE46))
                ) {
                    Text(text = "Pilih Gambar",
                        style = TextStyle(
                            fontFamily = Poppins,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        )
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                       displayName?.let { username ->
                           isLoading = true
                           val imageFile = File(selectedImage?.path)
                           val descriptionText = description
                           val titleText = title
                           val originalBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                           val compressedByteArray = compressImage(originalBitmap)

                           val requestBody = MultipartBody.Builder()
                               .setType(MultipartBody.FORM)
                               .addFormDataPart("description", descriptionText)
                               .addFormDataPart("title", titleText)
                               .addFormDataPart("username", username)
                               .addFormDataPart("photo", "photo.jpg", RequestBody.create("image/jpeg".toMediaTypeOrNull(), compressedByteArray))
                               .build()

                           val request = Request.Builder()
                               .url("https://backend-dot-storyverse-app.et.r.appspot.com/api/upload")
                               .post(requestBody)
                               .build()

                           val client = OkHttpClient()
                           client.newCall(request).enqueue(object : Callback {
                               override fun onFailure(call: Call, e: IOException) {
                                   e.printStackTrace()
                               }

                               override fun onResponse(call: Call, response: Response) {
                                   if (response.isSuccessful) {
                                       val responseBody = response.body?.string()
                                       if (responseBody != null) {
                                           Log.d("Response", responseBody)
                                       }

                                       coroutineScope.launch {
                                           snackbarVisible = true
                                           snackbarHostState.showSnackbar("Upload berhasil! Cerita Sedang Diproses")
                                       }
                                   } else {
                                       val errorMessage = response.body?.string()
                                       if (errorMessage != null) {
                                           Log.d("Failed", errorMessage)
                                       }
                                   }
                               }

                           })
                       }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96AE46))
                ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Upload,
                            contentDescription = null
                        )
                        Text(
                            text = "Upload",
                            style = TextStyle(
                                fontFamily = Poppins
                            )
                        )
                    }
                }
                DisposableEffect(Unit) {
                    onDispose {
                        isLoading = false
                    }
                }
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                ) { data ->
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        content = { Text(text = data.message) },
                        action = {
                            navController.navigate("home")
                        }
                    )
                }

            }
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    )
}

fun compressImage(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    var quality = 100
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

    while (outputStream.toByteArray().size > 50 * 1024 && quality > 0) {
        outputStream.reset()
        quality -= 5
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    }

    return outputStream.toByteArray()
}

@Composable
fun TopBarUpload(
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
        title = {},
        elevation = 0.dp,
        backgroundColor = Color(0xFFA5C67C)
    )
}