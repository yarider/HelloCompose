package com.example.hellocompose.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> = _username

    fun updateUsername(newName: String) {
        _username.value = newName
    }
}
