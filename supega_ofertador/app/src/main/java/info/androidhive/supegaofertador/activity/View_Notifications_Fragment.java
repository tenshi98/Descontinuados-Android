package info.androidhive.supegaofertador.activity;


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
import java.util.HashMap;
import java.util.List;

import info.androidhive.supegaofertador.R;
import info.androidhive.supegaofertador.helper.DB_notificaciones;
import info.androidhive.supegaofertador.helper.DB_usuarios;
import info.androidhive.supegaofertador.helper.SessionManager;
import info.androidhive.supegaofertador.model.Notifications;

public class View_Notifications_Fragment extends ListFragment    {

    //declaracion de botones
    private ImageButton btnContact;
    //se llama al modulo de sesion
    private SessionManager session;
    //se llama a la base de datos de contactos
    private DB_notificaciones dataNotificaciones;
    private DB_usuarios dataUsuarios;
    //se crea arreglo temporal
    protected ArrayList<String> mObjectIds = new ArrayList<String>();

    public View_Notifications_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_notificaciones));

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
        dataUsuarios  = new DB_usuarios(getActivity());
        dataNotificaciones = new DB_notificaciones(getActivity());
        // obtengo el id del cliente
        HashMap<String, String> user = dataUsuarios.getUserDetails();
        final String app_user = user.get("idCliente");

        //Actualizar notificaciones
        ((MainActivity) getActivity()).updateNoti(app_user);

        //llamo a la funcion encargada de dibujar el listado de contactos
        View_Notifications_Adapter adapter = new View_Notifications_Adapter(getActivity(), generateData());
        setListAdapter(adapter);



        //boton eliminar alertas
        btnContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ventana de confirmacion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Eliminacion de Datos");
                alertDialogBuilder.setMessage("¿Confirma que desea eliminar todas las notificaciones?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id){
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).delNoti();
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
    private ArrayList<View_Notifications_Model> generateData(){

        //obtengo los datos
        List<Notifications> contacts = dataNotificaciones.getAllNoti();
        ArrayList<View_Notifications_Model> viewNotificationsModels = new ArrayList<View_Notifications_Model>();
        String enteroString;

        //parseo los datos a la tabla
        for (Notifications cn : contacts) {
            //String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            //se crea un objeto con el id del item seleccionado
            enteroString = Integer.toString(cn.getID());
            mObjectIds.add(enteroString);

            String titulo = cn.getFecha()+" - "+cn.getHora();
            String texto = cn.getMensaje();
            //guardo los otros datos
            viewNotificationsModels.add(new View_Notifications_Model(R.drawable.ic_profile,titulo,texto));

        }

        return viewNotificationsModels;
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

        Fragment fragment = new View_NotificationsView_Fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }





}
