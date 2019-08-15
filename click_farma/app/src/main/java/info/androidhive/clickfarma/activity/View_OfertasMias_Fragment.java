package info.androidhive.clickfarma.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.clickfarma.R;
import info.androidhive.clickfarma.adapter.CustomListAdapter;
import info.androidhive.clickfarma.app.AppConfig;
import info.androidhive.clickfarma.app.AppController;
import info.androidhive.clickfarma.helper.SessionManager;
import info.androidhive.clickfarma.model.Movie;


public class View_OfertasMias_Fragment extends Fragment {


    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;
    private Button btnBack;

    //se crea arreglo temporal
    protected ArrayList<String> mObjectIds = new ArrayList<String>();


    //se llama al modulo de sesion
    private SessionManager session;



    public View_OfertasMias_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ofertas, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_ofertasmia));

        btnBack = (Button) rootView.findViewById(R.id.btnVolver);


        // administrador de sesiones
        session = new SessionManager(getActivity());
        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }



        listView = (ListView) rootView.findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);






        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Cargando...");
        pDialog.show();



        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(AppConfig.URL_OFERMIA,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setTitulo(obj.getString("titulo"));
                                movie.setThumbnailUrl(AppConfig.FOLDER_PHOTOS + obj.getString("imagen"));
                                movie.setDescripcion(obj.getString("descripcion"));
                                movie.setValor(obj.getInt("valor"));

                                 //Integer.toString(cn.getID());
                                mObjectIds.add(obj.getString("id"));



                                // adding movie to movies array
                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);



        //verifico donde el usuario presiono y redirijo a la ventana
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                ((MainActivity) getActivity()).passDataActivity(mObjectIds.get(position));

                Fragment fragment = new View_OfertasMiasView_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        // se redirije a la pantalla anterior
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Doctor_Fragment();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }









}
