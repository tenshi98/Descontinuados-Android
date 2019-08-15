package info.androidhive.clickfarma.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import info.androidhive.clickfarma.R;
import info.androidhive.clickfarma.helper.DB_notificaciones;
import info.androidhive.clickfarma.helper.SessionManager;
import info.androidhive.clickfarma.model.Notifications;

public class View_NotificationsView_Fragment extends Fragment {



    //Se llama a la base de datos
    private DB_notificaciones dataNotificaciones;

    //declaracion de botones
    private Button btnVolver;

    //declaracion de textos
    private TextView txtAlarma;
    private TextView txtTextoNoti;
    private TextView txtFechaHora;

    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;


    //Validacion de la web
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";



    public View_NotificationsView_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications_view_2, container, false);



        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_notificaciones_view));



        //Se toman los botones y textos del layout
        txtAlarma     = (TextView) rootView.findViewById(R.id.Alarma);
        txtFechaHora  = (TextView) rootView.findViewById(R.id.fecha_hora);
        txtTextoNoti  = (TextView) rootView.findViewById(R.id.TextoNotificacion);
        btnVolver     = (Button) rootView.findViewById(R.id.btnVolver);

        //llamo a la base de datos con las zonas
        dataNotificaciones = new DB_notificaciones(getActivity());
        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }


        //llamo la variable con el id almacenada en la actividad principal
        String itemID = ((MainActivity) getActivity()).getDataActivity();
        //la transformo a integer
        int foo = Integer.parseInt(itemID);
        //busco los datos del contacto a traves de su id
        Notifications notificacion = dataNotificaciones.getNoti(foo);

        final String TipoNotificacion      = notificacion.getTipo();
        final String Fecha                 = notificacion.getFecha();
        final String Hora                  = notificacion.getHora();
        final String Texto                 = notificacion.getTexto();
        final String Web                   = notificacion.getWeb();



        // se escriben los datos en los respectivos elementos
        txtAlarma.setText(TipoNotificacion);
        txtTextoNoti.setText(Texto);
        txtFechaHora.setText(Fecha+" - "+Hora);

        //Se verifica si lleva una pagina web, si es asi se levanta el navegador con la pagina
        if(!Web.equals("")){
            //Dialogo para abrir el gps
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Abrir navegador");
            alertDialogBuilder.setMessage("La notificacion recibida contiene un enlace, ¿desea abrirlo?")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        String PaginaWeb = "";
                                        if (!Web.startsWith(HTTP) && !Web.startsWith(HTTPS)) {
                                            PaginaWeb = HTTP + Web;
                                        }

                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PaginaWeb));
                                        startActivity(Intent.createChooser(intent, "Elegir Navegador"));
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









        // boton volver a la pantalla principal
        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Notifications_Fragment();
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
