package sosamerica.sosclick.cl;



import sosamerica.sosclick.cl.GCMNotificationIntentService;
import sosamerica.sosclick.cl.R;
import android.telephony.TelephonyManager;
import sosamerica.sosclick.cl.llamadas;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.webkit.WebSettings.PluginState;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import static sosamerica.sosclick.cl.CommonUtilities.AQUI;


@SuppressLint("JavascriptInterface")
public class aqui extends Activity {
    private WebView WebView1;
    public String best;//Para el nombre del proveedor que vamos a utilizar.
    public static double latitude;
    public static double longitude;
    public String deviceid;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
     
        // VERIFICA QUE EXISTA INTERNET DISPONIBLE
        checkConnectivity();
        // VERIFICA QUE EXISTA INTERNET DISPONIBLE     
        
    	// DESPIERTA Y LEE GPS
    	actualizarPosicion();
    	// DESPIERTA Y LEE GPS  
    	
        
    	TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() != null){
        	deviceid = tm.getDeviceId(); //Para telefonos moviles
         }
        else{
        	 deviceid = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); //Para tablets
         }
        
      
        
        
	    // Texto
        String ualerta="";
        String paso= String.valueOf(GCMNotificationIntentService.mensaje);
        if (paso != null && paso != "null" && !paso.equals("")) {
        	ualerta=paso;
        } else {
        	ualerta="Me encuentro en esta ubicación.";
        }
        
        String datos_envio="";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
           datos_envio=extras.getString("datos");
        }
        
	    String sTexto = String.valueOf(GCMNotificationIntentService.mensaje) ;
	    AlertDialog.Builder dialogo = new AlertDialog.Builder(this);	 	    
	    
	    String[] separated = sTexto.split("imei:");
	    
	    String finalurl ="";

    		dialogo.setTitle("SosJoven");
    		dialogo.setMessage(ualerta);
    		dialogo.setMessage(separated[0]);
    		dialogo.setPositiveButton("Aceptar", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				//ultimaalerta();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   
    		//ALERTA POR Alerta	
       		finalurl = AQUI + llamadas.regId  
	    						+"&msg=" 
	    						+ GCMNotificationIntentService.mensaje_completo
	    						+"&imei_viene=" 
	    						+ GCMNotificationIntentService.imei_viene
	    						+"&latitud=" 
	    						+ String.valueOf(latitude) 
	    						+"&longitud=" 
	    						+ String.valueOf(longitude)
	    						+ "&imei=" + deviceid
	    						+ "&dispositivo=android";

            	WebView1 = (WebView) findViewById(R.id.webview1);
            	WebView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            	WebView1.getSettings().setBuiltInZoomControls(false);
            	WebView1.setWebViewClient(new GeoWebViewClient());
	            WebChromeClient cClient = new WebChromeClient();
	            WebView1.setWebChromeClient(cClient);
	            WebView1.getSettings().setJavaScriptEnabled(true);
	            WebView1.addJavascriptInterface(this, "jsNativeInterface");
	            WebView1.getSettings().setGeolocationEnabled(true);
	            WebView1.getSettings().setDomStorageEnabled(true);				        
		    	WebView1.loadUrl(finalurl);		
		    	WebView1.setWebViewClient(new WebViewClient() {

			        @Override
			        public boolean shouldOverrideUrlLoading(WebView view, String url) {
			             if (url.indexOf("tel:") > -1) {
			                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
			                return true;
			            } else {
			                return false;
			            }
			        } 
		        });
} 
    
    /* ******************************************************************************************************************************************** */
    /*                                Redireccion en caso de fono y manejo de errores por pagina en blanco                                          */    
    /* ******************************************************************************************************************************************** */
            public class GeoWebViewClient extends WebViewClient {

                //Mostrar una pagina en blanco en caso de no haber conexion
                @Override
                public void onReceivedError(WebView view, int errorCode,
                        String description, String failingUrl) {
                            //Muestro una ventana en blanco
                            view.loadUrl("about:blank");
                            
                            // Creacion de cuadro de dialogo
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(aqui.this);
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
    
    //BOTON VOLVER ATRAS
	/* @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        	// Esto es lo que hace mi botón al pulsar ir a atrás
    	        Intent volveratras = new Intent(this, llamadas.class );
    	        startActivity(volveratras);
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }*/
//BOTON VOLVER ATRAS
    
    
    private boolean checkConnectivity()
    {
        boolean enabled = true;
 
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
         
        if ((info == null || !info.isConnected() || !info.isAvailable()))
        {
        	//ALERTA QUE NO EXISTE INTERNET DISPONIBLE    
    	    AlertDialog.Builder dialogo = new AlertDialog.Builder(this);	 
    		dialogo.setTitle("Error");
    		dialogo.setMessage("La conexión a internet no se encuentra disponible en estos momentos");
    		dialogo.setPositiveButton("Aceptar", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				cerrar_1();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   
    		//ALERTA QUE NO EXISTE INTERNET DISPONIBLE       
        	
        	
        }
        return enabled;         
    }
    
    private void cerrar_1()
    {
    	this.finish();		
    }


    private void cerrar()
    {
	Intent i20 = new Intent(this, cerrar.class );
	startActivity(i20);
	//PROBANDO
	finish();
	//PROBANDO		
    }
    
    private void ultimaalerta()
    {
	Intent i21 = new Intent(this, ultimaalerta.class );
	startActivity(i21);
	//PROBANDO
	finish();
	//PROBANDO		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

        case R.id.ver_notificaciones:
            Intent i = new Intent(this, iralweb.class );
            startActivity(i);
//            finish();
            return true; 
       case R.id.ulaerta:
            Intent i1 = new Intent(this, ultimaalerta.class );
            startActivity(i1);
         //   finish();
            return true;            
        case R.id.perfil:
            Intent i2 = new Intent(this, perfil.class );
            startActivity(i2);
         //   finish();
            return true;  
        case R.id.contactos:
            Intent i21 = new Intent(this, contactos.class );
            startActivity(i21);
         //   finish();
            return true;              
        case R.id.llamadas:
            Intent i3 = new Intent(this, llamadas.class );
            startActivity(i3);
         //   finish();
            return true;               
        case R.id.cerrar:
      	  // Intent i4 = new Intent(this, cerrar.class );
      	  // startActivity(i4);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void actualizarPosicion()
    {
    	LocationManager mlocManager= null;
        LocationListener mlocListener;
        mlocManager  =  (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	  if ( !mlocManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
      	    AlertDialog.Builder dialogo = new AlertDialog.Builder(this);	 
      		dialogo.setTitle("Error");
      		dialogo.setMessage("Tu GPS no se encuentra disponible en estos momentos, deberás habilitarlo para poder enviar tu ubicación");
      		dialogo.setPositiveButton("Aceptar", new OnClickListener() {
      			public void onClick(DialogInterface dialog, int which) {
      				
      			}
      		});
      		dialogo.create();
      		dialogo.setIcon(R.drawable.ic_launcher);         
      		dialogo.show();  
    	  } 
        Criteria criteria = new Criteria();
        best = mlocManager.getBestProvider(criteria, true);

        Location location =
        		mlocManager.getLastKnownLocation(best);

        //Nos registramos para recibir actualizaciones de la posición
        mlocListener = new LocationListener() {
            public void onLocationChanged(Location loc) {
                loc.getLatitude();
                loc.getLongitude();
                latitude=loc.getLatitude();
                longitude=loc.getLongitude();
            }
            public void onProviderDisabled(String provider){
            }
            public void onProviderEnabled(String provider){
            }
            public void onStatusChanged(String provider, int status, Bundle extras){
            }
        };
         mlocManager.requestLocationUpdates( best, 1, 0, mlocListener);
          muestraPosicion(location);   
    }
    
    private void muestraPosicion(Location loc) {
        if(loc != null)
        {
            loc.getLatitude();
            loc.getLongitude();
            latitude=loc.getLatitude();
            longitude=loc.getLongitude();
        }
        else
        {
            latitude=0.0;
            longitude=0.0;
        }
    }

}       
    
    
    
    
    
    
    
    
