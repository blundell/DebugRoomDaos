package com.blundell.tut.daoshop

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "items")
class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
)

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Query("SELECT * from items")
    fun streamAll(): Flow<List<Item>>
}


@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ShopDatabase : RoomDatabase(), DaoExtensions {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: ShopDatabase? = null

        /**
         * https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room#6
         */
        fun getDatabase(context: Context): ShopDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ShopDatabase::class.java, "shop_database")
                    .fallbackToDestructiveMigration()
            }
                .build()
                .also { Instance = it }
        }
    }
}


