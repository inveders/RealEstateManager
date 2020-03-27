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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.domain.SplitString;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils    .GlideApp;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAgentManagement extends RecyclerView.Adapter<RecyclerViewAgentManagement.ViewHolder> {

    private AgentManagementInterface callback;
    private Context context;
    private ShimmerFrameLayout shimmerFrameLayout;
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

        shimmerFrameLayout = v.findViewById(R.id.shimmer_view_container_detail);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mAgentName.setText(MainApplication.getResourses().getString(R.string.detail_property_real_estate_agent_text, realEstateAgentsList.get(position).getFirstname(), realEstateAgentsList.get(position).getLastname()));
        holder.mAgentAgency.setText(realEstateAgentsList.get(position).getAgencyName());
        if (realEstateAgentsList.get(position).getUrlPicture() != null) {
            showShimmer();
            File localFile = new File(realEstateAgentsList.get(position).getUrlPicture());

            File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String mFileName = "/" + localFile.getName();
            File goodFile = new File(storageDir,mFileName);
            if (goodFile.exists()) {
                if (goodFile.getPath() != null) {
                    GlideApp.with(MainApplication.getInstance().getApplicationContext())
                            .load(goodFile)
                            .apply(RequestOptions.circleCropTransform())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.mAgentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);
                                    stopShimmer();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Log.d("debago", "onResourceYEAH 5");
                                    stopShimmer();
                                    return false;
                                }
                            })
                            .into(holder.mAgentPhoto);

                }

            } else if (localFile.exists()){
                if (localFile.getPath() != null) {
                    GlideApp.with(MainApplication.getInstance().getApplicationContext())
                            .load(localFile)
                            .apply(RequestOptions.circleCropTransform())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.mAgentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);
                                    stopShimmer();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Log.d("debago", "onResourceYEAH 4");
                                    stopShimmer();
                                    return false;
                                }
                            })
                            .into(holder.mAgentPhoto);
                }
            }
            else {

                Glide.with(MainApplication.getInstance().getApplicationContext())
                        .load(R.drawable.ic_anon_user_48dp)
                        .apply(RequestOptions.circleCropTransform())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("debago", "Exception is : " + e);
                                stopShimmer();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("debago", "onResourceReady");
                                stopShimmer();
                                return false;
                            }
                        })
                        .into(holder.mAgentPhoto);

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

                                            StorageReference fileReference = FirebaseStorage.getInstance().getReference(realEstateAgentsList.get(position).getRealEstateAgentId()).child("Pictures")
                                                    .child(splitString.lastCharacters(realEstateAgentsList.get(position).getUrlPicture(),numberCharacter));

                                            String mFileName2 = "/" + splitString.lastCharacters(realEstateAgentsList.get(position).getUrlPicture(),numberCharacter);
                                            File storageDir2 = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                            File localFile2 = new File(storageDir2 + mFileName2);
                                            fileReference.getFile(localFile2).addOnSuccessListener(taskSnapshot -> {}).addOnFailureListener(exception -> GlideApp.with(MainApplication.getInstance().getApplicationContext())
                                                    .load(R.drawable.ic_anon_user_48dp)
                                                    .listener(new RequestListener<Drawable>() {
                                                        @Override
                                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                            Log.e("debago", "Exception is : " + e);
                                                            stopShimmer();
                                                            return false;
                                                        }

                                                        @Override
                                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                            Log.d("debago", "onResourceReady");
                                                            stopShimmer();
                                                            return false;
                                                        }
                                                    })
                                                    .into((holder.mAgentPhoto)));

                                            GlideApp.with(MainApplication.getInstance().getApplicationContext())
                                                    .load(fileReference)
                                                    .apply(RequestOptions.circleCropTransform())
                                                    .listener(new RequestListener<Drawable>() {
                                                        @Override
                                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                            Log.e("debago", "Exception is : " + e);
                                                            stopShimmer();
                                                            return false;
                                                        }

                                                        @Override
                                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                            Log.d("debago", "onResourceReady 2");
                                                            stopShimmer();
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
            stopShimmer();
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




    static class ViewHolder extends RecyclerView.ViewHolder {

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

    private void stopShimmer() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.hideShimmer();
    }

    private void showShimmer() {
        shimmerFrameLayout.showShimmer(true);
    }

}
