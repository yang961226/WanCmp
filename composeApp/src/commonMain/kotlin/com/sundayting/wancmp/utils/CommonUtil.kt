@file:OptIn(ExperimentalTime::class)

package com.sundayting.wancmp.utils

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun clock(): Long = Clock.System.now().toEpochMilliseconds()