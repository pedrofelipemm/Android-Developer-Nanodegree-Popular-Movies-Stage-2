package study.pmoreira.popularmovies.ui.catalog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.entity.Movie;
import study.pmoreira.popularmovies.ui.movie.MovieActivity;

class CatalogAdapter extends ArrayAdapter<Movie> {

    CatalogAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    static class ViewHolder {

        @BindView(R.id.poster_imageview)
        ImageView posterImageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    @Override
    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.catalog_item, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Movie movie = getItem(position);

        if (movie != null) {
            Picasso.with(getContext())
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.posterImageView);

            holder.posterImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)
                            getContext(), v, getContext().getString(R.string.transition_movie_thumbnail));

                    Intent intent = new Intent(getContext(), MovieActivity.class);
                    intent.putExtra(MovieActivity.EXTRA_MOVIE, movie);

                    getContext().startActivity(intent, options.toBundle());

                }
            });
        }

        return convertView;
    }
}
