package com.inved.realestatemanager.property;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testimmo.R;
import com.example.testimmo.models.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyViewHolder> {

    // CALLBACK
    public interface Listener { void onClickDeleteButton(int position); }
    private final Listener callback;

    // FOR DATA
    private List<Property> properties;

    // CONSTRUCTOR
    public PropertyAdapter(Listener callback) {
        this.properties = new ArrayList<>();
        this.callback = callback;
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_property_description_item, parent, false);

        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder holder, int position) {
        holder.updateWithProperty(this.properties.get(position), this.callback);
    }

    @Override
    public int getItemCount() {
        return this.properties.size();
    }

    public Property getItem(int position){
        return this.properties.get(position);
    }

    public void updateData(List<Property> properties){
        this.properties = properties;
        this.notifyDataSetChanged();
    }

}
