package com.jacob.data.models

class UserSession {
    var loggedInUser: UserDetails? = null
        private set

    fun logInUser(userDetails: UserDetails) {
        loggedInUser = userDetails
    }
}
