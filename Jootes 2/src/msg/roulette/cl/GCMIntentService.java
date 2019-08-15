/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package msg.roulette.cl;

import static msg.roulette.cl.CommonUtilities.SENDER_ID;
import static msg.roulette.cl.CommonUtilities.displayMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import msg.roulette.cl.R;
import msg.roulette.cl.R.drawable;
import msg.roulette.cl.R.string;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;




/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

 //   @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";
    //public static double latitude;
    //public static double longitude;
    public String best;//Para el nombre del proveedor que vamos a utilizar.
    public static String  mensaje;
    private static String str;

    public GCMIntentService() {
        super(SENDER_ID);

    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, getString(R.string.gcm_registered));
       /* 
        try
		{
			BufferedReader fin = 
					new BufferedReader(
							new InputStreamReader(
									openFileInput("datos.txt")));

			 str = fin.readLine();	
		}
	catch (Exception ex)
	{

	}
	*/
        
        str = "";
        ServerUtilities.register(context, registrationId, str);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        displayMessage(context, getString(R.string.gcm_unregistered));
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        } else {

        }
    }

    // AQUI SE ENVIA EL message 
    
    @Override
    protected void onMessage(Context context, Intent intent) {
      
        
        String message = intent.getExtras().getString("mensaje_viene");
        mensaje = intent.getExtras().getString("mensaje_viene");
        // Show message field of message
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);

    }

    // AQUI SE ENVIA EL message 
    
    @Override
    protected void onDeletedMessages(Context context, int total) {
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
        //notificarMensaje(context, message);
        
    }

    @Override
    public void onError(Context context, String errorId) {
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    

    private static void generateNotification(Context context, String message) {

    	NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);    
    	
    	int icon = R.drawable.ic_stat_gcm;
        long hora = System.currentTimeMillis();
        
        Notification notification = new Notification(icon, message, hora);
        long[] vibrate = {100,100,200,300};
        notification.vibrate = vibrate;
        
        String title = context.getString(R.string.app_name);
        
      //Agregando sonido
        notification.defaults |= Notification.DEFAULT_SOUND;
        //Agregando vibración
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        
        Intent notificationIntent = new Intent(context, iralweb.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);        
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);


    }

    
   
    
    }
    



