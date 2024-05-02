package com.inventoryapp.rcapp.ui.agentnav

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun ScreenshotButton(
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            scope.launch {
                // Panggil fungsi untuk mengambil tangkapan layar
                // dan menyimpannya ke galeri
                val bitmap = getScreenshot(context)
                saveScreenshotToGallery(bitmap)
            }
        }
    }
    Button(
        onClick = {
            launcher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            scope.launch {
//                // Ambil tangkapan layar
//                val bitmap = getScreenshot(context)
//                // Simpan tangkapan layar ke galeri
//                saveScreenshotToGallery(context, bitmap)
//            }
        }
    ) {
        Text(text = "Simpan Invoice")
    }
}

// Fungsi untuk mengambil tangkapan layar
private fun getScreenshot(context: Context): Bitmap {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    val width = size.x
    val height = size.y
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val view = View(context)
    view.layout(0, 0, width, height)
    view.draw(canvas)
    return bitmap
}

// Fungsi untuk menyimpan tangkapan layar ke galeri
private fun saveScreenshotToGallery(bitmap: Bitmap) {
    val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val file = File(folder, "Screenshot_${System.currentTimeMillis()}.png")
    try {
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun WriteToMediaStoreButton() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            createWriteRequest(context)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Button(onClick = { launcher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) }) {
        Text("Write to MediaStore")
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun createWriteRequest(context: Context) {
    val resolver: ContentResolver = context.contentResolver

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    }

    val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val imageUri = resolver.insert(contentUri, contentValues)

    imageUri?.let { uri ->
        val imageUris = mutableListOf(uri)
        val intentSender = MediaStore.createWriteRequest(resolver, listOf(uri))
        intentSender.send()
    }
}

@Preview
@Composable
fun WriteToMediaStoreButtonPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WriteToMediaStoreButton()
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun PrevReqOrderSheet(){
    ScreenshotButton()
}