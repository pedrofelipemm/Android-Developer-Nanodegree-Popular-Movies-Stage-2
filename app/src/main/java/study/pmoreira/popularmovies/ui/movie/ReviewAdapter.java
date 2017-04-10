package study.pmoreira.popularmovies.ui.movie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.entity.Review;

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Review> mReviews;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_review_author)
        TextView mReviewAuthorTextView;

        @BindView(R.id.movie_review_content)
        TextView mReviewContentTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    ReviewAdapter(Context context, List<Review> reviews, View emptyView) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        mReviews = reviews;

        if (reviews.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.movie_review_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Review review = mReviews.get(position);

        holder.mReviewAuthorTextView.setText(review.getAuthor());
        holder.mReviewContentTextView.setText(review.getContent());
        holder.mReviewContentTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}
