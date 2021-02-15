package viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import data.Item
import data.ItemDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.Repository

class ItemViewModel(application: Application): AndroidViewModel(application) {
    val data: LiveData<List<Item>>
    private val repository: Repository

    init {
        val itemDao = ItemDatabase.getDatabase(application).itemDao()
        repository = Repository(itemDao)
        data = repository.data
    }

    fun updateItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(item)
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItem(item)
        }
    }
}