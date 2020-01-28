package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.richardkuiper.cleanarchitecture.R
import com.richardkuiper.cleanarchitecture.common.Status.Error
import com.richardkuiper.cleanarchitecture.common.Status.Success
import com.richardkuiper.cleanarchitecture.common.observe
import com.richardkuiper.cleanarchitecture.common.viewmodel.viewModel
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.TopUserDetailFragmentArgs
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.UserListAdapter.OnItemClickedListener
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.model.UserListViewData
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_top_user_list.*
import javax.inject.Inject

class TopUserListFragment : DaggerFragment() {

    private lateinit var adapter: UserListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: TopUserListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel = viewModel(viewModelFactory) {
            viewLifecycleOwner.observe(users) { status ->
                when (status) {
                    is Success -> handleSuccess(status.data)
                    is Error -> handleError()
                }
            }
        }
    }

    private fun handleSuccess(list: List<UserListViewData>) {
        if (list.isEmpty()) {
            handleError()
        } else {
            userList.visibility = View.VISIBLE
            userListErrorView.visibility = View.GONE
            adapter.setUsers(list)
        }
    }

    private fun handleError() {
        userList.visibility = View.GONE
        userListErrorView.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        adapter = UserListAdapter()

        userList.addItemDecoration(
                DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                )
        )
        userList.adapter = adapter
        userList.layoutManager = LinearLayoutManager(requireContext())
        adapter.itemClickListener = object : OnItemClickedListener {
            override fun onItemClicked(accountId: Int) {
                navigateToDetailFragment(accountId)
            }
        }
    }

    private fun navigateToDetailFragment(accountId: Int) {
        NavHostFragment.findNavController(this)
                .navigate(
                        R.id.toTopUserDetailFragment,
                        TopUserDetailFragmentArgs(accountId).toBundle()
                )
    }
}