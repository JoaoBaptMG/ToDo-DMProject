package com.joaobapt.todo.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupForm(
    @SerialName("firstname")
    val firstName: String,
    @SerialName("lastname")
    val lastName: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("password_confirmation")
    val passwordConfirmation: String
)
