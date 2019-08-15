package info.androidhive.supegaofertador.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import info.androidhive.supegaofertador.R;


import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import info.androidhive.supegaofertador.helper.SessionManager;
import info.androidhive.supegaofertador.helper.DB_usuarios;


public class View_Home_Fragment extends Fragment {

    //declaracion de botones
    private ImageButton topbtn_1, topbtn_2, topbtn_3, topbtn_4;
    private Button btnComenzar;



    //private DB_usuarios dataUsuarios;




    //se llama a los modulos que manejan la base de datos y la sesion
    //private DB_usuarios dataUsuarios;
    private SessionManager session;

    public View_Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_home));


        //Se toman los botones
        topbtn_1      = (ImageButton) rootView.findViewById(R.id.topbtn_1);
        topbtn_2      = (ImageButton) rootView.findViewById(R.id.topbtn_2);
        topbtn_3      = (ImageButton) rootView.findViewById(R.id.topbtn_3);
        topbtn_4      = (ImageButton) rootView.findViewById(R.id.topbtn_4);
        btnComenzar   = (Button) rootView.findViewById(R.id.btnComenzar);

        // administrador de sesiones
        session       = new SessionManager(getActivity());
        //dataUsuarios  = new DB_usuarios(getActivity());





        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        // Link to Recover Screen
        topbtn_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //guardo el dato desde donde se preciono
                ((MainActivity) getActivity()).passDataActivity("1");
                //Inicio la pantalla
                Fragment fragment = new View_Job1_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        // Link to Recover Screen
        topbtn_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //guardo el dato desde donde se preciono
                ((MainActivity) getActivity()).passDataActivity("2");
                //Inicio la pantalla
                Fragment fragment = new View_Job1_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        // Link to Recover Screen
        topbtn_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //guardo el dato desde donde se preciono
                ((MainActivity) getActivity()).passDataActivity("3");
                //Inicio la pantalla
                Fragment fragment = new View_Job1_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        // Link to Recover Screen
        topbtn_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //guardo el dato desde donde se preciono
                ((MainActivity) getActivity()).passDataActivity("4");
                //Inicio la pantalla
                Fragment fragment = new View_Job1_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        // Link to Recover Screen
        btnComenzar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Job1_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });











        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
