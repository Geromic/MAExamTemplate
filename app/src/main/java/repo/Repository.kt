package repo

import androidx.lifecycle.LiveData
import data.Item
import data.ItemDao

class Repository(private val itemDao: ItemDao) {
    val data: LiveData<List<Item>> = itemDao.getItems()

    suspend fun updateItem(item: Item) {
        itemDao.updateItem(item)
    }

    suspend fun deleteItem(item: Item) {
        itemDao.deleteItem(item)
    }

    suspend fun addItem(item: Item) {
        itemDao.addItem(item)
    }
}