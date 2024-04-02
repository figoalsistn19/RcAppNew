package com.inventoryapp.rcapp.util

import java.lang.Exception

sealed class Resource<out R> {
    data class Success <out R> (val result: R): Resource<R>()
//    data class Failure (val exception: Exception): Resource<Nothing>()
    data class Failure<out R>(val throwable: Throwable) : Resource<R>()
    data object Loading : Resource<Nothing>()
}