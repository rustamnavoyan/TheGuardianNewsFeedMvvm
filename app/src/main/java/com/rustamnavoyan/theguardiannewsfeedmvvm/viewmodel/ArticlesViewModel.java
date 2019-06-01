package com.rustamnavoyan.theguardiannewsfeedmvvm.viewmodel;

import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArticlesViewModel extends ViewModel {
    private ArticleRepository mArticleRepository = new ArticleRepository();

    private int mPage;
    private MutableLiveData<List<ArticleItem>> mArticleItems = new MutableLiveData<>();

    public LiveData<List<ArticleItem>> getArticleItems() {
        return mArticleItems;
    }

    public void downloadArticles() {
        mPage++;
        mArticleRepository.downloadArticleList(mPage, articles -> {
            List<ArticleItem> items = new ArrayList<>();
            if (mArticleItems.getValue() != null) {
                items.addAll(mArticleItems.getValue());
            }
            items.addAll(articles);
            mArticleItems.setValue(items);
        });
    }
}
