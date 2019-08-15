package info.androidhive.loginandregistration.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import info.androidhive.loginandregistration.R;
import java.util.HashMap;
import java.util.Map;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.app.ServiceLocation;
import info.androidhive.loginandregistration.helper.DB_alertas;
import info.androidhive.loginandregistration.helper.DB_contactos;
import info.androidhive.loginandregistration.helper.DB_eventos;
import info.androidhive.loginandregistration.helper.DB_redes;
import info.androidhive.loginandregistration.helper.DB_usuarios;
import info.androidhive.loginandregistration.helper.DB_zonas;
import info.androidhive.loginandregistration.helper.DB_notificaciones;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.model.Alert;
import info.androidhive.loginandregistration.model.Contact;
import info.androidhive.loginandregistration.model.Eventos;
import info.androidhive.loginandregistration.model.Notifications;



public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {


	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;

	//Nombre en el menu
	private TextView txtappName;
	private TextView txtuserName;
	private DB_usuarios dataUsuarios;
	private DB_contactos dataContactos;
	private DB_zonas dataZonas;
	private DB_alertas dataAlertas;
	private DB_notificaciones dataNotificaciones;
	private DB_redes dataRedes;
	private DB_eventos dataEventos;
	private String passData;
	private JSONArray notificaciones = null;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.MyMaterialTheme);
		setContentView(R.layout.activity_main);


		txtappName = (TextView) findViewById(R.id.appName);
		txtuserName = (TextView) findViewById(R.id.userName);
		// SqLite database handler
		dataUsuarios         = new DB_usuarios(getApplicationContext());
		dataContactos        = new DB_contactos(getApplicationContext());
		dataZonas            = new DB_zonas(getApplicationContext());
		dataAlertas          = new DB_alertas(getApplicationContext());
		dataNotificaciones   = new DB_notificaciones(getApplicationContext());
		dataRedes            = new DB_redes(getApplicationContext());
		dataEventos          = new DB_eventos(getApplicationContext());


		// Fetching user details from SQLite
		HashMap<String, String> user = dataUsuarios.getUserDetails();

		String app_name = getString(R.string.app_name);
		String app_user = user.get("email");

		// Displaying the user details on the screen
		txtappName.setText(app_name);
		txtuserName.setText(app_user);


		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		drawerFragment = (FragmentDrawer)
				getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(this);


		//Se define con que pantalla se inicia
		String menuFragment = getIntent().getStringExtra("Notificaciones");
		//Si no existe una notificacion que redirija a otra parte, se comienza con la pantalla principal
		if (menuFragment != null){
			if (menuFragment.equals("viewNotificaciones")){

				Fragment fragment = new View_Notifications_Fragment();
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.container_body, fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		//Pantalla principal
		}else{
			displayView(0);
		}


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {

			//logoutUser();
			//Redirijo al fragment de cierre de sesion
			Fragment fragment = null;
			String title = null;
			fragment = new View_Exit_Fragment();
			title = getString(R.string.title_exit);

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment);
			fragmentTransaction.commit();

			// set the toolbar title
			getSupportActionBar().setTitle(title);

			return true;

		}



		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDrawerItemSelected(View view, int position) {
		displayView(position);
	}

	//Genero el menu
	private void displayView(int position) {
		Fragment fragment = null;

		switch (position) {
			case 0: fragment = new View_Home_Fragment();               break;
			case 1: fragment = new View_SosAmerica_Fragment();         break;
			case 2: fragment = new View_SosJoven_Fragment();           break;
			case 3: fragment = new View_Contacts_Fragment();           break;
			case 4: fragment = new View_Redes_Fragment();              break;
			case 5: fragment = new View_Tutorial_Fragment();           break;
			case 6: fragment = new View_Perfil_Fragment();             break;
			case 7: fragment = new View_MyAlarms_Fragment();           break;
			case 8: fragment = new View_Notifications_Fragment();      break;


			default: break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment);
			fragmentTransaction.commit();

		}
	}



	/**********************************************************************************************/
	/*                                 Modulo gestion de sesiones                                 */
	/**********************************************************************************************/
	//Cierra la sesion
	public void logoutUser() {

		//Cierro la sesion del usuario
		SessionManager session = new SessionManager(this);
		session.setLogin(false);

		//elimino los datos del usuario que estan almacenados en el celular
		dataUsuarios.deleteUsers();
		//elimino los datos de los contactos en el celular
		dataContactos.deleteAllContact();
		//elimino los datos de los mapas
		dataZonas.deleteAllZonas();
		//elimino las alertas
		dataAlertas.deleteAllAlert();
		//elimino las notificaciones
		dataNotificaciones.deleteAllNoti();
		//elimino las redes
		dataRedes.deleteAllRedes();
		//elimino todos los eventos
		dataEventos.deleteAllEvent();

		//envio a la pantalla de login
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
		//getApplicationContext().finish();

	}

	/**********************************************************************************************/
	/*                        Modulo para pasar informacion entre fragments                       */
	/**********************************************************************************************/
	//@Override
	public void passDataActivity(String position) {
		//Do something with the position value passed back
		passData = position;

	}
	public String getDataActivity() {

		return passData;

	}

	/**********************************************************************************************/
	/*                          Modulo para la modificacion de usuarios                           */
	/**********************************************************************************************/
	//Permite la modificacion del usuario
	public void  modUser(final String idCliente, final String int_email, final String  int_nombre,final String  int_rut,final String  int_fnacimiento,
						 final String int_direccion,final String  int_fono1,final String int_fono2,final String  int_departamento,final String  int_provincia,
						 final String  int_distrito,final String  fcreacion,final String  factualizacion) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {

			StringRequest strReq = new StringRequest(Request.Method.POST,
					AppConfig.URL_USER_UPDATE, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {


					try {
						JSONObject jObj = new JSONObject(response);
						boolean error = jObj.getBoolean("error");

						// Check for error node in json
						if (!error) {


							// Obtengo la id de la tabla del usuario
							HashMap<String, String> user = dataUsuarios.getUserDetails();
							String idUser = user.get("idUser");

							//actualizo los datos
							dataUsuarios.updateUser(idUser, idCliente, int_email, int_nombre, int_rut, int_fnacimiento, int_direccion, int_fono1, int_fono2, int_departamento,
									int_provincia, int_distrito, fcreacion, factualizacion);
							Toast.makeText(getApplicationContext(),"Datos actualizados correctamente",Toast.LENGTH_SHORT).show();

							Fragment fragment = null;
							fragment = new View_Perfil_Fragment();

							FragmentManager fragmentManager = getSupportFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
							fragmentTransaction.replace(R.id.container_body, fragment);
							fragmentTransaction.commit();

						} else {
							// Error in login. Get the error message
							String errorMsg = jObj.getString("error_msg");
							Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
							//logerror = errorMsg;
						}
					} catch (JSONException e) {
						// JSON error
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
					}

				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					// Log.e(TAG, "Login Error: " + error.getMessage());
					//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
					String statusCode = String.valueOf(error.networkResponse.statusCode);
					Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();

				}
			}) {

				@Override
				protected Map<String, String> getParams() {
					// Posting parameters to login url
					Map<String, String> params = new HashMap<String, String>();
					params.put("idCliente", idCliente);
					params.put("email", int_email);
					params.put("Nombre", int_nombre);
					params.put("Rut", int_rut);
					params.put("fNacimiento", int_fnacimiento);
					params.put("Departamento", int_departamento);
					params.put("Provincia", int_provincia);
					params.put("Distrito", int_distrito);
					params.put("Direccion", int_direccion);
					params.put("Fono1", int_fono1);
					params.put("Fono2", int_fono2);

					return params;
				}

			};

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}




	}


	/**********************************************************************************************/
	/*                          Modulo para la creacion de un contacto                            */
	/**********************************************************************************************/
	//Se agrega nuevo contacto
	public void  addContact(final String idCliente, final String phoneName, final String phoneNumber, final String tipoContacto) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";


		try {
		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_CONTACT_INSERT, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				//Log.d(TAG, "Login Response: " + response.toString());
				//hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {

						//obtengo la respuesta del servidor relacionada al contacto, si existe o no
						String Estado     = jObj.getString("contact_estado");
						String gsm        = jObj.getString("GSM");
						String idcliente  = jObj.getString("idClienteMain");

						//se guardan los contactos
						String numero = phoneNumber;
						numero = numero.replace("+569", "");
						numero = numero.replace("569", "");
						numero = numero.replace("+", "");
						numero = numero.replace("*", "");
						numero = numero.replace(" ", "");


						dataContactos.addContact(new Contact(phoneName, numero, Estado, gsm, idcliente, tipoContacto));

						// se muestra mensaje
						Toast.makeText(getApplicationContext(),"Usuario registrado correctamente",Toast.LENGTH_SHORT).show();

						//obtengo la respuesta del servidor y condiciono si muestro la opcion invitar
						if(Estado.equals("Registrado")){
							// se muestra mensaje
							Toast.makeText(getApplicationContext(),"Este usuario ya esta registrado dentro de nuestro sistema",Toast.LENGTH_SHORT).show();
						}else{
							//se muestra la ventana con las aplicaciones para compartir
							Compartir(phoneName, phoneNumber);
						}


						Fragment fragment = null;
						fragment = new View_Contacts_Fragment();

						FragmentManager fragmentManager = getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.replace(R.id.container_body, fragment);
						fragmentTransaction.commit();

					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
						//logerror = errorMsg;
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e(TAG, "Login Error: " + error.getMessage());
				//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
				String statusCode = String.valueOf(error.networkResponse.statusCode);
				Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();


			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("idCliente", idCliente);
				params.put("Nombre", phoneName);
				params.put("Fono", phoneNumber);
				params.put("TipoContacto", tipoContacto);


				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}
	}

	/**********************************************************************************************/
	/*                          Modulo para la eliminacion de un contacto                            */
	/**********************************************************************************************/
	//Se elimina contacto
	public void  delContact(final String idCliente,final int idContacto,final String Fono) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_CONTACT_DELETE, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				//Log.d(TAG, "Login Response: " + response.toString());
				//hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {

						dataContactos.deleteContact(idContacto);

						Toast.makeText(getApplicationContext(),"Datos eliminados correctamente",Toast.LENGTH_SHORT).show();

						Fragment fragment = null;
						fragment = new View_Contacts_Fragment();

						FragmentManager fragmentManager = getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.replace(R.id.container_body, fragment);
						fragmentTransaction.commit();

					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
						//logerror = errorMsg;
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e(TAG, "Login Error: " + error.getMessage());
				//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
				String statusCode = String.valueOf(error.networkResponse.statusCode);
				Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();


			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("idCliente", idCliente);
				params.put("Fono", Fono);

				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}
	}


	/**********************************************************************************************/
	/*                         Modulo que abre la ventana para compartir                          */
	/**********************************************************************************************/
	//al presionar boton compartir, envia algunos datos a las distintas aplicaciones
	public void Compartir(String phoneName,String phoneNumber){
		Intent intentCompartir = new Intent(Intent.ACTION_SEND);
		intentCompartir.setType("text/plain");
		intentCompartir.putExtra(Intent.EXTRA_SUBJECT, phoneNumber);
		intentCompartir.putExtra(Intent.EXTRA_TEXT,"Comparte nuestra aplicacion de seguridad con tus amigos");
		startActivity(Intent.createChooser(intentCompartir, "Comparte App Seguridad"));
	}

	/**********************************************************************************************/
	/*                     Modulo para establecer el servicio de segumiento                       */
	/**********************************************************************************************/
	public void seguimiento(int tipo) {


		int REQUEST_CODE=234324243;// Unique ID

		if (tipo == 1) {

			//Mensaje
			Toast.makeText(getApplicationContext(), "Seguimiento Iniciado", Toast.LENGTH_SHORT).show();
			//tarea
			Intent intent = new Intent(MainActivity.this, ServiceLocation.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (10 * 1000), pendingIntent);


		}else if (tipo == 2){
			//mensaje
			Toast.makeText(getApplicationContext(), "Seguimiento finalizado", Toast.LENGTH_SHORT).show();
			//tarea
			Intent intent = new Intent(MainActivity.this, ServiceLocation.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);


		}

	}

	public void passAlarmIsOn(String position) {
		//Do something with the position value passed back
		passData = position;

	}
	public String getAlarmIsOn() {

		if(passData == null){
			passData = "Off";
		}
		return passData;

	}

	/**********************************************************************************************/
	/*                        Modulo para setear el titulo de una ventana                         */
	/**********************************************************************************************/
	//permite cambiar el titulo del action bar desde cualquier sitio
	public void setActionBarTitle(String title) {
		getSupportActionBar().setTitle(title);
	}

	/**********************************************************************************************/
	/*                          Modulo para la creacion de una Alarma                             */
	/**********************************************************************************************/
	//Se crea una alarma
	public void  addAlarm(final String idCliente, final String tipo, final String subtipo, final String longitud, final String latitud ) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_ALERTAS, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				//Log.d(TAG, "Login Response: " + response.toString());
				//hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {

						//obtengo la respuesta del servidor relacionada al contacto, si existe o no
						String fecha        = jObj.getString("fecha");
						String hora         = jObj.getString("hora");
						String longitud     = jObj.getString("longitud");
						String latitud      = jObj.getString("latitud");
						String tipo_alerta  = jObj.getString("tipo_alerta");

						//se guardan los contactos
						//dataAlertas.addAlert(new Alert(fecha, hora, longitud, latitud, tipo_alerta));
						dataAlertas.addAlert(new Alert(fecha, hora, latitud, longitud, tipo_alerta));

						// se muestra mensaje
						Toast.makeText(getApplicationContext(),"Alerta Generada correctamente",Toast.LENGTH_SHORT).show();

						//Redirijo
						Fragment fragment = null;
						fragment = new View_Home_Fragment();

						FragmentManager fragmentManager = getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.replace(R.id.container_body, fragment);
						fragmentTransaction.commit();

					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
						//logerror = errorMsg;
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e(TAG, "Login Error: " + error.getMessage());
				//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
				String statusCode = String.valueOf(error.networkResponse.statusCode);
				Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();

				String ASD = "idCliente="+idCliente+"-tipo="+tipo+"-subtipo"+subtipo+"-longitud="+longitud+"-latitud"+latitud;
				Toast.makeText(getApplicationContext(), "Error de conexion: " + ASD, Toast.LENGTH_LONG).show();

			}
		}) {


			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("idCliente", idCliente);
				params.put("idTipoAlerta", tipo);
				params.put("idSubTipoAlerta", subtipo);
				params.put("Longitud", longitud);
				params.put("Latitud", latitud);


				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}

	}

	/**********************************************************************************************/
	/*                     Modulo para la creacion de una Alarma de SOSJoven                      */
	/**********************************************************************************************/
	//Se crea una alarma
	public void  addAlarmSosJoven(final String idCliente, final String tipo, final String subtipo, final String longitud, final String latitud ) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_ALERTAS_JOVEN, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				//Log.d(TAG, "Login Response: " + response.toString());
				//hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {

						//obtengo la respuesta del servidor relacionada al contacto, si existe o no
						String fecha        = jObj.getString("fecha");
						String hora         = jObj.getString("hora");
						String longitud     = jObj.getString("longitud");
						String latitud      = jObj.getString("latitud");
						String tipo_alerta  = jObj.getString("tipo_alerta");

						//se guardan los contactos
						//dataAlertas.addAlert(new Alert(fecha, hora, longitud, latitud, tipo_alerta));
						dataAlertas.addAlert(new Alert(fecha, hora, latitud, longitud, tipo_alerta));

						// se muestra mensaje
						Toast.makeText(getApplicationContext(),"Alerta Generada correctamente",Toast.LENGTH_SHORT).show();

						//Redirijo
						Fragment fragment = null;
						fragment = new View_SosJoven_Fragment();

						FragmentManager fragmentManager = getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.replace(R.id.container_body, fragment);
						fragmentTransaction.commit();

					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
						//logerror = errorMsg;
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e(TAG, "Login Error: " + error.getMessage());
				//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
				String statusCode = String.valueOf(error.networkResponse.statusCode);
				Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();

				String ASD = "idCliente="+idCliente+"-tipo="+tipo+"-subtipo"+subtipo+"-longitud="+longitud+"-latitud"+latitud;
				Toast.makeText(getApplicationContext(), "Error de conexion: " + ASD, Toast.LENGTH_LONG).show();

			}
		}) {


			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("idCliente", idCliente);
				params.put("idTipoAlerta", tipo);
				params.put("idSubTipoAlerta", subtipo);
				params.put("Longitud", longitud);
				params.put("Latitud", latitud);


				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}
	}

	/**********************************************************************************************/
	/*                                 Modulo para borrar la Alarma                               */
	/**********************************************************************************************/
	//se borra una alarma
	public void delAlarm() {

		//elimino las alertas
		dataAlertas.deleteAllAlert();

		//refresco la pantalla
		Fragment fragment = null;
		fragment = new View_MyAlarms_Fragment();

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.container_body, fragment);
		fragmentTransaction.commit();


	}
	/**********************************************************************************************/
	/*                                 Modulo borra la notrificacion                              */
	/**********************************************************************************************/
	//se borra la notificacion
	public void delNoti() {

		//elimino las alertas
		dataNotificaciones.deleteAllNoti();

		//refresco la pantalla
		Fragment fragment = null;
		fragment = new View_Notifications_Fragment();

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.container_body, fragment);
		fragmentTransaction.commit();


	}


	/**********************************************************************************************/
	/*                          Modulo para actualizar las notificaciones                         */
	/**********************************************************************************************/
	//Se actualiza una notificacion
	public void  updateNoti(final String idCliente) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_NOTIFICATIONS, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				//Log.d(TAG, "Login Response: " + response.toString());
				//hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {

						//Obtengo los numeros telefonicos del usuario
						try {

							notificaciones = jObj.getJSONArray("noti");
							for (int i = 0; i < notificaciones.length(); i++) {
								//obtengo el puntero para manejar el arreglo
								//String enteroString = Integer.toString(i);
								JSONObject sub = notificaciones.getJSONObject(i);

								//obtengo los datos del arreglo
								String fecha      = sub.getString("fecha");
								String hora       = sub.getString("hora");
								String longitud   = sub.getString("longitud");
								String latitud    = sub.getString("latitud");
								String tipo       = sub.getString("tipo");
								String fono       = sub.getString("fono");
								String idtipo     = sub.getString("idtipo");
								String texto      = sub.getString("texto");
								String web        = sub.getString("web");
								String idAlerta   = sub.getString("idAlerta");

								//agrego los contactos a la base interna
								dataNotificaciones.addNoti(new Notifications(fecha, hora, latitud, longitud, tipo, fono, idtipo, texto, web, idAlerta ));

							}

							// se muestra mensaje
							Toast.makeText(getApplicationContext(),"Notificaciones actualizadas",Toast.LENGTH_SHORT).show();

							//Redirijo
							Fragment fragment = null;
							fragment = new View_Notifications_Fragment();

							FragmentManager fragmentManager = getSupportFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
							fragmentTransaction.replace(R.id.container_body, fragment);
							fragmentTransaction.commit();


						} catch (JSONException e) {
							// JSON error
							e.printStackTrace();
							//Toast.makeText(getApplicationContext(), "Ninguna notificacion importada", Toast.LENGTH_LONG).show();
						}





					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
						//logerror = errorMsg;
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e(TAG, "Login Error: " + error.getMessage());
				//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
				String statusCode = String.valueOf(error.networkResponse.statusCode);
				Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();

			}
		}) {


			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("idCliente", idCliente);


				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}

	}

	/**********************************************************************************************/
	/*                          Modulo para solicitar la ubicacion                                */
	/**********************************************************************************************/
	//Se solicita la ubicacion
	public void  notificacion_unitaria(final String idCliente, final String tipo, final String subtipo, final String mensaje, final String destinatario, final String idClienteMain ) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_MAPA_PEDIR, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				//Log.d(TAG, "Login Response: " + response.toString());
				//hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {

						// se muestra mensaje
						Toast.makeText(getApplicationContext(),"Solicitud enviada correctamente",Toast.LENGTH_SHORT).show();

					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
						//logerror = errorMsg;
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e(TAG, "Login Error: " + error.getMessage());
				//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
				//String statusCode = String.valueOf(error.networkResponse.statusCode);
				//Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();

				//String ASD = "idCliente="+idCliente+"-tipo="+tipo+"-subtipo"+subtipo+"-longitud="+longitud+"-latitud"+latitud;
				//Toast.makeText(getApplicationContext(), "Error de conexion: " + ASD, Toast.LENGTH_LONG).show();

			}
		}) {


			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("idCliente", idCliente);
				params.put("idTipoAlerta", tipo);
				params.put("idSubTipoAlerta", subtipo);
				params.put("Mensaje", mensaje);
				params.put("Destinatario", destinatario);
				params.put("idClienteMain", idClienteMain);


				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}

	}

	/**********************************************************************************************/
	/*                          Modulo para la creacion de un evento                              */
	/**********************************************************************************************/
	//Se agrega nuevo evento
	public void  addEvent(final String idCliente, final String tipo, final String latitud, final String longitud ) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_EVENTO, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				//Log.d(TAG, "Login Response: " + response.toString());
				//hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {

						// se muestra mensaje
						Toast.makeText(getApplicationContext(),"Evento generado correctamente",Toast.LENGTH_SHORT).show();

						//Redirijo
						Fragment fragment = null;
						fragment = new View_Home_Fragment();

						FragmentManager fragmentManager = getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.replace(R.id.container_body, fragment);
						fragmentTransaction.commit();

					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
						//logerror = errorMsg;
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e(TAG, "Login Error: " + error.getMessage());
				//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
				String statusCode = String.valueOf(error.networkResponse.statusCode);
				Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();

				String ASD = "idCliente="+idCliente+"-tipo="+tipo+"-longitud="+longitud+"-latitud"+latitud;
				Toast.makeText(getApplicationContext(), "Error de conexion: " + ASD, Toast.LENGTH_LONG).show();

			}
		}) {


			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("idCliente", idCliente);
				params.put("idTipoEvento", tipo);
				params.put("Longitud", longitud);
				params.put("Latitud", latitud);


				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}

	}


	/**********************************************************************************************/
	/*                          Modulo para la modificacion de usuarios                           */
	/**********************************************************************************************/
	//Permite la modificacion del usuario
	public void  modUserLatLog(final String idCliente, final String Latitud, final String  Longitud) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_USER_UPDATE, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {


				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {

						// Obtengo la id de la tabla del usuario
						HashMap<String, String> user = dataUsuarios.getUserDetails();
						String idUser = user.get("idUser");

						//actualizo los datos
						dataUsuarios.updateUserLatLong(idUser, Latitud, Longitud);

					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
						//logerror = errorMsg;
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e(TAG, "Login Error: " + error.getMessage());
				//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
				String statusCode = String.valueOf(error.networkResponse.statusCode);
				Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();

			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("idCliente", idCliente);
				params.put("Latitud", Latitud);
				params.put("Longitud", Longitud);


				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}
	}

	/**********************************************************************************************/
	/*                          Modulo para solicitar la ubicacion                                */
	/**********************************************************************************************/
	//Se solicita la ubicacion
	public void  envioUbicacion(final String idCliente, final String idAlerta, final String longitud, final String latitud ) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
			StringRequest strReq = new StringRequest(Request.Method.POST,
					AppConfig.URL_ENVIO_UBICACION, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					//Log.d(TAG, "Login Response: " + response.toString());
					//hideDialog();

					try {
						JSONObject jObj = new JSONObject(response);
						boolean error = jObj.getBoolean("error");

						// Check for error node in json
						if (!error) {

							// se muestra mensaje
							Toast.makeText(getApplicationContext(),"Ubicacion enviada correctamente",Toast.LENGTH_SHORT).show();

						} else {
							// Error in login. Get the error message
							String errorMsg = jObj.getString("error_msg");
							Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
							//logerror = errorMsg;
						}
					} catch (JSONException e) {
						// JSON error
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
					}

				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					// Log.e(TAG, "Login Error: " + error.getMessage());
					//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
					//String statusCode = String.valueOf(error.networkResponse.statusCode);
					//Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();

					//String ASD = "idCliente="+idCliente+"-tipo="+tipo+"-subtipo"+subtipo+"-longitud="+longitud+"-latitud"+latitud;
					//Toast.makeText(getApplicationContext(), "Error de conexion: " + ASD, Toast.LENGTH_LONG).show();

				}
			}) {


				@Override
				protected Map<String, String> getParams() {
					// Posting parameters to login url
					Map<String, String> params = new HashMap<String, String>();
					params.put("idCliente", idCliente);
					params.put("idAlerta", idAlerta);
					params.put("longitud", longitud);
					params.put("latitud", latitud);

					return params;
				}

			};

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}

	}

	/**********************************************************************************************/
	/*                      Modulo para obtener eventos a mi alrededor                            */
	/**********************************************************************************************/
	//Se agrega nuevo contacto
	public void  searchEvent(final String sLatitud, final String sLongitud) {




		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
			StringRequest strReq = new StringRequest(Request.Method.POST,
					AppConfig.URL_PUNTOS, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					//Log.d(TAG, "Login Response: " + response.toString());
					//hideDialog();

					try {
						JSONObject jObj = new JSONObject(response);
						boolean error = jObj.getBoolean("error");

						// Check for error node in json
						if (!error) {

							//Obtengo los puntos
							try {

								JSONArray puntos = jObj.getJSONArray("puntos");



								//elimino todos los eventos
								dataEventos.deleteAllEvent();

								for (int i = 0; i < puntos.length(); i++) {

									//obtengo el puntero para manejar el arreglo
									JSONObject sub = puntos.getJSONObject(i);

									//obtengo los datos del arreglo
									String Fecha          = sub.getString("Fecha");
									String Hora           = sub.getString("Hora");
									String Longitud       = sub.getString("LongEvento");
									String Latitud        = sub.getString("LatEvento");
									String NombreCliente  = sub.getString("NombreCliente");
									String TipoEvento     = sub.getString("TipoEvento");



									//se agregan los nuevos eventos
									dataEventos.addEvent(new Eventos(Fecha, Hora, Latitud, Longitud, NombreCliente, TipoEvento));



								}
							} catch (JSONException e) {
								// JSON error
								e.printStackTrace();
								Toast.makeText(getApplicationContext(), "Ningun evento cercano a tu ubicacion", Toast.LENGTH_LONG).show();
							}


						} else {
							// Error in login. Get the error message
							String errorMsg = jObj.getString("error_msg");
							Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
							//logerror = errorMsg;
						}
					} catch (JSONException e) {
						// JSON error
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
					}

				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					// Log.e(TAG, "Login Error: " + error.getMessage());
					//Toast.makeText(getApplicationContext(), "Error de conexion: "+error.getMessage(), Toast.LENGTH_LONG).show();
					String statusCode = String.valueOf(error.networkResponse.statusCode);
					Toast.makeText(getApplicationContext(), "Error de conexion: "+statusCode, Toast.LENGTH_LONG).show();


				}
			}) {

				@Override
				protected Map<String, String> getParams() {
					// Posting parameters to login url
					Map<String, String> params = new HashMap<String, String>();
					params.put("Latitud", sLatitud);
					params.put("Longitud", sLongitud);

					return params;
				}

			};

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


		} catch (Exception e) {
			//verifica la conectividad y evita que la aplicacion deje de funcionar
			Toast.makeText(getApplicationContext(), "Error de conexion, intentelo mas tarde", Toast.LENGTH_LONG).show();
		}
	}









//fin del modulo
}
