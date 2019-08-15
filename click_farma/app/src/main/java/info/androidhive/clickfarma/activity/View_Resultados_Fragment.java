package info.androidhive.clickfarma.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.clickfarma.R;
import info.androidhive.clickfarma.app.AppConfig;
import info.androidhive.clickfarma.app.AppController;
import info.androidhive.clickfarma.helper.DB_usuarios;
import info.androidhive.clickfarma.helper.SessionManager;



public class View_Resultados_Fragment extends Fragment {

    //declaracion de texto
    private TextView txt_nombre;
    private TextView txt_especialidad;

    //declaracion de botones
    private Button btnVolver;
    private Button btnContactar;
    //private Button btnLlamar;

    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;
    private DB_usuarios dataUsuarios;

    private ProgressDialog pDialog;

    public String var_idMedico;
    public String var_fonoMedico = "";
    public String Error = "" ;





    public View_Resultados_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resultados, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_result));

        //Se toman los botones y textos del layout
        txt_nombre         = (TextView) rootView.findViewById(R.id.text_data);
        txt_especialidad   = (TextView) rootView.findViewById(R.id.text_especialidad);
        btnVolver          = (Button) rootView.findViewById(R.id.btnVolver);
        btnContactar       = (Button) rootView.findViewById(R.id.btnContactar);


        // se conecta a la base de datos interna
        dataUsuarios = new DB_usuarios(getActivity());
        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        //llamo la variable con el id almacenada en la actividad principal
        final String especialidad = ((MainActivity) getActivity()).getDataActivity();
        searchUser(especialidad);


        HashMap<String, String> user = dataUsuarios.getUserDetails();
        final String idCliente    = user.get("idCliente");
        final String FonoCliente  = user.get("Fono1");


        // abro la activity con el chat
        btnContactar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Se comprueban datos vacios
                if (var_idMedico.isEmpty()  ) {
                    Error = "No existe la identificacion del medico" ;
                }
                if (var_fonoMedico.isEmpty()  ) {
                    Error = "El medico seleccionado no posee un numero telefonico al cual llamar" ;
                }
                if (FonoCliente.isEmpty()  ) {
                    Error = "No ha completado sus datos" ;

                    //abro cuadro de dialogo para redirigir al perfil en caso de que no esten todos los datos llenados
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Notificacion");
                    alertDialogBuilder.setMessage("No ha completado todos sus datos personales, ¿desea modificar su perfil?")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Fragment fragment = new View_Perfil_Fragment();
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.container_body, fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();

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

                //verifico la existencia de los datos obligatorios
                if (Error.isEmpty()) {
                    ((MainActivity) getActivity()).callback(var_idMedico, var_fonoMedico, idCliente, FonoCliente);

                } else {
                    // mensaje de error en caso de no completar los datos
                    Toast.makeText(getActivity(), Error , Toast.LENGTH_SHORT).show();
                }


            }
        });





        // se redirije a la pantalla de edicion de datos del usuario
        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Buscar_Fragment();
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
    public void  searchUser(final String Especialidad) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Buscando ...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEARCH, new Response.Listener<String>() {

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

                        txt_nombre.setText(user.getString("Nombre"));
                        txt_especialidad.setText(user.getString("especialidad"));

                        var_idMedico   = user.getString("idMedico");
                        var_fonoMedico = user.getString("fonoMedico");

                        pDialog.dismiss();


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                        //logerror = errorMsg;
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "VolleyError: " + error, Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "VolleyError: " + error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "VolleyError: " + error.networkResponse.data, Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "VolleyError: " + error.getCause(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "VolleyError: " + error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();

            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Especialidad", Especialidad);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }









}
