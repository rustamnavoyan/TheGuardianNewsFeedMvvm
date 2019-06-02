package com.rustamnavoyan.theguardiannewsfeedmvvm.repository.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Article {
    @PrimaryKey
    @NonNull
    public String id;

    public String thumbnailUrl;

    public String title;

    public String category;

    public String apiUrl;

    public String bodyText;

    public boolean pinned;

    public boolean saved;
}
