package info.androidhive.clickfarma.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import info.androidhive.clickfarma.R;
import info.androidhive.clickfarma.helper.SessionManager;


public class View_Medicamentos3_Fragment extends Fragment {

    //botones
    private Button btn_volver;


    //private DB_usuarios dataUsuarios;
    private SessionManager session;

    public View_Medicamentos3_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medicamentos3, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_med3));


        //Se toman los botones
        btn_volver  = (Button) rootView.findViewById(R.id.btn_search);



        // administrador de sesiones
        session       = new SessionManager(getActivity());


        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }





        // se redirije a la pantalla de edicion de datos del usuario
        btn_volver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Medicamentos2_Fragment();
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
