package com.example.android.pokedex;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.android.pokedex.R.id.IMAGE;
import static com.example.android.pokedex.R.id.imgview;

/**
 * Created by rakesh on 6/29/2017.
 */

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {


    Context context;
    Cursor cursor;


    PokedexAdapter(Context c,Cursor cursor)
    {
        this.context = c;
        this.cursor = cursor;
    }

    @Override
    public PokedexViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.singleitem,parent,false);
        PokedexViewHolder pokedexViewHolder = new PokedexViewHolder(row);

        return pokedexViewHolder;
    }

    @Override
    public void onBindViewHolder(PokedexViewHolder holder, int position) {

        if(!cursor.moveToPosition(position))
            return;

        String name = cursor.getString(cursor.getColumnIndex(PokedexContract.PokedexEntry.NAME_COLUMN));
        String image = cursor.getString(cursor.getColumnIndex(PokedexContract.PokedexEntry.IMAGE_COLUMN));
        long id = cursor.getLong(cursor.getColumnIndex(PokedexContract.PokedexEntry._ID));

        Picasso.with(context).load(image).into(holder.img);
        holder.txt.setText(name.toUpperCase());
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }



    class PokedexViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txt;

        public PokedexViewHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.imgview);
            txt = (TextView) itemView.findViewById(R.id.txtview);
        }

    }
}

