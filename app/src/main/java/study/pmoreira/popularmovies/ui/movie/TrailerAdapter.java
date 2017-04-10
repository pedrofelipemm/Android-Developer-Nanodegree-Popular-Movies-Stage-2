package study.pmoreira.popularmovies.ui.movie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.pmoreira.popularmovies.R;
import study.pmoreira.popularmovies.entity.Trailer;

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Trailer> mTrailers;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_trailer_container)
        View mContainer;

        @BindView(R.id.movie_trailer_thumb)
        ImageView mTrailerThumbImageView;

        @BindView(R.id.movie_trailer_title)
        TextView mTrailerTitleTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    TrailerAdapter(Context context, List<Trailer> trailers, View emptyView) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        mTrailers = trailers;

        if (trailers.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.movie_trailer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Trailer trailer = mTrailers.get(position);

        holder.mTrailerTitleTextView.setText(mContext.getString(R.string.trailer_number, (position + 1)));
        Picasso.with(mContext)
                .load(trailer.getThumbUrl())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.mTrailerThumbImageView);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getVideoUrl()));
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}
