package com.example.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserProfile(
  val email: String,
  val displayName: String,
  val role: String, // "ADMIN" or "USER"
  val isAdmin: Boolean = false,
  val joinedDate: String = "Nov 2026",
  val avatarInitials: String = "YT"
)

class AuthViewModel : ViewModel() {

  private val _isLoggedIn = MutableStateFlow(true) // Start logged in with admin demo
  val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

  private val _currentUser = MutableStateFlow<UserProfile?>(
    UserProfile(
      email = "sharanukrishnapur48@gmail.com",
      displayName = "Sharana (Admin)",
      role = "SUPER_ADMIN",
      isAdmin = true,
      joinedDate = "Nov 2026",
      avatarInitials = "SK"
    )
  )
  val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

  private val _authError = MutableStateFlow<String?>(null)
  val authError: StateFlow<String?> = _authError.asStateFlow()

  fun loginWithEmail(email: String, pass: String): Boolean {
    if (email.isBlank() || !email.contains("@")) {
      _authError.value = "ಮಾನ್ಯವಾದ ಇಮೇಲ್ ಐಡಿ ನಮೂದಿಸಿ (Enter valid email)"
      return false
    }
    if (pass.length < 4) {
      _authError.value = "ಪಾಸ್‌ವರ್ಡ್ ಕನಿಷ್ಠ 4 ಅಕ್ಷರಗಳಾಗಿರಬೇಕು (Password min 4 chars)"
      return false
    }

    val isAdmin = email.contains("admin") || email.contains("sharanu")
    val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
    _currentUser.value = UserProfile(
      email = email,
      displayName = name,
      role = if (isAdmin) "SUPER_ADMIN" else "CREATOR_USER",
      isAdmin = isAdmin,
      avatarInitials = name.take(2).uppercase()
    )
    _isLoggedIn.value = true
    _authError.value = null
    return true
  }

  fun registerWithEmail(name: String, email: String, pass: String): Boolean {
    if (name.isBlank() || email.isBlank() || pass.isBlank()) {
      _authError.value = "ದಯವಿಟ್ಟು ಎಲ್ಲಾ ವಿವರಗಳನ್ನು ಭರ್ತಿ ಮಾಡಿ (Please fill all fields)"
      return false
    }
    _currentUser.value = UserProfile(
      email = email,
      displayName = name,
      role = "CREATOR_USER",
      isAdmin = false,
      avatarInitials = name.take(2).uppercase()
    )
    _isLoggedIn.value = true
    _authError.value = null
    return true
  }

  fun logout() {
    _currentUser.value = null
    _isLoggedIn.value = false
    _authError.value = null
  }

  fun clearError() {
    _authError.value = null
  }
}
