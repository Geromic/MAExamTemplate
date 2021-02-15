package data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var level: Int,
    var status: String,
    var from: Int,
    var to: Int
) {
    override fun toString(): String {
        return "$id. $name -- $level -- $status -- $from -- $to"
    }
}
