package com.vgarshyn.gitapp.rest.model

import com.google.gson.annotations.SerializedName

/**
 * POJO represent GitHub contributor's JSON
 * see more: https://developer.github.com/v3/repos/#list-contributors
 *
 * Created by v.garshyn on 11.02.18.
 */

data class Contributor(
        @SerializedName("login")
        val login: String,

        @SerializedName("id")
        val id: Long,

        @SerializedName("avatar_url")
        val avatarUrl: String,

        @SerializedName("gravatar_id")
        val gravatarId: String,

        @SerializedName("url")
        val url: String,

        @SerializedName("html_url")
        val htmlUrl: String,

        @SerializedName("followers_url")
        val followersUrl: String,

        @SerializedName("following_url")
        val followingUrl: String,

        @SerializedName("gists_url")
        val gistsUrl: String,

        @SerializedName("starred_url")
        val starredUrl: String,

        @SerializedName("subscriptions_url")
        val subscriptionsUrl: String,

        @SerializedName("organizations_url")
        val organizationsUrl: String,

        @SerializedName("repos_url")
        val reposUrl: String,

        @SerializedName("events_url")
        val eventsUrl: String,

        @SerializedName("received_events_url")
        val receivedEventsUrl: String,

        @SerializedName("type")
        val type: String,

        @SerializedName("site_admin")
        val siteAdmin: Boolean,

        @SerializedName("contributions")
        val contributions: Long)

