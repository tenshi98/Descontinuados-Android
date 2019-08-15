package sosamerica.sosclick.cl;

import sosamerica.sosclick.cl.R;
import static sosamerica.sosclick.cl.CommonUtilities.displayMessage;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;


public class GCMNotificationIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	public static String mensaje;
	public static String imei_viene;
	public static String mensaje_completo;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
    public static String  paso;
    public static int  id_notif;
    public int contador;    
    
	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		 mensaje = intent.getExtras().getString("mensaje_viene");
		 mensaje_completo = intent.getExtras().getString("mensaje_viene");
	        Calendar calendario = new GregorianCalendar();
		       int ano = calendario.get(Calendar.YEAR);
		       int mes = calendario.get(Calendar.MONTH);
		       int dia = calendario.get(Calendar.DATE);
		       int hora =calendario.get(Calendar.HOUR_OF_DAY);
		       int minutos = calendario.get(Calendar.MINUTE);
		       int segundos = calendario.get(Calendar.SECOND);
		       id_notif = ano+mes+segundos+dia+hora+minutos+segundos;     
		       
		        //paso = intent.getExtras().getString("collapse_key");        
		      //  id_notif = Integer.parseInt(paso);
		        
		      //EVALUO EL TEXTO PARA UNA ALERTA INSISTENTE  
		        int esta = 0;
			    contador = 0;        
			    String sTexto = String.valueOf(mensaje) ;
			    String sTextoBuscado = "alerta de seguridad";
			    while (sTexto.indexOf(sTextoBuscado) > -1) {
			      sTexto = sTexto.substring(sTexto.indexOf(
			      sTextoBuscado)+sTextoBuscado.length(),sTexto.length());
			      esta++;
			    }    
			    if (esta>0) {
			    	contador=1;
			    }
			    
			    String sTexto2 = String.valueOf(mensaje) ;
		        esta=0;
			    String sTextoBuscado2 = "donde estas";
			    while (sTexto2.indexOf(sTextoBuscado2) > -1) {
			      sTexto2 = sTexto2.substring(sTexto2.indexOf(
			      sTextoBuscado2)+sTextoBuscado2.length(),sTexto2.length());
			      esta++;
			    }    
			    if (esta>0) {
			    	contador=2;
			    }
			    
			    String sTexto3 = String.valueOf(mensaje) ;	    
		        esta=0; 
			    String sTextoBuscado3 = "Mi ubicacion actual";
			    while (sTexto3.indexOf(sTextoBuscado3) > -1) {
			      sTexto3 = sTexto3.substring(sTexto3.indexOf(
			      sTextoBuscado3)+sTextoBuscado3.length(),sTexto3.length());
			      esta++;
			    }    
			    if (esta>0) {
			    	contador=3;
			    }
			    
			    String sTexto4 = String.valueOf(mensaje) ;	    
		        esta=0; 
			    String sTextoBuscado4 = "**.";
			    while (sTexto4.indexOf(sTextoBuscado4) > -1) {
			      sTexto4 = sTexto4.substring(sTexto4.indexOf(
			      sTextoBuscado4)+sTextoBuscado3.length(),sTexto4.length());
			      esta++;

			    }    
			    if (esta>0) {
			    	contador=3;
			    }
			    
			    //String sTexto0 = String.valueOf(intent.getExtras().getString("mensaje_viene"));	 
			    String sTexto0 = String.valueOf(mensaje) ;	
		        esta=0; 
			    String sTextoBuscado0 = "imei:";
			    while (sTexto0.indexOf(sTextoBuscado0) > -1) {
			      sTexto0 = sTexto0.substring(sTexto0.indexOf(
			      sTextoBuscado0)+sTextoBuscado0.length(),sTexto0.length());
			      esta++;
			    }    

			    
			    
			    if (esta>0) {
			    	String[] arreglo = intent.getExtras().getString("mensaje_viene").split("imei:");
			    	mensaje = arreglo[0];
			    	imei_viene = arreglo[1];
			    }			
			    if (contador == 3) {
			    	String paso = mensaje.replace("**.", "");
			    	String paso1 = paso.replace("ubicacion", "ubicación");
			    	String paso2 = paso1.replace("llego", "llegó");			    	
			    	mensaje=paso2;
			     }			    
			      //EVALUO EL TEXTO PARA UNA ALERTA INSISTENTE  			    
			    
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) { 
				sendNotification("Deleted messages on server: "+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				for (int i = 0; i < 3; i++) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
				}

				sendNotification(mensaje);
			}
		}

		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		String title = getString(R.string.app_name);
		
		
	      //EVALUO EL TEXTO PARA VER DONDE VA EL MENSAJE 
   	    if (contador == 0) {
   	    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, iralweb.class), 0);
   	    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
   	    	.setSmallIcon(R.drawable.ic_launcher)
   	    	.setContentTitle(title+ " Informa.")
   	    	.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
   	    	.setContentText(msg)
   	    	.setTicker(msg)
   	    	.setWhen(System.currentTimeMillis())
   	    	.setAutoCancel(true)
   	    	.setDefaults(Notification.DEFAULT_ALL)
		;
	    		mBuilder.setContentIntent(contentIntent);
   	    		mNotificationManager.notify(id_notif, mBuilder.build());
	    }    
   	    
   	    if (contador == 1) {
   	    	final int soundResId = R.raw.police;
   	        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"+ soundResId);
   	    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
			new Intent(this, ultimaalerta.class), 0);
   	    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
   	    	.setSmallIcon(R.drawable.ic_launcher)
   	    	.setContentTitle(title+ " Ultima Alerta.")
   	    	.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
   	    	.setContentText(msg)
   	    	.setTicker(msg)
   	    	.setWhen(System.currentTimeMillis())
   	    	.setAutoCancel(true)
   	    	.setSound(uri)
   	    	//.setDefaults(Notification.DEFAULT_ALL)
   	    ;
   	    		mBuilder.setContentIntent(contentIntent);
   	    		mNotificationManager.notify(id_notif, mBuilder.build());
	    }
   	    
   	    if (contador == 2) {
   	    	Intent notificationIntent = new Intent(this, ubicame.class);
   	    	notificationIntent.putExtra("datos", mensaje_completo);
   	    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
   	    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
   	    	.setSmallIcon(R.drawable.ic_launcher)
   	    	.setContentTitle(title+ " Ubicacion.")
   	    	.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
   	    	.setContentText(msg)
   	    	.setTicker(msg)
   	    	.setWhen(System.currentTimeMillis())
   	    	.setAutoCancel(true)
   	    	.setDefaults(Notification.DEFAULT_ALL)
		;
	    		mBuilder.setContentIntent(contentIntent);
   	    		mNotificationManager.notify(id_notif, mBuilder.build());
	    }
   	    
   	    if (contador == 3) {
   	    	
   	    	/*
Intent notificationIntent = new Intent(context, Main.class);
	notificationIntent.putExtra("item_id", "1001"); // <-- HERE I PUT THE EXTRA VALUE
	PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
   	    	 */
   	    	
   	    	Intent notificationIntent = new Intent(this, aqui.class);
   	    	notificationIntent.putExtra("datos", mensaje_completo);
   	    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
   	    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
   	    	.setSmallIcon(R.drawable.ic_launcher)
   	    	.setContentTitle(title+ " Mi ubicacion.")
   	    	.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
   	    	.setContentText(msg)
   	    	.setTicker(msg)
   	    	.setWhen(System.currentTimeMillis())
   	    	.setAutoCancel(true)
   	    	.setDefaults(Notification.DEFAULT_ALL)
		;
	    		mBuilder.setContentIntent(contentIntent);
   	    		mNotificationManager.notify(id_notif, mBuilder.build());
	    }   
   	    
   	    //EVALUO EL TEXTO PARA VER DONDE VA EL MENSAJE



	}
}
