package com.rustamnavoyan.theguardiannewsfeedmvvm.repository;


import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.data.Result;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.network.ArticlesApiClient;

import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {
    public interface OnDownloadCallback {
        void onDownloaded(List<ArticleItem> articleItems);
    }

    public interface OnContentDownloadCallback {
        void onDownloaded(Result result);
    }

    private static final int PAGE_SIZE = 10;

    public void downloadArticleList(int page, OnDownloadCallback callback) {
        ArticlesApiClient articlesApiClient = new ArticlesApiClient();
        articlesApiClient.getArticleList(page, PAGE_SIZE, response -> {
            List<Result> results = response.getResponse().getResults();
            List<ArticleItem> articleItems = new ArrayList<>();
            for (Result result : results) {
                ArticleItem articleItem = new ArticleItem();
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
}
