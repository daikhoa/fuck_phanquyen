package com.example.cuoiki.Trang.Nhanvien

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cuoiki.Csdl.Nhanvien
import com.example.cuoiki.Viewmodel.Nhanvienviewmodel

@Composable
fun Themnv(navController: NavController) {
    val viewModel: Nhanvienviewmodel = viewModel()
    var tenNv by remember { mutableStateOf("") }
    var sdt by remember { mutableStateOf("") }
    var taiKhoan by remember { mutableStateOf("") }
    var matKhau by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val viewmodel1 : Nhanvienviewmodel=viewModel()
    val context=  LocalContext.current
    LaunchedEffect(Unit) {
        if (!viewmodel1.isAdmin()) {
            Toast.makeText(context, "Chỉ admin mới truy cập được!", Toast.LENGTH_SHORT).show()
            navController.navigate("Chonban") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Thêm Nhân Viên Mới", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = tenNv,
            onValueChange = { tenNv = it },
            label = { Text("Tên nhân viên") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = sdt,
            onValueChange = { sdt = it },
            label = { Text("Số điện thoại") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = taiKhoan,
            onValueChange = { taiKhoan = it },
            label = { Text("Tài khoản") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = matKhau,
            onValueChange = { matKhau = it },
            label = { Text("Mật khẩu") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                if (tenNv.isBlank()) {
                    errorMessage = "Tên nhân viên không được để trống"
                } else if (sdt.isNotBlank() && sdt.toIntOrNull() == null) {
                    errorMessage = "Số điện thoại phải là số hợp lệ"
                } else {
                    try {
                        viewModel.them(
                            Nhanvien(
                                idnhanvien = 0, // Room tự tăng
                                tennv = tenNv,
                                sdt = sdt.toIntOrNull() ?: 0, // Mặc định 0 nếu trống
                                taikhoan = taiKhoan,
                                mk = matKhau
                            )
                        )
                        navController.popBackStack()
                    } catch (e: Exception) {
                        errorMessage = "Lỗi khi thêm nhân viên: ${e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lưu")
        }

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Quay lại")
        }
    }
}