package info.androidhive.jootes.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import info.androidhive.jootes.R;
import info.androidhive.jootes.helper.SessionManager;
import android.webkit.WebChromeClient;

import java.util.HashMap;

import info.androidhive.jootes.app.AppConfig;
import info.androidhive.jootes.helper.DB_usuarios;


public class View_Chat_Fragment extends Fragment {


    private WebView mWebView;
    //se llama a los modulos que manejan la base de datos y la sesion
    private SessionManager session;
    private DB_usuarios dataUsuarios;







    public View_Chat_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        //seteo el titulo de la ventana
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_chat));

        ((MainActivity) getActivity()).toggleOptions(true);





        //llamo la variable con el id almacenada en la actividad principal
        String itemID = ((MainActivity) getActivity()).getDataActivity();


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


        String BaseUrl = AppConfig.URL_CHAT
                        +"?room="+itemID
                        +"&name="+user.get("Nombre");


        mWebView = (WebView) rootView.findViewById(R.id.activity_main_webview);
        mWebView.setWebViewClient(new WebViewClient());
        // Brower niceties -- pinch / zoom, follow links in place
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        //mWebView.setWebViewClient(new GeoWebViewClient());
        // Below required for geolocation
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setGeolocationEnabled(true);
        //mWebView.setWebChromeClient(new GeoWebChromeClient());
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.loadUrl(BaseUrl);





















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


    public class GeoWebViewClient extends WebViewClient {
        //Evitar que se abra el navegador al presionar un enlace
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String finalurl) {

            // When user clicks a hyperlink, load in the existing WebView
            if (finalurl.indexOf("tel:") > -1) {

                // Creacion de cuadro de dialogo
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("¿Desea llamar al siguiente numero?");
                alertDialog.setMessage(finalurl);
                //Creacion del boton cancelar
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.cancel();
                            }
                        });
                // Creacion del boton aceptar
                alertDialog.setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Inicio una nueva activity
                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(finalurl)));
                            }
                        });
                // Muestro la alerta
                alertDialog.show();

                return true;
            } else {
                view.loadUrl(finalurl);
                return true;
            }
        }


        //Mostrar una pagina en blanco en caso de no haber conexion
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            //Muestro una ventana en blanco
            view.loadUrl("about:blank");

            // Creacion de cuadro de dialogo
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Problemas de conectividad");
            alertDialog.setMessage("Al parecer en este momento no cuentas con acceso a internet, intentalo mas tarde");
            //Creacion del boton cancelar
            alertDialog.setNegativeButton("Cancelar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.cancel();
                        }
                    });
            // Creacion del boton aceptar
            alertDialog.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            // Inicio una nueva activity

                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));

                        }
                    });
            // Muestro la alerta
            alertDialog.show();

            //super.onReceivedError(view, errorCode, description, failingUrl);
        }

    }



    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            //Log.i(TAG, "onGeolocationPermissionsShowPrompt()");

            final boolean remember = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ubicacion");
            builder.setMessage("Se necesita la ubicacion")
                    .setCancelable(true).setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // origin, allow, remember
                    callback.invoke(origin, true, remember);
                }
            }).setNegativeButton("No permitir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // origin, allow, remember
                    callback.invoke(origin, false, remember);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }









}
