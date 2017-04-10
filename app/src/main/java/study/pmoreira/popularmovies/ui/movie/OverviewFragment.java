package study.pmoreira.popularmovies.ui.movie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.business.MovieBusiness;
import study.pmoreira.popularmovies.entity.Movie;

import static study.pmoreira.popularmovies.ui.movie.MovieActivity.EXTRA_MOVIE;

public class OverviewFragment extends Fragment {

    private static final int ONE_HOUR_MINUTES = 60;

    static final String ARG_MOVIE = EXTRA_MOVIE;

    @BindView(R.id.movie_runtime_textview)
    TextView mRuntimeTextView;

    @BindView(R.id.movie_overview_textview)
    TextView mOverviewTextView;

    @BindView(R.id.movie_release_date_textview)
    TextView mReleaseDateTextView;

    @BindView(R.id.movie_rating_bar)
    RatingBar mMovieRatingBar;

    @BindView(R.id.movie_favorite_switch)
    SwitchCompat mMovieFavSwitch;

    private MovieBusiness mMovieBusiness;

    private Movie mMovie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_overview, container, false);
        ButterKnife.bind(this, view);

        mMovieBusiness = new MovieBusiness(getContext());

        Bundle args;
        if ((args = getArguments()) != null && (mMovie = args.getParcelable(ARG_MOVIE)) != null) {
            initViews(mMovie);
        }

        return view;
    }

    private void initViews(Movie movie) {
        mOverviewTextView.setText(movie.getOverview());
        mReleaseDateTextView.setText(getString(R.string.relase_date_formatted, movie.getReleaseDate()));
        mMovieRatingBar.setRating(movie.getVoteAverage().floatValue() / 2);

        new FavMovieAsyncTask().execute(movie.getTitle());

        if (movie.getRuntime() != null) {
            mRuntimeTextView.setText(formatRuntime(movie.getRuntime()));
            setUpFavSwitch();
            hideLoading();
        } else {
            new MovieAyncTask().execute(movie.getId());
        }
    }

    private void hideLoading() {
        ((MovieActivity) getActivity()).hideLoading();
    }

    private void setUpFavSwitch() {
        final Movie movie = mMovie;
        mMovieFavSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovieFavSwitch.isChecked()) {
                    new InsertMovieAsyncTask().execute(movie);
                } else {
                    new DeleteMovieAsyncTask().execute(movie.getId());
                }
            }
        });
    }

    private class MovieAyncTask extends AsyncTask<Long, Void, Integer> {
        @Override
        protected Integer doInBackground(Long... params) {
            if (params.length < 1) {
                throw new IllegalArgumentException(getString(R.string.required_parameter, "movieId"));
            }

            return mMovieBusiness.findRuntime(params[0]);
        }

        @Override
        protected void onPostExecute(Integer runtime) {
            mRuntimeTextView.setText(formatRuntime(runtime));
            mMovie.setRuntime(runtime);
            setUpFavSwitch();
            hideLoading();
        }
    }

    private class FavMovieAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            if (params.length < 1) {
                throw new IllegalArgumentException(getString(R.string.required_parameter, "movieTitle"));
            }

            return mMovieBusiness.exists(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean exists) {
            mMovieFavSwitch.setChecked(exists);
        }
    }

    private class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Movie... params) {
            if (params.length < 1) {
                throw new IllegalArgumentException(getString(R.string.required_parameter, Movie.class.getName()));
            }

            return mMovieBusiness.insert(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean exists) {
            if (!exists) {
                Toast.makeText(getContext(), R.string.movie_fav_error, Toast.LENGTH_SHORT).show();
            }
            mMovieFavSwitch.setChecked(exists);
        }
    }

    private class DeleteMovieAsyncTask extends AsyncTask<Long, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Long... params) {
            if (params.length < 1) {
                throw new IllegalArgumentException(getString(R.string.required_parameter, "movieId"));
            }

            return mMovieBusiness.delete(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean deleted) {
            if (!deleted) {
                Toast.makeText(getContext(), R.string.movie_unfav_error, Toast.LENGTH_SHORT).show();
                mMovieFavSwitch.setChecked(true);
            }
        }
    }

    private String formatRuntime(Integer runtime) {
        if (runtime == null || runtime < 1) {
            return getString(R.string.runtime_not_available);
        }

        int hour = runtime / ONE_HOUR_MINUTES;
        int min = runtime % ONE_HOUR_MINUTES;
        return getString(R.string.runtime_formatted, hour, min);
    }
}
