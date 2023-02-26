package com.example.quizapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link deletechoose#newInstance} factory method to
 * create an instance of this fragment.
 */
public class deletechoose extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView maths;
    private ImageView science;
    public deletechoose() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment deletechoose.
     */
    // TODO: Rename and change types and number of parameters
    public static deletechoose newInstance(String param1, String param2) {
        deletechoose fragment = new deletechoose();
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
        View view = inflater.inflate(R.layout.fragment_deletechoose, container, false);
        maths = view.findViewById(R.id.BtnMath);
        science = view.findViewById(R.id.BtnScience);
        ImageButton back = view.findViewById(R.id.imageButton2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",data);
                Navigation.findNavController(view).navigate(R.id.action_deletechoose_to_adminmenu,bundle);
            }
        });

        maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Go to Edit Question");
                builder.setMessage("Choose Difficulty for Maths");
                builder.setPositiveButton("Easy",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle = new Bundle();
                                String name = requireArguments().getString("username");
                                bundle.putString("username",name);
                                bundle.putString("quiz","mathseasy");
                                Navigation.findNavController(view).navigate(R.id.action_deletechoose_to_deletequestion,bundle);

                            }
                        });
                builder.setNegativeButton("Hard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        String name = requireArguments().getString("username");
                        bundle.putString("username",name);
                        bundle.putString("quiz","mathshard");
                        Navigation.findNavController(view).navigate(R.id.action_deletechoose_to_deletequestion,bundle);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Go to Edit Question");
                builder.setMessage("Choose Difficulty for Science");
                builder.setPositiveButton("Easy",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle = new Bundle();
                                String name = requireArguments().getString("username");
                                bundle.putString("username",name);
                                bundle.putString("quiz","scienceeasy");
                                Navigation.findNavController(view).navigate(R.id.action_deletechoose_to_deletequestion,bundle);

                            }
                        });
                builder.setNegativeButton("Hard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        String name = requireArguments().getString("username");
                        bundle.putString("username",name);
                        bundle.putString("quiz","sciencehard");
                        Navigation.findNavController(view).navigate(R.id.action_deletechoose_to_deletequestion,bundle);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        return view;
    }
}