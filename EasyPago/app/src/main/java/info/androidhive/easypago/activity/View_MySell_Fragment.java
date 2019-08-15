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
import info.androidhive.easypago.helper.DB_ventas;
import info.androidhive.easypago.helper.SessionManager;
import info.androidhive.easypago.model.Ventas;


public class View_MySell_Fragment extends ListFragment    {

    //declaracion de botones
    private ImageButton btnContact;
    //se llama al modulo de sesion
    private SessionManager session;
    //se llama a la base de datos de contactos
    private DB_ventas dataVentas;

    public View_MySell_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_sell, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_ventas));

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
        dataVentas = new DB_ventas(getActivity());

        //llamo a la funcion encargada de dibujar el listado de contactos
        View_MySell_Adapter adapter = new View_MySell_Adapter(getActivity(), generateData());
        setListAdapter(adapter);



        //boton eliminar alertas
        btnContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ventana de confirmacion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Eliminacion de Datos");
                alertDialogBuilder.setMessage("¿Confirma que desea eliminar todas las ventas?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id){
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).delVentas();
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
    private ArrayList<View_MySell_Model> generateData(){

        //obtengo los datos
        List<Ventas> contacts = dataVentas.getAllVentaSorted();
        ArrayList<View_MySell_Model> viewSellModels = new ArrayList<View_MySell_Model>();
        String enteroString;

        //parseo los datos a la tabla
        for (Ventas cn : contacts) {

            String titulo = "Venta el "+cn.getFecha();
            String texto = cn.getFono()+" por "+cn.getMonto();
            //guardo los otros datos
            viewSellModels.add(new View_MySell_Model(R.drawable.ic_profile,titulo,texto));

        }

        return viewSellModels;
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
