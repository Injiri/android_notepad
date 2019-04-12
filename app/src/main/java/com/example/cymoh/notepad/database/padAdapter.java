package com.example.cymoh.notepad.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cymoh.notepad.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class padAdapter extends RecyclerView.Adapter<padAdapter.padViewHolder> {
    private  Context context;
    private   List<Note> noteList;

    @NonNull
    @Override
    public padAdapter.padViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
View view = LayoutInflater.from(viewGroup.getContext()).
        inflate(R.layout.note_row_list, viewGroup,false);
return  new padViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull padAdapter.padViewHolder padViewHolder, int i) {
Note note = noteList.get(i);
padViewHolder.note.setText(note.getNote());
padViewHolder.dot.setText(Html.fromHtml("&#8226"));
padViewHolder.timestamp.setText(formartDate(note.getTimeStamp()));
    }

    private String formartDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtout = new SimpleDateFormat("MMM d");
                return fmtout.format(date);
            }catch (ParseException e) {e.printStackTrace();
        }
    return "";
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public padAdapter(Context context, List<Note> notesList){
        this.context = context;
        this.noteList = notesList;
    }

    public class padViewHolder extends  RecyclerView.ViewHolder{
        public TextView note;
        public  TextView dot;
        public  TextView timestamp;


        public padViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note);
            dot = itemView.findViewById(R.id.dot);
            timestamp = itemView.findViewById(R.id.timestamp);
        }

    }
}
