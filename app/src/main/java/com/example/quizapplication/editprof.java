package com.example.quizapplication;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editprof#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editprof extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users = db.collection("Users");

    private EditText name ;
    private EditText email ;
    private EditText school ;
    private EditText pass;
    private EditText newpass;
    private String passs ;

    private int start = 0;



    public editprof() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editprof.
     */
    // TODO: Rename and change types and number of parameters
    public static editprof newInstance(String param1, String param2) {
        editprof fragment = new editprof();
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

        View view = inflater.inflate(R.layout.fragment_editprof, container, false);
        Spinner spinner = view.findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        list.add("basic");
        list.add("cool");
        list.add("butterfly");
        list.add("warm");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        pass = view.findViewById(R.id.pass);
        newpass = view.findViewById(R.id.newpass);
        name = view.findViewById(R.id.namee);
        email = view.findViewById(R.id.emaill);
        school = view.findViewById(R.id.schooll);
        TextView score = view.findViewById(R.id.score1);

        Button confirm = view.findViewById(R.id.confirm);
        Typeface externalFont= ResourcesCompat.getFont(getContext(),R.font.poppins);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) spinner.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) spinner.getChildAt(0)).setTypeface(externalFont);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = getArguments().getString("username");


                String namee = name.getText().toString();
                String emaill = email.getText().toString();
                String schooll = school.getText().toString();
                String pic = (String) spinner.getSelectedItem();


                if (!isEmpty(name) || !isEmpty(email)){
                    users.get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        User user = documentSnapshot.toObject(User.class);
                                        user.setDocumentID(documentSnapshot.getId());

                                        String documentID = user.getDocumentID();
                                        String username = user.getUsername();


                                        if (username.equals(data)) {
                                            passs = user.getPassword();
                                            DocumentReference userRef = db.collection("Users").document(documentID);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("username", namee);
                                            userRef.update("username", namee);
                                            if(validate(emaill)) {
                                                userRef.update("email", emaill);
                                            }else{
                                                Toast.makeText(getActivity(), "Email Not Valid", Toast.LENGTH_SHORT).show();
                                            }
                                            userRef.update("school", schooll);
                                            userRef.update("pic", pic);
                                            if (passs.equals(pass.getText().toString()) && !isEmpty(pass) && !isEmpty(newpass) && isValidPassword(newpass.getText().toString())) {
                                                userRef.update("password", newpass.getText().toString());
                                                Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();
                                            }

                                            Navigation.findNavController(view).navigate(R.id.action_editprof_to_profile2, bundle);

                                            return;
                                        }


                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "LMAO", Toast.LENGTH_SHORT).show();
                                }
                            });
            }else
                    Toast.makeText(getActivity(), "Please Don't Leave In Blanks", Toast.LENGTH_SHORT).show();
            }
        });






        users.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                            User user = documentSnapshot.toObject(User.class);
                            user.setDocumentID(documentSnapshot.getId());
                            String data = getArguments().getString("username");

                            String documentID = user.getDocumentID();
                            String username = user.getUsername();
                            String emaill = user.getEmail();
                            String schooll = user.getSchool();
                            String pic = user.getPic();
                            double score = user.getScore();
                            if(username.equals(data)){
                                TextView score1 = view.findViewById(R.id.score1);


                                ImageView image = view.findViewById(R.id.imageView3);
                                Resources res = getResources();
                                int resID = res.getIdentifier(pic,"drawable", getActivity().getPackageName());

                                image.setImageResource(resID);

                                name.setText(username);
                                email.setText(emaill);
                                school.setText(schooll);
                                String n = df.format(score);
                                score1.setText(n);

                            }
                        }



                    }
                });

        ImageButton back = view.findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",data);
                Navigation.findNavController(view).navigate(R.id.action_editprof_to_profile2,bundle);
            }
        });







        return view;
    }

    public void update(View view){
        String data = getArguments().getString("username");

        EditText name = view.findViewById(R.id.namee);
        EditText email = view.findViewById(R.id.emaill);
        EditText school = view.findViewById(R.id.schooll);

        String namee = name.getText().toString();
        String emaill = email.getText().toString();
        String schooll = school.getText().toString();


        /*users.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                            User user = documentSnapshot.toObject(User.class);
                            user.setDocumentID(documentSnapshot.getId());

                            String documentID = user.getDocumentID();
                            String username = user.getUsername();



                            if(username.equals(username) ){
                                DocumentReference userRef = db.collection("Users").document(documentID);
                                Bundle bundle = new Bundle();
                                bundle.putString("username",username);
                                userRef.update("username",namee);
                                userRef.update("email",emaill);
                                userRef.update("school",schooll);
                                Navigation.findNavController(view).navigate(R.id.action_editprof_to_profile2,bundle);

                                return;
                            }else
                                Toast.makeText(getActivity(),"Wrong Username Or Password!!",Toast.LENGTH_SHORT).show();



                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "LMAO", Toast.LENGTH_SHORT).show();
                    }
                });*/
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