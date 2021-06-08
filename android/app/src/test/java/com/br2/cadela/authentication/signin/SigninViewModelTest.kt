package com.br2.cadela.authentication.signin

import com.br2.cadela.InstantExecutorExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
import androidx.lifecycle.Observer
import io.mockk.*

@ExtendWith(value = [InstantExecutorExtension::class])
class SigninViewModelTest {
    private lateinit var api: SigninApi
    private lateinit var tokenRepository: TokenRepository
    private lateinit var loadingObserver: Observer<Boolean>
    private lateinit var signinService: SigninService

    @BeforeEach
    fun setup(){
        loadingObserver = mockk()
        every { loadingObserver.onChanged(any()) } returns Unit

        api = mockk()
        tokenRepository = mockk()

        signinService = spyk(SigninService(api, tokenRepository))
    }

    @Test
    fun `Sign in`() {
        val sut = SigninViewModel(signinService)
        sut.loading.observeForever(loadingObserver)

        val email = "any_email@mail.com"
        val password = "any_password"
        sut.signin(email, password)

        coVerify { signinService.signin(email, password) }
        verifyAll {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
    }
}