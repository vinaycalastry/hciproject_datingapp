package com.example.android.testamante.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.testamante.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String interests = "";
    private EditText occupationEditText;
    private EditText aboutEditText;
    private TextView aboutTextCount;

    private EditText interestsEditText;
    private TextView interestsTextCount;

    private OnFragmentInteractionListener mListener;

    private DatabaseReference mDatabase;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        occupationEditText = rootView.findViewById(R.id.profileOccupation);
        aboutEditText = rootView.findViewById(R.id.profileAbout);
        aboutTextCount = rootView.findViewById(R.id.profileAboutCount);
        aboutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String currentText = aboutEditText.getText().toString();
                int currentLength = currentText.length();
                aboutTextCount.setText("" + currentLength);
            }
        });

        interestsEditText = rootView.findViewById(R.id.profileInterests);
        interestsTextCount = rootView.findViewById(R.id.profileInterestsCount);
        interestsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String currentText = aboutEditText.getText().toString();
                int currentLength = currentText.length();
                interestsTextCount.setText("" + currentLength);
            }
        });
        mDatabase = firebaseDatabase.getReference().child("Profiles/0/").child(currentFirebaseUser.getUid());
        // Attach a listener to read the data at our Profiles reference
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("name").getValue() != null) {
                    aboutEditText.setText((CharSequence) dataSnapshot.child("about").getValue());
                    //interestsEditText.setText((CharSequence) dataSnapshot.child("interests").getValue());
                    occupationEditText.setText((CharSequence) dataSnapshot.child("occupation").getValue());

                    try {
                        List<String> list = (ArrayList<String>) dataSnapshot.child("interests").getValue();
                        for (int i = 0; i < list.size(); i++) {
                            if (interests.trim().length() > 0) {
                                interests = interests + ",";
                            }

                            interests = interests + list.get(i);


                        }

                        interestsEditText.setText(interests);
                    } catch (Exception e) {
                        Log.e("JSONArray", e.toString());
                    }
                } else {
                    aboutEditText.setText("Tell us about you...");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                aboutEditText.setText("Tell us about you...");
            }
        });

        Button saveBtn = rootView.findViewById(R.id.aboutSaveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSave();
            }
        });
        return rootView;
    }

    private void attemptSave() {

        // Reset errors.
        aboutEditText.setError(null);
        interestsEditText.setError(null);
        occupationEditText.setError(null);

        // Store values at the time of the register attempt.
        String about = aboutEditText.getText().toString().trim();
        String interests = interestsEditText.getText().toString().trim();
        String occupation = occupationEditText.getText().toString().trim();

        boolean isInValid = false;
        View focusView = null;


        if (TextUtils.isEmpty(about)) {
            aboutEditText.setError(getString(R.string.error_field_required));
            focusView = aboutEditText;
            isInValid = true;
        } else if (TextUtils.isEmpty(interests)) {
            interestsEditText.setError(getString(R.string.error_field_required));
            focusView = interestsEditText;
            isInValid = true;
        } else if (TextUtils.isEmpty(occupation)) {
            occupationEditText.setError(getString(R.string.error_field_required));
            focusView = occupationEditText;
            isInValid = true;
        }


        if (isInValid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mDatabase.child("about").setValue(about);
            String[] arr = interests.split(",");
            List list = (List) Arrays.asList(arr);
            mDatabase.child("interests").setValue(list);
            mDatabase.child("occupation").setValue(occupation);

            if (mListener != null) {
                mListener.onFragmentInteraction(2);
            }
        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int i);
    }
}
