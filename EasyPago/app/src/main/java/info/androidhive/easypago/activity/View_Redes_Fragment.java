package info.androidhive.easypago.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.easypago.R;
import info.androidhive.easypago.helper.DB_redes;
import info.androidhive.easypago.helper.SessionManager;
import info.androidhive.easypago.model.Redes;


public class View_Redes_Fragment extends ListFragment    {

    //se llama al modulo de sesion
    private SessionManager session;
    //se llama a la base de datos de contactos
    private DB_redes dataRedes;
    //se crea arreglo temporal
    protected ArrayList<String> mObjectIds = new ArrayList<String>();

    public View_Redes_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_redes, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_redes));

        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        //llamo a la tabla con los contactos
        dataRedes = new DB_redes(getActivity());

        //llamo a la funcion encargada de dibujar el listado de contactos
        View_Redes_Adapter adapter = new View_Redes_Adapter(getActivity(), generateData());
        setListAdapter(adapter);






        // Inflate the layout for this fragment
        return rootView;
    }

    //funcion encargada de dibujar los contactos
    private ArrayList<View_Redes_Model> generateData(){

        //obtengo los datos
        List<Redes> contacts = dataRedes.getAllRedes();
        ArrayList<View_Redes_Model> viewRedesModels = new ArrayList<View_Redes_Model>();
        String enteroString;

        //parseo los datos a la tabla
        for (Redes cn : contacts) {

            //se crea un objeto con el id del item seleccionado
            enteroString = Integer.toString(cn.getID());
            mObjectIds.add(enteroString);
            //condiciono el icono a mostrar deacuerdo al estado del contacto
            int icon = R.drawable.ic_user_registrado;

            //guardo los otros datos
            viewRedesModels.add(new View_Redes_Model(icon,cn.getName(),cn.getPhoneNumber()));

        }

        return viewRedesModels;
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

        Fragment fragment = new View_Contacts_View_Fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }





}
