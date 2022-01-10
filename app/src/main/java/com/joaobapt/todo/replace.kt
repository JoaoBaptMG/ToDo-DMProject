package com.joaobapt.todo

// Support function
fun <E> Iterable<E>.replace(old: E, new: E) = map { if (it == old) new else it }

