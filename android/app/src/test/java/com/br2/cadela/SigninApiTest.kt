package com.br2.cadela

import com.br2.cadela.authentication.signin.SigninApiService
import com.br2.cadela.authentication.signin.SigninApi
import com.br2.cadela.shared.ApiClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import java.net.HttpURLConnection

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SigninApiTest: SigninApiContract() {
    override val sut: SigninApi
        get() = _sut

        private val mockServer = MockWebServer()
        private lateinit var _sut: SigninApi

        @BeforeAll
        fun setup(){
            mockServer.start()
            val mockResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("{\n" +
                        "  \"token\": \"any_token\",\n" +
                        "  \"user\": {\n" +
                        "    \"firstName\": \"John\",\n" +
                        "    \"lastName\": \"McLane\",\n" +
                        "    \"email\": \"any_email@mail.com\",\n" +
                        "    \"password\": \"\$2a\$10\$RxHgxKDLq1YddJSfP8ehEuuWiZmC4aGg8nxX0HQnZovvkFaTtDbYi\",\n" +
                        "    \"id\": \"any_user_id\"\n" +
                        "  }\n" +
                        "}")
            mockServer.enqueue(mockResponse)
            val apiClient = ApiClient(mockServer.url("/"))
            _sut = SigninApi(apiClient)
        }

        @AfterAll
        fun cleanUp(){
            mockServer.shutdown()
        }

}