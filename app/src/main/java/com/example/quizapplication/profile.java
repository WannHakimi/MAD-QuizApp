package com.example.quizapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users = db.collection("Users");
    private DocumentReference del;
    private static final DecimalFormat df = new DecimalFormat("0.00");




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Log.d("TAG", "Pressed...");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button edit = view.findViewById(R.id.edit);
        ImageButton back = view.findViewById(R.id.imageButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",data);
                Navigation.findNavController(view).navigate(R.id.action_profile2_to_homepage,bundle);
            }
        });



        Button confirm = view.findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Delete Account");
                builder.setMessage("Are You Sure To Delete Account");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                users.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        String data = requireArguments().getString("username");
                                        for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                                            String documentID = queryDocumentSnapshot.getId();
                                            String name = queryDocumentSnapshot.getString("username");
                                            if(name.equals(data)){
                                                del = db.collection("Users").document(documentID);
                                                del.delete();
                                                Navigation.findNavController(view).navigate(R.id.action_profile2_to_login);
                                            }
                                        }
                                    }
                                });

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        String name = getArguments().getString("username");
        if(name.equals("admin")){
            edit.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.INVISIBLE);
        }




        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                String data = getArguments().getString("username");
                bundle.putString("username",data);
                Navigation.findNavController(view).navigate(R.id.action_profile2_to_editprof,bundle);
            }
        });










        users.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data = getArguments().getString("username");

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                            User user = documentSnapshot.toObject(User.class);

                            String username = user.getUsername();
                            String email = user.getEmail();
                            String school = user.getSchool();
                            String pic = user.getPic();
                            double score = user.getScore();

                            if(username.equals(data) ){
                                ImageView image = view.findViewById(R.id.imageView3);
                                TextView Username = view.findViewById(R.id.name);
                                TextView Email = view.findViewById(R.id.email);
                                TextView School = view.findViewById(R.id.school);
                                TextView Score = view.findViewById(R.id.score);
                                String n = df.format(score);
                                Score.setText(n);
                                Username.setText(username);
                                Email.setText(email);
                                School.setText(school);

                                Resources res = getResources();
                                int resID = res.getIdentifier(pic,"drawable", getActivity().getPackageName());

                                image.setImageResource(resID);



                            }



                        }


                    }
                });


        return view;
    }

    private void dialog(String score){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Congrats You Finished the Quiz with "+score+" points");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void back(){
        String data = getArguments().getString("username");
        Bundle bundle = new Bundle();
        bundle.putString("username",data);
        Navigation.findNavController(getView()).navigate(R.id.action_profile2_to_homepage,bundle);
    }
}