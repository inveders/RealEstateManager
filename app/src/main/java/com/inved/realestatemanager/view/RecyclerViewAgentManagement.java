package com.inved.realestatemanager.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.domain.SplitString;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.GlideApp;
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
        holder.mAgentAgency.setText(realEstateAgentsList.get(position).getAgencyName());
        if (realEstateAgentsList.get(position).getUrlPicture() != null) {

            File localFile = new File(realEstateAgentsList.get(position).getUrlPicture());

            if (localFile.exists()) {
                Log.d("debago", "good file internal exist for agent " + localFile);


                if (localFile.getPath() != null) {
                    GlideApp.with(MainApplication.getInstance().getApplicationContext())
                            .load(localFile)
                            .apply(RequestOptions.circleCropTransform())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.d("debago", "Exception is : " + e);
                                    holder.mAgentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    return false;
                                }
                            })
                            .into(holder.mAgentPhoto);
                }

            } else if (localFile.exists()){
                Log.d("debago", "good file external exist for agent " + localFile);

                if (localFile.getPath() != null) {
                    GlideApp.with(MainApplication.getInstance().getApplicationContext())
                            .load(localFile)
                            .apply(RequestOptions.circleCropTransform())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.d("debago", "Exception is : " + e);
                                    holder.mAgentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    return false;
                                }
                            })
                            .into(holder.mAgentPhoto);
                }
            }
            else {


                Log.d("debago", "good file NOT exist for agent ");
                SplitString splitString = new SplitString();

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    String agentId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    if (agentId != null) {

                        RealEstateAgentHelper.getAgentWhateverAgency(agentId).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                if (task.getResult() != null) {
                                    if (task.getResult().getDocuments().size() != 0) {
                                        String photoUriFromFirebase = task.getResult().getDocuments().get(0).getString("urlPicture");
                                        if(photoUriFromFirebase!=null){
                                            int numberCharacter = photoUriFromFirebase.length();

                                            StorageReference fileReference = FirebaseStorage.getInstance().getReference(agentId).child("Pictures")
                                                    .child(splitString.lastCharacters(realEstateAgentsList.get(position).getUrlPicture(),numberCharacter));

                                            String mFileName2 = "/" + splitString.lastCharacters(realEstateAgentsList.get(position).getUrlPicture(),numberCharacter);
                                            File storageDir2 = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                            File localFile2 = new File(storageDir2 + mFileName2);
                                            fileReference.getFile(localFile2).addOnSuccessListener(taskSnapshot -> Log.d("debago", ";local item file created from ViewHolder Agent Management")).addOnFailureListener(exception -> {
                                                Log.d("debago", ";local tem file not created  created " + exception.toString());
                                                GlideApp.with(MainApplication.getInstance().getApplicationContext())
                                                        .load(R.drawable.ic_anon_user_48dp)
                                                        .into((holder.mAgentPhoto));
                                            });

                                            GlideApp.with(MainApplication.getInstance().getApplicationContext())
                                                    .load(fileReference)
                                                    .apply(RequestOptions.circleCropTransform())
                                                    .listener(new RequestListener<Drawable>() {
                                                        @Override
                                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                            Log.e("debago", "Exception is : " + e);
                                                            holder.mAgentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);
                                                            return false;
                                                        }

                                                        @Override
                                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                            Log.d("debago", "onResourceReady");

                                                            return false;
                                                        }
                                                    })
                                                    .into(holder.mAgentPhoto);
                                        }

                                    }
                                }
                            }
                        });







                    }


                }
            }


        } else {
            holder.mAgentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);
        }

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null){

                holder.mAgentName.setOnClickListener(v -> {
                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(realEstateAgentsList.get(position).getRealEstateAgentId())){
                        callback.onEditAgent(realEstateAgentsList.get(position).getRealEstateAgentId());
                    }else{
                        Toast.makeText(context, MainApplication.getResourses().getString(R.string.no_good_agent), Toast.LENGTH_SHORT).show();
                    }
                });
                holder.mAgentPhoto.setOnClickListener(v -> {
                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(realEstateAgentsList.get(position).getRealEstateAgentId())){
                        callback.onEditAgent(realEstateAgentsList.get(position).getRealEstateAgentId());
                    }else{
                        Toast.makeText(context, MainApplication.getResourses().getString(R.string.no_good_agent), Toast.LENGTH_SHORT).show();
                    }
                });
                holder.mAgentAgency.setOnClickListener(v -> {
                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(realEstateAgentsList.get(position).getRealEstateAgentId())){
                        callback.onEditAgent(realEstateAgentsList.get(position).getRealEstateAgentId());
                    }else{
                        Toast.makeText(context, MainApplication.getResourses().getString(R.string.no_good_agent), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }


    }




    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mAgentName;
        ImageView mAgentPhoto;
        TextView mAgentAgency;


        ViewHolder(View itemView) {

            super(itemView);

            mAgentName = itemView.findViewById(R.id.agent_management_name_agent_text);
            mAgentPhoto = itemView.findViewById(R.id.agent_management_item_image);
            mAgentAgency = itemView.findViewById(R.id.agent_management_agence_name_text);

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
        void onEditAgent(String id);
    }

}
