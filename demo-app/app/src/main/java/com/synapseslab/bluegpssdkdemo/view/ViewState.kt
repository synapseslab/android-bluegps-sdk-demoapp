/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 5/12/21 10:13 AM.
 * Last modified 5/12/21 10:13 AM
 */

package com.synapseslab.bluegpssdkdemo.view

/**
 * State for managing UI operations.
 */
sealed class ViewState<T> {
    class Loading<T> : ViewState<T>()
    class Success<T>(val data: T) : ViewState<T>()
    class Failed<T>(val message: String) : ViewState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}
