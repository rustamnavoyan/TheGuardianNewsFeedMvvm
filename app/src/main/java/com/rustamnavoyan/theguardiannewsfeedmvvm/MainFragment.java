package com.rustamnavoyan.theguardiannewsfeedmvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rustamnavoyan.theguardiannewsfeedmvvm.adapters.ArticleListAdapter;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.rustamnavoyan.theguardiannewsfeedmvvm.viewmodel.ArticlesViewModel;

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

        RecyclerView recyclerView = view.findViewById(R.id.articles_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ArticleListAdapter adapter = new ArticleListAdapter(this);
        recyclerView.setAdapter(adapter);
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
                    mViewModel.downloadArticles();
                }
            }
        });

        mViewModel.getArticleItems().observe(this, adapter::setArticleList);

        if (savedInstanceState == null) {
            mViewModel.downloadArticles();
        }

        return view;
    }

    @Override
    public void onItemClicked(ArticleItem article) {
        Intent intent = new Intent(getContext(), ArticlePageActivity.class);
        intent.putExtra(ArticlePageActivity.EXTRA_ARTICLE_ITEM, article);
        startActivity(intent);
    }
}
