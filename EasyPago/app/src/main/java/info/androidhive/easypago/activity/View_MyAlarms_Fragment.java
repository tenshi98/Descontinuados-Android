package info.androidhive.easypago.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.easypago.R;
import info.androidhive.easypago.helper.DB_alertas;
import info.androidhive.easypago.helper.SessionManager;
import info.androidhive.easypago.model.Alert;


public class View_MyAlarms_Fragment extends ListFragment    {

    //declaracion de botones
    private ImageButton btnContact;
    //se llama al modulo de sesion
    private SessionManager session;
    //se llama a la base de datos de contactos
    private DB_alertas dataAlertas;
    //se crea arreglo temporal
    protected ArrayList<String> mObjectIds = new ArrayList<String>();

    public View_MyAlarms_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_alarm, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_alertas));

        //se declaran los botones
        btnContact = (ImageButton) rootView.findViewById(R.id.btnAddContact);

        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        //llamo a la tabla con los contactos
        dataAlertas = new DB_alertas(getActivity());

        //llamo a la funcion encargada de dibujar el listado de contactos
        View_MyAlarms_Adapter adapter = new View_MyAlarms_Adapter(getActivity(), generateData());
        setListAdapter(adapter);



        //boton eliminar alertas
        btnContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ventana de confirmacion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Eliminacion de Datos");
                alertDialogBuilder.setMessage("¿Confirma que desea eliminar todas las alertas?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id){
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).delAlarm();
                                    }
                                });
                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();



            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    //funcion encargada de dibujar los contactos
    private ArrayList<View_MyAlarms_Model> generateData(){

        //obtengo los datos
        List<Alert> contacts = dataAlertas.getAllAlertSorted();
        ArrayList<View_MyAlarms_Model> viewAlarmsModels = new ArrayList<View_MyAlarms_Model>();
        String enteroString;

        //parseo los datos a la tabla
        for (Alert cn : contacts) {
            //String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            //se crea un objeto con el id del item seleccionado
            enteroString = Integer.toString(cn.getID());
            mObjectIds.add(enteroString);

            String titulo = cn.getFecha()+" - "+cn.getHora();
            String texto = cn.getTipoAlerta();
            //guardo los otros datos
            viewAlarmsModels.add(new View_MyAlarms_Model(R.drawable.ic_profile,titulo,texto));

        }

        return viewAlarmsModels;
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

        Fragment fragment = new View_MyAlarmsView_Fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }





}
