package id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.DetailActivity;
import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.R;
import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.model.NowPlaying;

/**
 * Created by paymin on 14/05/2017.
 */

public class TRAdapter extends RecyclerView.Adapter<TRAdapter.ViewHolder> {

    private List<NowPlaying> nowPlayings;
    private Context context;

    public TRAdapter(List<NowPlaying> nowPlayings, Context context) {
        this.nowPlayings = nowPlayings;
        this.context = context;
    }

    @Override
    public TRAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.np_list_item, parent, false);
        return  new TRAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TRAdapter.ViewHolder holder, final int position) {
        final NowPlaying nowPlaying = nowPlayings.get(position);


        holder.textViewHead.setText(nowPlaying.getHead());
        holder.textViewDesc.setText(nowPlaying.getDesc());

        Glide
                .with(context)
                .load(nowPlaying.getImageUrl())
                .into(holder.imageViewOtof);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleBlogIntent = new Intent(context, DetailActivity.class);
                singleBlogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                singleBlogIntent.putExtra("blog_id", position);
                singleBlogIntent.putExtra("jenis", "TR");
                context.startActivity(singleBlogIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nowPlayings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewHead;
        public TextView textViewDesc;
        public ImageView imageViewOtof;
        public LinearLayout linearLayout;
        public TextView textViewReview;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            imageViewOtof = (ImageView) itemView.findViewById(R.id.imageViewOtof);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            //textViewReview = (TextView) itemView.findViewById(R.id.textViewReview);

            //textViewHead.setText();
        }
    }
}
