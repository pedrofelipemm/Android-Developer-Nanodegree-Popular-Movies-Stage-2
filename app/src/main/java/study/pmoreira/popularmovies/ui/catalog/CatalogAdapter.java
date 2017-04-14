package study.pmoreira.popularmovies.ui.catalog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.entity.Movie;
import study.pmoreira.popularmovies.ui.movie.MovieActivity;

class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Movie> mMovies;

    CatalogAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        mMovies = movies;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster_imageview)
        ImageView posterImageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.catalog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);

        if (movie != null) {
            Picasso.with(mContext)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.posterImageView);

            holder.posterImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)
                            mContext, v, mContext.getString(R.string.transition_movie_thumbnail));

                    Intent intent = new Intent(mContext, MovieActivity.class);
                    intent.putExtra(MovieActivity.EXTRA_MOVIE, movie);
                    intent.putExtra(CatalogActivity.EXTRA_FROM_FAV, ((CatalogActivity) mContext).isFavorite());

                    mContext.startActivity(intent, options.toBundle());

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}
