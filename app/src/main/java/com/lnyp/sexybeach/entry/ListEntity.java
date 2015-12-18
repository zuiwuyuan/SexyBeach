package com.lnyp.sexybeach.entry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李宁 on 2015/12/18.
 */
public class ListEntity implements Parcelable {
    private int gallery;
    private int id;
    private String src;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gallery);
        dest.writeInt(this.id);
        dest.writeString(this.src);
    }

    public ListEntity() {
    }

    protected ListEntity(Parcel in) {
        this.gallery = in.readInt();
        this.id = in.readInt();
        this.src = in.readString();
    }

    public static final Parcelable.Creator<ListEntity> CREATOR = new Parcelable.Creator<ListEntity>() {
        public ListEntity createFromParcel(Parcel source) {
            return new ListEntity(source);
        }

        public ListEntity[] newArray(int size) {
            return new ListEntity[size];
        }
    };

    public int getGallery() {
        return gallery;
    }

    public void setGallery(int gallery) {
        this.gallery = gallery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
