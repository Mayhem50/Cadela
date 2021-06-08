package com.br2.cadela.authentication.signin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(record: TokenRecord)
}