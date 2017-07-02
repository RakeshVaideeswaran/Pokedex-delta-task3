package com.example.android.pokedex;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class HistoryActivity extends AppCompatActivity{

    RecyclerView rv;
    SQLiteDatabase db;
    PokedexAdapter adapter;
    PokedexDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rv = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        dbHelper = new PokedexDbHelper(this);
        db  = dbHelper.getWritableDatabase();

        Cursor cursor = getAllItems();
        adapter = new PokedexAdapter(this,cursor);
        rv.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                long id = (long) viewHolder.itemView.getTag();
                deleteItem(id);

            }
        }).attachToRecyclerView(rv);

    }

    public void deleteItem(long id)
    {
        db.delete(PokedexContract.PokedexEntry.TABLENAME, PokedexContract.PokedexEntry._ID + "=" + id,null);
        adapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Cursor cursor = getAllItems();
        //adapter = new PokedexAdapter(this,cursor);
        rv.setAdapter(adapter);

    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.clearhistory,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(adapter.getItemCount() > 0)
        {
            db.delete(PokedexContract.PokedexEntry.TABLENAME,null,null);
            adapter.notifyDataSetChanged();

        }

        Intent i = new Intent();
        setResult(RESULT_OK,i);
        finish();

        return true;
    }


    public Cursor getAllItems()
    {
        return db.query(
                PokedexContract.PokedexEntry.TABLENAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

    }

}
