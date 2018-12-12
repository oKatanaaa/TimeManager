package com.okatanaa.timemanager.services

import com.okatanaa.timemanager.model.Time

object Settings {
    lateinit var globalStartTime: Time

    val DEFAULT_GLOBAL_START_TIME = Time(7, 0)
}