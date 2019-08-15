package info.androidhive.clickfarma.activity;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.clickfarma.R;
import info.androidhive.clickfarma.app.AppConfig;
import info.androidhive.clickfarma.app.AppController;
import info.androidhive.clickfarma.gcm.GCMClientManager;
import info.androidhive.clickfarma.helper.DB_usuarios;
import info.androidhive.clickfarma.helper.SessionManager;


public class LoginFacebookActivity extends Activity {


    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProgressDialog pDialog;
    public String GSMNumber;
    private SessionManager session;
    private DB_usuarios dataUsuarios;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setTheme(R.style.Login);
        setContentView(R.layout.activity_login_facebook);

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        String PROJECT_NUMBER=AppConfig.GCM_SENDER_ID;
        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                GSMNumber = registrationId;
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                /*info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );*/



                String Nombre     = "SinNombre";
                String email      = "";
                String password   = "1234";
                String sexo       = "";
                String edad       = "";
                registerUser(Nombre, email, password, sexo, edad);



                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    String email = me.optString("email");
                                    info.setText("email : " + email);
                                }
                            }
                        }).executeAsync();






            }

            @Override
            public void onCancel() {

                //info.setText("Login attempt canceled.");

            }

            @Override
            public void onError(FacebookException e) {

                //info.setText("Login attempt failed.");

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    private void registerUser(final String Nombre, final String email,final String password,final String sexo,final String edad) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registrando ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FACEREGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        //obtengo los datos desde el archivo php
                        JSONObject user = jObj.getJSONObject("user");
                        //traspaso a variables cada uno de los datos obtenidos
                        String idCliente        = user.getString("idCliente");
                        String email            = user.getString("email");
                        String Nombre           = user.getString("Nombre");
                        String Rut              = user.getString("Rut");
                        String fNacimiento      = user.getString("fNacimiento");
                        String Direccion        = user.getString("Direccion");
                        String Fono1            = user.getString("Fono1");
                        String Fono2            = user.getString("Fono2");
                        String Departamento     = user.getString("Departamento");
                        String Provincia        = user.getString("Provincia");
                        String Distrito         = user.getString("Distrito");
                        String fcreacion        = user.getString("fcreacion");
                        String factualizacion   = user.getString("factualizacion");
                        String Saldo            = user.getString("Saldo");
                        String Sexo             = user.getString("Sexo");
                        String Edad             = user.getString("Edad");

                        // Inserting row in users table
                        dataUsuarios.addUser(idCliente, email, Nombre, Rut, fNacimiento, Direccion, Fono1, Fono2, Departamento, Provincia,
                                Distrito, fcreacion, factualizacion, Saldo, Sexo, Edad);

                        //Se crea la sesion
                        session.setLogin(true);

                        // Redirijo a la pantalla principal
                        Intent intent = new Intent(LoginFacebookActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();




                    } else {
                        // Error occurred in registration. Get the error
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error:"+ e.getMessage() , Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Nombre", Nombre);
                params.put("email", email);
                params.put("password", password);
                params.put("sexo", sexo);
                params.put("edad", edad);
                //Datos creados manualmente para crear al usuario
                params.put("idSistema",  AppConfig.ID_SISTEMA);
                params.put("idEstado", "1");
                params.put("idTipo", "2");

                params.put("dispositivo", "android");
                params.put("IMEI", "");
                params.put("GSM", GSMNumber);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




}
