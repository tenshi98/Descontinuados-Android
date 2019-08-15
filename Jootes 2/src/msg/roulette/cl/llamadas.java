package msg.roulette.cl;

import com.google.android.gcm.GCMRegistrar;

import msg.roulette.cl.R;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import static msg.roulette.cl.CommonUtilities.SERVER_LLAMADAS;
import static msg.roulette.cl.CommonUtilities.SENDER_ID;



public class llamadas extends Activity {
    private WebView llamar;
    public String best;//Para el nombre del proveedor que vamos a utilizar.
    public static double latitude;
    public static double longitude;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.llamadas);
        
        //verifica si el gps esta activo
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
	
        	Criteria criteria = new Criteria();
            best = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates( best, 1000, 0, new MiLocationListener());	
        	
        }else{
        	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS se encuentra desactivado, desea activarlo?")
            .setCancelable(false)
            .setPositiveButton("Activar GPS",
                    new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                    Intent callGPSSettingIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(callGPSSettingIntent);
                }
            });
            alertDialogBuilder.setNegativeButton("Cancelar",
                    new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
       
      
    	
        try
		{

        	
        	final String regIdMain = GCMRegistrar.getRegistrationId(this);
	        if (regIdMain.equals("")) {
	         GCMRegistrar.register(this, SENDER_ID); 
	        } else {
	        }
	        final String regId = GCMRegistrar.getRegistrationId(this);
				        

				    	String finalurl = SERVER_LLAMADAS 
				    			+"?id="
				    			+ regId
				    			+"&msg=" 
							    + String.valueOf(GCMIntentService.mensaje) 
				    			+"&latitud=" 
				    			+ String.valueOf(llamadas.latitude)
							    +"&longitud=" 
							    + String.valueOf(llamadas.longitude)
							    ;
				    	

		    	llamar = (WebView) findViewById(R.id.llamadas);
		    	llamar.getSettings().setJavaScriptEnabled(true);
		    	llamar.addJavascriptInterface(this, "jsNativeInterface");
		    	llamar.loadUrl(finalurl);
		    	//evitar que la aplicacion abra ventanas del navegador
		    	llamar.setWebViewClient(new WebViewClient() {
			        @Override
			        public boolean shouldOverrideUrlLoading(WebView view, String url){
			            return false;
			        } 
		        });
		        
		}
		catch (Exception ex){ }     

    } 
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_llamar:
            	Intent i0 = new Intent(this, llamadas.class );
            	startActivityForResult(i0,0);
                return true;
            case R.id.ver_notificaciones:
            	Intent i1 = new Intent(this, iralweb.class );
            	startActivityForResult(i1,0);
                return true;
            case R.id.options_perfil:
            	Intent i2 = new Intent(this, perfil.class );
            	startActivityForResult(i2,0);
                return true;
            case R.id.options_coversacion:
            	Intent i3 = new Intent(this, conversaciones.class );
            	startActivityForResult(i3,0);
                return true;
            case R.id.options_exit:
            	Intent i4 = new Intent(this, usrexit.class );
            	startActivityForResult(i4,0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
  
    public class MiLocationListener implements LocationListener{
        public void onLocationChanged(Location loc){
 


                        if(loc != null) {
                            loc.getLatitude();
                            loc.getLongitude();
                            latitude=loc.getLatitude();
                            longitude=loc.getLongitude();
                        } else {
                            latitude=0.0;
                            longitude=0.0;
                        }
 
        }
        public void onProviderDisabled(String provider){
                Toast.makeText( getApplicationContext(),"Gps Desactivado",Toast.LENGTH_SHORT ).show();
        }
        public void onProviderEnabled(String provider){
                Toast.makeText( getApplicationContext(),"Gps Activo",Toast.LENGTH_SHORT ).show();
        }
        public void onStatusChanged(String provider, int status, Bundle extras){}
    }   
  
}