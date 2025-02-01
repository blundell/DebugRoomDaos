package com.blundell.tut.daoshop

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DebugItemDao {
    @Query("DELETE FROM items")
    fun deleteAll()
}
