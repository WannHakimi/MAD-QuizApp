package com.example.quizapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link choosesub#newInstance} factory method to
 * create an instance of this fragment.
 */
public class choosesub extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public choosesub() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment choosesub.
     */
    // TODO: Rename and change types and number of parameters
    public static choosesub newInstance(String param1, String param2) {
        choosesub fragment = new choosesub();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choosesub, container, false);

        ImageView maths = view.findViewById(R.id.BtnMath);
        ImageView science = view.findViewById(R.id.BtnScience);
        ImageView back = view.findViewById(R.id.imageButton2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",name);
                Navigation.findNavController(view).navigate(R.id.action_choosesub_to_homepage);
            }
        });


        maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),R.raw.ready);

                mediaPlayer.start();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Enter Quiz");
                builder.setMessage("Are You Sure To Start Quiz");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String data = requireArguments().getString("username");
                                String diff = requireArguments().getString("difficulty");
                                Bundle bundle = new Bundle();
                                bundle.putString("username", data);
                                bundle.putString("difficulty", diff);
                                bundle.putString("subject","maths");
                                Navigation.findNavController(view).navigate(R.id.action_choosesub_to_quiz,bundle);

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

        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),R.raw.ready);

                mediaPlayer.start();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Enter Quiz");
                builder.setMessage("Are You Sure To Start Quiz");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String data = requireArguments().getString("username");
                                String diff = requireArguments().getString("difficulty");
                                Bundle bundle = new Bundle();
                                bundle.putString("username", data);
                                bundle.putString("difficulty", diff);
                                bundle.putString("subject","science");
                                Navigation.findNavController(view).navigate(R.id.action_choosesub_to_quiz,bundle);

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