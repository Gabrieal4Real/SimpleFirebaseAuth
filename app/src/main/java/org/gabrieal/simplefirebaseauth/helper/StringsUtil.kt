package org.gabrieal.simplefirebaseauth.helper

fun String.isEmailValid(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    return Regex(emailRegex).matches(this)
}

//simple validation
fun String.isPasswordValid(): Boolean {
    return this.length >= 6
}