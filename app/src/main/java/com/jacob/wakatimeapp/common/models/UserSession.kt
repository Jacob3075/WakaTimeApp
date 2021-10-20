package com.jacob.wakatimeapp.common.models

class UserSession {
    var loggedInUser: UserDetails? = null
        private set

    fun logInUser(userDetails: UserDetails) {
        loggedInUser = userDetails
    }
}
