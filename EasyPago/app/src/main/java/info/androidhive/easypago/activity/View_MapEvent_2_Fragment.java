package info.androidhive.easypago.activity;


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
import android.widget.Button;
import android.widget.ImageButton;


import java.util.HashMap;

import info.androidhive.easypago.R;
import info.androidhive.easypago.helper.DB_usuarios;


public class View_MapEvent_2_Fragment extends Fragment {


    //Se agrega el boton volver
    private Button btnVolver;
    private ImageButton btn_1;
    private ImageButton btn_2;
    private ImageButton btn_3;
    private ImageButton btn_4;
    private ImageButton btn_5;
    private ImageButton btn_6;

    //se llama a los modulos que manejan la base de datos y la sesion
    private DB_usuarios dataUsuarios;


    public View_MapEvent_2_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_event_2, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_event2));

        //llamo la variable con el id almacenada en la actividad principal
        final String itemID = ((MainActivity) getActivity()).getDataActivity();
        String[] parts = itemID.split("and");
        final String latitude  = parts[0];
        final String longitude = parts[1];



        btnVolver = (Button) rootView.findViewById(R.id.btnVolver);

        btn_1 = (ImageButton) rootView.findViewById(R.id.btn_event_1);
        btn_2 = (ImageButton) rootView.findViewById(R.id.btn_event_2);
        btn_3 = (ImageButton) rootView.findViewById(R.id.btn_event_3);
        btn_4 = (ImageButton) rootView.findViewById(R.id.btn_event_4);
        btn_5 = (ImageButton) rootView.findViewById(R.id.btn_event_5);
        btn_6 = (ImageButton) rootView.findViewById(R.id.btn_event_6);

        // se conecta a la base de datos interna
        dataUsuarios = new DB_usuarios(getActivity());

        // obtengo los datos del usuario
        HashMap<String, String> user = dataUsuarios.getUserDetails();

        final String var_idCliente       = user.get("idCliente");


        // boton 1
        btn_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ventana de confirmacion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirmar evento");
                alertDialogBuilder.setMessage("¿Confirma que desea generar este evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).addEvent(var_idCliente, "1", latitude, longitude);
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

        // boton 2
        btn_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ventana de confirmacion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirmar evento");
                alertDialogBuilder.setMessage("¿Confirma que desea generar este evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).addEvent(var_idCliente, "2", latitude, longitude);
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
        // boton 3
        btn_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ventana de confirmacion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirmar evento");
                alertDialogBuilder.setMessage("¿Confirma que desea generar este evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).addEvent(var_idCliente, "3", latitude, longitude);
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
        // boton 4
        btn_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ventana de confirmacion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirmar evento");
                alertDialogBuilder.setMessage("¿Confirma que desea generar este evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).addEvent(var_idCliente, "4", latitude, longitude);
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
        // boton 5
        btn_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ventana de confirmacion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirmar evento");
                alertDialogBuilder.setMessage("¿Confirma que desea generar este evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).addEvent(var_idCliente, "5", latitude, longitude);
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
        // boton 6
        btn_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //ventana de confirmacion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirmar evento");
                alertDialogBuilder.setMessage("¿Confirma que desea generar este evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //se llama a la instancia de eliminacion de contacto
                                        ((MainActivity) getActivity()).addEvent(var_idCliente, "6",latitude, longitude);
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






        // se redirije a la pantalla siguiente
        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_MapEvent_Fragment();
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
