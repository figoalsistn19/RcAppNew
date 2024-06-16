package com.inventoryapp.rcapp.ui.internalnav

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.UserRole
import com.inventoryapp.rcapp.ui.agentnav.ListItemStock
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.util.Resource
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Date


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InternalStockAlert(
    internalProductViewModel: InternalProductViewModel?
){
    val context = LocalContext.current
    val time = Date()
    fun createPdf(context: Context, products: List<InternalProduct>) {
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
                val document = Document(pdfDocument)

                // Define the table with 2 columns
                val table = Table(floatArrayOf(1f, 1f))

                // Add header cells
                val headerCellProductName = Cell().add(Paragraph("Nama Barang"))
                    .setBackgroundColor(DeviceRgb(255,225,85))
                    .setTextAlignment(TextAlignment.CENTER)
                val headerCellQty = Cell().add(Paragraph("Qty"))
                    .setBackgroundColor(DeviceRgb(255,225,85))
                    .setTextAlignment(TextAlignment.CENTER)

                table.addHeaderCell(headerCellProductName)
                table.addHeaderCell(headerCellQty)

                // Add product data cells
                products.forEach { product ->
                    table.addCell(Cell().add(Paragraph(product.productName.toString())))
                    table.addCell(Cell().add(Paragraph(product.qtyProduct.toString())))
                }

                document.add(table)
                document.close()
                stream.close()

                Toast.makeText(context, "PDF created successfully", Toast.LENGTH_SHORT).show()

                sharePdf(context,uri)
            }
        }
    }

    fun createNotaPdf(context: Context) {
        fun createCell(content: String, bold: Boolean = false): Cell {
            val cell = Cell().add(Paragraph(content))
            if (bold) {
                cell.setFontSize(12f)
            }
            cell.setBorder(SolidBorder(DeviceRgb(0, 0, 0), 1f))
            cell.setTextAlignment(TextAlignment.CENTER)
            return cell
        }
//        val filePath = context.getExternalFilesDir(null)?.absolutePath + "/nota_pre_order.pdf"
//        val file = File(filePath)
//
//        val pdfWriter = PdfWriter(FileOutputStream(file))
//        val pdfDocument = PdfDocument(pdfWriter)
//        val document = Document(pdfDocument)

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
                val document = Document(pdfDocument)

                document.add(Paragraph("Nota Pre Order Barang Makanan Ringan").setFontSize(20f))
                document.add(Paragraph("Perusahaan: XYZ").setFontSize(12f))
                document.add(Paragraph("Waktu: $time").setFontSize(12f))
                document.add(Paragraph("Alamat: Jalan Contoh No. 123").setFontSize(12f))

                val table = Table(floatArrayOf(1f, 2f))
                table.setWidth(100f)

                table.addCell(createCell("Barang", bold = true))
                table.addCell(createCell("Jumlah", bold = true))

                // Add sample data (ganti dengan data yang sesuai)
                table.addCell(createCell("Keripik Singkong", bold = false))
                table.addCell(createCell("5 Pack", bold = false))

                table.addCell(createCell("Keripik Pisang", bold = false))
                table.addCell(createCell("3 Pack", bold = false))

                document.add(table)

                document.add(Paragraph("Perusahaan yang Ditujui: ABC").setFontSize(12f))
                document.add(Paragraph("Tanda Tangan: ____________________").setFontSize(12f))

                // Close the document
                document.close()
                stream.close()

                Toast.makeText(context, "PDF created successfully", Toast.LENGTH_SHORT).show()

                sharePdf(context,uri)
            }
        }

        // Show a message or perform any other action after PDF creation
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
//            fun filterProducts(products: List<InternalProduct>): List<InternalProduct> {
//                return products.filter { it.qtyProduct!! <= it.qtyMin!! }
//            }
            internalProductList = (internalProduct as Resource.Success<List<InternalProduct>>).result
//                    val selectedCategories = remember { mutableStateOf<Set<String>>(emptySet()) }

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
                    .padding(horizontal = 4.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
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
//                                createPdf(context, internalProductList)
                                createNotaPdf(context)
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
@Preview
@Composable
fun InternalStockAlertPreview(){
//    InternalStockAlert()
}