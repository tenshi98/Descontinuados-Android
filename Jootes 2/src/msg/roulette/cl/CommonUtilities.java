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




import android.content.Context;
import android.content.Intent;



/**
 * Helper class providing methods and constants common to other classes in the
 * app.
 */
public final class CommonUtilities {

	//llamar a la pagina de notificaciones
    static final String SERVER_URL = "http://www.jootes.cl/notificaciones";
    //se vuelve a llamar a la pagina de notificaciones
    //static final String SERVER_URL_DATOS = "http://www.jootes.cl/notificaciones.php";
    static final String SERVER_URL_DATOS = "http://www.jootes.cl/app/mensajes.php";
    //se llama a la pagina de mensajes
    static final String SERVER_LLAMADAS = "http://www.jootes.cl/app/roulette.php";
    //se llama a la pagina con los datos del usuario, con la posibilidad de editarlos
    static final String SERVER_PERFIL = "http://www.jootes.cl/app/usr_perfil.php";
    //se llama a la pagina con los datos del usuario, con la posibilidad de editarlos
    static final String SERVER_CONVER = "http://www.jootes.cl/app/usr_conver.php";
    //pagina para desactivar el perfil del usuario
    static final String SERVER_EXIT = "http://www.jootes.cl/app/usr_exit.php";
    /**
     * Google API project id registered to use GCM.
     */
    static final String SENDER_ID = "456382220500";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM_sos";

    /**
     * Intent used to display a message in the screen.
     */
    static final String DISPLAY_MESSAGE_ACTION =
            "msg.multimail.cl.DISPLAY_MESSAGE";

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
