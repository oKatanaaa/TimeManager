package com.okatanaa.timemanager.model

class Event {
    val title: String

    constructor(title: String) {
        this.title = title
    }

    override fun toString(): String {
        return title
    }
}