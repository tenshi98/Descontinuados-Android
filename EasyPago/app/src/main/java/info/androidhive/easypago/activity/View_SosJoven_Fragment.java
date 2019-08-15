package info.androidhive.easypago.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import info.androidhive.easypago.R;
import info.androidhive.easypago.helper.SessionManager;


public class View_SosJoven_Fragment extends Fragment {

    //declaracion de botones
    private ImageButton btnUbicacion;
    private ImageButton btnDestino;
    private ImageButton btnAuto;
    private ImageButton btnLlamame;



    //se llama a los modulos que manejan la base de datos y la sesion

    private SessionManager session;

    public View_SosJoven_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sos_joven, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_sosjoven));

        //Se toman los botones
        btnUbicacion = (ImageButton) rootView.findViewById(R.id.Sosjoven_1);
        btnDestino = (ImageButton) rootView.findViewById(R.id.Sosjoven_2);
        btnAuto = (ImageButton) rootView.findViewById(R.id.Sosjoven_3);
        btnLlamame = (ImageButton) rootView.findViewById(R.id.Sosjoven_4);



        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        // boton para ir al manual del sistema
        btnUbicacion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Guardo el valor del boton
                ((MainActivity) getActivity()).passDataActivity("2");

                //llamo a la actividad correspondiente
                Fragment fragment = new View_SosJoven_Alarma_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        // boton para Invitar Contactos
        btnDestino.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Guardo el valor del boton
                ((MainActivity) getActivity()).passDataActivity("3");

                //llamo a la actividad correspondiente
                Fragment fragment = new View_SosJoven_Alarma_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        // boton generar alarma
        btnAuto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Guardo el valor del boton
                ((MainActivity) getActivity()).passDataActivity("4");

                //llamo a la actividad correspondiente
                Fragment fragment = new View_SosJoven_Alarma_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        // boton para redirigir a la pantalla de sosjoven
        btnLlamame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Guardo el valor del boton
                ((MainActivity) getActivity()).passDataActivity("5");

                //llamo a la actividad correspondiente
                Fragment fragment = new View_SosJoven_Alarma_Fragment();
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
