package com.felo.masane3_test.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felo.masane3_test.data.models.UserModel
import com.felo.masane3_test.data.state.DataState
import com.felo.masane3_test.data.state.LoginUIState
import com.felo.masane3_test.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    val loginUIState = MutableStateFlow(LoginUIState())

    val loginResponse = mutableStateOf(false)


    fun login(
        phone: String,
        password: String
    ) =
        viewModelScope.launch {
            authRepository.login(phone, password).observeForever {
                loginResponse.value = it?.loading?.isLoading ?: false
            }
        }


    fun updateMobileField(mobile: String) {
        loginUIState.update { it.copy(mobileField = mobile) }
    }

    fun updatePasswordField(password: String) {
        loginUIState.update { it.copy(passwordField = password) }
    }

    fun changePasswordVisuablity() {
        loginUIState.update {
            it.copy(isVisuable = !it.isVisuable)
        }
    }
}