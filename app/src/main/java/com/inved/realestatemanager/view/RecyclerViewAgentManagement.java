package com.inved.realestatemanager.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAgentManagement extends RecyclerView.Adapter<RecyclerViewAgentManagement.ViewHolder> {

    private AgentManagementInterface callback;
    private Context context;

    private List<RealEstateAgents> realEstateAgentsList;


    public RecyclerViewAgentManagement(Context context, AgentManagementInterface callback) {
        this.realEstateAgentsList = new ArrayList<>();
        this.context = context;
        this.callback = callback;

    }

    @NonNull
    @Override
    public RecyclerViewAgentManagement.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_agent_management_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mAgentName.setText(MainApplication.getResourses().getString(R.string.detail_property_real_estate_agent_text, realEstateAgentsList.get(position).getFirstname(), realEstateAgentsList.get(position).getLastname()));

        if (realEstateAgentsList.get(position).getUrlPicture() != null) {
            holder.mAgentPhoto.setImageURI(Uri.parse(realEstateAgentsList.get(position).getUrlPicture()));
        } else {
            holder.mAgentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);
        }


        holder.mButtonDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainApplication.getInstance().getApplicationContext());
            builder.setMessage(R.string.alert_dialog_message)
                    .setCancelable(false)
                    .setPositiveButton(MainApplication.getResourses().getString(R.string.alert_dialog_yes), (dialog, id) -> {
                                callback.onClickDeleteButton(position);
                            }
                    )
                    .setNegativeButton(MainApplication.getResourses().getString(R.string.alert_dialog_no), (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();

        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mAgentName;
        ImageView mAgentPhoto;
        ImageView mButtonDelete;


        ViewHolder(View itemView) {

            super(itemView);

            mAgentName = itemView.findViewById(R.id.agent_management_item_text);

            mAgentPhoto = itemView.findViewById(R.id.agent_management_item_image);
            mButtonDelete = itemView.findViewById(R.id.agent_management_item_delete);

        }

    }

    public void setData(List<RealEstateAgents> data) {
        realEstateAgentsList = data;
        //Fill the Recycler View
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {

        if (realEstateAgentsList == null) return 0;

        return realEstateAgentsList.size();

    }

    public interface AgentManagementInterface {
        void onClickDeleteButton(long id);

    }

}
