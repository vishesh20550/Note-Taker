package com.example.notetaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    static ArrayList<String> notes= new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView= findViewById(R.id.textView3);
        ListView listView= findViewById(R.id.listView);
        sharedPreferences= getApplicationContext().getSharedPreferences("com.example.notetaker", Context.MODE_PRIVATE);
        HashSet<String> set= (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if(set==null) {
            textView.setVisibility(View.INVISIBLE);
            notes.add("Example Note");
        }
        else if(set.size()==0){
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            notes = new ArrayList<>(set);
        }
        arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(),NoteEditor.class);
            intent.putExtra("noteId",position);
            startActivity(intent);
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete")
                    .setMessage("Do you want to delete it permanently ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        notes.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        if(notes.isEmpty())
                            textView.setVisibility(View.VISIBLE);
                        HashSet<String> hashSet= new HashSet<>(MainActivity.notes);
                        sharedPreferences.edit().putStringSet("notes",hashSet).apply();
                    })
                    .setNegativeButton("No",null)
                    .show();
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.addNote){
            textView.setVisibility(View.INVISIBLE);
            Intent intent= new Intent(getApplicationContext(),NoteEditor.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}