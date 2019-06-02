package com.rustamnavoyan.theguardiannewsfeedmvvm.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleItem implements Parcelable {
    private String mThumbnailUrl;
    private String mTitle;
    private String mCategory;

    public ArticleItem() {
    }

    protected ArticleItem(Parcel in) {
        mThumbnailUrl = in.readString();
        mTitle = in.readString();
        mCategory = in.readString();
    }

    public static final Creator<ArticleItem> CREATOR = new Creator<ArticleItem>() {
        @Override
        public ArticleItem createFromParcel(Parcel in) {
            return new ArticleItem(in);
        }

        @Override
        public ArticleItem[] newArray(int size) {
            return new ArticleItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mThumbnailUrl);
        parcel.writeString(mTitle);
        parcel.writeString(mCategory);
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
}
