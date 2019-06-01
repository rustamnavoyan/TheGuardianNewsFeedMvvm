package com.rustamnavoyan.theguardiannewsfeedmvvm.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rustamnavoyan.theguardiannewsfeedmvvm.R;
import com.rustamnavoyan.theguardiannewsfeedmvvm.databinding.ListItemArticleBinding;
import com.rustamnavoyan.theguardiannewsfeedmvvm.model.ArticleItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private ListItemArticleBinding mBinding;

        ArticleViewHolder(ListItemArticleBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }

    private List<ArticleItem> mArticleItemList = new ArrayList<>();

    public void setArticleList(List<ArticleItem> articleItemList) {
        mArticleItemList = articleItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemArticleBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_item_article, parent, false);
        return new ArticleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleItem articleItem = mArticleItemList.get(position);
        holder.mBinding.setThumbnailUrl(articleItem.getThumbnailUrl());
        holder.mBinding.setTitle(articleItem.getTitle());
        holder.mBinding.setCategory(articleItem.getCategory());
        holder.mBinding.executePendingBindings();
    }

    @BindingAdapter({"bind:thumbnail"})
    public static void loadThumbnail(ImageView view, String thumbnailUrl) {
        Picasso.get().load(thumbnailUrl).into(view);
    }

    @Override
    public int getItemCount() {
        return mArticleItemList.size();
    }
}
