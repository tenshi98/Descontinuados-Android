package info.androidhive.supegaofertador.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import info.androidhive.supegaofertador.R;
import info.androidhive.supegaofertador.helper.DB_notificaciones;
import info.androidhive.supegaofertador.helper.SessionManager;
import info.androidhive.supegaofertador.model.Notifications;

public class View_NotificationsView_Fragment extends Fragment {



    //Se llama a la base de datos
    private DB_notificaciones dataNotificaciones;

    private MapView mapView;
    private GoogleMap map;
    private LatLng CENTER = null;

    //declaracion de botones
    private Button btnVolver;
    private Button btnAceptar;

    //declaracion de textos
    private TextView txtAlarma;
    private TextView txtTextoNoti;
    private TextView txtFechaHora;
    private TextView txtCelular;


    private LinearLayout block1;
    private LinearLayout block2;
    private String direccionCompleta = "";



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
        mapView       = (MapView) rootView.findViewById(R.id.mapView);
        txtAlarma     = (TextView) rootView.findViewById(R.id.Alarma);
        txtTextoNoti  = (TextView) rootView.findViewById(R.id.TextoNotificacion);
        txtFechaHora  = (TextView) rootView.findViewById(R.id.fecha_hora);
        txtCelular    = (TextView) rootView.findViewById(R.id.Numero_celular);

        //bloques contenedores
        block1  = (LinearLayout) rootView.findViewById(R.id.block_dato1);
        block2  = (LinearLayout) rootView.findViewById(R.id.block_dato2);

        //Botones
        btnAceptar     = (Button) rootView.findViewById(R.id.btnAceptar);
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
        final String Job                   = notificacion.getJob();
        double x_latitud                   = Double.parseDouble(notificacion.getLatitud());
        double x_longitud                  = Double.parseDouble(notificacion.getLongitud());
        final String idTipo                = notificacion.getidtipo();



        /********************************************************************************/
        //En caso de una invitacion
        if(idTipo.equals("1")) {

            //Oculto los textos no utilizados
            btnAceptar.setVisibility(View.GONE);

            // se escriben los datos en los respectivos elementos
            txtAlarma.setText(TipoNotificacion);
            txtTextoNoti.setText(Texto);
            txtFechaHora.setText(Fecha + " - " + Hora);



        /********************************************************************************/
        //En caso de una notificacion
        }else if(idTipo.equals("2")){

            //Oculto los textos no utilizados
            btnAceptar.setVisibility(View.GONE);

            // se escriben los datos en los respectivos elementos
            txtAlarma.setText(TipoNotificacion);
            txtTextoNoti.setText(Texto);
            txtFechaHora.setText(Fecha + " - " + Hora);


        /********************************************************************************/
        //En caso de un trabajo
        }else if(idTipo.equals("3")){

            //Oculto los textos no utilizados
            block1.setVisibility(View.GONE);
            block2.setVisibility(View.GONE);

            txtAlarma.setText(TipoNotificacion);
            txtTextoNoti.setText(Texto);


            //obtengo los datos de mi ubicacion
            Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(x_latitud, x_longitud, 1);

                if (addresses.size() > 0) {
                    for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                        direccionCompleta += addresses.get(0).getAddressLine(i) + " ";
                    }
                }


            } catch (Exception e1) {
                e1.printStackTrace();
            }

            //se declara el mapa
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            //
            CENTER = new LatLng(x_latitud, x_longitud);

            //mapa
            map = mapView.getMap();
            //limpio el mapa
            map.clear();
            if (map == null) {
                Toast.makeText(getActivity(), "Map Fragment Not Found or no Map in it", Toast.LENGTH_SHORT).show();
            }

            final LatLng MELBOURNE = new LatLng(x_latitud, x_longitud);
            Marker melbourne = map.addMarker(new MarkerOptions()
                    .position(MELBOURNE)
                    .title("Ubicacion Notificacion")
                    .snippet(direccionCompleta)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));

            melbourne.showInfoWindow();


            map.moveCamera(CameraUpdateFactory.zoomTo(16));

            if (CENTER != null) {
                map.animateCamera(CameraUpdateFactory.newLatLng(CENTER), 1750, null);
            }


            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setZoomControlsEnabled(true);



        }



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
