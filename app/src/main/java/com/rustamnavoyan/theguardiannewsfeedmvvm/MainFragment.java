package com.rustamnavoyan.theguardiannewsfeedmvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rustamnavoyan.theguardiannewsfeedmvvm.adapters.ArticleListAdapter;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.repository.db.Article;
import com.rustamnavoyan.theguardiannewsfeedmvvm.util.ConnectionUtil;
import com.rustamnavoyan.theguardiannewsfeedmvvm.viewmodel.ArticlesViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends Fragment implements
        ArticleListAdapter.OnItemClickListener {
    private ArticlesViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView pinnedRecyclerView = view.findViewById(R.id.pinned_articles_recycler_view);
        pinnedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        ArticleListAdapter pinnedAdapter = new ArticleListAdapter(true, this);
        pinnedRecyclerView.setAdapter(pinnedAdapter);

        RecyclerView recyclerView = view.findViewById(R.id.articles_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ArticleListAdapter adapter = new ArticleListAdapter(false, this);
        recyclerView.setAdapter(adapter);
        if (ConnectionUtil.isConnected(getContext())) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    // start loading articles if there are 2 items to reach the last loaded item
                    if (totalItemCount <= (lastVisibleItem + 2)) {
                        adapter.setLoading();
                        mViewModel.downloadArticles();
                    }
                }
            });
            mViewModel.getArticleItems().observe(this, adapter::setArticleList);
            if (savedInstanceState == null) {
                mViewModel.downloadArticles();
            }
        } else {
            mViewModel.getSavedArticleItems().observe(this, articleItems -> {
                if (articleItems != null && !articleItems.isEmpty()) {
                    adapter.setArticleList(convert(articleItems));
                } else {
                    adapter.clearArticles();
                }
            });
            if (savedInstanceState == null) {
                mViewModel.downloadArticles();
            }
        }
        mViewModel.getPinnedArticleItems().observe(this, articleItems -> {
            if (articleItems != null && !articleItems.isEmpty()) {
                pinnedRecyclerView.setVisibility(View.VISIBLE);
                pinnedAdapter.setArticleList(convert(articleItems));
            } else {
                pinnedRecyclerView.setVisibility(View.GONE);
                pinnedAdapter.clearArticles();
            }
        });

        return view;
    }

    private static List<ArticleItem> convert(List<Article> articles) {
        List<ArticleItem> articleItems = new ArrayList<>();
        for (Article article : articles) {
            ArticleItem articleItem = new ArticleItem(article.id);
            articleItem.setThumbnailUrl(article.thumbnailUrl);
            articleItem.setTitle(article.title);
            articleItem.setCategory(article.category);
            articleItem.setApiUrl(article.apiUrl);
            articleItem.setPinned(article.pinned);
            articleItems.add(articleItem);
        }

        return articleItems;
    }

    @Override
    public void onItemClicked(ArticleItem article) {
        Intent intent = new Intent(getContext(), ArticlePageActivity.class);
        intent.putExtra(ArticlePageActivity.EXTRA_ARTICLE_ITEM, article);
        startActivity(intent);
    }
}
