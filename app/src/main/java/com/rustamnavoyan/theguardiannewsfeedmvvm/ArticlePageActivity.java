package com.rustamnavoyan.theguardiannewsfeedmvvm;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rustamnavoyan.theguardiannewsfeedmvvm.databinding.ActivityArticlePageBinding;
import com.rustamnavoyan.theguardiannewsfeedmvvm.manage.PeriodicDownloadManager;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.Article;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.util.ConnectionUtil;
import com.rustamnavoyan.theguardiannewsfeedmvvm.viewmodel.ArticlePageViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ArticlePageActivity extends AppCompatActivity {
    public static final String EXTRA_ARTICLE_ITEM = "com.rustamnavoyan.theguardiannewsfeed.ARTICLE_ITEM";

    private Article mArticle;
    private ArticlePageViewModel mViewModel;

    public static final String IMAGE_TRANSITION_NAME = "image_transition_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ArticlePageViewModel.class);
        ArticleItem articleItem = getIntent().getParcelableExtra(EXTRA_ARTICLE_ITEM);
        mArticle = new Article();
        mArticle.setArticleItem(articleItem);

        ActivityArticlePageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_article_page);
        binding.setImageUrl(articleItem.getThumbnailUrl());
        binding.setTitle(articleItem.getTitle());
        binding.setLifecycleOwner(this);
        binding.setPageViewModel(mViewModel);
        ViewCompat.setTransitionName(binding.articleImage, IMAGE_TRANSITION_NAME);

        boolean connected = ConnectionUtil.isConnected(this);
        if (savedInstanceState == null) {
            if (connected) {
                mViewModel.downloadArticleContent(articleItem.getApiUrl());
            }
            mViewModel.loadArticle(articleItem.getId());
        }
        mViewModel.getArticle().observe(this, article -> {
            if (article != null) {
                if (!connected) {
                    mViewModel.getHideProgress().setValue(true);
                    mViewModel.getContent().setValue(article.bodyText);
                }
                articleItem.setPinned(article.pinned);
                mArticle.setSaved(article.saved);
            }
            invalidateOptionsMenu();
        });
        mViewModel.getContent().observe(this, text -> {
            mArticle.setArticleBodyText(text);
            invalidateOptionsMenu();
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        PeriodicDownloadManager.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();

        PeriodicDownloadManager.schedule();
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

            case R.id.save:
                toggleSave();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.pin).setTitle(mArticle.getArticleItem().isPinned() ? R.string.unpin : R.string.pin);
        menu.findItem(R.id.save).setEnabled(mArticle.getArticleBodyText() != null);
        menu.findItem(R.id.save).setTitle(mArticle != null && mArticle.isSaved() ? R.string.delete : R.string.save);

        return true;
    }

    private void togglePin() {
        mArticle.getArticleItem().setPinned(!mArticle.getArticleItem().isPinned());
        mViewModel.saveArticle(mArticle);
    }

    private void toggleSave() {
        mArticle.setSaved(!mArticle.isSaved());
        mViewModel.saveArticle(mArticle);
    }
}
