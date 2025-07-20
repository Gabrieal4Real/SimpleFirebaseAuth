package org.gabrieal.simplefirebaseauth.feature.auth.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.gabrieal.simplefirebaseauth.BaseActivity
import org.gabrieal.simplefirebaseauth.R
import org.gabrieal.simplefirebaseauth.databinding.ActivityAuthBinding
import org.gabrieal.simplefirebaseauth.feature.auth.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity() {

    private lateinit var binding: ActivityAuthBinding

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onBindData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        binding.lifecycleOwner = this
    }

    override fun setupUI() {
        binding.emailTIET.doOnTextChanged { email, _, _, _ ->
            viewModel.verifyAndSaveEmail(email.toString())
        }

        binding.passwordTIET.doOnTextChanged { password, _, _, _ ->
            viewModel.verifyAndSavePassword(password.toString())
        }

        binding.authBtn.setOnClickListener {
            if (viewModel.uiState.value.isRegisterMode) {
                viewModel.registerViaFirebase()
                return@setOnClickListener
            }

            viewModel.loginViaFirebase()
        }

        binding.authToggleTV.setOnClickListener {
            viewModel.toggleRegister()
        }
    }

    override fun observeResponses() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(viewModel.uiState) {
                    launch {
                        map { it.loading }
                            .distinctUntilChanged() // only triggers when changed or assigned first
                            .collectLatest { showLoading ->
                                binding.progressBarLL.visibility = if (showLoading) {
                                    View.VISIBLE
                                } else {
                                    View.GONE
                                }
                            }
                    }

                    launch {
                        map { it.isRegisterMode }
                            .distinctUntilChanged()
                            .collectLatest { isRegister ->
                                toggleRegisterUI(isRegister)
                            }
                    }

                    launch {
                        map { it.errorEmail }
                            .collectLatest { error ->
                                binding.emailTIET.error = error
                            }
                    }

                    launch {
                        map { it.errorPassword }
                            .collectLatest { error ->
                                binding.passwordTIET.error = error
                            }
                    }

                    launch {
                        map { it.enableButton }
                            .distinctUntilChanged()
                            .collectLatest { enabled ->
                                binding.authBtn.isEnabled = enabled
                            }
                    }

                    launch {
                        map { it.error }
                            .distinctUntilChanged()
                            .drop(1) // drop initial value so it doesnt get triggered on launch
                            .collectLatest { error ->
                                showDialog(error ?: "Something went wrong")
                            }
                    }
                }
            }
        }
    }

    private fun toggleRegisterUI(isRegister: Boolean) {
        binding.emailTIET.setText("")
        binding.passwordTIET.setText("")
        binding.emailTIET.error = null
        binding.passwordTIET.error = null

        if (isRegister) {
            binding.authToggleTV.text = getString(R.string.already_a_member_click_here_to_login)
            binding.authBtn.text = getString(R.string.register)
            return
        }

        binding.authToggleTV.text = getString(R.string.not_registered_yet_click_here_to_sign_up)
        binding.authBtn.text = getString(R.string.login)
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Notice")
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton("OK") { dialog, _ ->
                viewModel.setError(null)
                dialog.dismiss()
            }
            .show()
    }
}