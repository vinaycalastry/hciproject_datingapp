package com.example.android.testamante.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.testamante.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private EditText usernameEditText;
    private EditText dobEditText;
    private Spinner iamASpinner;
    private Spinner interestedInSpinner;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog dobDatePickerDialog;

    private DatabaseReference mDatabase;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public ProfileDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileDetailsFragment newInstance(String param1, String param2) {
        ProfileDetailsFragment fragment = new ProfileDetailsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profile_details, container, false);
        usernameEditText = rootView.findViewById(R.id.profileUserName);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dobEditText = rootView.findViewById(R.id.profileDob);
        dobEditText.setInputType(InputType.TYPE_NULL);
        // Set the current day initially
        setDateTimeField();

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dobDatePickerDialog.show();
            }
        });

        iamASpinner = rootView.findViewById(R.id.profileGenderSpinner);
        interestedInSpinner = rootView.findViewById(R.id.profileInterestedIn);

        //Log.v("Spinner",String.valueOf(interestedInSpinner.getSelectedItem()));

        Button nextBtn = rootView.findViewById(R.id.profileActivityNextButton);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSave();
            }
        });
        mDatabase = firebaseDatabase.getReference().child("Profiles/0/").child(currentFirebaseUser.getUid());

        // Attach a listener to read the data at our Profiles reference
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("name").getValue() != null) {
                    usernameEditText.setText((CharSequence) dataSnapshot.child("name").getValue());
                    dobEditText.setText((CharSequence) dataSnapshot.child("dob").getValue());
                    String gender = (String) dataSnapshot.child("gender").getValue();
                    if (gender.equals(getResources().getStringArray(R.array.gender_array)[0])) {
                        iamASpinner.setSelection(0);
                    } else {
                        iamASpinner.setSelection(1);
                    }
                    String interest = (String) dataSnapshot.child("interestedin").getValue();
                    if (interest.equals(getResources().getStringArray(R.array.gender_array)[0])) {
                        interestedInSpinner.setSelection(0);
                    } else {
                        interestedInSpinner.setSelection(1);
                    }

                    } else {
                    usernameEditText.setText("Enter Name");
                    dobEditText.setText("09-10-1992");
                    iamASpinner.setSelection(1);
                    interestedInSpinner.setSelection(1);
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                usernameEditText.setText("Enter Name");
                dobEditText.setText("09-10-1992");
                iamASpinner.setSelection(1);
                interestedInSpinner.setSelection(1);
            }
        });

        return rootView;
    }

    private void attemptSave() {
        // Store values at the time of the save attempt.
        String username = usernameEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String iamA = String.valueOf(iamASpinner.getSelectedItem());
        String interestedIn = String.valueOf(interestedInSpinner.getSelectedItem());

        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Please fill Username",
                    Toast.LENGTH_SHORT).show();
            focusView = usernameEditText;

        } else if (TextUtils.isEmpty(dob)) {
            Toast.makeText(getContext(), "Please fill DOB",
                    Toast.LENGTH_SHORT).show();
            focusView = dobEditText;
        } else if (TextUtils.isEmpty(iamA)) {
            Toast.makeText(getContext(), "Please select your gender",
                    Toast.LENGTH_SHORT).show();
            focusView = iamASpinner;
        } else if (TextUtils.isEmpty(interestedIn)) {
            Toast.makeText(getContext(), "Please select your gender",
                    Toast.LENGTH_SHORT).show();
            focusView = interestedInSpinner;
        }


        mDatabase = firebaseDatabase.getReference().child("Profiles/0/").child(currentFirebaseUser.getUid());
        mDatabase.child("name").setValue(username);
        mDatabase.child("dob").setValue(dob);
        mDatabase.child("gender").setValue(iamA);
        mDatabase.child("interestedin").setValue(interestedIn);
        if (mListener != null) {
            mListener.onFragmentInteraction(0);
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

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        dobDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dobEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dobEditText.setHint(dateFormatter.format(Calendar.getInstance().getTime()));

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
