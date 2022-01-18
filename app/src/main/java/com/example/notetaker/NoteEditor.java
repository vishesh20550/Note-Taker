package com.example.notetaker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditor extends AppCompatActivity {
    EditText editText;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        editText = findViewById(R.id.editTextTextMultiLine);
        Intent intent= getIntent();
        int noteId= intent.getIntExtra("noteId",-1);
        if(noteId!=-1)
            editText.setText(MainActivity.notes.get(noteId));
        else{
         MainActivity.notes.add("");
         noteId= MainActivity.notes.size()-1;
        }
        int finalNoteId = noteId;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.notes.set(finalNoteId,String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                sharedPreferences= getApplicationContext().getSharedPreferences("com.example.notetaker", Context.MODE_PRIVATE);
                HashSet<String> set= new HashSet<>(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes",set).apply();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}