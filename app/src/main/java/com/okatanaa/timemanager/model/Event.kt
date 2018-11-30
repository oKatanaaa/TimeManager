package com.okatanaa.timemanager.model

import android.os.Parcel
import android.os.Parcelable

class Event(var title: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return title
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}