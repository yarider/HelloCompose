package com.example.hellocompose.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hellocompose.data.local.AppDatabase
import com.example.hellocompose.data.local.ProductEntity
import com.example.hellocompose.data.repository.ProductRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ProductViewModel(application: Application) : AndroidViewModel(application) {
    
    private val dao = AppDatabase.getInstance(application).productDao()
    private val repository = ProductRepository(dao)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val products: StateFlow<List<ProductEntity>> = _query
        .debounce(300) // Затримка 300мс перед пошуком
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getProducts()
            } else {
                repository.searchProducts(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Завантажуємо початкові дані
        viewModelScope.launch {
            repository.refreshCache()
        }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun refreshData() {
        viewModelScope.launch {
            repository.refreshCache()
        }
    }
}
