package com.example.quizapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homepage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homepage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String diff;

    public homepage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homepage.
     */
    // TODO: Rename and change types and number of parameters
    public static homepage newInstance(String param1, String param2) {
        homepage fragment = new homepage();
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


        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        Button prof = view.findViewById(R.id.profile);
        Button choose = view.findViewById(R.id.choose);
        Button history = view.findViewById(R.id.hist);
        ImageButton back = view.findViewById(R.id.imageButton);

        CheckBox easy = view.findViewById(R.id.easy);
        CheckBox hard = view.findViewById(R.id.hard);
        Button leader = view.findViewById(R.id.leader);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username",requireArguments().getString("username"));
                Navigation.findNavController(view).navigate(R.id.action_homepage_to_historypage,bundle);
            }
        });

        leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username",requireArguments().getString("username"));
                Navigation.findNavController(view).navigate(R.id.action_homepage_to_leaderboard,bundle);
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hard.isChecked() && !easy.isChecked()){
                    Toast.makeText(getActivity(), "Choose a Difficulty", Toast.LENGTH_SHORT).show();

                }else {
                    if(hard.isChecked()) {
                        String data = requireArguments().getString("username");
                        Bundle bundle = new Bundle();
                        bundle.putString("username", data);
                        bundle.putString("difficulty", "hard");
                        Navigation.findNavController(view).navigate(R.id.action_homepage_to_choosesub, bundle);
                    }else{
                        String data = requireArguments().getString("username");
                        Bundle bundle = new Bundle();
                        bundle.putString("username", data);
                        bundle.putString("difficulty", "easy");
                        Navigation.findNavController(view).navigate(R.id.action_homepage_to_choosesub, bundle);
                    }
                }
            }
        });




        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hard.isChecked()){
                    hard.toggle();

                }
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(easy.isChecked()){
                    easy.toggle();

                }
            }
        });

        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",data);
                Navigation.findNavController(view).navigate(R.id.action_homepage_to_profile2,bundle);
            }
        });

        ImageButton faq = view.findViewById(R.id.faq);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Navigate");
                builder.setMessage("Where Do You Want To Go Next");
                builder.setPositiveButton("FAQ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String name = requireArguments().getString("username");
                                Bundle bundle = new Bundle();
                                bundle.putString("username",name);
                                Navigation.findNavController(view).navigate(R.id.action_homepage_to_faq2,bundle);
                            }
                        });
                builder.setNegativeButton("Feedback", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = requireArguments().getString("username");
                        Bundle bundle = new Bundle();
                        bundle.putString("username",name);
                        Navigation.findNavController(view).navigate(R.id.action_homepage_to_rating,bundle);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Logout");
                builder.setMessage("Are You Sure To logout");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Navigation.findNavController(view).navigate(R.id.action_homepage_to_login);

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


}