package com.papanews.ak;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.papanews.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<ItemModel> items;
    URL imageURL;
    private Context mContext;
    int cc=-1,ccc=0;
    int[] gbg = { R.drawable.gradientbackground, R.drawable.gradientbackground2, R.drawable.gradientbackgroound3};


    public CardStackAdapter(List<ItemModel> items, Context context) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_pager, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
        Log.e("getImage position - ", String.valueOf(position));

        int han[]={0,1,2,1,0};
        if(cc>=4){
            cc=1;
        }else{
            cc++;
        }
//        Log.e("TAGhan", String.valueOf(cc));
//        Log.e("TAGhan", String.valueOf(han[cc]));
        holder.CardbgColor.setBackgroundResource(gbg[han[cc]]);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, shortDe, longDe, long_text, news_views,video, audio,date,converted;
        ImageView sourcimg;
        ImageView image;
        TextView sourcetxt;
        RelativeLayout CardbgColor;
        Button bt;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.item_age);
            long_text = itemView.findViewById(R.id.longText);
            news_views = itemView.findViewById(R.id.views);
            image = itemView.findViewById(R.id.imageimage);
            title = itemView.findViewById(R.id.news_title);
            shortDe = itemView.findViewById(R.id.discription);
            longDe = itemView.findViewById(R.id.item_city);
            sourcimg  = itemView.findViewById(R.id.srcimage);
            sourcetxt = itemView.findViewById(R.id.srcName);
            CardbgColor = itemView.findViewById(R.id.CardbgColor);
            audio = itemView.findViewById(R.id.item_name);
            converted = itemView.findViewById(R.id.converted);
            date = itemView.findViewById(R.id.date);

        }

        void setData(ItemModel data) {
            title.setText(data.gettitle());
            CardbgColor.setBackgroundColor(Color.parseColor("#222b34"));
            Log.e("getImage", String.valueOf(data.getImage()));

            Picasso.get().load(data.getSourceimage()).placeholder(R.drawable.noimage)
                    .resize(250, 250)
                    .into(sourcimg);


            Picasso.get().load(data.getImage()).placeholder(R.drawable.noimage)
                    .resize(550, 550)
                    .into(image);

            sourcetxt.setText(data.getSourcename());

            shortDe.setText(data.getshortd());
            longDe.setText(data.getlongd());
            news_views.setText(data.getViews());
            long_text.setText(data.getLongText());
            video.setText(data.getVideo());
            audio.setText(data.getAudioType());
            converted.setText(data.getConverted());
            date.setText(data.getDate());

        }
    }

    public List<ItemModel> getItems() {
        return items;
    }
    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

}
