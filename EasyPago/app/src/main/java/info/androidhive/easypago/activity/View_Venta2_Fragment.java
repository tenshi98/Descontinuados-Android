package info.androidhive.easypago.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.easypago.R;
import info.androidhive.easypago.app.AppConfig;
import info.androidhive.easypago.app.AppController;
import info.androidhive.easypago.helper.DB_usuarios;
import info.androidhive.easypago.helper.DB_ventas;
import info.androidhive.easypago.helper.SessionManager;
import info.androidhive.easypago.model.Ventas;


public class View_Venta2_Fragment extends Fragment {



    //declaracion de botones
    private TextView txtNombre;
    private TextView txtFono;
    private TextView txtValorpin;

    private String idCliente;

    private Button btnVolver;
    private Button btnConfirmar;




    //se llama a los modulos que manejan la base de datos y la sesion
    private DB_usuarios dataUsuarios;
    private DB_ventas dataVentas;
    private SessionManager session;

    public View_Venta2_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venta_2, container, false);



        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_pide_ult_ubi));

        //Se toman los botones y textos del layout
        txtNombre    = (TextView) rootView.findViewById(R.id.Nombre);
        txtFono      = (TextView) rootView.findViewById(R.id.fono);
        txtValorpin  = (TextView) rootView.findViewById(R.id.Valorpin);


        btnVolver     = (Button) rootView.findViewById(R.id.btnVolver);
        btnConfirmar  = (Button) rootView.findViewById(R.id.btnConfirmar);


        // administrador de sesiones
        session       = new SessionManager(getActivity());
        dataUsuarios  = new DB_usuarios(getActivity());
        dataVentas    = new DB_ventas(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }


        //llamo la variable con el id almacenada en la actividad principal
        String itemID = ((MainActivity) getActivity()).getDataActivity();
        String[] parts = itemID.split("zzandzz");
        String Fono  = parts[0];
        final String PIN = parts[1];



        //se realiza limpieza del numero telefonico
        Fono = Fono.replace("+569", "");
        Fono = Fono.replace("569", "");
        Fono = Fono.replace("+", "");
        Fono = Fono.replace("*", "");
        Fono = Fono.replace(" ", "");

        final String Fono2 = Fono;

        searchUser(Fono, PIN);


        // boton volver a la pantalla principal
        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Venta1_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }


        });

        // boton volver a la pantalla principal
        btnConfirmar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Verificar si el saldo es suficiente para poder efectuar la operacion

                // obtengo los datos del usuario
                HashMap<String, String> user = dataUsuarios.getUserDetails();
                final String Saldo       = user.get("Saldo");
                final String idUser      = user.get("idUser");
                final String idCliente2   = user.get("idCliente");

                int xvar1 = Integer.parseInt(Saldo);
                int xvar2 = 0;
                String carga = "";
                switch (PIN) {
                    case "1":  xvar2 = 5 ;  carga = "5 Soles"; break;
                    case "2":  xvar2 = 10 ; carga = "10 Soles"; break;
                    case "3":  xvar2 = 20 ; carga = "20 Soles"; break;
                    case "4":  xvar2 = 5 ;  carga = "5 plus Soles"; break;
                    case "5":  xvar2 = 10 ; carga = "10 plus Soles"; break;
                    case "6":  xvar2 = 20 ; carga = "20 plus Soles"; break;

                }
                String int_cliente = txtNombre.getText().toString().trim();



                //verifico la existencia de los datos obligatorios
                if ( !int_cliente.isEmpty() ) {
                    if (0<=(xvar1-xvar2)  ) {

                        //fecha actual
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
                        Date now = new Date();
                        String strDate = sdfDate.format(now);


                        //actualizo el saldo en el celular
                        int num = xvar1-xvar2;
                        String nuevosaldo = String.valueOf(num);
                        dataUsuarios.updateUserSaldo(idUser,nuevosaldo );
                        dataVentas.addVenta(new Ventas(strDate, carga, Fono2));

                        //actualizo el saldo en la web
                        ((MainActivity)getActivity()).addVenta(idCliente2,idCliente, PIN, Fono2,nuevosaldo );
                        Toast.makeText(getActivity(),"Venta Realizada correctamente",Toast.LENGTH_SHORT).show();

                        Fragment fragment = new View_Home_Fragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                    } else {
                        // mensaje de error en caso de no completar los datos
                        Toast.makeText(getActivity(), "No posee el saldo suficiente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // mensaje de error en caso de no completar los datos
                    Toast.makeText(getActivity(), "Sin datos, ingrese el numero correcto", Toast.LENGTH_SHORT).show();
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



    //Se agrega nuevo contacto
    public void  searchUser(final String Fono, final String PIN) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DATOS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Login Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        //obtengo los datos desde el archivo php
                        JSONObject user = jObj.getJSONObject("user");
                        //traspaso a variables cada uno de los datos obtenidos
                        String Nombre    = user.getString("Nombre");
                        String Fono      = user.getString("Fono");
                        idCliente        = user.getString("idCliente");

                        txtNombre.setText(Nombre);
                        txtFono.setText(Fono);
                        String nuevoPin = "";
                        switch (PIN) {
                            case "1":  nuevoPin = "5 Soles"; break;
                            case "2":  nuevoPin = "10 Soles"; break;
                            case "3":  nuevoPin = "20 Soles"; break;
                            case "4":  nuevoPin = "5 plus Soles"; break;
                            case "5":  nuevoPin = "10 plus Soles"; break;
                            case "6":  nuevoPin = "20 plus Soles"; break;

                        }


                        txtValorpin.setText(nuevoPin);



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
                params.put("Fono", Fono);



                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


}

