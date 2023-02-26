package com.example.quizapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link quizhistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class quizhistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference username = db.collection("Users");
    private CollectionReference history = db.collection("History");
    private ToggleButton choicee1;
    private ToggleButton choicee2;
    private ToggleButton choicee3;
    private ToggleButton choicee4;
    private TextView questionn;
    private TextView qnum;
    private int num = 0;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public quizhistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment quizhistory.
     */
    // TODO: Rename and change types and number of parameters
    public static quizhistory newInstance(String param1, String param2) {
        quizhistory fragment = new quizhistory();
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
        View view = inflater.inflate(R.layout.fragment_quizhistory, container, false);
        questionn = view.findViewById(R.id.question);
        choicee1 = view.findViewById(R.id.choice1);
        choicee2 = view.findViewById(R.id.choice2);
        choicee3 = view.findViewById(R.id.choice3);
        choicee4 = view.findViewById(R.id.choice4);
        qnum = view.findViewById(R.id.qnum);
        ImageView next = view.findViewById(R.id.next);
        ImageView previous = view.findViewById(R.id.previous);
        Button back = view.findViewById(R.id.exit);
        String yes = requireArguments().getString("quiz");
        DocumentReference doc = db.collection("History").document(yes);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String quiz = (String) documentSnapshot.get("quiz");
                CollectionReference questionwhere = db.collection(quiz);
                questionwhere.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<Question> questions = new ArrayList<>();
                        Map<String,Object> map = documentSnapshot.getData();
                        int n = (map.size()-4)/3;
                        for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots) {
                            Question user = queryDocumentSnapshot.toObject(Question.class);
                            for(int i = 0;i<n;i++){
                                if(queryDocumentSnapshot.get("key").equals(documentSnapshot.get("question"+i))){

                                    long lol1 = (long) documentSnapshot.get("useransquestion"+i);

                                    user.setUserAnswer((int)lol1);

                                    questions.add(user);
                                }
                                System.out.println(questions.isEmpty());
                            }

                        }



                        setQnum(0);
                        displayQuestion(0,questions);
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(num < questions.size()-1) {
                                    num += 1;
                                    resetColor();
                                    setQnum(num);
                                    displayQuestion(num, questions);

                                }
                            }
                        });

                        previous.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(num != 0) {
                                    num -= 1;
                                    resetColor();
                                    setQnum(num);
                                    displayQuestion(num, questions);

                                }
                            }
                        });

                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Exit Quiz History");
                builder.setMessage("Are You Sure To Exit");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle = new Bundle();
                                String name = requireArguments().getString("username");
                                bundle.putString("username",name);
                                Navigation.findNavController(view).navigate(R.id.action_quizhistory_to_historypage,bundle);

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

        return view;
    }

    private void displayQuestion(int num,ArrayList<Question> questions){
        Question user = questions.get(num);

        int ans = questions.get(num).getAnswer();
        int userans = questions.get(num).getUserAnswer();


        switch (userans){
            case 1 : {
                choicee1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f46a61")));


                break;
            }
            case 2: {

                choicee2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f46a61")));

                break;
            }
            case 3:{
                choicee3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f46a61")));
                break;
            }
            case 4:{
                choicee4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f46a61")));
                break;
            }
            default:{
                choicee1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f46a61")));
                choicee2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f46a61")));
                choicee3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f46a61")));
                choicee4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f46a61")));
                break;
            }
        }

            switch (ans){
                case 1 : {

                    choicee1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7ed957")));

                    break;
                }
                case 2: {

                    choicee2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7ed957")));

                    break;
                }
                case 3:{
                    choicee3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7ed957")));
                    break;
                }
                case 4:{
                    choicee4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7ed957")));
                    break;
                }
            }

        this.questionn.setText(user.getQuestion());
        this.choicee1.setText(user.getChoice1());
        this.choicee2.setText(user.getChoice2());
        this.choicee3.setText(user.getChoice3());
        this.choicee4.setText(user.getChoice4());
    }

    private void resetColor(){
        choicee1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
        choicee2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
        choicee3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
        choicee4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
    }

    private void setQnum(int n){
        n += 1;
        qnum.setText("QUESTION "+n);
    }

}