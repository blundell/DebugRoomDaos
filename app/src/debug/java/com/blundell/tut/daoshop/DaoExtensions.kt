package com.blundell.tut.daoshop

/**
 * DAO extensions available in the debug source set only (matching interface in the release source set)
 */
interface DaoExtensions {
    fun debugItemDao(): DebugItemDao
}
