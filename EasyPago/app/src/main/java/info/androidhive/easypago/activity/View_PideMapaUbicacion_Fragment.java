package info.androidhive.easypago.activity;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.androidhive.easypago.R;
import info.androidhive.easypago.app.AppConfig;
import info.androidhive.easypago.app.AppController;
import info.androidhive.easypago.helper.SessionManager;


public class View_PideMapaUbicacion_Fragment extends Fragment {

    private MapView mapView;
    private GoogleMap map;
    private LatLng CENTER = null;

    //declaracion de botones
    private Button btnVolver;
    private TextView txtFecha;
    private String direccionCompleta = "";


    //declaracion de textos


    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;

    public View_PideMapaUbicacion_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pide_mapa_ubicacion, container, false);



        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_pide_ult_ubi));

        //Se toman los botones y textos del layout
        mapView       = (MapView) rootView.findViewById(R.id.mapView);
        btnVolver     = (Button) rootView.findViewById(R.id.btnVolver);
        txtFecha      = (TextView) rootView.findViewById(R.id.fechaActualizacion);

        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }


        //llamo la variable con el id almacenada en la actividad principal
        String itemID = ((MainActivity) getActivity()).getDataActivity();

        //se declara el mapa
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        setupMapa(itemID);


        // boton volver a la pantalla principal
        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_PideMapa_View_Fragment();
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



    //Se agrega nuevo contacto
    public void  setupMapa(final String idCliente) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MAPA_UBICACION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Login Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        //obtengo la respuesta del servidor relacionada al contacto, si existe o no
                        String Latitud    = jObj.getString("Latitud");
                        String Longitud   = jObj.getString("Longitud");
                        String Fecha      = jObj.getString("Fecha");

                        //parseo los datos
                        double x_lat = Double.parseDouble(Latitud.toString());
                        double x_long = Double.parseDouble(Longitud.toString());

                        //obtengo los datos de mi ubicacion
                        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                        try {
                            List<Address> addresses = geoCoder.getFromLocation(x_lat, x_long, 1);


                            if (addresses.size() > 0){
                                for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++) {
                                    direccionCompleta += addresses.get(0).getAddressLine(i) + " ";
                                }
                            }

                        }catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        //
                        CENTER = new LatLng(x_lat, x_long);

                        //mapa
                        map = mapView.getMap();
                        if (map == null) {
                            Toast.makeText(getActivity(), "Map Fragment Not Found or no Map in it", Toast.LENGTH_SHORT).show();
                        }

                        //muestro la ubiccion
                        map.clear();

                        final LatLng MELBOURNE = new LatLng(x_lat, x_long);
                        Marker melbourne = map.addMarker(new MarkerOptions()
                                .position(MELBOURNE)
                                .title("Ultima Ubicacion Conocida")
                                .snippet(direccionCompleta)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_2)));

                        melbourne.showInfoWindow();

                        txtFecha.setText("Ultima actualizacion el "+Fecha);

                        map.moveCamera(CameraUpdateFactory.zoomTo(16));
                        if (CENTER != null) {
                            map.animateCamera(CameraUpdateFactory.newLatLng(CENTER), 1750, null);
                        }


                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        map.getUiSettings().setZoomControlsEnabled(true);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                        //logerror = errorMsg;
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idCliente", idCliente);



                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


}

