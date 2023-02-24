/*
 * Copyright (c) 2022 Synapses s.r.l.s. All rights reserved.
 * https://synapseslab.com/
 */

package com.synapseslab.bluegpssdkdemo.utils


fun getConvertedValue(value: Float, maxValue: Float, max: Float, min: Float): Float {
    return convertRange(
        0f,
        maxValue,
        min,
        max,
        value
    )
}

fun convertRange(
    originalStart: Float,
    originalEnd: Float, // original range
    newStart: Float,
    newEnd: Float, // desired range
    value: Float
): Float {
    val scale = (newEnd - newStart) / (originalEnd - originalStart)
    return newStart + (value - originalStart) * scale
}