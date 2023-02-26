package com.example.quizapplication;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editndelpage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editndelpage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    private TextView dataa;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public editndelpage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editndelpage.
     */
    // TODO: Rename and change types and number of parameters
    public static editndelpage newInstance(String param1, String param2) {
        editndelpage fragment = new editndelpage();
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
        View view = inflater.inflate(R.layout.fragment_editndelpage, container, false);
        ImageButton back = view.findViewById(R.id.imageButton);
        String quizz = getArguments().getString("quiz");
        CollectionReference quiz = db.collection(quizz);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = requireArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username",data);
                Navigation.findNavController(view).navigate(R.id.action_editndelpage_to_adminmenu,bundle);
            }
        });

        dataa = view.findViewById(R.id.data);

        quiz.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";
                int n = 1;
                List<String> listt = new ArrayList<>();
                ArrayList<String> quiz = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){

                    Question question = queryDocumentSnapshot.toObject(Question.class);
                    String documentID = queryDocumentSnapshot.getId();
                            data += Integer.toString(n)+". "+question.getQuestion()+"\n-------------------------------------------\n";
                            listt.add(Integer.toString(n));
                            n++;
                            quiz.add(documentID);
                }

                dataa.setText(data);

                Spinner spinner = view.findViewById(R.id.spinner2);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listt);
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

                Button button = view.findViewById(R.id.choose);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        String name = requireArguments().getString("username");
                        bundle.putString("username",name);
                        String spin = (String) spinner.getSelectedItem();
                        int num = Integer.parseInt(spin)-1;
                        bundle.putString("quiz",quiz.get(num));
                        bundle.putString("cat",getArguments().getString("quiz"));
                        Navigation.findNavController(view).navigate(R.id.action_editndelpage_to_editquestion,bundle);
                    }
                });
            }
        });


        return view;
    }
}