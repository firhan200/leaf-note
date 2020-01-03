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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.firhan.leafnote.R;
import com.firhan.leafnote.interfaces.INoteListClickListener;
import com.firhan.leafnote.rooms.entities.Note;

import org.w3c.dom.Text;

import java.util.List;

public class NotesRecyclerViewAdapter extends ListAdapter<Note, NotesRecyclerViewAdapter.ViewHolder> {
    private INoteListClickListener noteListClickListener;
    private Context context;

    public NotesRecyclerViewAdapter(Context context, INoteListClickListener noteListClickListener) {
        super(DIFF_CALLBACK);

        this.context = context;
        this.noteListClickListener = noteListClickListener;
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Note>() {
            @Override
            public boolean areItemsTheSame(
                    @NonNull Note oldNote, @NonNull Note newNote) {
                // User properties may have changed if reloaded from the DB, but ID is fixed
                return oldNote.getId() == newNote.getId();
            }
            @Override
            public boolean areContentsTheSame(
                    @NonNull Note oldNote, @NonNull Note newNote) {
                // NOTE: if you use equals, your object must properly override Object#equals()
                // Incorrectly returning false here will result in too many animations.
                return oldNote.equals(newNote);
            }
        };

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
        Note note = getItem(position);

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
