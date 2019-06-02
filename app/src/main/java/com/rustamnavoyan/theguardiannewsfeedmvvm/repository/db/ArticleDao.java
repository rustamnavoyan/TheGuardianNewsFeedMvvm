package com.rustamnavoyan.theguardiannewsfeedmvvm.repository.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM article WHERE pinned == 1")
    LiveData<List<Article>> loadPinnedArticles();

    @Query("SELECT * FROM article WHERE saved == 1")
    LiveData<List<Article>> loadSavedArticles();

    @Query("SELECT * FROM article WHERE id=:id LIMIT 1")
    LiveData<Article> getArticle(String id);

    @Insert(onConflict = REPLACE)
    void insertArticle(Article article);

    @Delete
    void deleteArticle(Article article);
}
