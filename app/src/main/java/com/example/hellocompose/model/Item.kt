package com.example.hellocompose.model

data class Item(
    val id: Int,
    val title: String,
    val description: String
)

// Тестові дані
val sampleItems = List(20) { i ->
    Item(
        id = i,
        title = "Елемент $i",
        description = "Це детальний опис для елемента $i. Тут може бути більше інформації про цей елемент."
    )
}
