package com.mankovskaya.githubtest.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mankovskaya.githubtest.R
import com.mankovskaya.githubtest.core.android.BaseFragment
import com.mankovskaya.githubtest.databinding.FragmentLoginBinding
import com.mankovskaya.githubtest.model.feature.LoginAction
import com.mankovskaya.githubtest.model.feature.LoginEvent
import com.mankovskaya.githubtest.model.feature.LoginViewModel
import kotlinx.android.synthetic.main.view_toolbar.*
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<LoginViewModel>() {
    override val fragmentViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        val view: View = binding.root
        with(binding) {
            lifecycleOwner = this@LoginFragment
            viewModel = fragmentViewModel
            stateViewModel = fragmentViewModel.stateViewModel
            loginButton.setOnClickListener { fragmentViewModel.reactOnAction(LoginAction.Login) }
            emailEditText.doOnTextChanged { text, _, _, _ ->
                fragmentViewModel.reactOnAction(LoginAction.EmailChanged(text?.toString() ?: ""))
            }
            passwordEditText.doOnTextChanged { text, _, _, _ ->
                fragmentViewModel.reactOnAction(LoginAction.PasswordChanged(text?.toString() ?: ""))
            }
            listenToEvents {
                when(it) {
                    is LoginEvent.NavigateToRepositories -> {
                        findNavController().navigate(R.id.action_loginFragment_to_RepoFragment)
                    }
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}