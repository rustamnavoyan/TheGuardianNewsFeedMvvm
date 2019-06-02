package com.rustamnavoyan.theguardiannewsfeedmvvm.viewmodel;

import android.app.Application;
import android.widget.ImageView;

import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.ArticleRepository;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.db.Article;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ArticlePageViewModel extends AndroidViewModel {
    private ArticleRepository mArticleRepository;

    private MutableLiveData<String> mContent = new MutableLiveData<>();
    private MutableLiveData<Boolean> mHideProgress = new MutableLiveData<>();
    private LiveData<Article> mArticle = new MutableLiveData<>();

    public ArticlePageViewModel(@NonNull Application application) {
        super(application);

        mArticleRepository = new ArticleRepository(application);
    }

    public MutableLiveData<String> getContent() {
        return mContent;
    }

    public MutableLiveData<Boolean> getHideProgress() {
        return mHideProgress;
    }

    public LiveData<Article> getArticle() {
        return mArticle;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get().load(imageUrl).into(view);
    }

    public void downloadArticleContent(String url) {
        mArticleRepository.downloadArticleContent(url, content -> {
            mHideProgress.setValue(content.getFields() != null);
            if (content.getFields() != null) {
                mContent.setValue(content.getFields().getBodyText());
            }
        });
    }

    public void saveArticle(com.rustamnavoyan.theguardiannewsfeedmvvm.model.Article article) {
        ArticleItem articleItem = article.getArticleItem();
        Article articleTable = new Article();
        articleTable.id = articleItem.getId();
        articleTable.thumbnailUrl = articleItem.getThumbnailUrl();
        articleTable.title = articleItem.getTitle();
        articleTable.category = articleItem.getCategory();
        articleTable.apiUrl = articleItem.getApiUrl();
        articleTable.bodyText = article.getArticleBodyText();
        articleTable.pinned = articleItem.isPinned();
        articleTable.saved = article.isSaved();

        mArticleRepository.saveArticle(articleTable);
    }

    public void loadArticle(String id) {
        mArticle = mArticleRepository.getArticle(id);
    }
}
