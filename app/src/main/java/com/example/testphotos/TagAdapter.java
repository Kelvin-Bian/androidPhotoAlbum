package com.example.testphotos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testphotos.classes.Tag;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    private MutableLiveData<ArrayList<Tag>> tags;
    private OnItemClickListener onItemClickListenerEdit;
    private OnItemClickListener onItemClickListenerDelete;

    public TagAdapter(){
        this.tags = new MutableLiveData<ArrayList<Tag>>();
        ArrayList<Tag> test = new ArrayList<>();
        for(int i=0;i<5; i++){
            test.add(new Tag("person","p"+i));
            if(i<1)
                test.add(new Tag("location", "home"));
        }
        this.tags.setValue(test);
    }
    public TagAdapter(ArrayList<Tag> tags){
        this.tags = new MutableLiveData<ArrayList<Tag>>();
        this.tags.setValue(tags);
    }
    public interface OnItemClickListener {
        void onItemClick(int position, String text);
    }

    // Set the click listener
    public void setOnItemClickListenerEdit(OnItemClickListener listener) {
        this.onItemClickListenerEdit = listener;
    }
    public void setOnItemClickListenerDelete(OnItemClickListener listener) {
        this.onItemClickListenerDelete = listener;
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        public TextView tagValue;
        public Button editButton;

        public Button deleteButton;

        public TagViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tagValue = (TextView) view.findViewById(R.id.tagValue);
            editButton = (Button) view.findViewById(R.id.edit);
            deleteButton = (Button) view.findViewById(R.id.delete);
        }

        public TextView getTagTextView() {
            return tagValue;
        }
    }
    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.getTagTextView().setText(tags.getValue().get(position).toString());
        TextView tagValue = holder.getTagTextView();
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here
                String text = tagValue.getText().toString();
                if (onItemClickListenerEdit != null) {
                    onItemClickListenerEdit.onItemClick(holder.getAdapterPosition(), text);
                }
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here
                String text = tagValue.getText().toString();
                if (onItemClickListenerDelete != null) {
                    onItemClickListenerDelete.onItemClick(holder.getAdapterPosition(), text);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.getValue().size();
    }

    public void setTags(ArrayList<Tag> t){
        this.tags.setValue(t);
    }
}
