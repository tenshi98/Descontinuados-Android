package info.androidhive.supegatrab.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.supegatrab.R;
import info.androidhive.supegatrab.app.AppConfig;
import info.androidhive.supegatrab.app.AppController;
import info.androidhive.supegatrab.helper.DB_usuarios;
import info.androidhive.supegatrab.helper.SessionManager;



public class View_UploadIMG_Fragment extends Fragment {


    private DB_usuarios dataUsuarios;
    private SessionManager session;
    private Button buttonChoose;
    private Button buttonUpload;
    private Button btnBack;
    private ImageView imageView;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    //private String UPLOAD_URL ="http://190.98.210.36/jootes/app_android/upload_img.php";



    public String var_idCliente;

    public View_UploadIMG_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_uploadimg, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_img));


        // administrador de sesiones
        session       = new SessionManager(getActivity());
        dataUsuarios  = new DB_usuarios(getActivity());


        //si no esta logeado redirige al login
        if (!session.isLoggedIn()) {
            ((MainActivity)getActivity()).logoutUser();
            getActivity().finish();
        }

        // obtengo los datos del usuario
        HashMap<String, String> user = dataUsuarios.getUserDetails();
        var_idCliente       = user.get("idCliente");


        buttonChoose = (Button) rootView.findViewById(R.id.buttonChoose);
        buttonUpload = (Button) rootView.findViewById(R.id.buttonUpload);
        btnBack      = (Button) rootView.findViewById(R.id.btnVolver);
        imageView    = (ImageView) rootView.findViewById(R.id.imageView);



        buttonChoose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                uploadImage();

            }
        });

        // se redirije a la pantalla anterior
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new View_Perfil_Fragment();
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



    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Subiendo...","Por favor, espere...",false,false);

        String tag_string_req = "req_login";


        try {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_UPLOAD, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    //Log.d(TAG, "Login Response: " + response.toString());
                    //hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {

                            loading.dismiss();
                            Toast.makeText(getActivity(), "Modificacion Realizada", Toast.LENGTH_LONG).show();

                            Fragment fragment = new View_Perfil_Fragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();



                        } else {
                            // Error in login. Get the error message
                            loading.dismiss();
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                           // Toast.makeText(getActivity(), "error_msg", Toast.LENGTH_LONG).show();
                            //logerror = errorMsg;
                        }
                    } catch (JSONException e) {
                        // JSON error
                        loading.dismiss();
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // Log.e(TAG, "Login Error: " + error.getMessage());
                    //Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
                    loading.dismiss();
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    Toast.makeText(getActivity(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();

                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();

                    String image = getStringImage(bitmap);
                    //Adding parameters
                    params.put("image", image);
                    params.put("name", var_idCliente);
                    params.put("idCliente", var_idCliente);


                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } catch (Exception e) {
            loading.dismiss();
            //verifica la conectividad y evita que la aplicacion deje de funcionar
            Toast.makeText(getActivity(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
        }







    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





}
