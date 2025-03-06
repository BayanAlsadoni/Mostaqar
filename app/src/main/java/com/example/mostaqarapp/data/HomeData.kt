package com.example.mostaqarapp.data

import android.os.Parcel
import android.os.Parcelable


class HomeData(
    var id:String?="",
    var ownerName:String?="",
    var ownerId:String?="",
    var ownerImage:String?="",
    var title:String?="",
    var price:String?="",
    var location:String?="",
    var description:String?="",
    var homeImage:String? ="",
    var homeVideo:String? ="",
    var homeType:String? = "",
    var space:String? = "",
    var stayTime:String? = "",
    var homeDetails:HomeDetails? = null): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
//        TODO("homeDetails")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(ownerName)
        parcel.writeString(ownerId)
        parcel.writeString(ownerImage)
        parcel.writeString(title)
        parcel.writeString(price)
        parcel.writeString(location)
        parcel.writeString(description)
        parcel.writeString(homeImage)
        parcel.writeString(homeVideo)
        parcel.writeString(homeType)
        parcel.writeString(space)
        parcel.writeString(stayTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeData> {
        override fun createFromParcel(parcel: Parcel): HomeData {
            return HomeData(parcel)
        }

        override fun newArray(size: Int): Array<HomeData?> {
            return arrayOfNulls(size)
        }
    }
}
