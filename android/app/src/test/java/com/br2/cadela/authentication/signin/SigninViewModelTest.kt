package com.br2.cadela.authentication.signin

import androidx.lifecycle.Observer
import com.br2.cadela.InstantExecutorExtension
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(value = [InstantExecutorExtension::class])
class SigninViewModelTest {
    private lateinit var api: SigninApi
    private lateinit var tokenRepository: TokenRepository
    private lateinit var loadingObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<String>
    private lateinit var signinService: SigninService
    private lateinit var sut: SigninViewModel

    private val email = "any_email@mail.com"
    private val password = "any_password"

    @BeforeEach
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(newSingleThreadContext("UI"))
        loadingObserver = mockk()
        every { loadingObserver.onChanged(any()) } returns Unit
        errorObserver = mockk()
        every { errorObserver.onChanged(any()) } returns Unit

        api = mockk()
        tokenRepository = mockk()

        signinService = spyk(SigninService(api, tokenRepository))
        sut = SigninViewModel(signinService)
    }

    @Test
    fun `Sign in`() = runBlockingTest {
        sut.loading.observeForever(loadingObserver)
        sut.error.observeForever(errorObserver)

        sut.signin(email, password)

        coVerify { signinService.signin(email, password) }
        verifyAll {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
    }

    @Test
    fun `Fail signin if empty email`() = runBlocking {
        sut.error.observeForever(errorObserver)
        sut.signin("", password)
        coVerify { signinService.signin("", password) }
        verify { errorObserver.onChanged("Empty email") }
    }

    @Test
    fun `Update error if empty password`() = runBlocking {
        sut.error.observeForever(errorObserver)
        sut.signin(email, "")
        coVerify { signinService.signin(email, "") }
        verify { errorObserver.onChanged("Empty password") }
    }

    @Test
    fun `Update error if service throws`() = runBlocking {
        coEvery { signinService.signin(any(), any()) } throws Exception("Problem")

        sut = SigninViewModel(signinService)
        sut.error.observeForever(errorObserver)
        sut.signin(email, password).join()
        coVerify { signinService.signin(any(), any()) }
        coVerify { errorObserver.onChanged("Problem") }
    }

    @Test
    fun `Reset error on new signin`() = runBlocking {
        sut.error.observeForever(errorObserver)
        sut.signin("", password).join()
        coVerify { signinService.signin(any(), any()) }
        coVerify { errorObserver.onChanged("Empty email") }
        sut.signin(email, password).join()
        coVerify { errorObserver.onChanged("") }
    }
}