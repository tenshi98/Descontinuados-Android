package sosamerica.sosclick.cl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyAlarmService extends Service {
	public String best;//Para el nombre del proveedor que vamos a utilizar.
    public static double latitude;
    public static double longitude;
    public String deviceid;
    public static String  parametro_imei;
@Override
public void onCreate() {
	
 // TODO Auto-generated method stub
//Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
}

@Override
public IBinder onBind(Intent intent) {
 // TODO Auto-generated method stub
 //Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
 return null;
}

@Override
public void onDestroy() {
 // TODO Auto-generated method stub
 super.onDestroy();
 //Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
}

@SuppressWarnings("deprecation")
@Override
public void onStart(Intent intent, int startId) {
 // TODO Auto-generated method stub
 super.onStart(intent, startId);
	// DESPIERTA Y LEE GPS
	actualizarPosicion();
	// DESPIERTA Y LEE GPS   
	// GRABA LOS DATOS 
	if (latitude!=0) { grabadatos(); }
	// GRABA LOS DATOS 
}

@Override
public boolean onUnbind(Intent intent) {
 // TODO Auto-generated method stub
//Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
 return super.onUnbind(intent);
}

private void actualizarPosicion()
{ 
	TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
if (tm.getDeviceId() != null){
	parametro_imei = tm.getDeviceId(); //Para telefonos moviles
 }
else{
	parametro_imei = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); //Para tablets
 }
	LocationManager mlocManager= null;
    LocationListener mlocListener;
    mlocManager  =  (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    
    Criteria criteria = new Criteria();
    best = mlocManager.getBestProvider(criteria, true);
    Location location =	mlocManager.getLastKnownLocation(best);

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
     mlocManager.requestLocationUpdates( best, 1000, 0, mlocListener);
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

private void grabadatos()
{
       		 Toast.makeText(this, "Enviando Ubicacion: "
			 +"LATI: "+String.valueOf(latitude)
			 +" LONG: "+String.valueOf(longitude)
			 , Toast.LENGTH_LONG).show();
	 Thread th1 = new Thread(new Runnable() {
        @Override
        public void run() {
       	 try {	 
       		 String chequeo = "http://www.sosclick.cl/app/";
             HttpClient mClient = new DefaultHttpClient();
             StringBuilder sb=new StringBuilder(chequeo+"seguimiento.php");
             HttpPost mpost = new HttpPost(sb.toString());
             List<BasicNameValuePair> nameValuepairs = new ArrayList<BasicNameValuePair>(3);
             nameValuepairs.add(new BasicNameValuePair("imei",String.valueOf(parametro_imei)));
             nameValuepairs.add(new BasicNameValuePair("long",String.valueOf(longitude)));
             nameValuepairs.add(new BasicNameValuePair("lati",String.valueOf(latitude)));
             mpost.setEntity(new UrlEncodedFormEntity(nameValuepairs));
             HttpResponse responce = mClient.execute(mpost);
           } catch (Exception e) {
        	   Log.e("log_tag", "no grabo nada IMEI "+String.valueOf(parametro_imei)
        				 +"LATI "+String.valueOf(latitude)
        				 +"LONG "+String.valueOf(longitude)
        				 + e.toString());
           }

        }
      }); 
	th1.start();
}
}