package study.pmoreira.popularmovies.ui.catalog;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.business.MovieBusiness;
import study.pmoreira.popularmovies.business.MovieUrlBuilder;
import study.pmoreira.popularmovies.entity.Movie;
import study.pmoreira.popularmovies.utils.NetworkUtils;

public class CatalogActivity extends AppCompatActivity {

    private static final String STATE_MOVIES = "STATE_MOVIES";
    private static final String STATE_TITLE = "STATE_TITLE";
    private static final String STATE_CATALOG_RECYCLERVIEW_LAYOUT = "STATE_CATALOG_RECYCLERVIEW_LAYOUT";

    private static final int CATALOG_COLUMN_SPAN = 2;

    @BindView(R.id.catalog_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.catalog_recyclerview)
    RecyclerView mCatalogRecyclerView;

    @BindView(R.id.error_textview)
    TextView mErrorTextView;

    MovieBusiness mMovieBusiness;

    private List<Movie> mMovies = new ArrayList<>();

    Parcelable mCatalogLayoutState;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mMovieBusiness = new MovieBusiness(this);

        initCatalog(savedInstanceState);
    }

    private void initCatalog(Bundle savedInstanceState) {
        mCatalogRecyclerView.setLayoutManager(new GridLayoutManager(this, CATALOG_COLUMN_SPAN));

        if (savedInstanceState != null && (mMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIES)) != null
                && !mMovies.isEmpty()) {
            mCatalogRecyclerView.setAdapter(new CatalogAdapter(this, mMovies));
            mCatalogLayoutState = savedInstanceState.getParcelable(STATE_CATALOG_RECYCLERVIEW_LAYOUT);
            setTitle(savedInstanceState.getCharSequence(STATE_TITLE));
        } else if (NetworkUtils.isNetworkAvailable(this)) {
            new CatalogAsyncTask().execute();
        } else {
            showError(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(STATE_TITLE, getTitle());
        outState.putParcelableArrayList(STATE_MOVIES, new ArrayList<>(mMovies));
        outState.putParcelable(STATE_CATALOG_RECYCLERVIEW_LAYOUT,
                mCatalogRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.catalog_menu_most_popular:
                new CatalogAsyncTask().execute(MovieUrlBuilder.ORDER_BY_MOST_POPULAR);
                setTitle(getString(R.string.popular_movies));
                return true;
            case R.id.catalog_menu_top_rated:
                new CatalogAsyncTask().execute(MovieUrlBuilder.ORDER_BY_TOP_RATED);
                setTitle(getString(R.string.top_rated_movies));
                return true;
            case R.id.catalog_menu_favorite:
                new FavoriteCatalogAsyncTask(this).execute();
                setTitle(getString(R.string.favorited_movies));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFavorite() && mCatalogLayoutState == null) {
            new FavoriteCatalogAsyncTask(this).execute(MovieUrlBuilder.ORDER_BY_TOP_RATED);
        }
    }

    private void showError(boolean showError) {
        mCatalogRecyclerView.setVisibility(showError ? View.GONE : View.VISIBLE);
        mErrorTextView.setVisibility(showError ? View.VISIBLE : View.GONE);
        mErrorTextView.setText(getString(R.string.error_no_connection));
    }

    private void showError(boolean showError, String errorMessage) {
        showError(showError);
        mErrorTextView.setText(errorMessage);
    }

    public boolean isFavorite() {
        return getTitle().equals(getString(R.string.favorited_movies));
    }

    private class CatalogAsyncTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            String orderBy = MovieUrlBuilder.ORDER_BY_MOST_POPULAR;
            if (params.length > 0) {
                orderBy = params[0];
            }

            return mMovieBusiness.findMovies(orderBy);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mMovies = movies;
            mCatalogRecyclerView.setAdapter(new CatalogAdapter(CatalogActivity.this, movies));
            showError(movies.isEmpty());
        }
    }

    private class FavoriteCatalogAsyncTask extends AsyncTask<String, Void, List<Movie>> {

        private Context mContext;

        private FavoriteCatalogAsyncTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            return mMovieBusiness.findAllFavoriteMovie();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mMovies = movies;
            mCatalogRecyclerView.setAdapter(new CatalogAdapter(CatalogActivity.this, movies));
            showError(movies.isEmpty(), mContext.getString(R.string.no_favorite_movie));
        }
    }
}
