package com.firhan.leafnote.ui.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firhan.leafnote.R;
import com.firhan.leafnote.interfaces.INoteListClickListener;
import com.firhan.leafnote.rooms.entities.Note;

import org.w3c.dom.Text;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {
    private List<Note> notes;
    private INoteListClickListener noteListClickListener;
    private Context context;

    public NotesRecyclerViewAdapter(Context context, List<Note> notes, INoteListClickListener noteListClickListener) {
        this.context = context;
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

        //set bg
        if(note.getSelected()){
            holder.listItemNoteContainer.setBackground(ContextCompat.getDrawable(this.context, R.drawable.note_list_item_selected));
            holder.listItemTitle.setTextColor(Color.WHITE);
            holder.listItemBody.setTextColor(Color.WHITE);

            animateSelected(holder);
        }else{
            holder.listItemNoteContainer.setBackground(ContextCompat.getDrawable(this.context, R.drawable.note_list_item_background));
            holder.listItemTitle.setTextColor(ContextCompat.getColor(this.context, R.color.noteListItemTitle));
            holder.listItemBody.setTextColor(ContextCompat.getColor(this.context, R.color.noteListItemBody));

            animateUnselected(holder);
        }
    }

    private void animateSelected(ViewHolder holder){
        long duration = 150;
        float margin = 125;
        ObjectAnimator content = ObjectAnimator.ofFloat(holder.listItemNoteContentContainer, "translationX", 0f, margin).setDuration(duration);
        ObjectAnimator checkIconX = ObjectAnimator.ofFloat(
                holder.listItemNoteCheckIcon,
                "scaleX",
                0f,
                1f
        ).setDuration(duration);
        ObjectAnimator checkIconY = ObjectAnimator.ofFloat(
                holder.listItemNoteCheckIcon,
                "scaleY",
                0f,
                1f
        ).setDuration(duration);
        ObjectAnimator rotateIcon = ObjectAnimator.ofFloat(
                holder.listItemNoteCheckIcon,
                "rotation",
                0f,
                360f
        ).setDuration(duration);

        //animate together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(content, checkIconX, checkIconY, rotateIcon);
        animatorSet.start();
    }

    private void animateUnselected(ViewHolder holder){
        long duration = 150;
        float margin = 125;
        ObjectAnimator content = ObjectAnimator.ofFloat(holder.listItemNoteContentContainer, "translationX", margin, 0f).setDuration(duration);
        ObjectAnimator checkIconX = ObjectAnimator.ofFloat(
                holder.listItemNoteCheckIcon,
                "scaleX",
                1f,
                0.5f
        ).setDuration(duration);
        ObjectAnimator checkIconY = ObjectAnimator.ofFloat(
                holder.listItemNoteCheckIcon,
                "scaleY",
                1f,
                0.5f
        ).setDuration(duration);
        ObjectAnimator rotateIcon = ObjectAnimator.ofFloat(
                holder.listItemNoteCheckIcon,
                "rotation",
                360f,
                0f
        ).setDuration(duration);

        //animate together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(content, checkIconX, checkIconY, rotateIcon);
        animatorSet.start();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //vars
        TextView listItemTitle, listItemBody;
        ConstraintLayout listItemNoteContainer;
        LinearLayout listItemNoteContentContainer;
        ImageView listItemNoteCheckIcon;
        //interface
        INoteListClickListener noteListClickListener;

        ViewHolder(@NonNull View itemView, INoteListClickListener noteListClickListener) {
            super(itemView);

            //init note click
            this.noteListClickListener = noteListClickListener;

            listItemTitle = itemView.findViewById(R.id.list_item_title);
            listItemBody = itemView.findViewById(R.id.list_item_body);
            listItemNoteContainer = itemView.findViewById(R.id.list_item_note_container);
            listItemNoteContentContainer = itemView.findViewById(R.id.list_item_note_content_container);
            listItemNoteCheckIcon = itemView.findViewById(R.id.list_item_note_check_icon);

            //on click
            itemView.setOnClickListener(this);

            //long press
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            noteListClickListener.onNoteClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            noteListClickListener.onNoteLongPress(getAdapterPosition());

            return true;
        }
    }
}
