package info.androidhive.jootes.activity;


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
import info.androidhive.jootes.R;
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
import info.androidhive.jootes.app.AppConfig;
import info.androidhive.jootes.app.AppController;
import info.androidhive.jootes.helper.DB_usuarios;
import info.androidhive.jootes.helper.DB_notificaciones;
import info.androidhive.jootes.helper.SessionManager;
import info.androidhive.jootes.model.Notifications;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {


	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;

	//Nombre en el menu
	private TextView txtappName;
	private TextView txtuserName;
	private DB_usuarios dataUsuarios;
	private DB_notificaciones dataNotificaciones;
	private String passData;
	private String phoneData;
	private JSONArray notificaciones = null;
	public String var_fonocliente = "";

	public MenuItem menu1;
	Menu menuPrincipal;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.MyMaterialTheme);
		setContentView(R.layout.activity_main);


		txtappName = (TextView) findViewById(R.id.appName);
		txtuserName = (TextView) findViewById(R.id.userName);
		// SqLite database handler
		dataUsuarios         = new DB_usuarios(getApplicationContext());
		dataNotificaciones   = new DB_notificaciones(getApplicationContext());



		// Fetching user details from SQLite
		HashMap<String, String> user = dataUsuarios.getUserDetails();

		String app_name   = getString(R.string.app_name);
		String app_user   = user.get("email");
		var_fonocliente   = user.get("Fono1");

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

		menu1 = menu.findItem(R.id.action_search);
		menu1.setVisible(false);
		//if(interruptor!=0){
			//muestro un item en especifico
			//menu.findItem(R.id.action_search).setVisible(true);
		//}
		menuPrincipal = menu;

		return true;
	}




	public void toggleOptions(boolean isChecked) {


		if(menu1 != null){
			menu1.setVisible(isChecked);
		}else{
			Toast.makeText(getApplicationContext(), "Menu no existe", Toast.LENGTH_SHORT).show();
		}

	};





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

		if(id == R.id.action_search){

			String var_fonoContacto = getPhone();

			if (!var_fonocliente.isEmpty() && !var_fonoContacto.isEmpty()  ) {

				//enviar la notificacion al otro usuario y redirigir en caso de ok
				llamadaChat(var_fonocliente, var_fonoContacto);
				return true;

			} else {
				// mensaje de error en caso de no completar los datos
				Toast.makeText(getApplicationContext(), "No existe el numero telefonico de usted o del destino", Toast.LENGTH_SHORT).show();
				return true;

			}
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
			case 1: fragment = new View_Perfil_Fragment();             break;
			case 2: fragment = new View_Notifications_Fragment();      break;

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
		//elimino las notificaciones
		dataNotificaciones.deleteAllNoti();

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
	/*                        Modulo para pasar informacion entre fragments                       */
	/**********************************************************************************************/
	//@Override
	public void passPhone(String position) {
		//Do something with the position value passed back
		phoneData = position;

	}
	public String getPhone() {

		return phoneData;

	}

	/**********************************************************************************************/
	/*                          Modulo para la modificacion de usuarios                           */
	/**********************************************************************************************/
	//Permite la modificacion del usuario
	public void  modUser(final String idCliente, final String int_email, final String  int_nombre,final String  int_rut,final String  int_fnacimiento,
						 final String int_direccion,final String  int_fono1,final String int_fono2,final String  int_departamento,final String  int_provincia,
						 final String  int_distrito,final String  fcreacion,final String  factualizacion,final String  sexo,final String  edad) {
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
									int_provincia, int_distrito, fcreacion, factualizacion, sexo, edad);
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
					params.put("Sexo", sexo);
					params.put("Edad", edad);

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
	/*                        Modulo para setear el titulo de una ventana                         */
	/**********************************************************************************************/
	//permite cambiar el titulo del action bar desde cualquier sitio
	public void setActionBarTitle(String title) {
		getSupportActionBar().setTitle(title);
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
									String mensaje   = sub.getString("mensaje");
									String texto     = sub.getString("texto");
									String Fecha     = sub.getString("Fecha");
									String Hora      = sub.getString("Hora");
									String Web       = sub.getString("Web");
									String tipoNoti  = sub.getString("tipoNoti");
									String room      = sub.getString("room");


									//agrego los contactos a la base interna
									dataNotificaciones.addNoti(new Notifications(mensaje,texto,Fecha,Hora,Web,tipoNoti,room ));

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
	/*                          Modulo para la modificacion de usuarios                           */
	/**********************************************************************************************/
	//Permite la modificacion del usuario
	public void  modUserSaldo(final String idCliente) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
			StringRequest strReq = new StringRequest(Request.Method.POST,
					AppConfig.URL_RECARGA, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {


					try {
						JSONObject jObj = new JSONObject(response);
						boolean error = jObj.getBoolean("error");

						// Check for error node in json
						if (!error) {

							//Obtengo el saldo como respuesta
							String Saldo        = jObj.getString("Saldo");

							// Obtengo la id de la tabla del usuario
							HashMap<String, String> user = dataUsuarios.getUserDetails();
							String idUser = user.get("idUser");

							//actualizo los datos
							dataUsuarios.updateUserSaldo(idUser, Saldo);

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
	/*                     Modulo para la creacion de una Alarma de SOSJoven                      */
	/**********************************************************************************************/
	//Se crea una alarma
	public void  invitarChat(final String idCliente, final String idContacto, final String room ) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
			StringRequest strReq = new StringRequest(Request.Method.POST,
					AppConfig.URL_INVITACION, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					//Log.d(TAG, "Login Response: " + response.toString());
					//hideDialog();

					try {
						JSONObject jObj = new JSONObject(response);
						boolean error = jObj.getBoolean("error");

						// Check for error node in json
						if (!error) {

							Fragment fragment = new View_Chat_Fragment();
							FragmentManager fragmentManager = getSupportFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
							fragmentTransaction.replace(R.id.container_body, fragment);
							fragmentTransaction.addToBackStack(null);
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
					params.put("idContacto", idContacto);
					params.put("room", room);



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
	//Se llama
	public void  llamadaChat(final String fonocliente, final String fonoContacto ) {

		// Tag used to cancel the request
		String tag_string_req = "req_login";

		try {
			StringRequest strReq = new StringRequest(Request.Method.POST,
					AppConfig.URL_CALLBACK, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					//Log.d(TAG, "Login Response: " + response.toString());
					//hideDialog();

					try {
						JSONObject jObj = new JSONObject(response);
						boolean error = jObj.getBoolean("error");

						// Check for error node in json
						if (!error) {

							//finalizo la actividad
							//MainActivity.this.finish();


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
					params.put("minumero", fonocliente);
					params.put("numllamado", fonoContacto);

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
