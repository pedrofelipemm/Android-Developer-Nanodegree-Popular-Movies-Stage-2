package study.pmoreira.popularmovies.ui.movie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.business.ReviewBusiness;
import study.pmoreira.popularmovies.entity.Review;
import study.pmoreira.popularmovies.utils.ScreenUtils;

import static study.pmoreira.popularmovies.ui.movie.MovieActivity.ARG_MOVIE_ID;

public class ReviewFragment extends Fragment {

    @BindView(R.id.movie_review_recyclerview)
    RecyclerView mReviewRecylerView;

    @BindView(R.id.movie_review_emptyview)
    TextView mReviewEmptyView;

    private ReviewBusiness mReviewBusiness;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_review, container, false);
        ButterKnife.bind(this, view);

        mReviewBusiness = new ReviewBusiness(getContext());

        mReviewRecylerView.setLayoutManager(getReviewRecyclerLayoutManager());

        Bundle args;
        if ((args = getArguments()) != null) {
            new ReviewAyncTask().execute(args.getLong(ARG_MOVIE_ID));
        }

        return view;
    }

    private LayoutManager getReviewRecyclerLayoutManager() {
        LayoutManager layoutManager;

        if (ScreenUtils.isTabletLandscape(getContext())) {
            layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        } else if (ScreenUtils.isTablet(getContext()) || ScreenUtils.isLandscape(getContext())) {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            layoutManager = new LinearLayoutManager(getContext());
        }

        return layoutManager;
    }

    private void hideLoading() {
        ((MovieActivity) getActivity()).hideLoading();
    }

    private class ReviewAyncTask extends AsyncTask<Long, Void, List<Review>> {
        @Override
        protected List<Review> doInBackground(Long... params) {
            if (params.length < 1) {
                throw new IllegalArgumentException(getString(R.string.required_parameter, "movieId"));
            }

            return mReviewBusiness.findReviews(params[0]);
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            mReviewRecylerView.setAdapter(new ReviewAdapter(getContext(), reviews, mReviewEmptyView));
            hideLoading();
        }
    }

}
