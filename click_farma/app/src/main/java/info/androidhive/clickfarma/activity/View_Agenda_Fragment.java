package info.androidhive.clickfarma.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.clickfarma.R;
import info.androidhive.clickfarma.helper.DB_alarms;
import info.androidhive.clickfarma.helper.SessionManager;
import info.androidhive.clickfarma.model.Alarms;


public class View_Agenda_Fragment extends ListFragment {

    //declaracion de botones
    private Button btn_addalarm,btn_volver;


    //se llama a los modulos que manejan la base de datos y la sesion
    //private DB_usuarios dataUsuarios;
    private SessionManager session;
    private DB_alarms dataAlarmas;

    //se crea arreglo temporal
    protected ArrayList<String> mObjectIds = new ArrayList<String>();

    public View_Agenda_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agenda1, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_agenda));


        //Se toman los botones
        btn_addalarm  = (Button) rootView.findViewById(R.id.btn_addalarm);
        btn_volver    = (Button) rootView.findViewById(R.id.btn_volver);

        // administrador de sesiones
        session       = new SessionManager(getActivity());

        //base de datos
        dataAlarmas = new DB_alarms(getActivity());

        View_Agenda_Adapter adapter = new View_Agenda_Adapter(getActivity(), generateData());
        setListAdapter(adapter);


        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }



        //
        btn_addalarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_AgendaAdd_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        //
        btn_volver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Home_Fragment();
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


    //funcion encargada de dibujar los contactos
    private ArrayList<View_Agenda_Model> generateData(){

        //obtengo los datos
        List<Alarms> contacts = dataAlarmas.getAllNoti();
        ArrayList<View_Agenda_Model> View_Agenda_Models = new ArrayList<View_Agenda_Model>();
        String enteroString;

        //parseo los datos a la tabla
        for (Alarms cn : contacts) {

            //se crea un objeto con el id del item seleccionado
            enteroString = Integer.toString(cn.getID());
            mObjectIds.add(enteroString);

            String titulo = cn.getTexto();
            String texto = cn.getHora();
            //guardo los otros datos
            View_Agenda_Models.add(new View_Agenda_Model(R.drawable.ic_profile,titulo,texto));

        }

        return View_Agenda_Models;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




    //Detecta el item presionado y redirije a una nueva actividad
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {


        ((MainActivity) getActivity()).passDataActivity(mObjectIds.get(position));

        Fragment fragment = new View_AgendaEdit_Fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();




    }



}
