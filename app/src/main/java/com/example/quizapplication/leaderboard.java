package com.example.quizapplication;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link leaderboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class leaderboard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users = db.collection("Users");
    private CollectionReference history = db.collection("History");
    private static final DecimalFormat df = new DecimalFormat("0");
    private TextView analysis;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public leaderboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leaderboard.
     */
    // TODO: Rename and change types and number of parameters
    public static leaderboard newInstance(String param1, String param2) {
        leaderboard fragment = new leaderboard();
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
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        TextView username = view.findViewById(R.id.userTab);
        TextView school = view.findViewById(R.id.schoolTab);
        TextView score = view.findViewById(R.id.scoreTab);
        analysis = view.findViewById(R.id.analysis);

        history.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String name = getArguments().getString("username");
                double mscore = 0;
                double sscore = 0;
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    if(queryDocumentSnapshot.get("username").equals(name)){
                        if(queryDocumentSnapshot.get("quiz").equals("mathseasy")||queryDocumentSnapshot.get("quiz").equals("mathshard"))
                        mscore += (double)queryDocumentSnapshot.get("pointgain");
                        else {
                            sscore += (double)queryDocumentSnapshot.get("pointgain");
                        }
                    }
                }
                if(mscore>sscore){
                    analysis.setText("You Are Better In Maths Than Science");
                }else if(mscore<sscore){
                    analysis.setText("You Are Better In Science Than Maths");
                }else
                    analysis.setText("Not Applicable");
            }
        });

        users.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        ArrayList<User> userss = new ArrayList<>();

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){
                            User user = documentSnapshot.toObject(User.class);

                            userss.add(user);

                            String username = user.getUsername();
                            String school = user.getSchool();
                            double score = user.getScore();
                            String scoree = df.format(score);









                        }

                        username.setText(leaderboard(userss,1));
                        school.setText(leaderboard(userss,2));
                        score.setText(leaderboard(userss,3));



                    }
                });

        ImageButton back = view.findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",data);
                if(data.equals("admin")){
                    Navigation.findNavController(view).navigate(R.id.action_leaderboard_to_adminmenu,bundle);
                }else
                Navigation.findNavController(view).navigate(R.id.action_leaderboard_to_homepage,bundle);
            }
        });

        return view;
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private String leaderboard(ArrayList<User> arrange,int x ){
        Collections.sort(arrange);
        String content = "";
        if(x ==1 ) {
            for (User user : arrange) {
                String username = user.getUsername();
                if(!username.equals("admin")) {
                    String school = user.getSchool();
                    double score = user.getScore();
                    String scoree = df.format(score);
                    content += "\t\t\t" + username + "\n";
                }

            }
        }else if(x ==2){
            for (User user : arrange) {
                String username = user.getUsername();
                String school = user.getSchool();
                if(school.equals("")){
                    school = "Not Stated";
                }
                double score = user.getScore();
                String scoree = df.format(score);
                if(!username.equals("admin"))
                content += "\t\t"+school+"\n";

            }
        }else
        {
            for (User user : arrange) {
                String username = user.getUsername();
                String school = user.getSchool();
                double score = user.getScore();
                String scoree = df.format(score);
                if(!username.equals("admin"))
                content += "\t\t"+scoree+"\n";

            }
        }
        return content;
    }

}