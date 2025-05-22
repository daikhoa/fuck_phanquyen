package com.example.cuoiki.Viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cuoiki.Csdl.Nhanvien
import com.example.cuoiki.database.DataBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Nhanvienviewmodel(application: Application) : AndroidViewModel(application) {
    private val nhanvienDao = DataBase.getDatabase(application).nhanviendao()
    val nhanvien: Flow<List<Nhanvien>> = nhanvienDao.getAllNhanvien()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun them(nhanvien: Nhanvien) = viewModelScope.launch {
        nhanvienDao.themnhanvien(nhanvien)
    }

    fun sua(nhanvien: Nhanvien) = viewModelScope.launch {
        nhanvienDao.suanhanvien(nhanvien)
    }

    fun xoa(nhanvien: Nhanvien) = viewModelScope.launch {
        nhanvienDao.xoanhanvien(nhanvien)
    }

    fun login(taikhoan: String, matkhau: String) = viewModelScope.launch {
        _loginState.value = LoginState.Loading
        val nhanvien = nhanvienDao.getNhanvienByTaikhoan(taikhoan)
        if (nhanvien != null && nhanvien.mk == matkhau) {
            saveLoginState(nhanvien)
            _loginState.value = LoginState.Success(nhanvien)
        } else {
            _loginState.value = LoginState.Error("Tài khoản hoặc mật khẩu sai")
        }
    }

    fun logout() = viewModelScope.launch {
        val sharedPref = getApplication<Application>().getSharedPreferences("auth", 0)
        sharedPref.edit().clear().apply()
        _loginState.value = LoginState.Idle
    }

    fun isLoggedIn(): Boolean {
        val sharedPref = getApplication<Application>().getSharedPreferences("auth", 0)
        val userId = sharedPref.getInt("userId", -1)
        return userId != -1 && sharedPref.contains("taikhoan")
    }

    fun isAdmin(): Boolean {
        val sharedPref = getApplication<Application>().getSharedPreferences("auth", 0)
        return sharedPref.getString("taikhoan", "") == "admin"
    }

    private fun saveLoginState(nhanvien: Nhanvien) {
        val sharedPref = getApplication<Application>().getSharedPreferences("auth", 0)
        sharedPref.edit().apply {
            putInt("userId", nhanvien.idnhanvien)
            putString("taikhoan", nhanvien.taikhoan)
            apply()
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val nhanvien: Nhanvien) : LoginState()
    data class Error(val message: String) : LoginState()
}