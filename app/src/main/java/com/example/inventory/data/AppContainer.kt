package com.example.inventory.data

import android.content.Context

interface AppContainer {
    val itemsRepository: ItemsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    private val database: InventoryDatabase by lazy {
        InventoryDatabase.getDatabase(context)
    }

    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(database.itemDao())
    }
}
