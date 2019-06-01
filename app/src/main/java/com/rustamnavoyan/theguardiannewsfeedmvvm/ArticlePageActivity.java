package com.rustamnavoyan.theguardiannewsfeedmvvm;

import android.os.Bundle;

import com.rustamnavoyan.theguardiannewsfeedmvvm.databinding.ActivityArticlePageBinding;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.viewmodel.ArticlePageViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ArticlePageActivity extends AppCompatActivity {
    public static final String EXTRA_ARTICLE_ITEM = "com.rustamnavoyan.theguardiannewsfeed.ARTICLE_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArticlePageViewModel viewModel = ViewModelProviders.of(this).get(ArticlePageViewModel.class);
        ArticleItem articleItem = getIntent().getParcelableExtra(EXTRA_ARTICLE_ITEM);

        ActivityArticlePageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_article_page);
        binding.setImageUrl(articleItem.getThumbnailUrl());
        binding.setTitle(articleItem.getTitle());
        binding.setLifecycleOwner(this);
        binding.setPageViewModel(viewModel);

        viewModel.downloadArticleContent(articleItem.getApiUrl());
    }
}
