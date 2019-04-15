package com.example.cymoh.notepad;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cymoh.notepad.database.Note;
import com.example.cymoh.notepad.database.SqliteDbHelper;
import com.example.cymoh.notepad.database.padAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private padAdapter noteAdapter;
    private List<Note> noteList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesTextView;

    SqliteDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        noNotesTextView = findViewById(R.id.empty_text_view);
        recyclerView = findViewById(R.id.recycler_view);

        db = new SqliteDbHelper(this);
        noteList.addAll(db.getAllNotes());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDialog(false, null, -1);

            }
        });

        noteAdapter = new padAdapter(this, noteList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerAnimator(LinearLayoutManager.VERTICAL, this, 16));
        recyclerView.setAdapter(noteAdapter);

        toggleEmptyNotes();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                //choose to edit or delete note at the position
                ActionsDialog(position);
            }
        }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void noteDialog(final boolean shouldUpdate, final Note note, final int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.note_pad_dialog, null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setView(view);

        final EditText noteInput = view.findViewById(R.id.note);
        TextView dialoogTitle = view.findViewById(R.id.dialog_title);
        dialoogTitle.setText(!shouldUpdate ? getString(R.string.new_note_title_lbl) : getString(R.string.lbl_edit_note_title));
        if (shouldUpdate && note != null) {
            noteInput.setText(note.getNote());
        }
        alertDialog.setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();

        alertDialog1.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            //Tost a message when no text is enterd
                                            if (TextUtils.isEmpty(noteInput.getText().toString())) {
                                                Toast.makeText(MainActivity.this, "Please entr Note", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else {
                                                alertDialog1.dismiss();
                                            }
                                            if (shouldUpdate && note != null) {
                                                updateNote(noteInput.getText().toString(), position);
                                            } else {
                                                createNote(noteInput.getText().toString());
                                            }
                                        }

                                    }
                );
    }

    private void createNote(String note) {
        long id = db.insertNote(note, null);

        Note n = db.getNote(id);
        if (n != null) {
            noteList.add(0, n);
            //refresh list
            noteAdapter.notifyDataSetChanged();

            toggleEmptyNotes();


        }

    }

    private void updateNote(String note, int position) {

        Note n = noteList.get(position);
        n.setNote(note);
        db.updateNote(n);

        //reffresh the list
        noteList.set(position, n);
        noteAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }


    private void deleteNote(int position) {
        Note n = noteList.get(position);
        db.deleteNote(n);
        noteList.remove(position);
        noteAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    // opens dialog with delete update options
    private void ActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    noteDialog(true, noteList.get(position), position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }

    private void toggleEmptyNotes() {
        //notesList.size()
        if (db.getNotesCount() > 0) {
            noNotesTextView.setVisibility(View.GONE);
        } else

        {
            noNotesTextView.setVisibility(View.VISIBLE);
        }
    }
}
