package info.androidhive.loginandregistration.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import info.androidhive.loginandregistration.R;

import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import info.androidhive.loginandregistration.app.ServiceLocation;
import info.androidhive.loginandregistration.helper.DB_eventos;
import info.androidhive.loginandregistration.helper.DB_usuarios;
import info.androidhive.loginandregistration.helper.DB_zonas;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.model.Eventos;
import info.androidhive.loginandregistration.model.Zone;


public class View_Home_Fragment extends Fragment {

    //declaracion de botones
    private ImageButton btnSeguimiento;
    private ImageButton btnEvento;
    private ImageButton btnDireccion;
    private ImageButton btnSosAmerica;

    //mapa
    private MapView mapView;
    private GoogleMap map;
    private LatLng CENTER = null;
    public LocationManager locationManager;
    private double longitude;
    private double latitude;

    //se llama a los modulos que manejan la base de datos y la sesion
    private DB_usuarios dataUsuarios;
    private DB_zonas dataZonas;
    private SessionManager session;
    private DB_eventos dataEventos;

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
        btnSeguimiento = (ImageButton) rootView.findViewById(R.id.btn_menu_1);
        btnEvento = (ImageButton) rootView.findViewById(R.id.btn_menu_2);
        btnDireccion = (ImageButton) rootView.findViewById(R.id.btn_menu_3);
        btnSosAmerica = (ImageButton) rootView.findViewById(R.id.btn_menu_4);



        // administrador de sesiones
        session       = new SessionManager(getActivity());
        dataZonas     = new DB_zonas(getActivity());
        dataUsuarios  = new DB_usuarios(getActivity());
        dataEventos   = new DB_eventos(getActivity());


        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        //verifico si el seguimiento esta activo
        String verificaAlarma = ((MainActivity)getActivity()).getAlarmIsOn();
        switch (verificaAlarma) {

            case "Off":
                btnSeguimiento.setImageResource(R.drawable.pmenu_verifica_ruta);
                break;
            case "On":
                btnSeguimiento.setImageResource(R.drawable.pmenu_verifica_ruta_activo);
                break;
            default: //en caso de que sea la primiera vez y el interruptor este vacio
                btnSeguimiento.setImageResource(R.drawable.pmenu_verifica_ruta);
                break;
        }

        // obtengo los datos del usuario
        HashMap<String, String> user = dataUsuarios.getUserDetails();
        final String idUser    = user.get("idUser");
        String m_latitud ;
        String m_longitud;
        if(user.get("latitud")==null){  m_latitud  = "-12.0453";  }else{m_latitud  = user.get("latitud"); }
        if(user.get("longitud")==null){ m_longitud = "-77.0311";  }else{m_longitud = user.get("longitud"); }
        setMapView1(m_latitud, m_longitud);

        //al presionar boton de ubicacion en el mapa ejecuta funcion
         map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                //se redibuja mapa
                setMapView2();

                //obtengo la ubicacion actual
                String zlatitude = new Double(latitude).toString();
                String zlongitude = new Double(longitude).toString();

                //se guarda la ubicacion
                ((MainActivity) getActivity()).modUserLatLog(idUser, zlatitude, zlongitude);

                //Solicito los puntos cercanos a la pase de datos
                ((MainActivity) getActivity()).searchEvent(zlatitude, zlongitude);


                List<Eventos> puntos = dataEventos.getAllEvent();
                for (Eventos point : puntos) {

                    double dlat = Double.parseDouble(point.getLatitud());
                    double dlong = Double.parseDouble(point.getLongitud());

                    Toast.makeText(getActivity(), point.getLatitud()+point.getLongitud(), Toast.LENGTH_LONG).show();

                    final LatLng EventoMarker = new LatLng(dlat, dlong);
                    map.addMarker(new MarkerOptions()
                            .position(EventoMarker)
                            .title(point.getTipoEvento())
                            .snippet(point.getHora()+" "+point.getNombre())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_blue)));

                }


                return false;
            }
        });



        // boton para ir al manual del sistema
        btnSeguimiento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String alarmUp = ((MainActivity)getActivity()).getAlarmIsOn();

                switch (alarmUp) {

                    case "Off":
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Activacion Ruta Segura");
                        alertDialogBuilder.setMessage("Esta a punto de activar el servicio de ruta segura, tenga en cuenta que al activarlo debera mantener el GPS Activo lo cual repercutira en la duracion de la bateria")
                                .setCancelable(false)
                                .setPositiveButton("Activar Servicio",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                //llamo a la funcion para activar el seguimiento
                                                ((MainActivity)getActivity()).seguimiento(1);
                                                //Actualizo el interruptor a on
                                                ((MainActivity)getActivity()).passAlarmIsOn("On");
                                                //cambio el grafico del boton
                                                btnSeguimiento.setImageResource(R.drawable.pmenu_verifica_ruta_activo);
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
                        //Actualizo el interruptor a Off
                        ((MainActivity)getActivity()).passAlarmIsOn("Off");
                        //cambio el grafico del boton
                        btnSeguimiento.setImageResource(R.drawable.pmenu_verifica_ruta);
                        break;
                    default: //en caso de que sea la primiera vez y el interruptor este vacio
                        //llamo a la funcion para activar el seguimiento
                        ((MainActivity)getActivity()).seguimiento(1);
                        //Actualizo el interruptor a on
                        ((MainActivity)getActivity()).passAlarmIsOn("On");
                        //cambio el grafico del boton
                        btnSeguimiento.setImageResource(R.drawable.pmenu_verifica_ruta_activo);
                        break;
                }
            }
        });

        // boton para Invitar Contactos
        btnEvento.setOnClickListener(new View.OnClickListener() {

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

        // boton ruta segura
        btnDireccion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_DestinationStep1_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        // boton generar alarma
        btnSosAmerica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_SosAmerica_Fragment();
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


                //Obtengo solo los nombres de la zona
                List<Zone> zonas = dataZonas.getNameZonas();

                //parseo los datos a la tabla
                for (Zone cn : zonas) {

                    //obtengo los datos internos de cada zona
                    List<Zone> ubicaciones = dataZonas.getLatLongZonas(cn.getNombre());

                    //creo un nuevo poligono
                    PolygonOptions opts = new PolygonOptions();

                    //construyo un objeto para almacenar el area
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();


                    //recorro la tabla
                    for (Zone latlong : ubicaciones) {

                        //parseo los datos
                        double x_lat = Double.parseDouble(latlong.getLatitud().toString());
                        double x_long = Double.parseDouble(latlong.getLongitud().toString());

                        //agrego los datos a poligono
                        opts.add(new LatLng(x_lat, x_long));

                        //almaceno los datos dentro del area
                        builder.include(new LatLng (x_lat,x_long));

                    }

                    //transformo el color de la zona a RGB
                    int r = Integer.valueOf(cn.getColorCode().substring(1, 3), 16);
                    int g = Integer.valueOf(cn.getColorCode().substring(3, 5), 16);
                    int b = Integer.valueOf(cn.getColorCode().substring(5, 7), 16);

                    //dibujo el borde sin transparencia
                    opts.strokeColor(Color.rgb(r, g, b));
                    //coloreo el interior del poligono con el mismo color de los bordes pero con un 20% de opacidad
                    opts.fillColor(Color.argb(20, r, g, b));

                    //agrego todos los datos al pama
                    map.addPolygon(opts);


                }

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
                        //Boolean localBoolean = Boolean.valueOf(locationManager.isProviderEnabled("Network"));


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
                            //Dialogo para abrir el gps
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
                            Toast.makeText(getActivity(), "Map Fragment Not Found or no Map in it", Toast.LENGTH_SHORT).show();
                        }

                        //map.setIndoorEnabled(true);
                        map.setMyLocationEnabled(true);
                        map.moveCamera(CameraUpdateFactory.zoomTo(16));
                        if (CENTER != null) {
                            map.animateCamera(CameraUpdateFactory.newLatLng(CENTER), 1750, null);
                        }
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        map.getUiSettings().setZoomControlsEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(true);


                        //Obtengo solo los nombres de la zona
                        List<Zone> zonas = dataZonas.getNameZonas();

                        //parseo los datos a la tabla
                        for (Zone cn : zonas) {

                            //obtengo los datos internos de cada zona
                            List<Zone> ubicaciones = dataZonas.getLatLongZonas(cn.getNombre());

                            //creo un nuevo poligono
                            PolygonOptions opts = new PolygonOptions();

                            //construyo un objeto para almacenar el area
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();

                            //recorro la tabla
                            for (Zone latlong : ubicaciones) {

                                //parseo los datos
                                double x_lat = Double.parseDouble(latlong.getLatitud().toString());
                                double x_long = Double.parseDouble(latlong.getLongitud().toString());

                                //agrego los datos a poligono
                                opts.add(new LatLng(x_lat, x_long));

                                //almaceno los datos dentro del area
                                builder.include(new LatLng (x_lat,x_long));

                            }

                            //transformo el color de la zona a RGB
                            int r = Integer.valueOf(cn.getColorCode().substring(1, 3), 16);
                            int g = Integer.valueOf(cn.getColorCode().substring(3, 5), 16);
                            int b = Integer.valueOf(cn.getColorCode().substring(5, 7), 16);

                            //dibujo el borde sin transparencia
                            opts.strokeColor(Color.rgb(r, g, b));
                            //coloreo el interior del poligono con el mismo color de los bordes pero con un 20% de opacidad
                            opts.fillColor(Color.argb(20, r, g, b));

                            //agrego todos los datos al pama
                            map.addPolygon(opts);

                        }






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
            Toast.makeText(getActivity(),"Exception 2" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }





}
