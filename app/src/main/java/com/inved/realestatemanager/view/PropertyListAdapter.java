package com.inved.realestatemanager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.models.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListViewHolder> {

    // CALLBACK

    private final PropertyListViewHolder.PropertyListInterface callback;
    private Context context;

    // FOR DATA
    private List<Property> properties;

    // CONSTRUCTOR
    public PropertyListAdapter(Context context, PropertyListViewHolder.PropertyListInterface callback) {
        this.properties = new ArrayList<>();
        this.callback = callback;
        this.context = context;
    }

    @Override
    @NonNull
    public PropertyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.fragment_list_property_item, parent, false);

        return new PropertyListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyListViewHolder holder, int position) {

        holder.updateWithProperty(this.properties.get(position), this.callback);
    }

    @Override
    public int getItemCount() {

        return this.properties.size();
    }

    public void updateData(List<Property> properties){
        this.properties = properties;
        this.notifyDataSetChanged();
    }

}
