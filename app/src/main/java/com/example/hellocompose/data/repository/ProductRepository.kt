package com.example.hellocompose.data.repository

import com.example.hellocompose.data.local.ProductDao
import com.example.hellocompose.data.local.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: ProductDao) {
    
    fun getProducts(): Flow<List<ProductEntity>> = dao.getAllProducts()
    
    fun searchProducts(query: String): Flow<List<ProductEntity>> = 
        dao.searchProducts(query)
    
    suspend fun refreshCache() {
        // Імітуємо API-запит і отримуємо нові дані
        val newProducts = listOf(
            ProductEntity(name = "Laptop", category = "Electronics", price = 1500.0),
            ProductEntity(name = "Mouse", category = "Accessories", price = 25.0),
            ProductEntity(name = "Keyboard", category = "Accessories", price = 50.0),
            ProductEntity(name = "Monitor", category = "Electronics", price = 300.0),
            ProductEntity(name = "Headphones", category = "Accessories", price = 75.0),
            ProductEntity(name = "Smartphone", category = "Electronics", price = 800.0),
            ProductEntity(name = "Tablet", category = "Electronics", price = 450.0),
            ProductEntity(name = "USB Cable", category = "Accessories", price = 10.0),
            ProductEntity(name = "Webcam", category = "Electronics", price = 120.0),
            ProductEntity(name = "Speaker", category = "Accessories", price = 60.0)
        )
        
        // Очищуємо старі дані та вставляємо нові
        dao.clearAll()
        dao.insertAll(newProducts)
    }
}
