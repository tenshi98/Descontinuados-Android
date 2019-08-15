package info.androidhive.supegaofertador.activity;


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
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.supegaofertador.R;
import info.androidhive.supegaofertador.helper.DB_contactos;
import info.androidhive.supegaofertador.helper.SessionManager;
import info.androidhive.supegaofertador.model.Contact;


public class View_Contacts_Fragment extends ListFragment {

    //declaracion de botones
    private ImageButton btnContact;
    private Button btn_volver;
    //se llama al modulo de sesion
    private SessionManager session;
    //se llama a la base de datos de contactos
    private DB_contactos dataContactos;
    //se crea arreglo temporal
    protected ArrayList<String> mObjectIds = new ArrayList<String>();

    public View_Contacts_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_contacts));

        //se declaran los botones
        btnContact  = (ImageButton) rootView.findViewById(R.id.btnAddContact);
        btn_volver  = (Button) rootView.findViewById(R.id.btnVolver);

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



        //boton agregar contacto
        btnContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment fragment = new View_Contacts_Add_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        // se redirije a la pantalla de edicion de datos del usuario
        btn_volver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Seguridad_Fragment();
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
    private ArrayList<View_Contacts_Model> generateData(){

        //obtengo los datos
        List<Contact> contacts = dataContactos.getAllContacts();
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

        ((MainActivity) getActivity()).passDataActivity(mObjectIds.get(position));

        Fragment fragment = new View_Contacts_View_Fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }





}