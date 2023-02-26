package com.example.quizapplication;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class register extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users = db.collection("Users");
    private CollectionReference hist = db.collection("History");
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);




    private String mParam1;
    private String mParam2;
    private boolean isTaken = false;

    private EditText user;
    private EditText emaill;
    private EditText passwordd;
    private EditText confirm;

    public register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment register.
     */
    // TODO: Rename and change types and number of parameters
    public static register newInstance(String param1, String param2) {
        register fragment = new register();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        user = view.findViewById(R.id.username);
        emaill = view.findViewById(R.id.emailAdd);
        passwordd = view.findViewById(R.id.password);
        confirm = view.findViewById(R.id.password2);
        Button btn = view.findViewById(R.id.regBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String username = user.getText().toString();
                String email = emaill.getText().toString();
                String password = passwordd.getText().toString();
                String confirmm = confirm.getText().toString();


                

                if (!isEmpty(user) || !isEmpty(emaill) || !isEmpty(passwordd) || !isEmpty(confirm)) {


                    users.get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    String data = "";
                                    String usern = user.getText().toString();
                                    String email = emaill.getText().toString();
                                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                                        User user = documentSnapshot.toObject(User.class);
                                        user.setDocumentID(documentSnapshot.getId());

                                        String documentID = user.getDocumentID();
                                        String username = user.getUsername();
                                        String emaili = user.getEmail();

                                        if(usern.equals(username) || email.equals(emaili)){

                                            Toast.makeText(getActivity(), "Credentials Taken", Toast.LENGTH_SHORT).show();

                                            isTaken = true;
                                            break;
                                        }




                                    }

                                    if(!isTaken) {

                                        if(!validate(email)){
                                            Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                                        }else {
                                            if(isValidPassword(password) && isValidPassword(confirmm)) {
                                                if(password.equals(confirmm)) {
                                                    User user = new User(username, email, password, "Not Stated", 0, "basic");

                                                    users.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getActivity(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                            Navigation.findNavController(view).navigate(R.id.action_register_to_login);
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(getActivity(), "Please retype password correctly", Toast.LENGTH_SHORT).show();
                                                }

                                            }else {
                                                Toast.makeText(getActivity(), "Password must contain at least one numeric ,lowercase,uppercase ,special symbol among @#$% with length 8-20" , Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                    }else {
                                        user.setText("");
                                        emaill.setText("");
                                        passwordd.setText("");

                                    }
                                }
                            });




                }else
                    Toast.makeText(getActivity(), "Please Input All Missing Blanks", Toast.LENGTH_SHORT).show();
            }
        });


        ImageView back = view.findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_register_to_login);
            }
        });


        return view;
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean isValidPassword(String password)
    {
        boolean isValid = true;
        if (password.length() > 15 || password.length() < 8)
        {
            isValid = false;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars ))
        {
            isValid = false;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars ))
        {
            isValid = false;
        }
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers ))
        {
            isValid = false;
        }
        String specialChars = "(.*[@,#,$,%].*$)";
        if (!password.matches(specialChars ))
        {
            isValid = false;
        }
        return isValid;
    }

}