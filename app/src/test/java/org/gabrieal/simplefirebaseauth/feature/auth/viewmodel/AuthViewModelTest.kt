package org.gabrieal.simplefirebaseauth.feature.auth.viewmodel

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        auth = mockk()
        viewModel = AuthViewModel(auth)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
        unmockkStatic(Log::class)
    }

    @Test
    fun `verifyAndSaveEmail sets error for invalid email`() = runTest {
        // Given
        val invalidEmail = "invalid"

        // When
        viewModel.verifyAndSaveEmail(invalidEmail)

        // Then
        assertEquals("Invalid email", viewModel.uiState.value.errorEmail)
    }

    @Test
    fun `verifyAndSaveEmail clears error for valid email`() = runTest {
        // Given
        val validEmail = "test@example.com"

        // When
        viewModel.verifyAndSaveEmail(validEmail)

        // Then
        assertEquals(null, viewModel.uiState.value.errorEmail)
    }

    @Test
    fun `verifyAndSavePassword sets error for invalid password`() = runTest {
        // Given
        val shortPassword = "123"

        // When
        viewModel.verifyAndSavePassword(shortPassword)

        // Then
        assertEquals("Password must be at least 6 characters", viewModel.uiState.value.errorPassword)
    }

    @Test
    fun `verifyAndSavePassword clears error for valid password`() = runTest {
        // Given
        val validPassword = "123456"

        // When
        viewModel.verifyAndSavePassword(validPassword)

        // Then
        assertEquals(null, viewModel.uiState.value.errorPassword)
    }

    @Test
    fun `toggleRegister flips isRegisterMode`() = runTest {
        // Given
        val before = viewModel.uiState.value.isRegisterMode

        // When
        viewModel.toggleRegister()

        // Then
        assertEquals(!before, viewModel.uiState.value.isRegisterMode)
    }

    @Test
    fun `setError sets error in state`() = runTest {
        // Given
        val error = "Some error"

        // When
        viewModel.setError(error)

        // Then
        assertEquals("Some error", viewModel.uiState.value.error)
    }

    @Test
    fun `registerViaFirebase success clears loading and error`() = runTest {
        // Given
        val task = mockk<Task<AuthResult>>()
        every { auth.createUserWithEmailAndPassword(any(), any()) } returns task
        every { task.addOnSuccessListener(any()) } answers {
            firstArg<OnSuccessListener<AuthResult>>().onSuccess(mockk())
            task
        }
        every { task.addOnFailureListener(any()) } returns task

        viewModel.verifyAndSaveEmail("test@example.com")
        viewModel.verifyAndSavePassword("123456")

        // When
        viewModel.registerViaFirebase()

        // Then
        assertEquals(false, viewModel.uiState.value.loading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `registerViaFirebase failure sets error and clears loading`() = runTest {
        // Given
        val task = mockk<Task<AuthResult>>()
        val exception = Exception("Register failed")

        every { auth.createUserWithEmailAndPassword(any(), any()) } returns task
        every { task.addOnSuccessListener(any()) } returns task
        every { task.addOnFailureListener(any()) } answers {
            firstArg<OnFailureListener>().onFailure(exception)
            task
        }

        viewModel.verifyAndSaveEmail("test@example.com")
        viewModel.verifyAndSavePassword("123456")

        // When
        viewModel.registerViaFirebase()

        // Then
        assertEquals(false, viewModel.uiState.value.loading)
        assertEquals("Register failed", viewModel.uiState.value.error)
    }

    @Test
    fun `authenticateUser success clears loading and error`() = runTest {
        // Given
        val task = mockk<Task<AuthResult>>()
        every { auth.signInWithEmailAndPassword(any(), any()) } returns task
        every { task.addOnSuccessListener(any()) } answers {
            firstArg<OnSuccessListener<AuthResult>>().onSuccess(mockk())
            task
        }
        every { task.addOnFailureListener(any()) } returns task

        viewModel.verifyAndSaveEmail("test@example.com")
        viewModel.verifyAndSavePassword("123456")

        // When
        viewModel.authenticateUser()

        // Then
        assertEquals(false, viewModel.uiState.value.loading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `authenticateUser failure sets error and clears loading`() = runTest {
        // Given
        val task = mockk<Task<AuthResult>>()
        val exception = Exception("Login failed")

        every { auth.signInWithEmailAndPassword(any(), any()) } returns task
        every { task.addOnSuccessListener(any()) } returns task
        every { task.addOnFailureListener(any()) } answers {
            firstArg<OnFailureListener>().onFailure(exception)
            task
        }

        viewModel.verifyAndSaveEmail("test@example.com")
        viewModel.verifyAndSavePassword("123456")

        // When
        viewModel.authenticateUser()

        // Then
        assertEquals(false, viewModel.uiState.value.loading)
        assertEquals("Login failed", viewModel.uiState.value.error)
    }
}
