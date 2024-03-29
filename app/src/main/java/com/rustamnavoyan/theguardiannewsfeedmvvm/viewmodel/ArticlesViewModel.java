package com.rustamnavoyan.theguardiannewsfeedmvvm.viewmodel;

import android.app.Application;

import com.rustamnavoyan.theguardiannewsfeedmvvm.manage.PreferenceHelper;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.ArticleRepository;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.db.Article;
import com.rustamnavoyan.theguardiannewsfeedmvvm.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ArticlesViewModel extends AndroidViewModel {
    private ArticleRepository mArticleRepository;

    private int mPage;
    private MutableLiveData<List<ArticleItem>> mArticleItems = new MutableLiveData<>();
    private LiveData<List<Article>> mSavedArticleItems;
    private LiveData<List<Article>> mPinnedArticleItems;

    public ArticlesViewModel(@NonNull Application application) {
        super(application);

        mArticleRepository = new ArticleRepository(application);
        mSavedArticleItems = mArticleRepository.loadSavedArticles();
        mPinnedArticleItems = mArticleRepository.loadPinnedArticles();
    }

    public MutableLiveData<List<ArticleItem>> getArticleItems() {
        return mArticleItems;
    }

    public LiveData<List<Article>> getSavedArticleItems() {
        return mSavedArticleItems;
    }

    public LiveData<List<Article>> getPinnedArticleItems() {
        return mPinnedArticleItems;
    }

    public void downloadArticles() {
        mPage++;
        mArticleRepository.downloadArticleList(mPage, articles -> {
            // TODO Do in better place
            if (mPage == 1) {
                Date date = DateTimeUtil.parseDate(articles.get(0).getPublishedDate());
                PreferenceHelper.getInstance(getApplication()).saveDate(date);
            }
            List<ArticleItem> items = new ArrayList<>();
            if (mArticleItems.getValue() != null) {
                items.addAll(mArticleItems.getValue());
            }
            items.addAll(articles);
            mArticleItems.setValue(items);
        });
    }

    public void loadSavedArticles() {
        mPage++;
        mArticleRepository.loadSavedArticles();
    }
}
