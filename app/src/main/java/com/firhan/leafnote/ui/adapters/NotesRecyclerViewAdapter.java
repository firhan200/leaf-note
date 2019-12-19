package com.firhan.leafnote.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firhan.leafnote.R;
import com.firhan.leafnote.interfaces.INoteListClickListener;
import com.firhan.leafnote.rooms.entities.Note;

import org.w3c.dom.Text;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {
    private List<Note> notes;
    private INoteListClickListener noteListClickListener;

    public NotesRecyclerViewAdapter(List<Note> notes, INoteListClickListener noteListClickListener) {
        this.notes = notes;
        this.noteListClickListener = noteListClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);

        return new ViewHolder(view, noteListClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get note
        Note note = notes.get(position);

        //show data to layout
        holder.listItemTitle.setText(note.getTitle());
        holder.listItemBody.setText(note.getBody());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //vars
        TextView listItemTitle, listItemBody;
        //interface
        INoteListClickListener noteListClickListener;

        ViewHolder(@NonNull View itemView, INoteListClickListener noteListClickListener) {
            super(itemView);

            //init note click
            this.noteListClickListener = noteListClickListener;

            listItemTitle = itemView.findViewById(R.id.list_item_title);
            listItemBody = itemView.findViewById(R.id.list_item_body);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            noteListClickListener.onNoteClick(getAdapterPosition());
        }
    }
}
