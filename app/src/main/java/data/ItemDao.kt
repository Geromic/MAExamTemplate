package data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {
    @Insert
    suspend fun addItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    /*@Query("SELECT * FROM ITEM_TABLE WHERE id=:id")
    fun getItemById(id: String)*/

    @Query("SELECT * FROM ITEM_TABLE ORDER BY id ASC")
    fun getItems(): LiveData<List<Item>>
}