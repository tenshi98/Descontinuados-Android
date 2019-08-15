package info.androidhive.supegatrab.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import info.androidhive.supegatrab.R;
import info.androidhive.supegatrab.helper.SessionManager;
import info.androidhive.supegatrab.helper.DB_contactos;
import info.androidhive.supegatrab.helper.DB_usuarios;
import info.androidhive.supegatrab.model.Contact;


public class View_Seguridad_Fragment extends Fragment {

    //declaracion de botones
    private Button btn_seguridad1, btn_seguridad2;
    //se llama a la base de datos de contactos
    private DB_contactos dataContactos;
    private DB_usuarios dataUsuarios;



    //se llama a los modulos que manejan la base de datos y la sesion
    //private DB_usuarios dataUsuarios;
    private SessionManager session;

    public View_Seguridad_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seguridad, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_seguridad));


        //Se toman los botones
        btn_seguridad1 = (Button) rootView.findViewById(R.id.btn_seguridad1);
        btn_seguridad2 = (Button) rootView.findViewById(R.id.btn_seguridad2);


        // administrador de sesiones
        session       = new SessionManager(getActivity());
        dataContactos = new DB_contactos(getActivity());
        dataUsuarios  = new DB_usuarios(getActivity());


        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }


        // seguridad
        btn_seguridad1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Fragment fragment = new View_Contacts_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        // alerta
        btn_seguridad2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int cuenta = dataContactos.getContactsCount();

                if(cuenta>0){

                    //obtengo los datos
                    List<Contact> contacts = dataContactos.getAllContacts();
                    // obtengo el id del cliente
                    HashMap<String, String> user = dataUsuarios.getUserDetails();
                    final String app_user  = user.get("idCliente");
                    final String app_name  = user.get("Nombre");

                    int ncontact1;
                    int ncontact2;

                    //se asignan valores
                    ncontact1 = 0;
                    ncontact2 = 0;
                    //se envian los mensajes a quienes estan registrados
                    for (Contact cn : contacts) {
                        if(cn.getEstado().equals("Registrado")){
                            ncontact1++;
                        }else{
                            ncontact2++;
                        }
                    }

                    final int tcontact1 = ncontact1;
                    final int tcontact2 = ncontact2;

                    //Dialogo para abrir el gps
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Notificacion");
                    alertDialogBuilder.setMessage("Esta a punto de enviar " + ncontact1 + " notificaciones a sus contactos")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            /****************************************************************************************/
                                            //se verifica si la variavle de registrados es superior a 0
                                            if (tcontact1 > 0) {

                                                ((MainActivity) getActivity()).seguridad(app_user, app_name);

                                            }
                                            /****************************************************************************************/
                                            //se verifica si la variavle de no registrados es superior a 0
                                            if (tcontact2 > 0) {

                                                final int finalNcontact = tcontact2;
                                                //Dialogo para abrir el gps
                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                                alertDialogBuilder.setTitle("Notificacion");

                                                alertDialogBuilder.setMessage("Esta a punto de enviar " + tcontact2 + " sms a los contactos que no " +
                                                        "estan en el sistema, tenga en cuenta que al enviarlos significa un costo monetario.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Aceptar",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {


                                                                        //obtengo los datos
                                                                        List<Contact> contacts = dataContactos.getAllContacts();
                                                                        //se envian los mensajes a quienes no estan registrados
                                                                        for (Contact cn : contacts) {
                                                                            if (cn.getEstado().equals("No Registrado")) {

                                                                                sms(cn.getPhoneNumber(), app_name, finalNcontact);

                                                                            }
                                                                        }
                                                                        Toast.makeText(getActivity(), "SMS Correctamente enviados", Toast.LENGTH_SHORT).show();
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

                                            }
                                            /****************************************************************************************/
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

                }else{
                    Toast.makeText(getActivity(), "Aun no posee contactos, primero invite a algunos contactos dentro de su red", Toast.LENGTH_SHORT).show();
                }




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

    private void sms(String telefono, String nombre,int nosalio_cantidad){

        final String texto= "Hola soy "+nombre+" y me siento mal de salud, llamame";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefono, null, texto, null, null);
        }
        catch (Exception e) {
            Toast.makeText(getActivity(), "El mensaje no ha podido ser enviado", Toast.LENGTH_LONG).show();
        }

    }





}
