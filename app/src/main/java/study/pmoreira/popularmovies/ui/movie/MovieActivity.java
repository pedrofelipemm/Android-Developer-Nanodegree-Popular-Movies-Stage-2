package study.pmoreira.popularmovies.ui.movie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.entity.Movie;
import study.pmoreira.popularmovies.utils.NetworkUtils;
import study.pmoreira.popularmovies.utils.ScreenUtils;

import static study.pmoreira.popularmovies.ui.movie.OverviewFragment.ARG_MOVIE;

public class MovieActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    public static final String ARG_MOVIE_ID = "ARG_MOVIE_ID";

    @BindView(R.id.movie_poster_imageview)
    ImageView mPosterImageView;

    @BindView(R.id.movie_title_textview)
    TextView mTitleTextView;

    @BindView(R.id.movie_appBar)
    AppBarLayout mMovieAppBar;

    @BindView(R.id.movie_progressbar)
    ProgressBar mMovieProgressBar;

    @Nullable
    @BindView(R.id.movie_tabLayout)
    TabLayout mMovieTabLayout;

    @Nullable
    @BindView(R.id.movie_viewPager)
    ViewPager mMovieViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.movie_activity_network_error, Toast.LENGTH_LONG).show();
        } else {
            showLoading();
        }

        initViews();
    }

    @SuppressWarnings("ConstantConditions")
    private void initViews() {
        Movie movie = Movie.EMPTY_MOVIE;
        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

            mTitleTextView.setText(movie.getTitle());

            Picasso.with(this)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(mPosterImageView);
        }

        if (ScreenUtils.isTabletLandscape(this)) {
            initFragments(movie);
        } else {
            mMovieViewPager.setAdapter(new MovieTabsAdapter(this, getSupportFragmentManager(), movie));
            mMovieTabLayout.setupWithViewPager(mMovieViewPager);
        }
    }

    private void initFragments(Movie movie) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);

        OverviewFragment overviewFragment = new OverviewFragment();
        overviewFragment.setArguments(args);

        args = new Bundle();
        args.putLong(ARG_MOVIE_ID, movie.getId());

        TrailerFragment trailerFragment = new TrailerFragment();
        trailerFragment.setArguments(args);

        ReviewFragment reviewFragment = new ReviewFragment();
        reviewFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movie_overview_container, overviewFragment)
                .replace(R.id.movie_trailer_container, trailerFragment)
                .replace(R.id.movie_review_container, reviewFragment)
                .commit();

    }

    void showLoading() {
        mMovieProgressBar.setVisibility(View.VISIBLE);
    }

    void hideLoading() {
        mMovieProgressBar.setVisibility(View.GONE);
    }
}
