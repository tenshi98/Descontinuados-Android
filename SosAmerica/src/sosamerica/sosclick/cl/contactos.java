package sosamerica.sosclick.cl;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import sosamerica.sosclick.cl.R;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings.Secure;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import static sosamerica.sosclick.cl.CommonUtilities.chequeo;

public class contactos extends Activity {
    private WebView perfil;

    public String mobilenumber;
    public Uri imageUri;
    public String deviceid;
    public String sb1;
    public String datosfinal;
    public String newfono;
    public int  variable;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        
    	TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() != null){
        	deviceid = tm.getDeviceId(); //Para telefonos moviles
         }
        else{
        	 deviceid = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); //Para tablets
         }
        
        
        
       
        String finalurl = "file:///android_asset/ui.xhtml";        
        perfil = (WebView) findViewById(R.id.perfil);
        perfil.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        perfil.getSettings().setBuiltInZoomControls(false);
        perfil.getSettings().setJavaScriptEnabled(true);
        perfil.addJavascriptInterface(new JavascriptManager(), "MainActivity");
        perfil.getSettings().setGeolocationEnabled(true);
        perfil.loadUrl(finalurl);	

		    	perfil.setWebViewClient(new WebViewClient() {
				    		
					      @Override
					 public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
					 if (url.indexOf("sms:") > -1){
						 if (variable==0) {
							 variable=1;
					    			final String texto="Se parte de mi red de seguridad, descarga SOS America en Android: http://tinyurl.com/qf8cksh o en IOS: https://goo.gl/U6Crc7 o buscalo en tu store.";
					    			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contactos.this);
					    			alertDialogBuilder.setTitle("Alerta");
					    			alertDialogBuilder.setMessage("Esta a punto de enviar el siguiente SMS '"+texto+"' desde su dispositivo, lo que significará el costo del envío de un sms, ¿desea enviar el mensaje?")
					    			.setCancelable(false)
					    			.setPositiveButton("Aceptar",new DialogInterface.OnClickListener(){
						        	public void onClick(DialogInterface dialog, int id){
						        	//ABRO EL INTENT DE GESTIONADOR
						        		String phoneNo = String.valueOf(url);
						        		phoneNo = phoneNo.replace("+519", "");
					        	        newfono = phoneNo.replace("sms:+519", "");
					        	      	Intent a = new Intent(contactos.this, gestionador.class);
					        	        a.putExtra("dedonde", "contactos");
					        	        a.putExtra("newfono", newfono);
					        	        a.putExtra("phoneNo", phoneNo);
					        	        startActivity(a);
					        	    //ABRO EL INTENT DE GESTIONADOR
						            }
						        });
						        alertDialogBuilder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
						            public void onClick(DialogInterface dialog, int id){
						                dialog.cancel();
						                variable=0;
						            }
						        });
						        AlertDialog alert = alertDialogBuilder.create();
						        alert.show();
					    			
	}
					    		    return true;
						 
					  }else {
					                return false;
					  }
					  }
				        });
  
        
    } 
    
       
    class JavascriptManager{

    	@JavascriptInterface
		public String getContacts(){
            return Escribircontacto();
    	}

    	
    }
      
    
    
    /* ************************************************************************************************************* */
    /*                                  Se escriben los contactos telefonicos                                        */    
    /* ************************************************************************************************************* */ 
    	public String Escribircontacto() {
            Cursor phoneNumCursor = getContentResolver().query(Phone.CONTENT_URI, null,null,null, Phone.DISPLAY_NAME + " ASC");
            phoneNumCursor.moveToFirst();
            StringBuffer data = new StringBuffer();
            
            try {

                HttpClient mClient = new DefaultHttpClient();
                StringBuilder sb=new StringBuilder(chequeo+"revisor.php");
                HttpPost mpost = new HttpPost(sb.toString());
                List nameValuepairs = new ArrayList(1);
                nameValuepairs.add(new BasicNameValuePair("imei",deviceid));
                mpost.setEntity(new UrlEncodedFormEntity(nameValuepairs));
                HttpResponse responce = mClient.execute(mpost);
                HttpEntity entity = responce.getEntity();
                BufferedReader buf = new BufferedReader(new InputStreamReader(entity.getContent()));
                StringBuilder sb1 = new StringBuilder();
                String line = null;
     
                while ((line = buf.readLine()) != null) {
                    sb1.append(line+"\r\n");
                }
                datosfinal=sb1.toString();
              
            } catch (Exception e) {
                //Si se produce un error, lo mostramos
                //Log.w(" error ", e.toString());
            }
  
  

  
  
 String numeros = datosfinal;
 String[] numerosComoArray = numeros.split(","); 
 String sms1=numerosComoArray[0];
 String sms2=numerosComoArray[1]; 
 String sms3=numerosComoArray[2];
 String sms4=numerosComoArray[3];
 String sms5=numerosComoArray[4];
 String contar1=numerosComoArray[5];
 String contar2=numerosComoArray[6];
 String contar3=numerosComoArray[7];
 String contar4=numerosComoArray[8];
 String contar5=numerosComoArray[9];




            
            /* ************************************************************************************************************* */             
            while (!phoneNumCursor.isAfterLast()){
              /*  String Number=phoneNumCursor.getString(phoneNumCursor.getColumnIndex(Phone.NUMBER));
                String Name2=phoneNumCursor.getString(phoneNumCursor.getColumnIndex(Phone.DISPLAY_NAME));
                String newString0 = Number.replace("+56", "");
                newString0 = Number.replace("+51", "");                
                String newString = newString0.replace(" ", "");
                String primero=newString.substring(0, 1);
                String newString22="";
                if (newString.length()>8) {
                	newString=newString.substring(1, 10);
                	
                }*/
            	
                String Number=phoneNumCursor.getString(phoneNumCursor.getColumnIndex(Phone.NUMBER));
                String Name2=phoneNumCursor.getString(phoneNumCursor.getColumnIndex(Phone.DISPLAY_NAME));
                String newString0 = Number.replace("+51", "");
                String newString1 = newString0.replace("+56", "");
                String newString = newString1.replace(" ", "");
                String primero=newString.substring(0, 1);
                if (newString.length()>9) {
                	newString=newString.substring(1, 10);
                }
                
                if (newString.length()>7) {
                	if (primero.equals("0") || primero.equals("1") ) {
                	//NO HACE NADA
                	} else {

                		if (sms1.equals(newString) || sms2.equals(newString) || sms3.equals(newString) || sms4.equals(newString) || sms5.equals(newString) ) {
                			int control=0;
                				if ( sms1.equals(newString) && contar1.equals("1") ) {
                					control=1;
                    			}else if ( sms2.equals(newString) && contar2.equals("1") ) {
                    				control=1;
                    			}else if ( sms3.equals(newString) &&  contar3.equals("1") ) {
                    				control=1;
                    			}else if ( sms4.equals(newString) &&  contar4.equals("1") ) {
                    				control=1;
                    			}else if ( sms5.equals(newString) &&  contar5.equals("1") ) {
                    				control=1;
                    			}else{
                    				control=0;
                    			}
                				
                			
                				if ( control==1 ) {
                					data.append("<tr><td><div class='fleft'><p class='contacto'>"+Name2+"<br/><span class='invitadoregistrado'>Invitado - Registrado</span></p></div></td></tr>");
                					control=0;
                				}else{
                					data.append("<tr><td><div class='fleft'><p class='contacto'>"+Name2+"<br/><span class='registrado'>Invitado</span></p></div><div class='fright'><a class='btn_list btn-app' href='sms:+519"+newString+"'>"+newString+"</a></div></td></tr>");
                				}
                					
                 		}else{
                		       data.append("<tr><td><div class='fleft'><p class='contacto'>"+Name2+"</p></div><div class='fright'><a class='btn_list btn-app' href='sms:+519"+newString+"'>"+newString+"</a></div></td></tr>");                		
                		}
                	}
                }
            phoneNumCursor.moveToNext();
            }

    		return data.toString();	      	
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
             //   return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}