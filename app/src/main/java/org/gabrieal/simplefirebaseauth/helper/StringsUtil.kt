package org.gabrieal.simplefirebaseauth.helper

fun String.isEmailValid(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

//simple validation
fun String.isPasswordValid(): Boolean {
    return this.length >= 6
}