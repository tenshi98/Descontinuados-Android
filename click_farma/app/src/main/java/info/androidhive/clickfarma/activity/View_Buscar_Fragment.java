package info.androidhive.clickfarma.activity;



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
import android.widget.Spinner;
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

import info.androidhive.clickfarma.R;
import info.androidhive.clickfarma.app.AppConfig;
import info.androidhive.clickfarma.app.AppController;
import info.androidhive.clickfarma.helper.SessionManager;


public class View_Buscar_Fragment extends Fragment {

    //declaracion de botones y texto
    private Spinner spinner1;

    private Button btnSearch;
    private Button btnBack;

    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;

    private ProgressDialog pDialog;
    private JSONArray espec = null;



    public View_Buscar_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buscar, container, false);


        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_search));




        //Se toman los botones y textos del layout
        spinner1   = (Spinner) rootView.findViewById(R.id.spinner1);
        btnSearch  = (Button) rootView.findViewById(R.id.btnBuscar);
        btnBack    = (Button) rootView.findViewById(R.id.btnVolver);


        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        searchEspec();








        // Si se presiona el boton guardar datos
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //parseo todos los datos
                String int_especialidad  = spinner1.getSelectedItem().toString().trim();

                //verifico la existencia de los datos obligatorios
                if (!int_especialidad.equals("Seleccionar una Especialidad") ) {

                    //le paso a la variable interna la direccion
                    ((MainActivity) getActivity()).passDataActivity(int_especialidad);

                    Fragment fragment = new View_Resultados_Fragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {
                    // mensaje de error en caso de no completar los datos
                    Toast.makeText(getActivity(), "Ingrese los datos solicitados", Toast.LENGTH_SHORT).show();
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
    public void  searchEspec() {

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setMessage("Obteniendo Especialidades ...");
            pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_ESPEC, new Response.Listener<String>() {

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
                            list1.add("Seleccionar una Especialidad");


                            try {

                                espec = jObj.getJSONArray("espec");
                                for (int i = 0; i < espec.length(); i++) {
                                    //obtengo el puntero para manejar el arreglo
                                    JSONObject sub = espec.getJSONObject(i);

                                    //obtengo los datos del arreglo
                                    //String name           = sub.getString("Nombre");
                                    list1.add(sub.getString("Nombre"));

                                }
                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Ninguna especialidad encontrada", Toast.LENGTH_LONG).show();
                            }

                            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list1);
                            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner1.setAdapter(dataAdapter1);


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
