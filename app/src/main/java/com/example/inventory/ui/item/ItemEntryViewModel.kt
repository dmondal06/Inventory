/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.ItemsRepository
import com.example.inventory.data.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Represents UI state for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

/**
 * Represents details of an Item.
 */
data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = ""
)

/**
 * ViewModel for managing item entry UI state and interactions.
 */
class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    // Holds the UI state of the item entry screen
    private val _itemUiState = MutableStateFlow(ItemUiState())
    val itemUiState: StateFlow<ItemUiState> = _itemUiState

    /**
     * Updates the UI state based on user input.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        _itemUiState.value = ItemUiState(
            itemDetails = itemDetails,
            isEntryValid = validateInput(itemDetails)
        )
    }

    /**
     * Validates the user input. Returns true if all fields are filled.
     */
    private fun validateInput(uiState: ItemDetails = _itemUiState.value.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }

    /**
     * Converts the current UI state to an Item entity and saves it to the database.
     */
    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(_itemUiState.value.itemDetails.toItem())
        }
    }
}

/**
 * Extension function to convert [ItemDetails] to [Item].
 */
fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0
)

/**
 * Extension function to convert [Item] to [ItemUiState].
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails].
 */
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString()
)
