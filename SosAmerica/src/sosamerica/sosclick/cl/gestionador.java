package sosamerica.sosclick.cl;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import sosamerica.sosclick.cl.R;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.webkit.WebView;
import static sosamerica.sosclick.cl.CommonUtilities.chequeo;

public class gestionador extends Activity {
    private WebView main;

    public String deviceid;
    public String sb1;
    public String datosfinal;
    public int  variable;

    private Handler puente = new Handler() {
    	  @Override
    	  public void handleMessage(Message msg) {
    	   
    	  }
    	 };
    	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    	TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() != null){
        	deviceid = tm.getDeviceId(); //Para telefonos moviles
         }
        else{
        	 deviceid = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); //Para tablets
         }
        //ARMO LOS PROCESA SEGUN LO QUE NECESITE
   	 	Bundle bundle = getIntent().getExtras();
        String queviene= bundle.getString("dedonde");
      	 if (queviene.equals("contactos")) {
      		 procesa1();
      	 } else  {
      		 procesa2();
      	 } 
}
		
   
    private void procesa1()
    {
    	 final String texto="Se parte de mi red de seguridad, descarga SOS America en Android: http://tinyurl.com/qf8cksh o en IOS: https://goo.gl/U6Crc7 o buscalo en tu store.";
         try {
        	 	Bundle bundle = getIntent().getExtras();
 	         	SmsManager smsManager = SmsManager.getDefault();
 	         	smsManager.sendTextMessage(bundle.getString("newfono"), null, texto, null, null);
 	      } catch (Exception e) {
 	    	  	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(gestionador.this);
 	    	  	alertDialogBuilder.setTitle("Aviso");
 	    	  	alertDialogBuilder.setMessage("SMS no enviado, Por favor intentelo nuevamente.")
 	    	  	.setCancelable(false)
 	    	  	.setPositiveButton("Volver",new DialogInterface.OnClickListener(){
 	    	  		public void onClick(DialogInterface dialog, int id){
 	    	  			volver();	
 	    	  		}
 	    	  		});

      			AlertDialog alert = alertDialogBuilder.create();
      			alert.setIcon(R.drawable.ic_launcher);
      			alert.show();
 	         e.printStackTrace();
 	      }
 	         variable=0;
         
 	         Thread th1 = new Thread(new Runnable() {
 	             @Override
 	             public void run() {
 	            	 try {
 	            		Bundle bundle = getIntent().getExtras();
 	                    HttpClient mClient = new DefaultHttpClient();
 	                    StringBuilder sb=new StringBuilder(chequeo+"grabador.php");
 	                    HttpPost mpost = new HttpPost(sb.toString());
 	                    List<BasicNameValuePair> nameValuepairs = new ArrayList<BasicNameValuePair>(2);
 	                    nameValuepairs.add(new BasicNameValuePair("imei",deviceid));
 	                    nameValuepairs.add(new BasicNameValuePair("numero",bundle.getString("newfono")));
 	                    mpost.setEntity(new UrlEncodedFormEntity(nameValuepairs));
 	                    HttpResponse responce = mClient.execute(mpost);
 	                } catch (Exception e) {
 	                    //Si se produce un error, lo mostramos
 	                }

 	             }
 	            }); 
 	      th1.start();
 	   
     	 	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(gestionador.this);
 	        		alertDialogBuilder.setTitle("Aviso");
 	        		alertDialogBuilder.setMessage("La invitacion ha sido enviada")
 	        		.setCancelable(false)
 	        		.setPositiveButton("Volver",new DialogInterface.OnClickListener(){
 	        		public void onClick(DialogInterface dialog, int id){
 	        			volver();	
 	        			}
 	        		});

 	        			AlertDialog alert = alertDialogBuilder.create();
 	        			alert.setIcon(R.drawable.ic_launcher);
 	        			alert.show();
     
    } 
    
        private void procesa2()
    {
         
 	   
     	 	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(gestionador.this);
 	        		alertDialogBuilder.setTitle("Aviso");
 	        		alertDialogBuilder.setMessage("Vuelva al inicio")
 	        		.setCancelable(false)
 	        		.setPositiveButton("Volver",new DialogInterface.OnClickListener(){
 	        		public void onClick(DialogInterface dialog, int id){
 	        			volver();	
 	        			}
 	        		});

 	        			AlertDialog alert = alertDialogBuilder.create();
 	        			alert.setIcon(R.drawable.ic_launcher);
 	        			alert.show();
     
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
        
    private void volver()
    {
        Intent i221 = new Intent(this, contactos.class );
        startActivity(i221);	
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
           //     return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}