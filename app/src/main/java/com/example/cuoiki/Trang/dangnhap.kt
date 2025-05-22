package com.example.cuoiki.Trang

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cuoiki.Viewmodel.LoginState
import com.example.cuoiki.Viewmodel.Nhanvienviewmodel

@Composable
fun dangnhap(navController: NavController) {
    val viewModel: Nhanvienviewmodel = viewModel()
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    var taikhoan by remember { mutableStateOf("") }
    var matkhau by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Đăng Nhập", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = taikhoan,
            onValueChange = { taikhoan = it },
            label = { Text("Tài khoản") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = matkhau,
            onValueChange = { matkhau = it },
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (taikhoan.isNotBlank() && matkhau.isNotBlank()) {
                    viewModel.login(taikhoan, matkhau)
                } else {
                    errorMessage = "Nhập đầy đủ thông tin"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginState.Loading
        ) {
            Text("Đăng nhập")
        }

        LaunchedEffect(loginState) {
            when (loginState) {
                is LoginState.Success -> {
                    if (viewModel.isAdmin()) {
                        navController.navigate("menu") {
                            popUpTo("dangnhap") { inclusive = true }
                        }
                    } else {
                        navController.navigate("chonban") {
                            popUpTo("dangnhap") { inclusive = true }
                        }
                    }
                }
                is LoginState.Error -> {
                    errorMessage = (loginState as LoginState.Error).message
                }
                else -> {}
            }
        }
    }
}