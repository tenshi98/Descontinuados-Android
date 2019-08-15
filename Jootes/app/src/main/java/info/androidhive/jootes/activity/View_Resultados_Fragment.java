package info.androidhive.jootes.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.jootes.R;
import info.androidhive.jootes.app.AppConfig;
import info.androidhive.jootes.app.AppController;
import info.androidhive.jootes.helper.DB_usuarios;
import info.androidhive.jootes.helper.SessionManager;



public class View_Resultados_Fragment extends Fragment {

    //declaracion de texto
    private TextView txt_data;

    //declaracion de botones
    private Button btnVolver;
    private Button btnContactar;
    //private Button btnLlamar;

    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;
    private DB_usuarios dataUsuarios;

    private ProgressDialog pDialog;
    public String var_idCliente;
    public String var_fonocliente = "";

    public String var_idContacto;
    public String var_fonoContacto = "";

    private NetworkImageView imageView2;
    private ImageLoader imageLoader;


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
        txt_data       = (TextView) rootView.findViewById(R.id.text_data);
        btnVolver      = (Button) rootView.findViewById(R.id.btnVolver);
        btnContactar   = (Button) rootView.findViewById(R.id.btnContactar);
        imageView2     = (NetworkImageView) rootView.findViewById(R.id.imageView);


        //llamo la variable con el id almacenada en la actividad principal
        final String itemID = ((MainActivity) getActivity()).getDataActivity();
        String[] parts = itemID.split("and");
        final String Rango  = parts[0];
        final String Sexo   = parts[1];





        // se conecta a la base de datos interna
        dataUsuarios = new DB_usuarios(getActivity());
        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }
        // obtengo los datos del usuario
        HashMap<String, String> user = dataUsuarios.getUserDetails();
        var_idCliente       = user.get("idCliente");
        var_fonocliente     = user.get("Fono1");

        searchUser(Rango, Sexo, var_idCliente);


        // abro la activity con el chat
        btnContactar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {




                //generar el numero variable para la habitacion
                String room = "room" + var_idCliente;

                //le paso a la variable interna la direccion
                ((MainActivity) getActivity()).passDataActivity(room);

                //enviar la notificacion al otro usuario y redirigir en caso de ok
                ((MainActivity) getActivity()).invitarChat(var_idCliente, var_idContacto, room);


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
    public void  searchUser(final String Rango, final String Sexo, final String idCliente) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Buscando ...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_JOOTES, new Response.Listener<String>() {

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


                        txt_data.setText(Nombre);
                        var_idContacto   = user.getString("idContacto");
                        var_fonoContacto = user.getString("fonoContacto");

                        //guardo temporalmente el fono del contacto
                        ((MainActivity) getActivity()).passPhone(var_fonoContacto);

                        String image_url = AppConfig.FOLDER_UPLOAD + var_idContacto + "_" + Nombre + ".jpeg" ;


                        imageLoader = CustomVolleyRequest.getInstance(getActivity())
                                .getImageLoader();
                        imageLoader.get(image_url, ImageLoader.getImageListener(imageView2,
                                R.drawable.stub, android.R.drawable
                                        .ic_dialog_alert));
                        imageView2.setImageUrl(image_url, imageLoader);



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
                params.put("Rango", Rango);
                params.put("Sexo", Sexo);
                params.put("idCliente", idCliente);



                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }









}
