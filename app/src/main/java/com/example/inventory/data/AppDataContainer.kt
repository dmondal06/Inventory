package com.example.inventory.data

import android.content.Context
import com.example.inventory.data.ItemDatabase
import com.example.inventory.data.OfflineItemsRepository
import com.example.inventory.data.ItemsRepository


/**
 * [AppContainer] implementation that provides an instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    // Initialize the Room database instance
    private val database: ItemDatabase = ItemDatabase.getDatabase(context.applicationContext)

    /**
     * Implementation for [ItemsRepository] using the Room database
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(database.itemDao())
    }
}
