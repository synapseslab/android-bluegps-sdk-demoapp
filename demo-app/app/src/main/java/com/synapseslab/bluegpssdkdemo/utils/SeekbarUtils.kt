/*
 * Copyright (c) 2023 Synapses s.r.l.s. All rights reserved.
 *
 * Licensed under the Apache License.
 * You may obtain a copy of the License at
 *
 * https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/LICENSE.md
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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