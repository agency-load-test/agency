package com.onit.routing

import com.onit.service_calls.ServiceCall

interface Route {
    fun isDone() : Boolean
    fun next() : ServiceCall
}