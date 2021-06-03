package com.br2.cadela.shared

import com.br2.cadela.authentication.signin.User

data class SigninResponse(val user: User, val token: String)

class Api {
    fun signin(email: String, password: String): SigninResponse {
        return SigninResponse(
            User("any_user_id", "John", "McLane"),
            "any_token"
        )
    }
}