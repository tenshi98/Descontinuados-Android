package sosamerica.sosclick.cl;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sosamerica.sosclick.cl.R;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import static sosamerica.sosclick.cl.CommonUtilities.SERVER_LLAMADAS;
import static sosamerica.sosclick.cl.CommonUtilities.chequeo;



import com.google.android.gms.gcm.GoogleCloudMessaging;

public class llamadas extends Activity {
    private WebView llamadas;
    public String best;//Para el nombre del proveedor que vamos a utilizar.
    public String deviceid;
    GoogleCloudMessaging gcm;
    public String aquitoy2;
    public static double latitude;
    public static double longitude;
    public static final String REG_ID = "regId";
    Context context;
	public static String regId;
	private static final String APP_VERSION = "appVersion";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	

        super.onCreate(savedInstanceState);
        setContentView(R.layout.llamadas);
      
     // VERIFICA QUE EXISTA INTERNET DISPONIBLE
        checkConnectivity();

     //   VERIFICA QUE EXISTA INTERNET DISPONIBLE           
        
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
        
        // RESCATO EL ID DEL DISPOSITIVO
        //regId = registerGCM(deviceid);
        regId = registerGCM();
        // RESCATO EL ID DEL DISPOSITIVO
       // regId = "algo"; 
        

     
		String finalurl = SERVER_LLAMADAS + "?latitud=" + String.valueOf(latitude) 
							    +"&longitud=" + String.valueOf(longitude)
							    +"&imei=" + deviceid +"&id=" + regId + "&dispositivo=android";
		    	llamadas = (WebView) findViewById(R.id.llamadas);
		    	llamadas.getSettings().setJavaScriptEnabled(true);
		    	llamadas.addJavascriptInterface(new JavascriptManager(), "MainActivity");
		    	llamadas.getSettings().setSupportZoom(false); 
		    	llamadas.loadUrl(finalurl);	
		    	llamadas.setWebViewClient(new WebViewClient() {
			        @Override
			        public boolean shouldOverrideUrlLoading(WebView view, String url){
			             if (url.indexOf("tel:") > -1) {
				                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
				                return true;
				            } else {
				                return false;
				            }
			        } 
		        });

     } 

    class JavascriptManager
    {
        @JavascriptInterface
        public void getFromWebView()
        {
        	AlertDialog.Builder dialogo = new AlertDialog.Builder(llamadas.this);
    		dialogo.setTitle("ALERTA!!!");
    		dialogo.setMessage("Invitar a tus contactos a ser parte de tu red de seguridad.");
    		dialogo.setPositiveButton("Continuar", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				contactos();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   

        }
        @JavascriptInterface
        public void getFromWebView2()
        {
        	AlertDialog.Builder dialogo = new AlertDialog.Builder(llamadas.this);
    		dialogo.setTitle("ALERTA!!!");
    		dialogo.setMessage("Tu usuario ha sido bloqueado por la administración.");
    		dialogo.setPositiveButton("Continuar", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				cierre();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   

        }     
        @JavascriptInterface
        public void getFromWebView3()
        {
        	AlertDialog.Builder dialogo = new AlertDialog.Builder(llamadas.this);
    		dialogo.setTitle("ALERTA!!!");
    		dialogo.setMessage("Invitar a tus contactos a ser parte de tu red carrete Seguro.");
    		dialogo.setPositiveButton("Continuar", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				contactos();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   

        }
           @JavascriptInterface
        public void getFromWebView4()
        {
        	AlertDialog.Builder dialogo = new AlertDialog.Builder(llamadas.this);
    		dialogo.setTitle("ALERTA!!!");
    		dialogo.setMessage("Activaras Sistema de Seguimiento.");
    		dialogo.setPositiveButton("Continuar", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				activar();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   

        }
       @JavascriptInterface
        public void getFromWebView5()
        {
        	AlertDialog.Builder dialogo = new AlertDialog.Builder(llamadas.this);
    		dialogo.setTitle("ALERTA!!!");
    		dialogo.setMessage("Desactivaras Sistema de Seguimiento.");
    		dialogo.setPositiveButton("Continuar", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				desactivar();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   

        }      
        @JavascriptInterface
        public void getFromWebView6()
        {
        	AlertDialog.Builder dialogo = new AlertDialog.Builder(llamadas.this);
    		dialogo.setTitle("INFORMACION");
    		dialogo.setMessage("Notificacion Enviada.");
    		dialogo.setPositiveButton("Continuar", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				llamadas();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   

        }  
        @JavascriptInterface
        public void getFromWebView7()
        {
        	AlertDialog.Builder dialogo = new AlertDialog.Builder(llamadas.this);
    		dialogo.setTitle("INFORMACION");
    		dialogo.setMessage("Solicitud Enviada.");
    		dialogo.setPositiveButton("Continuar", new OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				llamadas();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   

        }  
        
        @JavascriptInterface
        public void getFromWebView777(String aquitoy)
        {
        	aquitoy2=aquitoy;

        }  
        
        
    }
    
    
    
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
    				cerrar();
    			}
    		});
    		dialogo.create();
    		dialogo.setIcon(R.drawable.ic_launcher);         
    		dialogo.show();   
    		//ALERTA QUE NO EXISTE INTERNET DISPONIBLE       
        	
        	
        }
        return enabled;         
    }
    
    

    
    

   private void activar()
    {
	Intent act = new Intent(this, AndroidAlarmService.class );
	startActivity(act);
	act.putExtra("quehace", "1");
	//PROBANDO
	finish();
	//PROBANDO		

    }
    private void desactivar()
    {
	Intent des = new Intent(this, AndroidAlarmService.class );
	startActivity(des);
	des.putExtra("quehace", "2");
	//PROBANDO
	finish();
	//PROBANDO		
    }
    private void cierre()
    {
	Intent i222 = new Intent(this, cerrar.class );
	startActivity(i222);
	//PROBANDO
	finish();
	//PROBANDO		
    }
    private void llamadas()
    {
	Intent illamadas = new Intent(this, llamadas.class );
	startActivity(illamadas);
	//PROBANDO
	finish();
	//PROBANDO		
    }    
    private void contactos()
    {
	Intent i20 = new Intent(this, contactos.class );
	startActivity(i20);
	//PROBANDO
	finish();
	//PROBANDO		
    }
  
    private void cerrar()
    {
    	this.finish();		
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
//              finish();
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
                //  return true;
              default:
                  return super.onOptionsItemSelected(item);
          }
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
//BOTON VOLVER ATRAS
    
    @Override
    public void onBackPressed() {
        if (llamadas.canGoBack() && aquitoy2.equals("INICIO")) {
        	finish();
        }else if (llamadas.canGoBack()) {
        	llamadas.goBack();
        }else{
        	super.onBackPressed();
        }
    }
  //BOTON VOLVER ATRAS   
    public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(context);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.d("GCM","registerGCM - successfully registered with GCM server - regId: "+ regId);
		} else {
			//Toast.makeText(getApplicationContext(),"RegId already available. RegId: " + regId,Toast.LENGTH_LONG).show();
		}
		return regId;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(llamadas.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i("GCM", "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i("GCM", "App version changed.");
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("GCM","I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	private void registerInBackground() {
		//Toast.makeText(getApplicationContext(),"RegId already available. RegId: " + regId,Toast.LENGTH_LONG).show();
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(CommonUtilities.SENDER_ID);
					Log.d("GCM", "registerInBackground - regId: "+ regId);
					msg = "Device registered, registration ID=" + regId;
					//ejecutar el codigo de victor
					
					actualizaGSM(regId);
					
					//ejecutar el codigo de victor
					//storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("GCM", "Error: " + msg);
				}
				Log.d("GCM", "AsyncTask completed: " + msg);
				return msg;
			}
/*
			@Override
			protected void onPostExecute(String msg) {
				 InputStream is = null;
				try {
					
					String postReceiverUrl = "http://sosamerica.sosclick.cl/app/actualiza_gcm.php";
		        	//String postReceiverUrl = chequeo+"actualiza_gcm.php?id="+regId+"&imei="+deviceid;?id=poto&imei=355864051171379
		        	Log.v("GCM", "postURL: " + postReceiverUrl);
		        	HttpClient httpClient = new DefaultHttpClient();
		        	HttpPost httpPost = new HttpPost(postReceiverUrl);
		        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        	nameValuePairs.add(new BasicNameValuePair("imei", deviceid));
		        	nameValuePairs.add(new BasicNameValuePair("id", regId));
		        	httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    		        HttpResponse response = httpClient.execute(httpPost);
    		        HttpEntity entity = response.getEntity();
    		        is = entity.getContent();
		        	Toast.makeText(getApplicationContext(),	"en el TRY actualizaGSM GCM: " + regId+"  Registered IMEI. " + deviceid, Toast.LENGTH_LONG).show();		        	 
		        	

		        } catch (Exception e) {
		        	//Log.e("GCM", "Error in http connection" + e.toString());
		        	Toast.makeText(getApplicationContext(),	"Error in http connection"+chequeo+"actualiza_gcm.php " + e.toString(), Toast.LENGTH_LONG).show();
		        }				
			}*/
		}.execute(null, null, null);
	}
	
	

	//TAREA	
	private void actualizaGSM(final String regid2)
    {
	
	// create your thread
    Thread t = new Thread(new Runnable() {
      public void run() {
        // you can use b in here

            try {

            	
				if (regId == null) {
					regId = "";
				}
            	String linked = chequeo+"actualiza_gcm.php?imei="+deviceid+"&id="+regId;
		        
		        InputStream is = null;
		       // StringBuilder sb = null;
		        // http post
		        try {
    		        HttpClient httpclient = new DefaultHttpClient();
    		        HttpPost httppost = new HttpPost(linked);
    		        // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    		        HttpResponse response = httpclient.execute(httppost);
    		        HttpEntity entity = response.getEntity();
    		        is = entity.getContent();
    		        Log.e("numero", "ok 1");
		        } catch (Exception e) {
		        	Log.e("numero", "Error in http connection" + e.toString());
		        }
		

           } catch (Exception ex) {
                ex.printStackTrace();
           }
           
      }
    });
    t.start();
    // wait for the thread
    try {
		t.join();
	} catch (InterruptedException e) {
		// Auto-generated catch block
		e.printStackTrace();
	}

	
    }
//TAREA
	
}