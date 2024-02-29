/*
 * Copyright (C) 2022 - present Instructure, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.instructure.pandautils.utils

import androidx.work.Data
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

fun Any.toJson(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    return Gson().fromJson<T>(this, T::class.java)
}

fun Long.humanReadableByteCount(): String {
    val unit = 1024
    if (this < unit) return "$this B"
    val exp = (ln(this.toDouble()) / ln(unit.toDouble())).toInt()
    val pre = "KMGTPE"[exp - 1].toString()
    return String.format(Locale.getDefault(), "%.1f %sB", this / unit.toDouble().pow(exp.toDouble()), pre)
}

fun Long?.orDefault(default: Long = 0): Long {
    return this ?: default
}

fun Int?.orDefault(default: Int = 0): Int {
    return this ?: default
}

fun Boolean?.orDefault(default: Boolean = false): Boolean {
    return this ?: default
}

fun Double?.orDefault(default: Double = 0.0): Double {
    return this ?: default
}

fun Data.newBuilder(): Data.Builder {
    return Data.Builder()
        .putAll(this)
}

suspend fun retry(retryCount: Int = 5, initialDelay: Long = 100, factor: Float = 2f, maxDelay: Long = 1000, block: suspend () -> Unit) {
    var currentDelay = initialDelay
    repeat(retryCount.coerceAtLeast(1)) {
        try {
            block()
            return
        } catch (e: Exception) {
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
    }
}

suspend fun <T> poll(
    pollInterval: Long = 1000,
    maxAttempts: Int = 10,
    block: suspend () -> T?,
    validate: suspend (T) -> Boolean
): T? {
    var attempts = 0
    while (attempts < maxAttempts || maxAttempts == -1) {
        val result = block()
        result?.let {
            if (validate(it)) {
                return result
            }
        }
        attempts++
        delay(pollInterval)
    }
    return null
}