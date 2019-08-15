package info.androidhive.clickfarma.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.clickfarma.R;
import info.androidhive.clickfarma.app.AppConfig;
import info.androidhive.clickfarma.app.AppController;
import info.androidhive.clickfarma.helper.SessionManager;


public class View_VademecumView_Fragment extends Fragment {



    private ProgressDialog pDialog;
    private TextView txt_Titulo;
    private TextView txt_Descripcion;
    private TextView txt_Categoria;


    private Button btnBack;




    //se llama al modulo de sesion
    private SessionManager session;



    public View_VademecumView_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listas_view, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_vademecumdet));


        btnBack                 = (Button) rootView.findViewById(R.id.btnVolver);
        txt_Titulo              = (TextView) rootView.findViewById(R.id.Titulo);
        txt_Descripcion         = (TextView) rootView.findViewById(R.id.Descripcion);
        txt_Categoria           = (TextView) rootView.findViewById(R.id.Categoria);


        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        //llamo la variable con el id almacenada en la actividad principal
        String itemID = ((MainActivity) getActivity()).getsaveCategory();

        //Se cargan los datos
        searchVidaSana(itemID);


        // se redirije a la pantalla anterior
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_VademecumList_Fragment();
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
    public void  searchVidaSana(final String idVademecum) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Obteniendo Datos ...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VIEWVADEMECUM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        try {

                            JSONObject datosOferta = jObj.getJSONObject("list");
                            //traspaso a variables cada uno de los datos obtenidos
                            String Titulo          = datosOferta.getString("Titulo");
                            String Descripcion     = datosOferta.getString("Descripcion");
                            String Categoria       = datosOferta.getString("Categoria");

                            if(!Titulo.isEmpty()){         txt_Titulo.setText(Titulo);}
                            if(!Descripcion.isEmpty()){    txt_Descripcion.setText(Html.fromHtml(Descripcion), TextView.BufferType.SPANNABLE);}
                            if(!Categoria.isEmpty()){      txt_Categoria.setText(Categoria);}

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "No hay datos" + e, Toast.LENGTH_LONG).show();
                        }


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
                params.put("idVademecum", idVademecum);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }










}
