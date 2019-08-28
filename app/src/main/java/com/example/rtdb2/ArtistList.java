package com.example.rtdb2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;

public class ArtistList extends ArrayAdapter<Artist> {

    private Activity context;
    private List<Artist> artistList;
    public ArtistList(Activity context, List<Artist> artistList){

        super(context, R.layout.list_layout, artistList);
        this.context = context;
        this.artistList = artistList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        Artist artist = artistList.get(position);

        textViewName.setText(artist.getArtistName());
        textViewGenre.setText(artist.getArtistGenres());

        return listViewItem;
    }
}
