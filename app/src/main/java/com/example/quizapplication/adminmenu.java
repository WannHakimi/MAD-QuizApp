package com.example.quizapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminmenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminmenu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button add;
    private Button delete;
    private Button leader;
    private Button profile;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminmenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminmenu.
     */
    // TODO: Rename and change types and number of parameters
    public static adminmenu newInstance(String param1, String param2) {
        adminmenu fragment = new adminmenu();
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
        View view = inflater.inflate(R.layout.fragment_adminmenu, container, false);
        ImageButton back = view.findViewById(R.id.imageButton);
        Button editq = view.findViewById(R.id.adedit);
        add = view.findViewById(R.id.adadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                Navigation.findNavController(view).navigate(R.id.action_adminmenu_to_addquestion,bundle);
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
                                Navigation.findNavController(view).navigate(R.id.action_adminmenu_to_login);

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

        editq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                Navigation.findNavController(view).navigate(R.id.action_adminmenu_to_chooseeditndelete,bundle);
            }
        });

        delete = view.findViewById(R.id.addelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                Navigation.findNavController(view).navigate(R.id.action_adminmenu_to_deletechoose,bundle);
            }
        });

        leader = view.findViewById(R.id.adleader);
        leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                Navigation.findNavController(view).navigate(R.id.action_adminmenu_to_leaderboard,bundle);
            }
        });





        return view;
    }
}