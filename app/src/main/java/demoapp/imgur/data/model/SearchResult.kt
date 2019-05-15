package demoapp.imgur.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/*
    Model for search result
 */
data class SearchResult constructor(
        @SerializedName("data")
        var data: List<Album>,
        var success: Boolean,
        var status: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(Album),
            parcel.readByte() != 0.toByte(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(data)
        parcel.writeByte(if (success) 1 else 0)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchResult> {
        override fun createFromParcel(parcel: Parcel): SearchResult {
            return SearchResult(parcel)
        }

        override fun newArray(size: Int): Array<SearchResult?> {
            return arrayOfNulls(size)
        }
    }
}