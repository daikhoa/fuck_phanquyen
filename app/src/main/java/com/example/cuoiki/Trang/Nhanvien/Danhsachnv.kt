package com.example.cuoiki.Trang.Nhanvien

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cuoiki.Viewmodel.Nhanvienviewmodel

@Composable
fun Danhsachnv(navController: NavController) {
    val viewModel: Nhanvienviewmodel = viewModel()
    val nhanVien by viewModel.nhanvien.collectAsStateWithLifecycle(initialValue = emptyList())
    val dsNhanVien = nhanVien
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (!viewModel.isAdmin()) {
            Toast.makeText(context, "Chỉ admin mới truy cập được!", Toast.LENGTH_SHORT).show()
            navController.navigate("Chonban") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }


    Scaffold(
        floatingActionButton = {
            Button(onClick = { navController.navigate("Themnv") }) {
                Text("Thêm nhân viên")
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { navController.navigate("Menu") }
                )
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(dsNhanVien) { nv ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Nhân viên: ${nv.tennv}", style = MaterialTheme.typography.titleMedium)
                            Text("SĐT: ${nv.sdt}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Row {
                            Button(
                                onClick = { navController.navigate("Suanv/${nv.idnhanvien}") },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Sửa")
                            }
                            Button(
                                onClick = { viewModel.xoa(nv) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Xóa", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}