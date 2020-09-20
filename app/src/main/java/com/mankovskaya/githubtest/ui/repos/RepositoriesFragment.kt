package com.mankovskaya.githubtest.ui.repos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mankovskaya.githubtest.R
import com.mankovskaya.githubtest.core.android.BaseFragment
import com.mankovskaya.githubtest.core.paging.PagingManager
import com.mankovskaya.githubtest.databinding.FragmentRepositoriesBinding
import com.mankovskaya.githubtest.domain.feature.repo.*
import com.mankovskaya.githubtest.domain.feature.repo.RepositoriesViewModel.Companion.PAGE_SIZE
import kotlinx.android.synthetic.main.view_toolbar.*
import org.koin.android.viewmodel.ext.android.viewModel

class RepositoriesFragment : BaseFragment<RepositoriesViewModel>(), PagingManager {
    override val fragmentViewModel: RepositoriesViewModel by viewModel()

    private val adapter by lazy { RepositoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listenToEvents { event ->
            when (event) {
                is RepoEvent.NavigateToLogin -> {
                    findNavController().navigate(R.id.action_repoFragment_to_LoginFragment)
                }
                is RepoEvent.NavigateToWelcome -> {
                    findNavController().navigate(R.id.action_repoFragment_to_WelcomeFragment)
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRepositoriesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_repositories, container, false
        )
        val view: View = binding.root
        with(binding) {
            lifecycleOwner = this@RepositoriesFragment
            viewModel = fragmentViewModel
            stateViewModel = fragmentViewModel.stateViewModel
            val repoLayoutManager = LinearLayoutManager(requireContext())
            repoRecyclerView.apply {
                layoutManager = repoLayoutManager
                adapter = this@RepositoriesFragment.adapter
                itemAnimator = DefaultItemAnimator()
            }
            repoRecyclerView.emitPaging(PAGE_SIZE)
            fragmentViewModel.getStateRelay().observe(this@RepositoriesFragment as LifecycleOwner,
                Observer<RepositoriesSearchState> {
                    adapter.setItems(it.repositories)
                    adapter.showLoading(it.lazyLoad)
                    configureAuthButton(it.authButton)
                })
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.repoFragment)).build()
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.inflateMenu(R.menu.repo_menu)
        val item: MenuItem = toolbar.menu.findItem(R.id.action_search)
        val searchView = androidx.appcompat.widget.SearchView(requireContext())
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
        item.actionView = searchView
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fragmentViewModel.reactOnAction(RepositorySearchAction.SearchChanged(query))
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    fragmentViewModel.reactOnAction(RepositorySearchAction.SearchChanged(newText))
                }
                return false
            }
        })
        val itemAuth: MenuItem = toolbar.menu.findItem(R.id.action_authButton)
        itemAuth.setOnMenuItemClickListener {
            fragmentViewModel.reactOnAction(RepositorySearchAction.AuthButtonPressed)
            false
        }
    }

    private fun configureAuthButton(
        authButtonState: AuthButtonState
    ) {
        val itemAuth: MenuItem = toolbar.menu.findItem(R.id.action_authButton)
        itemAuth.title = getString(authButtonState.textId)
    }
}