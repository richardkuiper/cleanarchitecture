package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.richardkuiper.cleanarchitecture.R
import com.richardkuiper.cleanarchitecture.common.Status.Error
import com.richardkuiper.cleanarchitecture.common.Status.Success
import com.richardkuiper.cleanarchitecture.common.observe
import com.richardkuiper.cleanarchitecture.common.viewmodel.viewModel
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.model.UserDetailViewData
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_top_user_detail.*
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class TopUserDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactoryProvider: TopUserDetailViewModelFactoryProvider

    lateinit var viewModel: TopUserDetailViewModel

    private val args: TopUserDetailFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val accountId = args.accountId

        (requireActivity() as? AppCompatActivity)?.supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        viewModel = viewModel(viewModelFactoryProvider.get(accountId)) {
            viewLifecycleOwner.observe(user) { status ->
                when (status) {
                    is Success -> handleSuccess(status.data)
                    is Error -> handleError()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleSuccess(user: UserDetailViewData) {
        userDetailView.visibility = View.VISIBLE
        userDetailErrorView.visibility = View.GONE
        name.text = user.name
        userType.text = user.userType
        reputation.text = user.reputation.toString()
        location.text = user.location
        memberSince.text = DATE_FORMAT.format(user.creationDate)
        downloadProfilePicture(user)
    }

    private fun downloadProfilePicture(user: UserDetailViewData) {
        Glide.with(requireContext())
                .load(user.profileImage)
                .placeholder(R.drawable.ic_person_black_24dp)
                .error(R.drawable.ic_person_black_24dp)
                .transition(DrawableTransitionOptions.withCrossFade(75))
                .into(profilePicture)
    }

    private fun handleError() {
        userDetailView.visibility = View.GONE
        userDetailErrorView.visibility = View.VISIBLE
    }

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    }
}