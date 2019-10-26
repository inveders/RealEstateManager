package com.inved.realestatemanager.property;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.models.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyViewHolder> {

    // CALLBACK
    public interface Listener { void onClickDeleteButton(int position); }
    private final Listener callback;
    private Context context;
    private TextView textViewNoProperty;

    // FOR DATA
    private List<Property> properties;

    // CONSTRUCTOR
    public PropertyAdapter(Context context,Listener callback) {
        this.properties = new ArrayList<>();
        this.callback = callback;
        this.context = context;
    }

    @Override
    @NonNull
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.fragment_list_property_item, parent, false);
        textViewNoProperty = view.findViewById(R.id.fragment_list_property_no_property_found_text);

        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder holder, int position) {

        holder.updateWithProperty(this.properties.get(position), this.callback);
    }

    @Override
    public int getItemCount() {

      /*  if(properties.size()>0){
            textViewNoProperty.setVisibility(View.GONE);
        }else{
            textViewNoProperty.setVisibility(View.VISIBLE);
        }*/
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
