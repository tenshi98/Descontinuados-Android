package info.androidhive.supegatrab.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import info.androidhive.supegatrab.R;


import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import info.androidhive.supegatrab.helper.SessionManager;
import info.androidhive.supegatrab.helper.DB_usuarios;


public class View_Home_Fragment extends Fragment {

    //declaracion de botones
    private ImageButton btnDisponible;
    private ImageButton btnPerfil;
    private ImageButton btnOfertas;
    private DB_usuarios dataUsuarios;
    private TextView txt_Nombre;
    private TextView txt_Profesion;
    private TextView txt_disponibilidad;



    //se llama a los modulos que manejan la base de datos y la sesion
    //private DB_usuarios dataUsuarios;
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
        btnDisponible        = (ImageButton) rootView.findViewById(R.id.btn_menu_1);
        btnPerfil            = (ImageButton) rootView.findViewById(R.id.btn_menu_2);
        btnOfertas           = (ImageButton) rootView.findViewById(R.id.btn_menu_3);
        txt_Nombre           = (TextView) rootView.findViewById(R.id.Nombre);
        txt_Profesion        = (TextView) rootView.findViewById(R.id.Profesion);
        txt_disponibilidad   = (TextView) rootView.findViewById(R.id.disponibilidad);




        // administrador de sesiones
        session       = new SessionManager(getActivity());
        dataUsuarios  = new DB_usuarios(getActivity());

        HashMap<String, String> user = dataUsuarios.getUserDetails();
        final String idCliente       = user.get("idCliente");
        String Nombre          = user.get("Nombre");
        String Profesion       = user.get("Profesion");

        if(!Nombre.isEmpty()){         txt_Nombre.setText(Nombre);}
        if(!Profesion.isEmpty()){      txt_Profesion.setText(Profesion);}


        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        //verifico si el seguimiento esta activo
        String verificaAlarma = ((MainActivity)getActivity()).getAlarmIsOn();
        switch (verificaAlarma) {

            case "Off":
                btnDisponible.setImageResource(R.drawable.icn_seguimientos);
                txt_disponibilidad.setText("No Disponible");
                break;
            case "On":
                btnDisponible.setImageResource(R.drawable.icn_seguimientos_activo);
                txt_disponibilidad.setText("Disponible");
                break;
            default: //en caso de que sea la primiera vez y el interruptor este vacio
                btnDisponible.setImageResource(R.drawable.icn_seguimientos);
                txt_disponibilidad.setText("No Disponible");
                break;
        }

        // boton para ir al manual del sistema
        btnDisponible.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String alarmUp = ((MainActivity)getActivity()).getAlarmIsOn();

                switch (alarmUp) {

                    case "Off":
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Activacion Seguimiento");
                        alertDialogBuilder.setMessage("Esta a punto de activar el servicio de seguimiento, tenga en cuenta que al activarlo debera mantener el GPS Activo lo cual repercutira en la duracion de la bateria")
                                .setCancelable(false)
                                .setPositiveButton("Activar Servicio",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                //llamo a la funcion para activar el seguimiento
                                                ((MainActivity)getActivity()).seguimiento(1);
                                                ((MainActivity)getActivity()).disponibilidad(idCliente, 1);
                                                //Actualizo el interruptor a on
                                                ((MainActivity)getActivity()).passAlarmIsOn("On");
                                                //cambio el grafico del boton
                                                btnDisponible.setImageResource(R.drawable.icn_seguimientos_activo);
                                                txt_disponibilidad.setText("Disponible");
                                            }
                                        });
                        alertDialogBuilder.setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alertDialogBuilder.create();
                        alert.show();

                        break;
                    case "On":
                        //llamo a la funcion para desactivar el seguimiento
                        ((MainActivity)getActivity()).seguimiento(2);
                        ((MainActivity)getActivity()).disponibilidad(idCliente, 2);
                        //Actualizo el interruptor a Off
                        ((MainActivity)getActivity()).passAlarmIsOn("Off");
                        //cambio el grafico del boton
                        btnDisponible.setImageResource(R.drawable.icn_seguimientos);
                        txt_disponibilidad.setText("No Disponible");
                        break;
                    default: //en caso de que sea la primiera vez y el interruptor este vacio
                        //llamo a la funcion para activar el seguimiento
                        ((MainActivity)getActivity()).seguimiento(1);
                        ((MainActivity)getActivity()).disponibilidad(idCliente, 1);
                        //Actualizo el interruptor a on
                        ((MainActivity)getActivity()).passAlarmIsOn("On");
                        //cambio el grafico del boton
                        btnDisponible.setImageResource(R.drawable.icn_seguimientos_activo);
                        txt_disponibilidad.setText("Disponible");
                        break;
                }
            }
        });



        btnPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Perfil_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        btnOfertas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_OfertasDiarias_Fragment();
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
