package com.firhan.leafnote.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firhan.leafnote.R;
import com.firhan.leafnote.rooms.entities.Note;

import org.w3c.dom.Text;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {
    List<Note> notes;

    public NotesRecyclerViewAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);

        return new ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        //vars
        TextView listItemTitle, listItemBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listItemTitle = itemView.findViewById(R.id.list_item_title);
            listItemBody = itemView.findViewById(R.id.list_item_body);
        }
    }
}
