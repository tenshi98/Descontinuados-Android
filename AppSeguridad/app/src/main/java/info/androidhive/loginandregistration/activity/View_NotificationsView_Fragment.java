package info.androidhive.loginandregistration.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.DB_notificaciones;
import info.androidhive.loginandregistration.helper.DB_usuarios;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.model.Notifications;


public class View_NotificationsView_Fragment extends Fragment {


    private MapView mapView;
    private GoogleMap map;
    private LatLng CENTER = null;
    public LocationManager locationManager;
    private double longitude;
    private double latitude;

    //Se llama a la base de datos
    private DB_notificaciones dataNotificaciones;

    //declaracion de botones
    private Button btnVolver;
    private Button btnLlamar;
    private Button btnEnvio;

    //declaracion de textos
    private TextView txtAlarma;
    private TextView txtFechaHora;
    private TextView txtNumero;
    private TextView txtTextoNoti;
    private String direccionCompleta = "";

    private LinearLayout block1;
    private LinearLayout block2;

    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;
    private DB_usuarios dataUsuarios;

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

        //llamo a la base de datos con las zonas
        dataNotificaciones = new DB_notificaciones(getActivity());

        //Se toman los botones y textos del layout
        mapView       = (MapView) rootView.findViewById(R.id.mapView);
        txtAlarma     = (TextView) rootView.findViewById(R.id.Alarma);
        txtFechaHora  = (TextView) rootView.findViewById(R.id.fecha_hora);
        txtNumero     = (TextView) rootView.findViewById(R.id.Numero_celular);
        txtTextoNoti  = (TextView) rootView.findViewById(R.id.TextoNotificacion);

        //bloques contenedores
        block1  = (LinearLayout) rootView.findViewById(R.id.block_dato1);
        block2  = (LinearLayout) rootView.findViewById(R.id.block_dato2);

        //Botones
        btnVolver = (Button) rootView.findViewById(R.id.btnVolver);
        btnLlamar = (Button) rootView.findViewById(R.id.btnLlamar);
        btnEnvio  = (Button) rootView.findViewById(R.id.btnEnvioUbicacion);

        //llamo a la base de datos
        dataUsuarios  = new DB_usuarios(getActivity());

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

        final String TipoNotificacion      = notificacion.getTipoNotificacion();
        final String Fecha                 = notificacion.getFecha();
        final String Hora                  = notificacion.getHora();
        final String NumeroTel             = notificacion.getFono();
        final String TipoAlerta            = notificacion.getTipoAlerta();
        final String Texto                 = notificacion.getTexto();
        final String Web                   = notificacion.getWeb();
        final String idAlerta              = notificacion.getIdAlert();
        double x_latitud                   = Double.parseDouble(notificacion.getLatitud());
        double x_longitud                  = Double.parseDouble(notificacion.getLongitud());


        //Condiciono el modelo de vista dependiendo del tipo de notificacion recibida


        /********************************************************************************/
        //En caso de una alerta
        if(TipoAlerta.equals("1")) {

            //Oculto los textos no utilizados
            btnEnvio.setVisibility(View.GONE);

            // se escriben los datos en los respectivos elementos
            txtAlarma.setText(TipoNotificacion);
            txtFechaHora.setText(Fecha + " " + Hora + "hrs");
            txtNumero.setText(NumeroTel);
            txtTextoNoti.setText(Texto);

            // boton llamar
            btnLlamar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", NumeroTel, null)));

                }


            });

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

            /********************************************************************************/
            //En caso de una notificacion
        }else if(TipoAlerta.equals("2")){


            //Oculto los textos no utilizados
            btnEnvio.setVisibility(View.GONE);

            // se escriben los datos en los respectivos elementos
            txtAlarma.setText(TipoNotificacion);
            txtFechaHora.setText(Fecha + " " + Hora + "hrs");
            txtNumero.setText(NumeroTel);
            txtTextoNoti.setText(Texto);

            // boton llamar
            btnLlamar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", NumeroTel, null)));

                }


            });

            //obtengo los datos de mi ubicacion
            Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(x_latitud, x_longitud, 1);

                if (addresses.size() > 0){
                    for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++) {
                        direccionCompleta += addresses.get(0).getAddressLine(i) + " ";
                    }
                }
            }catch (Exception e1) {
                e1.printStackTrace();
            }

            //se declara el mapa
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            //dato con el centro
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


       /********************************************************************************/
        //en caso una solicitud
        }else if(TipoAlerta.equals("3")){

            //Oculto los textos no utilizados
            block1.setVisibility(View.GONE);
            block2.setVisibility(View.GONE);
            btnLlamar.setVisibility(View.GONE);

            // se escriben los datos en los respectivos elementos
            txtAlarma.setText(TipoNotificacion);
            txtTextoNoti.setText(Texto);

            // obtengo el id del cliente
            HashMap<String, String> user = dataUsuarios.getUserDetails();
            final String app_user  = user.get("idCliente");

            mapView = (MapView) rootView.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            // obtengo los datos del usuario
            String m_latitud ;
            String m_longitud;
            if(user.get("latitud")==null){m_latitud = "-12.0453"; }else{m_latitud = user.get("latitud"); }
            if(user.get("longitud")==null){m_longitud = "-77.0311"; }else{m_longitud = user.get("longitud"); }
            setMapView1(m_latitud, m_longitud);

            //al presionar boton de ubicacion en el mapa ejecuta funcion
            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    setMapView2();

                    //Actualizo la ubicacion
                    String zlatitude= new Double(latitude).toString();
                    String zlongitude= new Double(longitude).toString();
                    ((MainActivity) getActivity()).modUserLatLog(app_user, zlatitude, zlongitude);

                    return false;
                }
            });

            // boton envio ubicacion
            btnEnvio.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //transformo la latitud y longitud
                    String s_long = Double.toString(longitude);
                    String s_lat  = Double.toString(latitude);

                    // verifico si la variable no va vacia
                    if (!s_long.equals("0.0") && !s_lat.equals("0.0")) {
                        //llamar metodo del main
                        ((MainActivity)getActivity()).envioUbicacion(app_user, idAlerta, s_long, s_lat);
                    } else {
                        //mensaje de notificacion
                        Toast.makeText(getActivity(), "Actualice el mapa o active el GPS antes de enviar la ubicacion", Toast.LENGTH_LONG).show();
                    }

                }


            });

       /********************************************************************************/
       //en caso un mensaje
        }else if(TipoAlerta.equals("4")){

            //Oculto los textos no utilizados
            mapView.setVisibility(View.GONE);
            block1.setVisibility(View.GONE);
            block2.setVisibility(View.GONE);
            btnLlamar.setVisibility(View.GONE);
            btnEnvio.setVisibility(View.GONE);

            // se escriben los datos en los respectivos elementos
            txtAlarma.setText(TipoNotificacion);
            txtTextoNoti.setText(Texto);

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




    private void setMapView1(String m_latitud, String m_longitud) {

        double x_latitud  = Double.parseDouble(m_latitud);
        double x_longitud = Double.parseDouble(m_longitud);
        CENTER = new LatLng(x_latitud, x_longitud);

        try {

            //MapsInitializer.initialize(getActivity());
            if (mapView != null) {


                map = mapView.getMap();
                //limpio el mapa
                map.clear();
                if (map == null) {
                    Toast.makeText(getActivity(), "Map Fragment Not Found or no Map in it", Toast.LENGTH_SHORT).show();
                }
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.zoomTo(16));

                if (CENTER != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLng(CENTER), 1750, null);
                }
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);




                //obtengo los datos de mi ubicacion
                Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocation(x_latitud, x_longitud, 1);

                    String direccionCompleta = "";


                    if (addresses.size() > 0) {
                        for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                            direccionCompleta += addresses.get(0).getAddressLine(i) + " ";
                        }
                    }
                    //muestro la ubiccion
                    map.clear();

                    final LatLng MELBOURNE = new LatLng(x_latitud, x_longitud);
                    Marker melbourne = map.addMarker(new MarkerOptions()
                            .position(MELBOURNE)
                            .title("Ultima Ubicacion Conocida")
                            .snippet(direccionCompleta)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));

                    melbourne.showInfoWindow();


                } catch (Exception e1) {
                    Toast.makeText(getActivity(), "No se ha podido obtener la ubicacion", Toast.LENGTH_LONG).show();

                }



            }





        } catch (Exception e) {
            Toast.makeText(getActivity(),"Exception 2" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }




    private void setMapView2() {


        try {
            MapsInitializer.initialize(getActivity());

            switch (GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(getActivity())) {
                case ConnectionResult.SUCCESS:

                    if (mapView != null) {

                        locationManager = ((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE));

                        Boolean localBoolean = Boolean.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));

                        if (localBoolean.booleanValue()) {


                            Criteria criteria = new Criteria();
                            String best = locationManager.getBestProvider(criteria, true);
                            final Location location = locationManager.getLastKnownLocation(best);

                            if (location == null) {
                                Toast.makeText(getActivity(), "Ultima ubicacion no disponible, presione el boton para encontrar tu ubicacion", Toast.LENGTH_LONG).show();
                            } else {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                CENTER = new LatLng(latitude, longitude);

                                //obtengo los datos de mi ubicacion
                                Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                                try {
                                    List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);

                                    String direccionCompleta = "";


                                    if (addresses.size() > 0) {
                                        for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                                            direccionCompleta += addresses.get(0).getAddressLine(i) + " ";
                                        }
                                    }
                                    //muestro la ubiccion
                                    map.clear();

                                    final LatLng MELBOURNE = new LatLng(latitude, longitude);
                                    Marker melbourne = map.addMarker(new MarkerOptions()
                                            .position(MELBOURNE)
                                            .title("Ubicacion Actual")
                                            .snippet(direccionCompleta)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));

                                    melbourne.showInfoWindow();

                                } catch (Exception e1) {
                                    Toast.makeText(getActivity(), "No se ha podido obtener la ubicacion", Toast.LENGTH_LONG).show();

                                }





                            }

                        } else {
                            //cuadro de dialogo con la opcion de activar el gps
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("Activacion GPS");
                            alertDialogBuilder.setMessage("GPS se encuentra desactivado, desea activarlo?")
                                    .setCancelable(false)
                                    .setPositiveButton("Activar GPS",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent callGPSSettingIntent = new Intent(
                                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                                    startActivity(callGPSSettingIntent);
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
                        map = mapView.getMap();

                        if (map == null) {
                            Toast.makeText(getActivity(), "Mapa no ubicado", Toast.LENGTH_SHORT).show();
                        }

                        map.setMyLocationEnabled(true);
                        map.moveCamera(CameraUpdateFactory.zoomTo(16));

                        //efecto de centrado de la camara
                        if (CENTER != null) {
                            map.animateCamera(CameraUpdateFactory.newLatLng(CENTER), 1750, null);
                        }

                        //seteo del mapa
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        map.getUiSettings().setZoomControlsEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(true);

                    }
                    break;
                case ConnectionResult.SERVICE_MISSING:
                    Toast.makeText(getActivity(),"SERVICE_MISSING", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                    Toast.makeText(getActivity(),"SERVICE_VERSION_UPDATE_REQUIRED", Toast.LENGTH_SHORT).show();
                    break;
                default:

            }
        } catch (Exception e) {
            //Toast.makeText(getActivity(),"Exception 2" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }





}
