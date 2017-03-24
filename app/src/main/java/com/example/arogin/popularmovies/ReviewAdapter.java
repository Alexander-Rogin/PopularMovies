package com.example.arogin.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by arogin on 3/24/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private final ReviewAdapter.ReviewAdapterOnClickHandler mClickHandler;
    Context mContext;
    private Review[] mReviews;

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shoulAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shoulAttachToParentImmediately);

        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        Review review = mReviews[position];
        holder.mReviewTV.setText(review.getReviewIntro());
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        }
        return mReviews.length;
    }

    public void setReviewsData(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public ReviewAdapter(ReviewAdapter.ReviewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface ReviewAdapterOnClickHandler {
        void onClick(Review chosenReview);
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mReviewTV;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewTV = (TextView) view.findViewById(R.id.tv_review_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Review review = mReviews[adapterPosition];
            mClickHandler.onClick(review);
        }
    }
}
