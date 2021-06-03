package com.br2.cadela.authentication.signin

import androidx.room.Entity

@Entity(tableName =  "tokens", primaryKeys = ["userId"])
data class TokenRecord(val token: String, val userId: String)