package com.rustamnavoyan.theguardiannewsfeedmvvm.repository.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Article.class}, version = 1)
public abstract class ArticleDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "article_database";
    private static ArticleDatabase INSTANCE;

    public static ArticleDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ArticleDatabase.class, DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public abstract ArticleDao getArticleDao();
}
