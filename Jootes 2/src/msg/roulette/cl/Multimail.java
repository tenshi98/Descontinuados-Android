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

import static msg.roulette.cl.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static msg.roulette.cl.CommonUtilities.EXTRA_MESSAGE;
import static msg.roulette.cl.CommonUtilities.SENDER_ID;
import static msg.roulette.cl.CommonUtilities.SERVER_URL;
import msg.roulette.cl.R;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Main UI for the demo app.
 */
public class Multimail extends Activity {

    TextView mDisplay;
    AsyncTask<Void, Void, Void> mRegisterTask;
    private static String str;
    @Override
    public void onCreate(Bundle savedInstanceState) {
 
           	
        super.onCreate(savedInstanceState);
//        checkNotNull(SERVER_URL, "SERVER_URL");
//        checkNotNull(SENDER_ID, "SENDER_ID");
        GCMRegistrar.checkDevice(this);

        GCMRegistrar.checkManifest(this);
        setContentView(R.layout.main);
        mDisplay = (TextView) findViewById(R.id.display);
//        registerReceiver(mHandleMessageReceiver,
//        new IntentFilter(DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);
        
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
        
        
        if (regId.equals("")) {
        	
        		GCMRegistrar.register(this, SENDER_ID, str);


        	
        } else {
            // EL DISPOSITIVO YA ESTA REGISTRADO.

            if (GCMRegistrar.isRegisteredOnServer(this)) {
            } else {
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        boolean registered =
                                ServerUtilities.register(context, regId, str);
                           if (!registered) {
                            GCMRegistrar.unregister(context);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

        case R.id.ingresar_datos:
            Intent i0 = new Intent(this, ingreso.class );
            startActivity(i0);
            return true;
        case R.id.ver_notificaciones:
            Intent i = new Intent(this, iralweb.class );
            startActivity(i);
        case R.id.options_llamar:
            Intent i1 = new Intent(this, llamadas.class );
            startActivity(i1);
            return true;
            case R.id.options_register:
                GCMRegistrar.register(this, SENDER_ID);
                return true;
            case R.id.options_unregister:
                GCMRegistrar.unregister(this);
                return true;
            case R.id.options_exit:
            	System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/

  /*  
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        unregisterReceiver(mHandleMessageReceiver);
        GCMRegistrar.onDestroy(this);
        super.onDestroy();
    }

    private void checkNotNull(Object reference, String name) {
        if (reference == null) {
            throw new NullPointerException(
                    getString(R.string.error_config, name));
        }
    }

    private final BroadcastReceiver mHandleMessageReceiver =
            new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            mDisplay.append(newMessage + "\n");
        }
    };
    */
    
}