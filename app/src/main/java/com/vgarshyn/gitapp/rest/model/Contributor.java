package com.vgarshyn.gitapp.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * POJO represent GitHub contributor's JSON
 * see more: https://developer.github.com/v3/repos/#list-contributors
 *
 * Created by v.garshyn on 11.02.18.
 */

public class Contributor {
    @SerializedName("login")
    public String login;

    @SerializedName("id")
    public Long id;

    @SerializedName("avatar_url")
    public String avatarUrl;

    @SerializedName("gravatar_id")
    public String gravatarId;

    @SerializedName("url")
    public String url;

    @SerializedName("html_url")
    public String htmlUrl;

    @SerializedName("followers_url")
    public String followersUrl;

    @SerializedName("following_url")
    public String followingUrl;

    @SerializedName("gists_url")
    public String gistsUrl;

    @SerializedName("starred_url")
    public String starredUrl;

    @SerializedName("subscriptions_url")
    public String subscriptionsUrl;

    @SerializedName("organizations_url")
    public String organizationsUrl;

    @SerializedName("repos_url")
    public String reposUrl;

    @SerializedName("events_url")
    public String eventsUrl;

    @SerializedName("received_events_url")
    public String receivedEventsUrl;

    @SerializedName("type")
    public String type;

    @SerializedName("site_admin")
    public Boolean siteAdmin;

    @SerializedName("contributions")
    public Long contributions;
}
