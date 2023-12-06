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

public class TagAdapter extends RecyclerView.Adapter {
    private MutableLiveData<ArrayList<Tag>> tags;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TagViewHolder)holder).getTagTextView().setText(tags.getValue().get(position).toString());
    }

    @Override
    public int getItemCount() {
        return tags.getValue().size();
    }
}
