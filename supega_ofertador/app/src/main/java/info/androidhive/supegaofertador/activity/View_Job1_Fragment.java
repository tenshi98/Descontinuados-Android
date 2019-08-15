package info.androidhive.supegaofertador.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.supegaofertador.R;
import info.androidhive.supegaofertador.app.AppConfig;
import info.androidhive.supegaofertador.app.AppController;
import info.androidhive.supegaofertador.helper.DB_usuarios;
import info.androidhive.supegaofertador.helper.SessionManager;


public class View_Job1_Fragment extends Fragment {

    //declaracion de botones
    private Spinner spinner1, spinner2;
    private Button btnSiguiente, btnBack;
    private ProgressDialog pDialog;
    private JSONArray profesiones = null;
    private JSONArray sexo = null;
    private TextView input_Descripcion;
    public String Error = "" ;


    //private DB_usuarios dataUsuarios;




    //se llama a los modulos que manejan la base de datos y la sesion
    //private DB_usuarios dataUsuarios;
    private SessionManager session;

    public View_Job1_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_job_1, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_newjob));


        //Se toman los botones
        spinner1      = (Spinner) rootView.findViewById(R.id.spinner1);
        spinner2      = (Spinner) rootView.findViewById(R.id.spinner2);
        btnSiguiente  = (Button) rootView.findViewById(R.id.btnSiguiente);
        btnBack       = (Button) rootView.findViewById(R.id.btnVolver);
        input_Descripcion            = (EditText) rootView.findViewById(R.id.Descripcion);



        // administrador de sesiones
        session       = new SessionManager(getActivity());
        //dataUsuarios  = new DB_usuarios(getActivity());





        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        ChargePag();








        // Si se presiona el boton guardar datos
        btnSiguiente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //parseo todos los datos
                Error = "";
                String especialidad = spinner1.getSelectedItem().toString().trim();
                String sexo = spinner2.getSelectedItem().toString().trim();
                String descipcion = input_Descripcion.getText().toString().trim();

                if (descipcion.isEmpty()) {
                    Error = "No ha ingresado una descripcion del trabajo";
                }
                if (sexo.isEmpty()) {
                    Error = "No ha seleccionado un sexo";
                }
                if (especialidad.isEmpty()) {
                    Error = "No ha seleccionado una profesion";
                }


                //verifico la existencia de los datos obligatorios
                if (Error.isEmpty()) {

                    //Creo variable
                    String pasvarx = especialidad + "_and_" + sexo + "_and_" + descipcion;
                    //le paso a la variable interna la direccion
                    ((MainActivity) getActivity()).passDataActivity(pasvarx);

                    Fragment fragment = new View_Job2_Fragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {
                    // mensaje de error en caso de no completar los datos
                    Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                }

            }
        });


        // se redirije a la pantalla anterior
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Home_Fragment();
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
    public void  ChargePag() {

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setMessage("Obteniendo Datos ...");
            pDialog.show();

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


                            //Se ingresan manualmente los datos del primer select
                            List<String> list1 = new ArrayList<String>();
                            list1.add("Seleccionar una Profesion");

                            List<String> list2 = new ArrayList<String>();
                            list2.add("Seleccionar un Sexo");


                            try {

                                //se guardan las profesiones
                                profesiones = jObj.getJSONArray("profesiones");
                                for (int i = 0; i < profesiones.length(); i++) {
                                    //obtengo el puntero para manejar el arreglo
                                    JSONObject sub1 = profesiones.getJSONObject(i);
                                    //obtengo los datos del arreglo
                                    list1.add(sub1.getString("Nombre"));
                                }

                                //se guardan los sexos
                                sexo = jObj.getJSONArray("sexo");
                                for (int i = 0; i < sexo.length(); i++) {
                                    //obtengo el puntero para manejar el arreglo
                                    JSONObject sub2 = sexo.getJSONObject(i);
                                    //obtengo los datos del arreglo
                                    list2.add(sub2.getString("Nombre"));
                                }


                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Ninguna especialidad encontrada", Toast.LENGTH_LONG).show();
                            }

                            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list1);
                            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner1.setAdapter(dataAdapter1);

                            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list2);
                            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(dataAdapter2);


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


                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } catch (Exception e) {
            //verifica la conectividad y evita que la aplicacion deje de funcionar
            Toast.makeText(getActivity(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
        }
    }





}
