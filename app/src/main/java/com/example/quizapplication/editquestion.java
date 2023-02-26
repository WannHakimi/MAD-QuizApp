package com.example.quizapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editquestion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editquestion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users = db.collection("Users");
    private EditText questionn;
    private EditText choice1;
    private EditText choice2;
    private EditText choice3;
    private EditText choice4;
    private EditText hint;
    private Button editq;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public editquestion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editquestion.
     */
    // TODO: Rename and change types and number of parameters
    public static editquestion newInstance(String param1, String param2) {
        editquestion fragment = new editquestion();
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
        View view = inflater.inflate(R.layout.fragment_editquestion, container, false);
        ImageButton button = view.findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getArguments().getString("username");
                String quiz = getArguments().getString("cat");
                Bundle bundle = new Bundle();
                bundle.putString("username",name);
                bundle.putString("quiz",quiz);
                Navigation.findNavController(view).navigate(R.id.action_editquestion_to_editndelpage2,bundle);
            }
        });
        questionn = view.findViewById(R.id.ques);
        choice1 = view.findViewById(R.id.choi1);
        choice2 = view.findViewById(R.id.choi2);
        choice3 = view.findViewById(R.id.choi3);
        choice4 = view.findViewById(R.id.choi4);
        hint = view.findViewById(R.id.hin);
        editq = view.findViewById(R.id.editq);
        CollectionReference users = db.collection("Users");
        DocumentReference yes = db.collection(getArguments().getString("cat")).document(getArguments().getString("quiz"));
        yes.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<String> list = new ArrayList<>();
                list.add("1");
                list.add("2");
                list.add("3");
                list.add("4");
                Spinner spinner = view.findViewById(R.id.cor);
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
                Question question = documentSnapshot.toObject(Question.class);
                questionn.setText(question.getQuestion());
                choice1.setText(question.getChoice1());
                choice2.setText(question.getChoice2());
                choice3.setText(question.getChoice3());
                choice4.setText(question.getChoice4());
                hint.setText(question.getHint());
                spinner.setSelection(question.getAnswer()-1);

                editq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data = getArguments().getString("quiz");

                        String question = questionn.getText().toString();
                        String choice11 = choice1.getText().toString();
                        String choice22 = choice2.getText().toString();
                        String choice33 = choice3.getText().toString();
                        String choice44 = choice4.getText().toString();
                        String hintt = hint.getText().toString();
                        String correct = (String) spinner.getSelectedItem();
                        int corr = Integer.parseInt(correct);



                        Bundle bundle = new Bundle();
                        yes.update("question",question);
                        yes.update("choice1",choice11);
                        yes.update("choice2",choice22);
                        yes.update("choice3",choice33);
                        yes.update("choice4",choice44);
                        yes.update("answer",corr);
                        String name = getArguments().getString("username");
                        bundle.putString("username",name);
                        Toast.makeText(getActivity(), "Question Successfully Edited", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_editquestion_to_adminmenu,bundle);
                    }
                });
            }
        });



        return view;
    }
}