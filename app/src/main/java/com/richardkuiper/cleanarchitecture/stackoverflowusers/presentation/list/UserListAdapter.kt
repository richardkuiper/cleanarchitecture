package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.richardkuiper.cleanarchitecture.R
import com.richardkuiper.cleanarchitecture.R.dimen
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.UserListAdapter.UserListViewHolder
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.model.UserListViewData

class UserListAdapter : RecyclerView.Adapter<UserListViewHolder>() {

    private val users = mutableListOf<UserListViewData>()
    internal var itemClickListener: OnItemClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return UserListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun setUsers(userList: List<UserListViewData>) {
        users.apply {
            clear()
            addAll(userList)
        }
        notifyDataSetChanged()
    }

    inner class UserListViewHolder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        private val displayName: TextView = itemView.findViewById(R.id.displayName)

        fun bind(user: UserListViewData) {
            displayName.text = user.displayName
            downloadProfilePicture(user)
            itemView.setOnClickListener {
                itemClickListener?.onItemClicked(user.accountId)
            }
        }

        private fun downloadProfilePicture(user: UserListViewData) {
            val requestOptions = RequestOptions()
                    .fitCenter()
                    .transform(RoundedCorners(itemView.context.resources.getDimension(dimen.profile_picture_rounded_corner_radius).toInt()))

            Glide.with(itemView.context)
                    .load(user.profileImage)
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .error(R.drawable.ic_person_black_24dp)
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade(75))
                    .into(profileImage)
        }
    }

    interface OnItemClickedListener {
        fun onItemClicked(accountId: Int)
    }
}