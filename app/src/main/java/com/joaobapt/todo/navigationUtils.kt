package com.joaobapt.todo

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.removeNavigationResult(key: String = "result") {
    findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)
}

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <T> Fragment.getNavigationParam(key: String) =
    findNavController().previousBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.removeNavigationParam(key: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.remove<T>(key)
}

fun <T> Fragment.setNavigationParam(result: T, key: String) {
    findNavController().currentBackStackEntry?.savedStateHandle?.set(key, result)
}
