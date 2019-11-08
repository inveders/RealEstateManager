package com.inved.realestatemanager.view;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;
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
            Uri fileUri = Uri.parse(realEstateAgentsList.get(position).getUrlPicture());
            if(fileUri.getPath()!=null){
                Glide.with(MainApplication.getInstance().getApplicationContext())
                        .load(new File(fileUri.getPath()))
                        .apply(RequestOptions.circleCropTransform())
                        .into((holder.mAgentPhoto));
            }


        } else {
            holder.mAgentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);
        }


        holder.mButtonDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.alert_dialog_message)
                    .setCancelable(false)
                    .setPositiveButton(MainApplication.getResourses().getString(R.string.alert_dialog_yes), (dialog, id) -> callback.onClickDeleteButton(realEstateAgentsList.get(position).getId())
                    )
                    .setNegativeButton(MainApplication.getResourses().getString(R.string.alert_dialog_no), (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();

        });

        holder.mAgentName.setOnClickListener(v -> callback.onEditAgent(realEstateAgentsList.get(position).getId()));
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
        void onClickDeleteButton(long realEstateAgentId);
        void onEditAgent(long id);
    }

}
