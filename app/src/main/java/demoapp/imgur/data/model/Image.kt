package demoapp.imgur.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/*
    Model for each image
 */

data class Image constructor(
        @SerializedName("id")
        val id: String,

        @SerializedName("title")
        val title: String?,

        @SerializedName("description")
        val description: String?,

        @SerializedName("type")
        val type: String?,

        @SerializedName("width")
        val width: Int?,

        @SerializedName("height")
        val height: Int?,

        @SerializedName("nsfw")
        val nsfw: Boolean?,

        @SerializedName("link")
        val link: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(type)
        parcel.writeValue(width)
        parcel.writeValue(height)
        parcel.writeValue(nsfw)
        parcel.writeString(link)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }

}