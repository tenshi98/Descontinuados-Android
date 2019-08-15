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
package sosamerica.sosclick.cl;

import android.content.Context;
import android.content.Intent;

/**
 * Helper class providing methods and constants common to other classes in the
 * app.
 */
public final class CommonUtilities {

	
    static final String SERVER_URL = "http://sosamerica.sosclick.cl/notificaciones";
    
    static final String SERVER_URL_DATOS = "http://sosamerica.sosclick.cl/notificaciones/notificaciones.php?id=";

    static final String SERVER_URL_ALERTAS = "http://sosamerica.sosclick.cl/app/alertas.php?id=";
    
    static final String SERVER_LLAMADAS = "http://sosamerica.sosclick.cl/app/gracias_cel_01.php";
    
    static final String PERFIL = "http://sosamerica.sosclick.cl/app/usr_perfil.php?id=";

    static final String DONDE = "http://sosamerica.sosclick.cl/app/donde.php?id=";
    
    static final String AQUI = "http://sosamerica.sosclick.cl/app/ubicacion.php?id=";
    
    static final String CONTACTOS = "http://sosamerica.sosclick.cl/app/invitaciones.php?id=";   
    
    static final String chequeo = "http://sosamerica.sosclick.cl/app/";    
    

    /**
     * Google API project id registered to use GCM.
     */
    static final String SENDER_ID = "512898463167";
    static final String GOOGLE_PROJECT_ID = "512898463167";
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM_sos";

    /**
     * Intent used to display a message in the screen.
     */
    static final String DISPLAY_MESSAGE_ACTION ="sosamerica.sosclick.cl.DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    static final String EXTRA_MESSAGE = "message";
    
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
    
}
