package com.example.mostaqarapp.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint


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
    var room:String? = "",
    var bathroom:String? = "",
    var lat:Double = 3.0,
    var lng:Double = 3.0,
): Parcelable {
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
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
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
        parcel.writeString(room)
        parcel.writeString(bathroom)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
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
