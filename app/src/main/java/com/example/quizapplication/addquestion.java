package com.example.quizapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addquestion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addquestion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button add;
    private EditText question;
    private EditText choice1;
    private EditText choice2;
    private EditText choice3;
    private EditText choice4;
    private EditText hint;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference mathseasy = db.collection("mathseasy");
    private final CollectionReference mathshard = db.collection("mathshard");
    private final CollectionReference scienceeasy = db.collection("scienceeasy");
    private final CollectionReference sciencehard = db.collection("sciencehard");

    private int keyme ;
    private int keymh ;
    private int keyse  ;
    private int keysh ;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addquestion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addquestion.
     */
    // TODO: Rename and change types and number of parameters
    public static addquestion newInstance(String param1, String param2) {
        addquestion fragment = new addquestion();
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
        View view = inflater.inflate(R.layout.fragment_addquestion, container, false);

        ImageButton back = view.findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",name);
                Navigation.findNavController(view).navigate(R.id.action_addquestion_to_adminmenu,bundle);
            }
        });

        add = view.findViewById(R.id.editq);
        question = view.findViewById(R.id.ques);
        choice1 = view.findViewById(R.id.choi1);
        choice2 = view.findViewById(R.id.choi2);
        choice3 = view.findViewById(R.id.choi3);
        choice4 = view.findViewById(R.id.choi4);
        hint = view.findViewById(R.id.hin);

        List<String> list = new ArrayList<>();
        list.add("Select Category");
        list.add("Maths Easy");
        list.add("Maths Hard");
        list.add("Science Easy");
        list.add("Science Hard");
        Spinner spinner = view.findViewById(R.id.cat);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
        List<String> listt = new ArrayList<>();

        listt.add("Select Correct Answer");
        listt.add("1");
        listt.add("2");
        listt.add("3");
        listt.add("4");
        Spinner spinnerr = view.findViewById(R.id.cor);
        ArrayAdapter<String> adapterr = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listt);
        adapterr.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerr.setAdapter(adapterr);
        spinnerr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) spinnerr.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) spinnerr.getChildAt(0)).setTypeface(externalFont);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yes = (String) spinner.getSelectedItem();
                String no = (String) spinnerr.getSelectedItem();
                if(yes.equals("Select Category") || no.equals("Select Correct Answer")){
                    Toast.makeText(getActivity(), "Select Category and Correct Answer", Toast.LENGTH_SHORT).show();
                }else{

                    if(spinner.getSelectedItem().equals("Maths Easy")){
                        mathseasy.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                keyme = 1;
                                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                                    keyme++;
                                }
                                String q = question.getText().toString();
                                String choi1 = choice1.getText().toString();
                                String choi2 = choice2.getText().toString();
                                String choi3 = choice3.getText().toString();
                                String choi4 = choice4.getText().toString();
                                String hin = hint.getText().toString();
                                String correct = (String) spinnerr.getSelectedItem();
                                int ans = Integer.parseInt(correct);


                                Question questionn = new Question(q,choi1,choi2,choi3,choi4,ans,hin,keyme);
                                mathseasy.add(questionn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Question Added Successfully", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        String username = requireArguments().getString("username");
                                        bundle.putString("username",username);
                                        Navigation.findNavController(view).navigate(R.id.action_addquestion_to_adminmenu,bundle);
                                    }
                                });
                            }
                        });
                    }

                    if(spinner.getSelectedItem().equals("Maths Hard")){
                        mathshard.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                keymh = 1;
                                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                                    keymh++;
                                }
                                String q = question.getText().toString();
                                String choi1 = choice1.getText().toString();
                                String choi2 = choice2.getText().toString();
                                String choi3 = choice3.getText().toString();
                                String choi4 = choice4.getText().toString();
                                String hin = hint.getText().toString();
                                String correct = (String) spinnerr.getSelectedItem();
                                int ans = Integer.parseInt(correct);


                                Question questionn = new Question(q,choi1,choi2,choi3,choi4,ans,hin,keymh);
                                mathshard.add(questionn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Question Added Successfully", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        String username = requireArguments().getString("username");
                                        bundle.putString("username",username);
                                        Navigation.findNavController(view).navigate(R.id.action_addquestion_to_adminmenu,bundle);
                                    }
                                });
                            }
                        });
                    }

                    if(spinner.getSelectedItem().equals("Science Easy")){
                        scienceeasy.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                keyse = 1;
                                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                                    keyse++;
                                }
                                String q = question.getText().toString();
                                String choi1 = choice1.getText().toString();
                                String choi2 = choice2.getText().toString();
                                String choi3 = choice3.getText().toString();
                                String choi4 = choice4.getText().toString();
                                String hin = hint.getText().toString();
                                String correct = (String) spinnerr.getSelectedItem();
                                int ans = Integer.parseInt(correct);


                                Question questionn = new Question(q,choi1,choi2,choi3,choi4,ans,hin,keyse);
                                scienceeasy.add(questionn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Question Added Successfully", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        String username = requireArguments().getString("username");
                                        bundle.putString("username",username);
                                        Navigation.findNavController(view).navigate(R.id.action_addquestion_to_adminmenu,bundle);
                                    }
                                });
                            }
                        });
                    }

                    if(spinner.getSelectedItem().equals("Science Hard")){
                        sciencehard.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                keysh = 1;
                                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                                    keysh++;
                                }
                                String q = question.getText().toString();
                                String choi1 = choice1.getText().toString();
                                String choi2 = choice2.getText().toString();
                                String choi3 = choice3.getText().toString();
                                String choi4 = choice4.getText().toString();
                                String hin = hint.getText().toString();
                                String correct = (String) spinnerr.getSelectedItem();
                                int ans = Integer.parseInt(correct);


                                Question questionn = new Question(q,choi1,choi2,choi3,choi4,ans,hin,keysh);
                                sciencehard.add(questionn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Question Added Successfully", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        String username = requireArguments().getString("username");
                                        bundle.putString("username",username);
                                        Navigation.findNavController(view).navigate(R.id.action_addquestion_to_adminmenu,bundle);
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });






        mathseasy.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    keyme++;
                }
            }
        });





        return view;
    }

    private void setReset(){
        question.setText("");
        choice1.setText("");
        choice2.setText("");
        choice3.setText("");
        choice4.setText("");
        hint.setText("");
    }





}