package com.example.android.testamante.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.testamante.R;
import com.example.android.testamante.models.MatchedProfile;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by suresh on 11/28/17.
 */

public class MatchesListAdapter extends
        RecyclerView.Adapter<MatchesListAdapter.ViewHolder> {

    private ArrayList<MatchedProfile> matchedProfilesList;
    private StorageReference mStorageRef;
    private Context mContext;

    public MatchesListAdapter(Context ctx, ArrayList<MatchedProfile> mList) {
        matchedProfilesList = mList;
        mContext = ctx;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.matches_item_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position


        // Set item views based on your views and data model
        if (matchedProfilesList != null) {
            MatchedProfile matchedProfile = matchedProfilesList.get(position);

            StorageReference mProfilepicRef = mStorageRef.child(matchedProfile.getProfileID() + ".jpg");
            Glide.with(mContext)
                    .using(new FirebaseImageLoader())
                    .load(mProfilepicRef)
                    .error(R.drawable.add_profile_picture)
                    .into(holder.profile_image);

//            Glide.with(mContext)
//                    .load(imagePath)
//                    .fitCenter()
//                    .into(holder.profile_image);

            //holder.profile_image.setText(item.getUserName());
        }
    }


    @Override
    public int getItemCount() {
        return matchedProfilesList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView profile_image;
        public Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            profile_image = (ImageView) itemView.findViewById(R.id.matched_profile_image);
            messageButton = (Button) itemView.findViewById(R.id.matched_profile_messageButton);

        }
    }
}
