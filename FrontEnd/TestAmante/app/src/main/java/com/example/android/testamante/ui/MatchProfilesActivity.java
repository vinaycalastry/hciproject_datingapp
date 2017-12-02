package com.example.android.testamante.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.testamante.R;
import com.example.android.testamante.adapters.MatchesListAdapter;
import com.example.android.testamante.models.MatchedProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MatchProfilesActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private RecyclerView matchesRecylerView;
    private ArrayList<MatchedProfile> matchedProfileList;
    private DatabaseReference mDatabase;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private TextView noMatchesTextView;
    private MatchesListAdapter matchesListAdapter;
    private MatchedProfile mProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profiles);

        noMatchesTextView = (TextView) findViewById(R.id.noMatchesHint);


        matchedProfileList = new ArrayList<>(0);
        fetchAndSaveProfiles();

        matchesRecylerView = (RecyclerView) findViewById(R.id.matchesRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        matchesRecylerView.setLayoutManager(gridLayoutManager);
        matchesRecylerView.setHasFixedSize(true);

        matchesListAdapter = new MatchesListAdapter(this, matchedProfileList);
        matchesRecylerView.setAdapter(matchesListAdapter);

    }

    private void updateRecyclerView() {
        if (matchedProfileList.size() == 0) {
            noMatchesTextView.setVisibility(View.VISIBLE);
        } else {
            matchesListAdapter.notifyDataSetChanged();
        }
    }


    private void fetchAndSaveProfiles() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://limitless-forest-94438.herokuapp.com/findmatch/";
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String reqUrl = url + currentFirebaseUser.getUid();

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, reqUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("Response", response.toString());
                        // Process the JSON
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject matchedProfile = response.getJSONObject(i);

                                mProfile = new MatchedProfile();
                                mProfile.setProfileID(matchedProfile.getString("profileid"));
                                mProfile.setProfilePicURL(matchedProfile.getString("picurl"));
                                mProfile.setDescription("Description Not Available !!!");

                                mDatabase = firebaseDatabase.getReference().child("Profiles/0/").child(mProfile.getProfileID());
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                             @Override
                                                                             public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                 if (dataSnapshot.child("about").getValue() != null) {
                                                                                     mProfile.setDescription((String) dataSnapshot.child("about").getValue());
                                                                                     updateRecyclerView();
                                                                                     //Log.i("information", (String) dataSnapshot.child("about").getValue());
                                                                                 }
                                                                             }

                                                                             @Override
                                                                             public void onCancelled(DatabaseError databaseError) {

                                                                             }
                                                                         }
                                );
                                matchedProfileList.add(mProfile);
                            }
                            updateRecyclerView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateRecyclerView();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        updateRecyclerView();
                    }
                }
        );
        queue.add(getRequest);
    }

    private void saveAndUpdateProfileToList() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.match_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.match_logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, SplashActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //finish();
                return true;

            case R.id.match_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("fromMatches", true);
                startActivity(intent);
                //finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
