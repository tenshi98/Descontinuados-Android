package info.androidhive.clickfarma.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
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
import info.androidhive.clickfarma.helper.DB_contactos;
import info.androidhive.clickfarma.model.Contact;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLoginFace;
    private Button btnLinkToRegister;
    private Button btnLinkToRecover;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private DB_usuarios dataUsuarios;
    private DB_contactos dataContactos;
    public String GSMNumber;
    private JSONArray fono = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Login);
        setContentView(R.layout.activity_login);

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

        inputEmail         = (EditText) findViewById(R.id.email);
        inputPassword      = (EditText) findViewById(R.id.password);
        btnLogin           = (Button) findViewById(R.id.btnLogin);
        btnLoginFace       = (Button) findViewById(R.id.btnLoginFace);
        btnLinkToRegister  = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLinkToRecover   = (Button) findViewById(R.id.btnLinkToRecoverScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        dataUsuarios   = new DB_usuarios(getApplicationContext());
        dataContactos  = new DB_contactos(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Ingrese los datos solicitados", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Link to Recover Screen
        btnLinkToRecover.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RecoverActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Link to Recover login face
        btnLoginFace.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginFacebookActivity.class);
                startActivity(i);
                finish();
            }
        });



    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Ingresando ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
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

                        //Obtengo los contactos del usuario
                        try {

                            fono = jObj.getJSONArray("fonos");
                            for (int i = 0; i < fono.length(); i++) {
                                //obtengo el puntero para manejar el arreglo
                                JSONObject sub = fono.getJSONObject(i);

                                //obtengo los datos del arreglo
                                String name           = sub.getString("name");
                                String phone_number   = sub.getString("phone_number");
                                String estado         = sub.getString("estado");
                                String gsm            = sub.getString("gsm");
                                String idcliente      = sub.getString("idcliente");
                                String TipoContacto   = sub.getString("TipoContacto");

                                //agrego los contactos a la base interna
                                dataContactos.addContact(new Contact(name, phone_number, estado, gsm, idcliente, TipoContacto));

                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Ningun contacto importado", Toast.LENGTH_LONG).show();
                        }



                        //Se crea la sesion
                        session.setLogin(true);

                        // Redirijo a la pantalla principal
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Error en el login", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Error en el login", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
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
