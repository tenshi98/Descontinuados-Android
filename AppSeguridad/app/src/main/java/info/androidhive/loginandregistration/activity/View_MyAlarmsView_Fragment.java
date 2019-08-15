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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import info.androidhive.loginandregistration.helper.DB_alertas;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.model.Alert;
import info.androidhive.loginandregistration.model.Contact;


public class View_MyAlarmsView_Fragment extends Fragment {


    private MapView mapView;
    private GoogleMap map;
    private LatLng CENTER = null;

    //private DB_zonas dataZonas;
    private DB_alertas dataAlertas;

    //declaracion de botones
    private Button btnVolver;

    //declaracion de textos
    private TextView txtAlarma;

    private String direccionCompleta = "";

    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;



    public View_MyAlarmsView_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_alarm_view, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_alert_view));

        //llamo a la base de datos con las zonas
        dataAlertas = new DB_alertas(getActivity());

        //Se toman los botones y textos del layout
        txtAlarma = (TextView) rootView.findViewById(R.id.Alarma);
        btnVolver = (Button) rootView.findViewById(R.id.btnVolver);

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
        Alert alerta = dataAlertas.getAlert(foo);

        final String TipoAlerta      = alerta.getTipoAlerta();
        double x_latitud = Double.parseDouble(alerta.getLatitud());
        double x_longitud = Double.parseDouble(alerta.getLongitud());

        // se escriben los datos en los respectivos elementos
        txtAlarma.setText(TipoAlerta);


        // boton volver a la pantalla principal
        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_MyAlarms_Fragment();
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

        //
       CENTER = new LatLng(x_latitud, x_longitud);

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

        //mapa
        map = mapView.getMap();
        if (map == null) {
            Toast.makeText(getActivity(), "Map Fragment Not Found or no Map in it", Toast.LENGTH_SHORT).show();
        }

        //muestro la ubiccion
        map.clear();

        final LatLng MELBOURNE = new LatLng(x_latitud, x_longitud);
        Marker melbourne = map.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("Ubicacion Alarma")
                .snippet(direccionCompleta)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));

        melbourne.showInfoWindow();

        //map.setIndoorEnabled(true);
        //map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(16));

        if (CENTER != null) {
            map.animateCamera(CameraUpdateFactory.newLatLng(CENTER), 1750, null);
        }

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        //map.getUiSettings().setMyLocationButtonEnabled(true);


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
