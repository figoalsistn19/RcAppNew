package com.inventoryapp.rcapp.ui.internalnav

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.UserRole
import com.inventoryapp.rcapp.di.PdfFileIntentFactory
import com.inventoryapp.rcapp.ui.agentnav.ListItemStock
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.util.Resource
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.itextpdf.layout.property.VerticalAlignment
import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InternalStockAlert(
    internalProductViewModel: InternalProductViewModel?
){
    val context = LocalContext.current
    val time = Date()

    @SuppressLint("ResourceType")
    fun createPdf(context: Context, products: List<InternalProduct>) {

        fun createCell(content: String, bold: Boolean = false): Cell {
            val cell = Cell().add(Paragraph(content))
            if (bold) {
                cell.setFontSize(12f)
            }
            cell.setBorder(SolidBorder(DeviceRgb(0, 0, 0), 1f))
            cell.setTextAlignment(TextAlignment.CENTER)
            return cell
        }

        fun createPdfFile(): File {
            val pdfFileName = "PO-$time.pdf"
            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS.toString() + "/RcApp/PreOrderSupplier")
            return File(directory, pdfFileName)
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "PO-$time.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/RcApp/PreOrderSupplier")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        uri?.let {
            val outputStream: OutputStream? = resolver.openOutputStream(it)
            outputStream?.let { stream ->
                val pdfWriter = PdfWriter(stream)
                val pdfDocument = PdfDocument(pdfWriter)
                val document = Document(pdfDocument, PageSize.A5)

                document.setMargins(36f, 36f, 36f, 36f)

                // Add header with logo, company name, and address
                val logoStream = context.resources.openRawResource(R.raw.rc_logo_png) // Ensure you have a logo in res/raw
                val logoBytes = logoStream.readBytes()
                val logoImage = Image(ImageDataFactory.create(logoBytes))
                logoImage.scaleToFit(120f, 120f)

                val companyName = Paragraph("UD RIANI CHEARA")
                    .setFontSize(20f)
                    .setBold()
                    .setMarginBottom(1f)
                    .setTextAlignment(TextAlignment.CENTER)

                val companyAddress = Paragraph("Jalan Sadang Raya No.58 Lingkar Barat \n Kota Bengkulu")
                    .setFontSize(12f)
                    .setMarginBottom(4f)
                    .setTextAlignment(TextAlignment.CENTER)

                val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 3f)))
                    .setWidth(UnitValue.createPercentValue(100f))
                headerTable.addCell(Cell().add(logoImage).setBorder(Border.NO_BORDER))
                val companyInfo = Paragraph().add(companyName).add(companyAddress)
                headerTable.addCell(Cell().add(companyInfo).setBorder(Border.NO_BORDER).setVerticalAlignment(
                    VerticalAlignment.MIDDLE)).setTextAlignment(TextAlignment.CENTER)

                document.add(headerTable)

                // Add destination company and issued date
                val destinationCompany = Paragraph(products.first().desc)
                    .setFontSize(12f)
                    .setMarginTop(10f)
                val destinationAddress = Paragraph("Jl. Pusan No. 23")
                    .setFontSize(12f)
                val issuedDate = Paragraph("Kota Bengkulu, ${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())} ")
                    .setFontSize(12f)
                    .setMarginTop(20f)
                    .setMarginLeft(1f)
                    .setTextAlignment(TextAlignment.RIGHT)

                document.add(destinationCompany)
                document.add(destinationAddress)

                // Define the table with 2 columns
                val table = Table(floatArrayOf(1f, 3f, 15f)).apply {
                    setWidth(100f)
                }

                // Add header cells
//                val headerCellProductName = Cell().add(Paragraph("Nama Barang"))
//                    .setBackgroundColor(DeviceRgb(255,225,85))
//                    .setTextAlignment(TextAlignment.CENTER)
//                val headerCellQty = Cell().add(Paragraph("Qty"))
//                    .setBackgroundColor(DeviceRgb(255,225,85))
//                    .setTextAlignment(TextAlignment.CENTER)
//
//                table.addHeaderCell(headerCellProductName)
//                table.addHeaderCell(headerCellQty)

                table.addCell(createCell("No", bold = true))
                table.addCell(createCell("Nama Barang", bold = true))
                table.addCell(createCell("Jumlah", bold = true))

                var no = 1
                // Add product data cells
                products.forEach { product ->
                    val qty = product.qtyProduct!! - product.qtyMin!! + 100
                    table.addCell(Cell().add(Paragraph(no.toString())))
                    table.addCell(Cell().add(Paragraph(product.productName.toString())))
                    table.addCell(Cell().add(Paragraph(qty.toString())))
                    no++
                }

                document.add(table)
                document.add(issuedDate)
                document.close()
                stream.close()

                Toast.makeText(context, "PDF telah disimpan di Download/RcApp/PreOrderSupplier", Toast.LENGTH_SHORT).show()

                showOpenPdfNotification(context, createPdfFile())

                sharePdf(context,uri)

            }
        }
    }

    val internalProduct by internalProductViewModel!!.internalProducts.observeAsState()

    val selectedCategories = remember { mutableStateOf<Set<String>>(emptySet()) }

    var internalProductList by remember {
        mutableStateOf(listOf<InternalProduct>())
    }

    var filteredItems by remember {
        mutableStateOf(listOf<InternalProduct>())
    }

    LaunchedEffect(Unit) {
        internalProductViewModel!!.fetchInternalProducts()
    }

    when (internalProduct) {
        is Resource.Success -> {

            internalProductList = (internalProduct as Resource.Success<List<InternalProduct>>).result.filter { it.qtyProduct!! <= it.qtyMin!! }

            filteredItems = if (selectedCategories.value.isEmpty()) {
                internalProductList
            } else {
                internalProductList.filter { item ->
                    item.desc in selectedCategories.value
                }
            }
        }
        is Resource.Loading -> {
            // Tampilkan indikator loading jika diperlukan
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 1.dp)
            )
        }
        is Resource.Failure -> {
            // Tampilkan pesan error jika diperlukan
            val error = (internalProduct as Resource.Failure).throwable
            Text(text = "Error: ${error.message}")
        }
        else -> {
            // Tampilkan pesan default jika diperlukan
            Text(text = "No data available")
        }
    }
//    val filteredItems = if (selectedCategories.value.isEmpty()) {
//        originalItems
//    } else if (selectedCategories.value.size == 1 && selectedCategories.value.contains("")) {
//        originalItems
//    } else {
//        originalItems.filter { item ->
//            item.category in selectedCategories.value
//        }
//    }

    Scaffold (
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        modifier = Modifier.padding(top = 65.dp),
        topBar = {
            HorizontalDivider()
            LazyRow (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                items(internalProductList.filter { it.desc != null }.distinctBy { it.desc }){
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        selected = selectedCategories.value.contains(it.desc),
                        onClick = {
                            selectedCategories.value = if (selectedCategories.value.contains(it.desc!!)) {
                                selectedCategories.value - it.desc!!
                            } else {
                                selectedCategories.value + it.desc!!
                            }
                        },
                        label = {
                            Text(
                                text = if (it.desc.isNullOrEmpty()) "tidak diketahui" else it.desc!!
                            )
                        }
                    )
                    // Add more FilterChip composables for other categories
                }
            }
        },
        bottomBar = {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Your other UI content here
                    Spacer(modifier = Modifier.weight(1f)) // Add flexibility with weight
                    if (internalProductViewModel!!.role.value == UserRole.Admin){
                        ExtendedFloatingActionButton(
                            onClick = {
                                createPdf(context, filteredItems)
//                                createNotaPdf(context)
                            },
                            shape = FloatingActionButtonDefaults.extendedFabShape,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            elevation = FloatingActionButtonDefaults.elevation(1.dp),
                            modifier = Modifier.padding(bottom = 20.dp, end = 18.dp)

                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Tombol tambah" )
                            Text(text = "Buat PO", modifier = Modifier.padding(start = 5.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp, start = 4.dp, end = 4.dp)
            ) { // Display filtered list
                items(filteredItems) { item ->
                    ListItemStock(
                        item
                    )
                }
            }

        }
    )
}
@SuppressLint("QueryPermissionsNeeded")
private fun sharePdf(context: Context, pdfUri: Uri) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "application/pdf"
    intent.putExtra(Intent.EXTRA_STREAM, pdfUri)

    // Create a chooser dialog to let the user select the app
    val chooserIntent = Intent.createChooser(intent, "Share PDF via...")

    // Verify that the intent will resolve to an activity
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(chooserIntent)
    } else {
        Toast.makeText(context, "No app found to handle PDF sharing", Toast.LENGTH_SHORT).show()
    }
}


@SuppressLint("MissingPermission")
private fun showOpenPdfNotification(context: Context, pdfFile: File) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "pdf_channel",
            "PDF Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val intent = PdfFileIntentFactory.createOpenPdfIntent(context, pdfFile)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Tambahkan FLAG_IMMUTABLE
    )

    val notificationBuilder = NotificationCompat.Builder(context, "pdf_channel")
        .setContentTitle("PreOrder Supplier")
        .setContentText("Surat PO berhasil dibuat. Klik untuk membuka.")
        .setSmallIcon(R.drawable.rc_logo)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    NotificationManagerCompat.from(context).notify(1, notificationBuilder.build())
}

@Preview
@Composable
fun InternalStockAlertPreview(){
//    InternalStockAlert()
}