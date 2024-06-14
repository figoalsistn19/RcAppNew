package com.inventoryapp.rcapp.ui.internalnav

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InternalStockAlert(){
    val supplierListDummy = listOf<String>("Supplier 1", "Supplier 2", "Supplier 3")
    var supplierList = listOf<String>("Supplier 1", "Supplier 2", "Supplier 3","Supplier 1", "Supplier 2", "Supplier 3")
    var selectedFilter = listOf<Boolean>(false, false, false)

    val originalItems = remember { // Initialize your list of items
        listOf(
            Item("Item 1", "Category A"),
            Item("Item 2", "Category B"),
            Item("Item 3", "Category A"),
            Item("Item 4", "Category C")
        )
    }

    val selectedCategories = remember { mutableStateOf<Set<String>>(emptySet()) }

    val filteredItems = if (selectedCategories.value.isEmpty()) {
        originalItems
    } else {
        originalItems.filter { item ->
            item.category in selectedCategories.value
        }
    }
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
                items(originalItems){
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        selected = selectedCategories.value.contains(it.category),
                        onClick = {
                            selectedCategories.value = if (selectedCategories.value.contains(it.category)) {
                                selectedCategories.value - it.category
                            } else {
                                selectedCategories.value + it.category
                            }
                        },
                        label = { Text(it.category) }
                    )
                    // Add more FilterChip composables for other categories
                }
            }

        },
        content = {
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp, start = 4.dp, end = 4.dp)
            ) { // Display filtered list
                items(filteredItems) { item ->
                    Text(item.name)
                }
            }
        }
    )
}

data class Item(val name: String, val category: String)
@Preview
@Composable
fun InternalStockAlertPreview(){
    InternalStockAlert()
}