package com.rustamnavoyan.theguardiannewsfeedmvvm.viewmodel;

import android.widget.ImageView;

import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.ArticleRepository;
import com.squareup.picasso.Picasso;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArticlePageViewModel extends ViewModel {
    private ArticleRepository mArticleRepository = new ArticleRepository();

    private MutableLiveData<String> mContent = new MutableLiveData<>();
    private MutableLiveData<Boolean> mHideProgress = new MutableLiveData<>();

    public MutableLiveData<String> getContent() {
        return mContent;
    }

    public MutableLiveData<Boolean> getHideProgress() {
        return mHideProgress;
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
}
