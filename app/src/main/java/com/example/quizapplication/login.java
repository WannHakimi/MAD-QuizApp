package com.example.quizapplication;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class login extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private EditText Username;
    private EditText Password;
    

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users = db.collection("Users");


    private String mParam1;
    private String mParam2;

    public login() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login.
     */
    // TODO: Rename and change types and number of parameters
    public static login newInstance(String param1, String param2) {
        login fragment = new login();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Username = view.findViewById(R.id.emailAdd);
        Password = view.findViewById(R.id.password);



        
        Button second = view.findViewById(R.id.regBtn);
        TextView reg = view.findViewById(R.id.textRegister);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_login_to_register);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                users.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String data = "";
                                String usern = Username.getText().toString();
                                String pass = Password.getText().toString();
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                                    User user = documentSnapshot.toObject(User.class);
                                    user.setDocumentID(documentSnapshot.getId());

                                    String documentID = user.getDocumentID();
                                    String username = user.getUsername();
                                    String password = user.getPassword();

                                    if(usern.equals(username) && pass.equals(password)){
                                        if(usern.equals("admin")){

                                            Bundle bundle = new Bundle();
                                            bundle.putString("username",usern);
                                            Navigation.findNavController(view).navigate(R.id.action_login_to_adminmenu, bundle);
                                            return;
                                        }else {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("username", usern);
                                            bundle.putString("password", pass);
                                            Navigation.findNavController(view).navigate(R.id.action_login_to_homepage, bundle);
                                            return;
                                        }


                                    }




                                }
                                Toast.makeText(getActivity(),"Wrong Username Or Password!!",Toast.LENGTH_SHORT).show();


                            }
                        });
            }
        });

        return view;
    }




}