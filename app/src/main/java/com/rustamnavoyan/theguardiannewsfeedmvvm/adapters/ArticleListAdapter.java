package com.rustamnavoyan.theguardiannewsfeedmvvm.adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
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

    public interface OnItemClickListener {
        void onItemClicked(ArticleItem article);
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private ListItemArticleBinding mBinding;

        ArticleViewHolder(ListItemArticleBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }

    private List<ArticleItem> mArticleItemList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private boolean mPinned;
    private int mScreenWidth = -1;

    public ArticleListAdapter(boolean pinned, OnItemClickListener itemClickListener) {
        mPinned = pinned;
        mItemClickListener = itemClickListener;
    }

    public void setArticleList(List<ArticleItem> articleItemList) {
        mArticleItemList = articleItemList;
        notifyDataSetChanged();
    }

    public void clearArticles() {
        mArticleItemList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemArticleBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_item_article, parent, false);
        ArticleViewHolder holder = new ArticleViewHolder(binding);
        if (mScreenWidth == -1) {
            mScreenWidth = getScreenWidth((Activity) binding.getRoot().getContext());
        }
        if (mPinned) {
            binding.getRoot().setBackgroundColor(parent.getContext().getResources().getColor(R.color.pinned_bg_color));
            ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
            layoutParams.width = (int) (mScreenWidth * 0.8);
        }
        binding.getRoot().setOnClickListener(view ->
                mItemClickListener.onItemClicked(mArticleItemList.get(holder.getAdapterPosition())));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleItem articleItem = mArticleItemList.get(position);
        holder.mBinding.setThumbnailUrl(articleItem.getThumbnailUrl());
        holder.mBinding.setTitle(articleItem.getTitle());
        holder.mBinding.setCategory(articleItem.getCategory());
        holder.mBinding.executePendingBindings();
    }

    private int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
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
