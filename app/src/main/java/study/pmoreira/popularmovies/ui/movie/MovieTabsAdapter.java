package study.pmoreira.popularmovies.ui.movie;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.entity.Movie;

import static study.pmoreira.popularmovies.R.string.movie_fragment_invalid_position;
import static study.pmoreira.popularmovies.ui.movie.MovieActivity.ARG_MOVIE_ID;
import static study.pmoreira.popularmovies.ui.movie.OverviewFragment.ARG_MOVIE;

class MovieTabsAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 3;

    private Context mContext;
    private Movie mMovie;

    MovieTabsAdapter(Context context, FragmentManager fm, Movie movie) {
        super(fm);
        mContext = context;
        mMovie = movie;
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args = new Bundle();

        switch (position) {
            case 0:
                args.putParcelable(ARG_MOVIE, mMovie);

                fragment = new OverviewFragment();
                fragment.setArguments(args);
                break;
            case 1:
                args.putLong(ARG_MOVIE_ID, mMovie.getId());

                fragment = new TrailerFragment();
                fragment.setArguments(args);
                break;
            case 2:
                args.putLong(ARG_MOVIE_ID, mMovie.getId());

                fragment = new ReviewFragment();
                fragment.setArguments(args);
                break;
            default:
                throw new IllegalArgumentException(mContext.getString(movie_fragment_invalid_position, position));
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.movie_fragment_title1);
            case 1:
                return mContext.getString(R.string.movie_fragment_title2);
            case 2:
                return mContext.getString(R.string.movie_fragment_title3);
            default:
                throw new IllegalArgumentException(mContext.getString(movie_fragment_invalid_position, position));
        }
    }
}
