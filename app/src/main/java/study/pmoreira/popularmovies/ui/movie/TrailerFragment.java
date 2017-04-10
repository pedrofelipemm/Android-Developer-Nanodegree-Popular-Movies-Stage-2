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
import study.pmoreira.popularmovies.business.TrailerBusiness;
import study.pmoreira.popularmovies.entity.Trailer;
import study.pmoreira.popularmovies.utils.ScreenUtils;

import static study.pmoreira.popularmovies.ui.movie.MovieActivity.ARG_MOVIE_ID;

public class TrailerFragment extends Fragment {

    @BindView(R.id.movie_trailer_recyclerview)
    RecyclerView mTrailerRecyclerView;

    @BindView(R.id.movie_trailer_emptyview)
    TextView mTrailerEmptyView;

    private TrailerBusiness mTrailerBusiness;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_trailer, container, false);
        ButterKnife.bind(this, view);

        mTrailerBusiness = new TrailerBusiness(getContext());

        mTrailerRecyclerView.setLayoutManager(getReviewRecyclerLayoutManager());

        Bundle args;
        if ((args = getArguments()) != null) {
            new TrailerAyncTask().execute(args.getLong(ARG_MOVIE_ID));
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

    private class TrailerAyncTask extends AsyncTask<Long, Void, List<Trailer>> {
        @Override
        protected List<Trailer> doInBackground(Long... params) {
            if (params.length < 1) {
                throw new IllegalArgumentException(getString(R.string.required_parameter, "movieId"));
            }

            return mTrailerBusiness.findTrailers(params[0]);
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            mTrailerRecyclerView.setAdapter(new TrailerAdapter(getContext(), trailers, mTrailerEmptyView));
        }
    }
}
