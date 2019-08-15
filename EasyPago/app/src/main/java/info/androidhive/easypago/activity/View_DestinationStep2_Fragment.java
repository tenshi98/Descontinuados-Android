package info.androidhive.easypago.activity;


import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import info.androidhive.easypago.R;
import info.androidhive.easypago.helper.DB_usuarios;
import info.androidhive.easypago.helper.DB_zonas;
import info.androidhive.easypago.helper.SessionManager;
import info.androidhive.easypago.model.Zone;


public class View_DestinationStep2_Fragment extends Fragment {


    //para crear mapa
    private MapView mapView;
    private GoogleMap map;
    public LocationManager locationManager;


    //se almacenan los datos de mi ubicacion actual
    private LatLng CENTER = null;
    private double a_longitude;
    private double a_latitude;
    String direccionInicio = "";
    int enteroInicio = 0;
    String peligroInicio = "";

    //se almacenan los datos de mi destino
    private LatLng NEWCENTER = null;
    private double b_longitude;
    private double b_latitude;
    String direccionDestino = "";
    int enteroDestino = 0;
    String peligroDestino = "";

    //base de datos de las zonas
    private DB_zonas dataZonas;
    private DB_usuarios dataUsuarios;

    //declaracion de botones
    private Button btnMiUbicacion;
    private Button btnMiDestino;
    private Button btnVolver;

    //declaracion de textos
    private TextView txtPeligro;
    private ImageView imagenPeligro;

    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;



    public View_DestinationStep2_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_destination_step2, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_search2));


        //llamo a la base de datos con las zonas
        dataZonas = new DB_zonas(getActivity());
        dataUsuarios  = new DB_usuarios(getActivity());

        //llamo la variable con el id almacenada en la actividad principal
        direccionDestino = ((MainActivity) getActivity()).getDataActivity();

        //Se toman los botones y textos del layout
        txtPeligro = (TextView) rootView.findViewById(R.id.Peligro);
        imagenPeligro = (ImageView) rootView.findViewById(R.id.circular_image_view);


        btnMiUbicacion = (Button) rootView.findViewById(R.id.btnMiUbicacion);
        btnMiDestino = (Button) rootView.findViewById(R.id.btnMiDestino);

        btnVolver = (Button) rootView.findViewById(R.id.btnVolver);

        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }


        // boton mostrar mi ubicacion
        btnMiUbicacion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                miUbicacion();

            }


        });
        // boton para mostrar mi destino
        btnMiDestino.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                miDestino();

            }


        });

        // boton volver a la pantalla principal
        btnVolver.setOnClickListener(new View.OnClickListener() {

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

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        // obtengo los datos del usuario
        HashMap<String, String> user = dataUsuarios.getUserDetails();
        final String idUser    = user.get("idUser");
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
                String zlatitude = new Double(a_latitude).toString();
                String zlongitude = new Double(a_longitude).toString();
                ((MainActivity) getActivity()).modUserLatLog(idUser, zlatitude, zlongitude);

                return false;
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
                //map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.zoomTo(16));

                if (CENTER != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLng(CENTER), 1750, null);
                }
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.getUiSettings().setZoomControlsEnabled(true);
                //map.getUiSettings().setMyLocationButtonEnabled(true);

                //obtengo los datos de mi ubicacion
                Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocation(x_latitud, x_longitud, 1);




                    if (addresses.size() > 0) {
                        for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                            direccionInicio += addresses.get(0).getAddressLine(i) + " ";
                        }
                    }
                    //muestro la ubiccion
                    map.clear();

                    final LatLng MELBOURNE = new LatLng(x_latitud, x_longitud);
                    Marker melbourne = map.addMarker(new MarkerOptions()
                            .position(MELBOURNE)
                            .title("Ultima Ubicacion Conocida")
                            .snippet(direccionInicio)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));

                    melbourne.showInfoWindow();


                } catch (Exception e1) {
                    Toast.makeText(getActivity(), "No se ha podido obtener la ubicacion", Toast.LENGTH_LONG).show();

                }

                //realizo el proceso contrario, a partir de una direccion obtengo la latitud y longitud
                Geocoder coder = new Geocoder(getActivity());
                List<Address> address;

                try {
                    address = coder.getFromLocationName(direccionDestino, 5);
                    if (address == null) {
                        Toast.makeText(getActivity(), "Ingrese una direccion valida", Toast.LENGTH_LONG).show();
                    }else{
                        Address miLocation = address.get(0);
                        b_latitude   = miLocation.getLatitude();
                        b_longitude  = miLocation.getLongitude();
                        NEWCENTER = new LatLng(b_latitude, b_longitude);

                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }




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

                    //verifico si estoy dentro de una zona peligrosa
                    //armo los datos del area
                    LatLngBounds bound = builder.build();
                    //verifico si el dispositivo esta dentro del objeto recien creado
                    if (bound.contains(new LatLng (a_latitude,a_longitude))){

                        String peligrosidad = "";
                        peligrosidad = cn.getPeligrosidad();

                        if(peligrosidad==""){
                            peligroInicio = "No existen datos";

                        }else{
                            peligroInicio = "Nivel de peligro " + peligrosidad;
                            enteroInicio = Integer.parseInt(peligrosidad);
                            switch (enteroInicio) {
                                case 0:
                                    imagenPeligro.setImageResource(R.drawable.mapa_seguro);
                                    break;
                                case 1:
                                    imagenPeligro.setImageResource(R.drawable.mapa_precaucion);
                                    break;
                                case 2:
                                    imagenPeligro.setImageResource(R.drawable.mapa_alerta);
                                    break;
                                case 3:
                                    imagenPeligro.setImageResource(R.drawable.mapa_peligro);
                                    break;
                            }
                        }

                    }else{
                        //si no hay datos se indica que la zona es segura
                        if (peligroInicio.isEmpty()){
                            peligroInicio ="Zona segura" ;
                            txtPeligro.setText("Zona segura" );
                            imagenPeligro.setImageResource(R.drawable.mapa_seguro);
                        }

                    }

                    //verifico si mi destino esta una zona peligrosa
                    //verifico si el dispositivo esta dentro del objeto recien creado
                    if (bound.contains(new LatLng (b_latitude,b_longitude))){

                        String peligrosidad = "";
                        peligrosidad = cn.getPeligrosidad();

                        if(peligrosidad==""){
                            peligroDestino = "No existen datos";
                            txtPeligro.setText("No existen datos");

                        }else{
                            peligroDestino = "Nivel de peligro " + peligrosidad;
                            enteroDestino = Integer.parseInt(peligrosidad);
                        }

                    }else{
                        //si no hay datos se indica que la zona es segura
                        if (peligroDestino.isEmpty()){
                            peligroDestino ="Zona segura" ;
                        }

                    }

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
                                a_latitude = location.getLatitude();
                                a_longitude = location.getLongitude();
                                CENTER = new LatLng(a_latitude, a_longitude);


                                //obtengo los datos de mi ubicacion
                                Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                                try {
                                    List<Address> addresses = geoCoder.getFromLocation(a_latitude, a_longitude, 1);

                                    direccionInicio="";
                                    if (addresses.size() > 0) {
                                        for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                                            direccionInicio += addresses.get(0).getAddressLine(i) + " ";
                                        }
                                    }
                                    //muestro la ubiccion
                                    map.clear();

                                    final LatLng MELBOURNE = new LatLng(a_latitude, a_longitude);
                                    Marker melbourne = map.addMarker(new MarkerOptions()
                                            .position(MELBOURNE)
                                            .title("Ubicacion Actual")
                                            .snippet(direccionInicio)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));

                                    melbourne.showInfoWindow();


                                } catch (Exception e1) {
                                    Toast.makeText(getActivity(), "No se ha podido obtener la ubicacion", Toast.LENGTH_LONG).show();

                                }

                                //realizo el proceso contrario, a partir de una direccion obtengo la latitud y longitud
                                Geocoder coder = new Geocoder(getActivity());
                                List<Address> address;

                                try {
                                    address = coder.getFromLocationName(direccionDestino, 5);
                                    if (address == null) {
                                        Toast.makeText(getActivity(), "Ingrese una direccion valida", Toast.LENGTH_LONG).show();
                                    }else{
                                        Address miLocation = address.get(0);
                                        b_latitude   = miLocation.getLatitude();
                                        b_longitude  = miLocation.getLongitude();
                                        NEWCENTER = new LatLng(b_latitude, b_longitude);

                                    }

                                } catch (Exception ex) {

                                    ex.printStackTrace();
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

                            //verifico si estoy dentro de una zona peligrosa
                            //armo los datos del area
                            LatLngBounds bound = builder.build();
                            //verifico si el dispositivo esta dentro del objeto recien creado
                            if (bound.contains(new LatLng (a_latitude,a_longitude))){

                                String peligrosidad = "";
                                peligrosidad = cn.getPeligrosidad();

                                if(peligrosidad==""){
                                    peligroInicio = "No existen datos";

                                }else{
                                    peligroInicio = "Nivel de peligro " + peligrosidad;
                                    enteroInicio = Integer.parseInt(peligrosidad);
                                    switch (enteroInicio) {
                                        case 0:
                                            imagenPeligro.setImageResource(R.drawable.mapa_seguro);
                                            break;
                                        case 1:
                                            imagenPeligro.setImageResource(R.drawable.mapa_precaucion);
                                            break;
                                        case 2:
                                            imagenPeligro.setImageResource(R.drawable.mapa_alerta);
                                            break;
                                        case 3:
                                            imagenPeligro.setImageResource(R.drawable.mapa_peligro);
                                            break;
                                    }
                                }

                            }else{
                                //si no hay datos se indica que la zona es segura
                                if (peligroInicio.isEmpty()){
                                    peligroInicio ="Zona segura" ;
                                    txtPeligro.setText("Zona segura" );
                                    imagenPeligro.setImageResource(R.drawable.mapa_seguro);
                                }

                            }

                            //verifico si mi destino esta una zona peligrosa
                            //verifico si el dispositivo esta dentro del objeto recien creado
                            if (bound.contains(new LatLng (b_latitude,b_longitude))){

                                String peligrosidad = "";
                                peligrosidad = cn.getPeligrosidad();

                                if(peligrosidad==""){
                                    peligroDestino = "No existen datos";
                                    txtPeligro.setText("No existen datos");

                                }else{
                                    peligroDestino = "Nivel de peligro " + peligrosidad;
                                    enteroDestino = Integer.parseInt(peligrosidad);
                                }

                            }else{
                                //si no hay datos se indica que la zona es segura
                                if (peligroDestino.isEmpty()){
                                    peligroDestino ="Zona segura" ;
                                }

                            }

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
            //Toast.makeText(getActivity(),"Exception 2" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }




    private void miUbicacion() {

        if (CENTER != null) {

            Marker melbourne1 = map.addMarker(new MarkerOptions()
                    .position(CENTER)
                    .title("Ubicacion Actual")
                    .snippet(direccionInicio)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));
            melbourne1.showInfoWindow();

            //map.addMarker(new MarkerOptions().position(CENTER).title("Ubicacion Actual").snippet(direccionInicio).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));
            map.moveCamera(CameraUpdateFactory.zoomTo(16));
            map.animateCamera(CameraUpdateFactory.newLatLng(CENTER), 1750, null);
            txtPeligro.setText(peligroInicio);
            switch (enteroInicio) {
                case 0:
                    imagenPeligro.setImageResource(R.drawable.mapa_seguro);
                    break;
                case 1:
                    imagenPeligro.setImageResource(R.drawable.mapa_precaucion);
                    break;
                case 2:
                    imagenPeligro.setImageResource(R.drawable.mapa_alerta);
                    break;
                case 3:
                    imagenPeligro.setImageResource(R.drawable.mapa_peligro);
                    break;
            }
            //Toast.makeText(getActivity(),"asd" + enteroInicio, Toast.LENGTH_SHORT).show();
        }

    }

    private void miDestino() {

        if (NEWCENTER != null) {

            Marker melbourne2 = map.addMarker(new MarkerOptions()
                    .position(NEWCENTER)
                    .title("Destino")
                    .snippet(direccionDestino)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));
            melbourne2.showInfoWindow();

            //map.addMarker(new MarkerOptions().position(NEWCENTER).title("Ubicacion Destino").snippet(direccionDestino).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));

            map.moveCamera(CameraUpdateFactory.zoomTo(16));
            map.animateCamera(CameraUpdateFactory.newLatLng(NEWCENTER), 1750, null);
            txtPeligro.setText(peligroDestino);
            switch (enteroDestino) {
                case 0:
                    imagenPeligro.setImageResource(R.drawable.mapa_seguro);
                    break;
                case 1:
                    imagenPeligro.setImageResource(R.drawable.mapa_precaucion);
                    break;
                case 2:
                    imagenPeligro.setImageResource(R.drawable.mapa_alerta);
                    break;
                case 3:
                    imagenPeligro.setImageResource(R.drawable.mapa_peligro);
                    break;
            }
            //Toast.makeText(getActivity(),"asd" + enteroDestino, Toast.LENGTH_SHORT).show();
        }

    }










}
