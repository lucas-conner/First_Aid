package com.example.pa4

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "first_aid_info")
data class FirstAidInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val term: String,
    val overview: String,
    val immediateActions: String,
    val symptoms: String = "",
    val commonTriggers: String = "",
    val treatmentAndAftercare: String = "",
    val preventionAndPreparedness: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(term)
        parcel.writeString(overview)
        parcel.writeString(immediateActions)
        parcel.writeString(symptoms)
        parcel.writeString(commonTriggers)
        parcel.writeString(treatmentAndAftercare)
        parcel.writeString(preventionAndPreparedness)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FirstAidInfo> {
        override fun createFromParcel(parcel: Parcel): FirstAidInfo {
            return FirstAidInfo(parcel)
        }

        override fun newArray(size: Int): Array<FirstAidInfo?> {
            return arrayOfNulls(size)
        }
    }
}
