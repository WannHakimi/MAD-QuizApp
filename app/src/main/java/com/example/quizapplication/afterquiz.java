package com.example.quizapplication;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link afterquiz#newInstance} factory method to
 * create an instance of this fragment.
 */
public class afterquiz extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView score;
    private TextView points;
    private TextView eligible;
    private Button cont;
    private Button generate;

    public afterquiz() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment afterquiz.
     */
    // TODO: Rename and change types and number of parameters
    public static afterquiz newInstance(String param1, String param2) {
        afterquiz fragment = new afterquiz();
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
        View view = inflater.inflate(R.layout.fragment_afterquiz, container, false);
        cont = view.findViewById(R.id.cont);
        generate = view.findViewById(R.id.cert);
        score = view.findViewById(R.id.finalscore);
        points = view.findViewById(R.id.gained);
        eligible = view.findViewById(R.id.textView14);

        eligible.setVisibility(View.INVISIBLE);
        generate.setVisibility(View.INVISIBLE);

        score.setText(getArguments().getString("score"));
        points.setText(getArguments().getString("points"));
        int score = Integer.parseInt(getArguments().getString("betul"));
        if(score == 10){
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),R.raw.congrats);

            mediaPlayer.start();
        }else if(score >= 7 ){
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),R.raw.yay);

            mediaPlayer.start();
        }else if(score >= 4){
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),R.raw.nice);

            mediaPlayer.start();
        }else{
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),R.raw.boo);

            mediaPlayer.start();
        }



        if(getArguments().getString("betul").equals(getArguments().getString("total"))){
            eligible.setVisibility(View.VISIBLE);
            generate.setVisibility(View.VISIBLE);
        }

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                String name = getArguments().getString("username");
                bundle.putString("username",name);
                Navigation.findNavController(view).navigate(R.id.action_afterquiz_to_homepage,bundle);
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title",getArguments().getString("title"));
                bundle.putString("username",getArguments().getString("username"));
                Navigation.findNavController(view).navigate(R.id.action_afterquiz_to_certificate,bundle);
            }
        });


        return view;
    }
}