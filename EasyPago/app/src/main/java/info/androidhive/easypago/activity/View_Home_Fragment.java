package info.androidhive.easypago.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import info.androidhive.easypago.R;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import info.androidhive.easypago.helper.DB_usuarios;
import info.androidhive.easypago.helper.SessionManager;


public class View_Home_Fragment extends Fragment {

    //declaracion de botones
    private Button btnventa;
    private TextView txt_Saldo;

    //se llama a los modulos que manejan la base de datos y la sesion
    private DB_usuarios dataUsuarios;
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
        btnventa = (Button) rootView.findViewById(R.id.btn_menu_1);
        txt_Saldo = (TextView) rootView.findViewById(R.id.text_saldo);




        // administrador de sesiones
        session       = new SessionManager(getActivity());
        dataUsuarios  = new DB_usuarios(getActivity());


        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }



        // obtengo los datos del usuario
        HashMap<String, String> user = dataUsuarios.getUserDetails();
        final String Saldo    = user.get("Saldo");
        txt_Saldo.setText("S/. "+Saldo);




        // boton para Invitar Contactos
        btnventa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Venta1_Fragment();
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
