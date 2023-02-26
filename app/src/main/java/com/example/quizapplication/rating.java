package com.example.quizapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link rating#newInstance} factory method to
 * create an instance of this fragment.
 */
public class rating extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference rating = db.collection("rating");

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String feedbackk = "";

    public rating() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment rating.
     */
    // TODO: Rename and change types and number of parameters
    public static rating newInstance(String param1, String param2) {
        rating fragment = new rating();
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
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        EditText feedback = view.findViewById(R.id.feedback);
        Button submit = view.findViewById(R.id.submitf);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        ImageButton back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                String name = getArguments().getString("username");
                bundle.putString("username",name);
                Navigation.findNavController(view).navigate(R.id.action_rating_to_homepage,bundle);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackk = feedback.getText().toString();
                float rate = 0;
                rate = ratingBar.getRating();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("feedback",feedbackk);
                hashMap.put("rating",rate);
                rating.add(hashMap);
                Toast.makeText(getActivity(), "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                String name = getArguments().getString("username");
                bundle.putString("username",name);
                Navigation.findNavController(view).navigate(R.id.action_rating_to_homepage,bundle);
            }
        });


        return view;
    }
}