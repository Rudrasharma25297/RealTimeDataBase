package com.example.rtdb2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";

    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerGenres;
    ListView listViewArtists;

    List<Artist> artists;

    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);

        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        artists = new ArrayList<>();


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        });


        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Artist artist = artists.get(i);
                Intent intent = new Intent(getApplicationContext(),AddTrackActivity.class);
                intent.putExtra(ARTIST_ID, artist.getArtistId());
                intent.putExtra(ARTIST_NAME, artist.getArtistName());

                startActivity(intent);
            }
        });

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artists.get(i);
                showUpdateDialog(artist.getArtistId(), artist.getArtistName());


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artists.clear();
                for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    Artist artist = artistSnapshot.getValue(Artist.class);

                    artists.add(artist);
                }

                ArtistList adapter = new ArtistList(MainActivity.this, artists);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

       private void showUpdateDialog(final String artistId, String artistName){
           AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
           LayoutInflater inflater = getLayoutInflater();
           final View dialogView = inflater.inflate(R.layout.update_dialog, null);

           dialogBuilder.setView(dialogView);

           final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextTrackName);
           final Spinner spinnerGenres = (Spinner)  dialogView.findViewById(R.id.spinnerGenres);
           final Button   buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);

           dialogBuilder.setTitle("Updating Artist" +artistName);

           final AlertDialog alertDialog = dialogBuilder.create();
           alertDialog.show();

           buttonUpdate.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String name = editTextName.getText().toString().trim();
                   String genre = spinnerGenres.getSelectedItem().toString();

                   if(TextUtils.isEmpty(name)){
                       editTextName.setError("Name required");
                       return;
                   }

                   updateArtist(artistId, name, genre);
                   alertDialog.dismiss();

               }
           });


    }


     private boolean updateArtist(String id, String name, String genre){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artists").child(id);
        Artist artist = new Artist(id, name, genre);
        databaseReference.setValue(artist);
        Toast.makeText(this, "Artist Update Successfully", Toast.LENGTH_LONG).show();
        return true;
     }

    private void addArtist(){
        String name = editTextName.getText().toString().trim();
        String genre= spinnerGenres.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name)){
            String id = databaseArtists.push().getKey();
            Artist artist = new Artist(id, name, genre);
            databaseArtists.child(id).setValue(artist);
            Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "You should enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
