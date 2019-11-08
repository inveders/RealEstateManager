package com.inved.realestatemanager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.models.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListViewHolder> {

    // CALLBACK

    private final PropertyListViewHolder.PropertyListInterface callback;
    private Context context;
    private TextView textViewNoProperty;

    // FOR DATA
    private List<Property> properties;
    private final RequestManager glide;

    // CONSTRUCTOR
    public PropertyListAdapter(Context context, PropertyListViewHolder.PropertyListInterface callback, RequestManager glide) {
        this.properties = new ArrayList<>();
        this.callback = callback;
        this.context = context;
        this.glide=glide;
    }

    @Override
    @NonNull
    public PropertyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.fragment_list_property_item, parent, false);
        textViewNoProperty = view.findViewById(R.id.fragment_list_property_no_property_found_text);

        return new PropertyListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyListViewHolder holder, int position) {

        holder.updateWithProperty(this.properties.get(position), this.callback,this.glide);
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
