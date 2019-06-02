package com.rustamnavoyan.theguardiannewsfeedmvvm;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rustamnavoyan.theguardiannewsfeedmvvm.databinding.ActivityArticlePageBinding;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.viewmodel.ArticlePageViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ArticlePageActivity extends AppCompatActivity {
    public static final String EXTRA_ARTICLE_ITEM = "com.rustamnavoyan.theguardiannewsfeed.ARTICLE_ITEM";

    private ArticleItem mArticleItem;
    private ArticlePageViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ArticlePageViewModel.class);
        mArticleItem = getIntent().getParcelableExtra(EXTRA_ARTICLE_ITEM);

        ActivityArticlePageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_article_page);
        binding.setImageUrl(mArticleItem.getThumbnailUrl());
        binding.setTitle(mArticleItem.getTitle());
        binding.setLifecycleOwner(this);
        binding.setPageViewModel(mViewModel);

        mViewModel.downloadArticleContent(mArticleItem.getApiUrl());
        mViewModel.loadArticle(mArticleItem.getId());
        mViewModel.getArticle().observe(this, article -> {
            if (article != null) {
                mArticleItem.setPinned(article.pinned);
            }
            invalidateOptionsMenu();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pin:
                togglePin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.pin).setTitle(mArticleItem.isPinned() ? R.string.unpin : R.string.pin);

        return true;
    }

    private void togglePin() {
        mArticleItem.setPinned(!mArticleItem.isPinned());
        mViewModel.updatePinnedState(mArticleItem);
    }
}
