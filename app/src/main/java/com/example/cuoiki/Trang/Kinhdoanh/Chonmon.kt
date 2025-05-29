@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.cuoiki.Trang.Kinhdoanh

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter // Import Coil
import com.example.cuoiki.R
import com.example.cuoiki.Viewmodel.Donhangviewmodel
import com.example.cuoiki.Viewmodel.Sanphamviewmodel
import com.example.cuoiki.Csdl.Donhang
import com.example.cuoiki.Csdl.Sanpham

@Composable
fun Chonmon(navController: NavController, idban: Int) {
    val sanphamViewmodel: Sanphamviewmodel = viewModel()
    val donhangViewmodel: Donhangviewmodel = viewModel()
    val sanphamList by sanphamViewmodel.sanpham.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chọn món - Bàn $idban") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Donhang/$idban") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(sanphamList) { sanpham ->
                var soluong by remember { mutableStateOf("1") }
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Hiển thị ảnh từ URL
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = sanpham.hinhanh,
                                placeholder = painterResource(R.drawable.ic_launcher_background),
                                error = painterResource(R.drawable.ic_launcher_background)
                            ),
                            contentDescription = sanpham.tensp,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 16.dp),
                            contentScale = ContentScale.Crop
                        )
                        // Thông tin sản phẩm
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = sanpham.tensp,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Giá: ${sanpham.giasp} VNĐ",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            OutlinedTextField(
                                value = soluong,
                                onValueChange = { if (it.all { char -> char.isDigit() }) soluong = it },
                                label = { Text("Số lượng") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(top = 8.dp)
                            )
                        }
                        Button(
                            onClick = {
                                val sl = soluong.toIntOrNull() ?: 1
                                donhangViewmodel.them(
                                    Donhang(
                                        tensp = sanpham.tensp,
                                        giasp = sanpham.giasp,
                                        soluong = sl,
                                        idban = idban
                                    )
                                )
                                navController.navigate("Donhang/$idban")
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("Thêm")
                        }
                    }
                }
            }
        }
    }
}