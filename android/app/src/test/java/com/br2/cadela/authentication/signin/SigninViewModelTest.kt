package com.br2.cadela.authentication.signin

import com.br2.cadela.InstantExecutorExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain

@ExtendWith(value = [InstantExecutorExtension::class])
class SigninViewModelTest {
    private lateinit var api: SigninApi
    private lateinit var tokenRepository: TokenRepository
    private lateinit var observer: Observer<Any>
    private lateinit var signinService: SigninService
    private lateinit var sut: SigninViewModel

    private val email = "any_email@mail.com"
    private val password = "any_password"

    @BeforeEach
    fun setup(){
        Dispatchers.setMain(newSingleThreadContext("UI"))
        observer = mockk()
        every { observer.onChanged(any()) } returns Unit

        api = mockk()
        tokenRepository = mockk()

        signinService = spyk(SigninService(api, tokenRepository))
        sut = SigninViewModel(signinService)
    }

    @Test
    fun `Sign in`() = runBlockingTest {
        sut.loading.observeForever(observer)

        sut.signin(email, password)

        coVerify { signinService.signin(email, password) }
        verifyAll {
            observer.onChanged(true)
            observer.onChanged(false)
        }
    }

    @Test
    fun `Fail signin if empty email`() = runBlockingTest {
        sut.error.observeForever(observer)
        sut.signin("", password)
        coVerify { signinService.signin("", password) }
        verify { observer.onChanged("Empty email") }
    }
}