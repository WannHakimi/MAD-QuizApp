package com.example.quizapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link quiz#newInstance} factory method to
 * create an instance of this fragment.
 */
public class quiz extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference username = db.collection("Users");
    private CollectionReference history = db.collection("History");



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  static final  long START_TIME_IN_MILLIS = 600000;
    private TextView countdowntimer ;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private String diff;


    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private double timetaken = 600000;
    private String quiz;
    private ToggleButton choice1;
    private ToggleButton choice2;
    private ToggleButton choice3;
    private ToggleButton choice4;
    private TextView question;
    private ImageView next ;
    private ImageView prev;
    private Button submit;
    private int counter;
    private String title;
    private Button hint;
    private TextView qnum;
    private double minus;
    private String newtitle;


    private ArrayList<Question> questions = new ArrayList<>();








    public quiz() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment quiz.
     */
    // TODO: Rename and change types and number of parameters
    public static quiz newInstance(String param1, String param2) {
        quiz fragment = new quiz();
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
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        countdowntimer = view.findViewById(R.id.countdown);
        minus=0;



        question = view.findViewById(R.id.question);
        choice1 = view.findViewById(R.id.choice1);
        choice2 = view.findViewById(R.id.choice2);
        choice3 = view.findViewById(R.id.choice3);
        choice4 = view.findViewById(R.id.choice4);
        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.previous);
        choice1 = view.findViewById(R.id.choice1);
        choice2= view.findViewById(R.id.choice2);
        choice3= view.findViewById(R.id.choice3);
        choice4= view.findViewById(R.id.choice4);
        submit= view.findViewById(R.id.exit);
        hint = view.findViewById(R.id.button);
        qnum = view.findViewById(R.id.qnum);

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(minus < 950) {
                    minus += 15;
                }
            }
        });






        CollectionReference users;
        String subject = getArguments().getString("subject");
        String difficulty = getArguments().getString("difficulty");



        if(subject.equals("maths")){
            if(difficulty.equals("easy")){
                users = db.collection("mathseasy");
                quiz = "mathseasy";
                title = "Maths Easy "+new Date().toString();
                newtitle = "Easy Quiz (Math)";
                diff = "easy";

            }else {
                users = db.collection("mathshard");
                quiz = "mathshard";
                title = "Maths Hard "+new Date().toString();
                newtitle = "Hard Quiz (Math)";
                diff = "hard";
            }

        }else{
            if(difficulty.equals("easy")){
                users = db.collection("scienceeasy");
                quiz = "scienceeasy";
                title = "Science Easy "+new Date().toString();
                newtitle = "Easy Quiz (Science)";
                diff = "easy";
            }else {
                users = db.collection("sciencehard");
                quiz = "sciencehard";
                title = "Science Hard "+new Date().toString();
                newtitle = "Hard Quiz (Science)";
                diff = "hard";
            }

        }





        users.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Question> questions = new ArrayList<>();



                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                            Question user = documentSnapshot.toObject(Question.class);

                            String question = user.getQuestion();
                            String choice1 = user.getChoice1();
                            String choice2 = user.getChoice2();
                            String choice3 = user.getChoice3();
                            String choice4 = user.getChoice4();
                            String hint = user.getHint();
                            int answer = user.getAnswer();

                            questions.add(user);
                        }
                        setQnum(counter);

                        ArrayList<Question> real = randomQuestion(questions);
                        counter = 0;
                        displayQuestion(0,real);
                        if(diff.equals("hard")) {
                            hint.setVisibility(View.INVISIBLE);
                            ImageView imageView = view.findViewById(R.id.imageView18);
                            imageView.setVisibility(View.INVISIBLE);
                        }

                        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),R.raw.song);

                        mediaPlayer.start();
                        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
                            @Override
                            public void onTick(long l) {
                                mTimeLeftInMillis = l;
                                updateCountDownText();
                            }

                            @Override
                            public void onFinish() {

                                double scoree = checkAnswer(real)-minus;
                                history.add(saveAns(real));
                                System.out.println(scoree);
                                mediaPlayer.release();


                                username.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                                            User user = documentSnapshot.toObject(User.class);
                                            user.setDocumentID(documentSnapshot.getId());
                                            String name = getArguments().getString("username");

                                            String documentID = user.getDocumentID();
                                            String username = user.getUsername();






                                            if(username.equals(name) ){
                                                DocumentReference userRef = db.collection("Users").document(documentID);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("username",name);

                                                double score = user.getScore();

                                                score = score + scoree;
                                                int betul = 0;
                                                double newscore = Math.floor(scoree);
                                                userRef.update("score",score);
                                                for (Question question:real) {
                                                    if(question.getAnswer()==question.getUserAnswer()){
                                                        betul++;
                                                    }
                                                }
                                                bundle.putString("score",String.valueOf(betul)+"/"+String.valueOf(real.size()));
                                                bundle.putString("points",Double.toString(newscore));
                                                bundle.putString("betul",Integer.toString(betul));
                                                bundle.putString("total",Integer.toString(real.size()));
                                                bundle.putString("title",newtitle);
                                                Navigation.findNavController(view).navigate(R.id.action_quiz_to_afterquiz,bundle);

                                                return;
                                            }



                                        }
                                    }
                                });

                            }
                        }.start();
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                if (choice1.isChecked()) {
                                    real.get(counter).setUserAnswer(1);
                                    setColour(1);
                                } else if (choice2.isChecked()) {
                                    real.get(counter).setUserAnswer(2);
                                    setColour(2);
                                } else if(choice3.isChecked()){
                                    real.get(counter).setUserAnswer(3);
                                    setColour(3);
                                }else if(choice4.isChecked()){
                                    real.get(counter).setUserAnswer(4);
                                    setColour(4);
                                }else {
                                }
                                if(counter < real.size()-1) {
                                    counter += 1;
                                    if(real.get(counter).getUserAnswer() == -1){
                                        setColour(-1);
                                        choice2.setChecked(false);
                                        choice3.setChecked(false);
                                        choice4.setChecked(false);
                                        choice1.setChecked(false);
                                    }
                                    setQnum(counter);
                                    displayQuestion(counter, real);

                                }

                            }
                        });

                        prev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                if (choice1.isChecked()) {
                                    real.get(counter).setUserAnswer(1);
                                    setColour(1);
                                } else if (choice2.isChecked()) {
                                    real.get(counter).setUserAnswer(2);
                                    setColour(2);
                                } else if(choice3.isChecked()){
                                    real.get(counter).setUserAnswer(3);
                                    setColour(3);
                                }else if(choice4.isChecked()){
                                    real.get(counter).setUserAnswer(4);
                                    setColour(4);
                                }else{
                                }
                                if(counter != 0) {
                                    counter -= 1;
                                    if(real.get(counter).getUserAnswer() == -1){
                                        choice2.setChecked(false);
                                        choice3.setChecked(false);
                                        choice4.setChecked(false);
                                        choice1.setChecked(false);
                                    }
                                    setQnum(counter);
                                    displayQuestion(counter, real);

                                }
                            }
                        });

                        choice1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                real.get(counter).setUserAnswer(1);
                                choice2.setChecked(false);
                                choice3.setChecked(false);
                                choice4.setChecked(false);
                                setColour(1);

                            }
                        });

                        choice2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                real.get(counter).setUserAnswer(2);
                                choice1.setChecked(false);
                                choice3.setChecked(false);
                                choice4.setChecked(false);
                                setColour(2);
                            }
                        });

                        choice3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                real.get(counter).setUserAnswer(3);
                                choice1.setChecked(false);
                                choice2.setChecked(false);
                                choice4.setChecked(false);
                                setColour(3);
                            }
                        });

                        choice4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                real.get(counter).setUserAnswer(4);
                                choice1.setChecked(false);
                                choice2.setChecked(false);
                                choice3.setChecked(false);
                                setColour(4);
                            }
                        });




                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {




                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setCancelable(true);
                                builder.setTitle("Submission Confirmation");
                                builder.setMessage("Are You Sure To Submit");
                                builder.setPositiveButton("Confirm",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                double scoree = checkAnswer(real);
                                                history.add(saveAns(real));
                                                System.out.println(scoree);
                                                mediaPlayer.release();

                                                username.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                                                            User user = documentSnapshot.toObject(User.class);
                                                            user.setDocumentID(documentSnapshot.getId());
                                                            String name = getArguments().getString("username");

                                                            String documentID = user.getDocumentID();
                                                            String username = user.getUsername();






                                                            if(username.equals(name) ){
                                                                DocumentReference userRef = db.collection("Users").document(documentID);
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("username",name);

                                                                double score = user.getScore();

                                                                score = score + scoree;
                                                                int betul = 0;
                                                                double newscore = Math.floor(scoree);
                                                                userRef.update("score",score);
                                                                for (Question question:real) {
                                                                    if(question.getAnswer()==question.getUserAnswer()){
                                                                        betul++;
                                                                    }
                                                                }
                                                                bundle.putString("score",String.valueOf(betul)+"/"+String.valueOf(real.size()));
                                                                bundle.putString("points",Double.toString(newscore));
                                                                bundle.putString("betul",Integer.toString(betul));
                                                                bundle.putString("total",Integer.toString(real.size()));
                                                                bundle.putString("title",newtitle);
                                                                Navigation.findNavController(view).navigate(R.id.action_quiz_to_afterquiz,bundle);

                                                                return;
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









                    }
                });






        return view;
    }

    private void updateCountDownText(){
        int minutes = (int)(mTimeLeftInMillis/1000)/60;
        int seconds = (int)(mTimeLeftInMillis/1000)%60;
        timetaken = timetaken-1000;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        countdowntimer.setText(timeLeftFormatted);
    }

    private void getQuestion(){


        CollectionReference users;
        String subject = getArguments().getString("subject");
        String difficulty = getArguments().getString("difficulty");


        if(subject.equals("maths")){
            if(difficulty.equals("easy")){
                users = db.collection("mathseasy");
            }else {
                users = db.collection("mathshard");
                quiz = "mathshard";
            }
        }else{
            if(difficulty.equals("easy")){
                users = db.collection("scienceeasy");
                quiz = "scienceeasy";
            }else {
                users = db.collection("sciencehard");
                quiz = "sciencehard";
            }

        }



        users.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Question> questions = new ArrayList<>();



                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                            Question user = documentSnapshot.toObject(Question.class);

                            String question = user.getQuestion();
                            String choice1 = user.getChoice1();
                            String choice2 = user.getChoice2();
                            String choice3 = user.getChoice3();
                            String choice4 = user.getChoice4();
                            String hint = user.getHint();
                            int answer = user.getAnswer();

                            questions.add(user);
                        }

                    }
                });



    }

    private ArrayList<Question> randomQuestion(ArrayList<Question> question){
        int total = question.size();
        System.out.println(total);
        ArrayList<Integer> ischecked = new ArrayList<>();
        for(int i = 0; i < total;i++){
            ischecked.add(i);
        }
        ischecked.add(-1);
        ArrayList<Question> real = new ArrayList<>();
        Random random = new Random();

        if(total > 10){
            for(int i = 0; i <= 9; ) {
                int number = random.nextInt(total );
                if(!isTaken(ischecked,number)) {
                    real.add(question.get(number));
                    ischecked.set(number,-1);
                    i++;
                }
            }

        }else{
            for(int i = 0; i < total; i++){
                real.add(question.get(i));
            }
        }

        return real;
    }

    private void displayQuestion(int num,ArrayList<Question> questions){
        Question user = questions.get(num);

        int ans = questions.get(num).getUserAnswer();
        if(ans != -1){
            switch (ans){
                case 1 : {
                    choice1.setChecked(true);
                    choice2.setChecked(false);
                    choice3.setChecked(false);
                    choice4.setChecked(false);
                    setColour(ans);
                    break;
                }
                case 2: {
                    choice1.setChecked(false);
                    choice2.setChecked(true);
                    choice3.setChecked(false);
                    choice4.setChecked(false);
                    setColour(ans);
                    break;
                }
                case 3:{
                    choice1.setChecked(false);
                    choice2.setChecked(false);
                    choice3.setChecked(true);
                    choice4.setChecked(false);
                    setColour(ans);
                    break;
                }
                case 4:{
                    choice1.setChecked(false);
                    choice2.setChecked(false);
                    choice3.setChecked(false);
                    choice4.setChecked(true);
                    setColour(ans);
                    break;
                }
            }
        }else {
            setColour(-1);
        }
        this.question.setText(user.getQuestion());
        this.choice1.setText(user.getChoice1());
        this.choice2.setText(user.getChoice2());
        this.choice3.setText(user.getChoice3());
        this.choice4.setText(user.getChoice4());
        this.choice1.setTextOff(user.getChoice1());
        this.choice2.setTextOff(user.getChoice2());
        this.choice3.setTextOff(user.getChoice3());
        this.choice4.setTextOff(user.getChoice4());
        this.choice1.setTextOn(user.getChoice1());
        this.choice2.setTextOn(user.getChoice2());
        this.choice3.setTextOn(user.getChoice3());
        this.choice4.setTextOn(user.getChoice4());





        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),questions.get(num).getHint(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double checkAnswer(ArrayList<Question> check){
        double score = 0;
        for(Question me: check){
            if(diff.equals("hard")) {
                if (me.getUserAnswer() == me.getAnswer()) {
                    score = (double) (score + 50 * (timetaken / 600000) + 10);

                }
            }
            if(diff.equals("easy")){
                if (me.getUserAnswer() == me.getAnswer()) {
                    score = (double) (score + 100 * (timetaken / 600000) + 10);

                }
            }
        }


        return score;
    }

    private Map<String,Object> saveAns(ArrayList<Question> check){

        int score = 0;
        double scoree = 0;
        int correct = 0;
        Map<String, Object> hist= new HashMap<>();
        String name = getArguments().getString("username");
        for(Question me: check){

            hist.put("question"+Integer.toString(score), me.getKey());
            hist.put("question"+Integer.toString(score)+"ans",me.getAnswer());
            hist.put("useransquestion"+Integer.toString(score),me.getUserAnswer());
            score++;
            if(me.getUserAnswer()==me.getAnswer()){
                correct ++;
                scoree = (double) (scoree+1000*(timetaken/600000)+100);
            }


        }
        int total = check.size();
        hist.put("total",total);
        hist.put("correct",correct);

        hist.put("score",Integer.toString(correct)+"/"+Integer.toString(score));
        hist.put("username",name);
        hist.put("quiz",quiz);
        hist.put("title",title);
        hist.put("pointgain",scoree);


        return hist;
    }





    private void setColour(int n){
            switch (n){
                case 1 : {
                    choice1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#403D9E")));
                    choice2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    break;
                }
                case 2: {
                    choice1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#403D9E")));
                    break;
                }
                case 3:{
                    choice2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#403D9E")));
                    break;
                }
                case 4:{
                    choice2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#403D9E")));
                    break;
                }
                default:{
                    choice2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    choice4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a167ba")));
                    break;
                }
            }

    }


    private void setQnum(int n){
        n += 1;
        qnum.setText("QUESTION "+n);
    }

    private boolean isTaken(ArrayList<Integer> i , int check){
        for(int me:i){
            if(me == check){
                return false;
            }
        }
        return true;
    }


}