package com.ufabc.docchain.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ufabc.docchain.data.AuthRepositoryI
import com.ufabc.docchain.data.AuthRepositoryImpl
import com.ufabc.docchain.presentation.ActivityStatus.LOADING
import com.ufabc.docchain.presentation.ActivityStatus.NORMAL
import com.ufabc.docchain.presentation.LoginViewModelAction.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(), LoginI {

    private val authRepository: AuthRepositoryI = AuthRepositoryImpl()

    private val _state = MutableLiveData<LoginViewModelState>()

    private val _action = MutableLiveData<LoginViewModelAction>()

    val state: LiveData<LoginViewModelState>
        get() = _state

    val action: LiveData<LoginViewModelAction>
        get() = _action

    init {
        _state.postValue(LoginViewModelState())
    }

    override fun submitLogin(email: String, password: String) {
        val success = validateInputs(email, password)

        if (success) {
            updateLoginStatus(LOADING)
            CoroutineScope(Dispatchers.Main).launch {
                val result = authRepository.signIn(email, password)

                if (result.isSuccess) {
                    val loggedInUserName = result.getOrElse { UNKNOWN_USER_STRING }
                    Log.d("DEBUG", "Logged in user name: [$loggedInUserName]")

                    postAction(StartMenuActivity(loggedInUserName))
                } else {
                    postAction(ShowFailAuthenticationToast)
                }

                updateLoginStatus(NORMAL)
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return if (email.isEmpty()) {
            postAction(ShowEmptyEmailInputToast)
            false
        } else if (password.isEmpty()) {
            postAction(ShowEmptyPasswordInputToast)
            false
        } else {
            true
        }
    }

    private fun updateLoginStatus(status: ActivityStatus) {
        val currentState = _state.value ?: LoginViewModelState()
        val newState = currentState.copy(loginStatus = status)

        postState(newState)
    }

    private fun postState(newState: LoginViewModelState?) {
        if (newState != null) {
            _state.postValue(newState)
        }
    }

    private fun postAction(action: LoginViewModelAction) {
        _action.value = action
    }

    companion object {
        private const val UNKNOWN_USER_STRING = "usuário não identificado"

        private const val EMPTY_STRING = ""
    }
}