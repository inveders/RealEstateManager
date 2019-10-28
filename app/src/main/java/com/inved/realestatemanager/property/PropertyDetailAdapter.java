package com.inved.realestatemanager.property;

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
import com.inved.realestatemanager.models.RealEstateAgents;

import java.util.ArrayList;
import java.util.List;

public class PropertyDetailAdapter extends RecyclerView.Adapter<PropertyDetailViewHolder> {

    // CALLBACK
    private Context context;
    private TextView textViewNoProperty;

    // FOR DATA
    private List<Property> properties;
    private List<RealEstateAgents> realEstateAgents;

    // CONSTRUCTOR
    public PropertyDetailAdapter(Context context) {
        this.properties = new ArrayList<>();

        this.context = context;

    }

    @Override
    @NonNull
    public PropertyDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.fragment_list_property_item, parent, false);
        textViewNoProperty = view.findViewById(R.id.fragment_list_property_no_property_found_text);

        return new PropertyDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyDetailViewHolder holder, int position) {

        holder.updateWithProperty(this.properties.get(position));
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
