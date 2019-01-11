package com.okatanaa.timemanager.services

import com.okatanaa.timemanager.model.Time

object Settings {
    val DEFAULT_GLOBAL_START_TIME = Time(7, 0)

    /**
     * This value can be reinitialized during initializing the global model.
     * It needs to be initialized because creating events depends on this value and it must
     * be a nonnull value.
     */
    var globalStartTime = DEFAULT_GLOBAL_START_TIME
}