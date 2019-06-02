package com.rustamnavoyan.theguardiannewsfeedmvvm.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.data.Result;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.db.Article;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.db.ArticleDatabase;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.network.ArticlesApiClient;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class ArticleRepository {
    public interface OnDownloadCallback {
        void onDownloaded(List<ArticleItem> articleItems);
    }

    public interface OnContentDownloadCallback {
        void onDownloaded(Result result);
    }

    private static final int PAGE_SIZE = 10;
    private ArticleDatabase mDatabase;

    public ArticleRepository(Context context) {
        mDatabase = ArticleDatabase.getDatabase(context);
    }

    public void downloadArticleList(int page, OnDownloadCallback callback) {
        ArticlesApiClient articlesApiClient = new ArticlesApiClient();
        articlesApiClient.getArticleList(page, PAGE_SIZE, response -> {
            List<Result> results = response.getResponse().getResults();
            List<ArticleItem> articleItems = new ArrayList<>();
            for (Result result : results) {
                ArticleItem articleItem = new ArticleItem(result.getId());
                articleItem.setTitle(result.getWebTitle());
                // TODO Probably this is not the category
                articleItem.setCategory(result.getPillarName());
                if (result.getFields() != null) {
                    articleItem.setThumbnailUrl(result.getFields().getThumbnail());
                }
                articleItem.setApiUrl(result.getApiUrl());
                articleItems.add(articleItem);
            }

            callback.onDownloaded(articleItems);
        });
    }

    public void downloadArticleContent(String url, OnContentDownloadCallback callback) {
        new ArticlesApiClient().getArticleContents(url, response ->
                callback.onDownloaded(response.getResponse().getContent()));
    }

    public LiveData<List<Article>> loadPinnedArticles() {
        return mDatabase.getArticleDao().loadPinnedArticles();
    }

    public LiveData<Article> getArticle(String articleId) {
        return mDatabase.getArticleDao().getArticle(articleId);
    }

    public void saveArticle(Article article) {
        new ArticleSaver(mDatabase).execute(article);
    }

    private static class ArticleSaver extends AsyncTask<Article, Void, Void> {
        private ArticleDatabase mDatabase;

        public ArticleSaver(ArticleDatabase database) {
            mDatabase = database;
        }

        @Override
        protected Void doInBackground(Article... articles) {
            Article savingArticle = articles[0];
            if (savingArticle.pinned || savingArticle.saved) {
                mDatabase.getArticleDao().insertArticle(articles[0]);
            } else {
                mDatabase.getArticleDao().deleteArticle(articles[0]);
            }

            return null;
        }
    }
}
