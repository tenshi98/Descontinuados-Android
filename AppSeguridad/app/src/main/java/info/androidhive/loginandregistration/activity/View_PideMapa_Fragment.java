package info.androidhive.loginandregistration.activity;


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

import java.util.ArrayList;
import java.util.List;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.DB_contactos;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.model.Contact;


public class View_PideMapa_Fragment extends ListFragment    {


    //se llama al modulo de sesion
    private SessionManager session;
    //se llama a la base de datos de contactos
    private DB_contactos dataContactos;
    //se crea arreglo temporal
    protected ArrayList<String> mObjectIds = new ArrayList<String>();

    public View_PideMapa_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pide_mapa, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_pide_mapa));

        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        //llamo a la tabla con los contactos
        dataContactos = new DB_contactos(getActivity());

        //llamo a la funcion encargada de dibujar el listado de contactos
        View_Contacts_Adapter adapter = new View_Contacts_Adapter(getActivity(), generateData());
        setListAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    //funcion encargada de dibujar los contactos
    private ArrayList<View_Contacts_Model> generateData(){

        //obtengo los datos
        List<Contact> contacts = dataContactos.getAllContactsRegister();
        ArrayList<View_Contacts_Model> viewContactsModels = new ArrayList<View_Contacts_Model>();
        String enteroString;

        //parseo los datos a la tabla
        for (Contact cn : contacts) {

            //se crea un objeto con el id del item seleccionado
            enteroString = Integer.toString(cn.getID());
            mObjectIds.add(enteroString);
            //condiciono el icono a mostrar deacuerdo al estado del contacto
            int icon;
            if(cn.getEstado().equals("Registrado")){
                icon = R.drawable.ic_user_registrado;
            }else{
                icon = R.drawable.ic_user_no_registrado;
            }
            //guardo los otros datos
            viewContactsModels.add(new View_Contacts_Model(icon,cn.getName(),cn.getPhoneNumber()));

        }

        return viewContactsModels;
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



        //String itemID = mObjectIds.get(position);
        //Toast.makeText(getActivity(), itemID, Toast.LENGTH_LONG).show();

        ((MainActivity) getActivity()).passDataActivity(mObjectIds.get(position));

        Fragment fragment = new View_PideMapa_View_Fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }





}
