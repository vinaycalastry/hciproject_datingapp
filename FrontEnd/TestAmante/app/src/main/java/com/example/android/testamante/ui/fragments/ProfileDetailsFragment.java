package com.example.android.testamante.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.testamante.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        dobEditText = (EditText) rootView.findViewById(R.id.profileDob);
        dobEditText.setInputType(InputType.TYPE_NULL);
        // Set the current day initially
        setDateTimeField();

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dobDatePickerDialog.show();
            }
        });

        iamASpinner = (Spinner) rootView.findViewById(R.id.profileGenderSpinner);
        interestedInSpinner = (Spinner) rootView.findViewById(R.id.profileInterestedIn);

        //Log.v("Spinner",String.valueOf(interestedInSpinner.getSelectedItem()));

        Button nextBtn = (Button) rootView.findViewById(R.id.profileActivityNextButton);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSave();
            }
        });
        mDatabase = firebaseDatabase.getReference().child("Profiles/0/").child(currentFirebaseUser.getUid());

        usernameEditText.setText(mDatabase.child("name").getKey());


        dobEditText.setText(mDatabase.child("dob").getKey());
        String gender = mDatabase.child("gender").getKey();
        if (gender.equals(getResources().getStringArray(R.array.gender_array)[0])) {
            iamASpinner.setSelection(0);
        } else {
            iamASpinner.setSelection(1);
        }
        String interest = mDatabase.child("interestedin").getKey();
        if (interest.equals(getResources().getStringArray(R.array.gender_array)[0])) {
            interestedInSpinner.setSelection(0);
        } else {
            interestedInSpinner.setSelection(1);
        }


        //      iamASpinner.getAdapter().;
        //mDatabase.child("name").setValue(username);
        //mDatabase.child("dob").setValue(dob);
        // mDatabase.child("gender").setValue();
        //  mDatabase.child("interestedin").setValue(interestedIn);
        return rootView;
    }

    private void attemptSave() {
        // Store values at the time of the save attempt.
        String username = usernameEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String iamA = String.valueOf(iamASpinner.getSelectedItem());
        String interestedIn = String.valueOf(interestedInSpinner.getSelectedItem());

        mDatabase = firebaseDatabase.getReference().child("Profiles/0/").child(currentFirebaseUser.getUid());
        mDatabase.child("name").setValue(username);
        mDatabase.child("dob").setValue(dob);
        mDatabase.child("gender").setValue(iamA);
        mDatabase.child("interestedin").setValue(interestedIn);

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }

}
