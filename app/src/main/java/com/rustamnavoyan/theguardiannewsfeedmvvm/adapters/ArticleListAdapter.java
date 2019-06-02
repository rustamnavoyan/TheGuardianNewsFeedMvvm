package com.rustamnavoyan.theguardiannewsfeedmvvm.adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
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

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.AbstractViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(View view, ArticleItem article);
    }

    static abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
        AbstractViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ArticleViewHolder extends AbstractViewHolder {
        private ListItemArticleBinding mBinding;

        ArticleViewHolder(ListItemArticleBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }

    static class ProgressViewHolder extends AbstractViewHolder {
        ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_PROGRESS = 1;

    private List<ArticleItem> mArticleItemList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private boolean mPinned;
    private int mScreenWidth = -1;
    private boolean mLoading;

    public ArticleListAdapter(boolean pinned, OnItemClickListener itemClickListener) {
        mPinned = pinned;
        mItemClickListener = itemClickListener;
    }

    public void setLoading() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        notifyItemInserted(mArticleItemList.size());
    }

    private void setLoaded() {
        if (!mLoading) {
            return;
        }
        mLoading = false;
        notifyItemRemoved(mArticleItemList.size());
    }

    public void setArticleList(List<ArticleItem> articleItemList) {
        setLoaded();

        mArticleItemList = articleItemList;
        notifyDataSetChanged();
    }

    public void clearArticles() {
        setLoaded();

        mArticleItemList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
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
                    mItemClickListener.onItemClicked(binding.thumbnail, mArticleItemList.get(
                            holder.getAdapterPosition())));
            return holder;
        }

        return new ProgressViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_progress, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            ArticleItem articleItem = mArticleItemList.get(position);
            articleViewHolder.mBinding.setThumbnailUrl(articleItem.getThumbnailUrl());
            articleViewHolder.mBinding.setTitle(articleItem.getTitle());
            articleViewHolder.mBinding.setCategory(articleItem.getCategory());
            articleViewHolder.mBinding.executePendingBindings();
        }
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
    public int getItemViewType(int position) {
        if (position < mArticleItemList.size()) {
            return VIEW_TYPE_ITEM;
        }
        return VIEW_TYPE_PROGRESS;
    }

    @Override
    public int getItemCount() {
        if (mArticleItemList.isEmpty()) {
            return 0;
        }
        // 1 for progress
        return mPinned || !mLoading ? mArticleItemList.size() : mArticleItemList.size() + 1;
    }
}
