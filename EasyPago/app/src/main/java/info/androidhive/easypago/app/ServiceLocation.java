package info.androidhive.easypago.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import info.androidhive.easypago.helper.DB_zonas;
import info.androidhive.easypago.model.Zone;



public class ServiceLocation extends BroadcastReceiver {


	public LocationManager locationManager;
	private double longitude;
	private double latitude;
	private DB_zonas dataZonas;



	@Override
	public void onReceive(Context context, Intent intent) {
		//Toast.makeText(context, "Time is up!!!!.",Toast.LENGTH_LONG).show();
		// Vibrate the mobile phone


		try {


			switch (GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(context)) {
				case ConnectionResult.SUCCESS:



					locationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));

					Boolean localBoolean = Boolean.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
					//Boolean localBoolean = Boolean.valueOf(locationManager.isProviderEnabled("Network"));


					if (localBoolean.booleanValue()) {


						Criteria criteria = new Criteria();
						String best = locationManager.getBestProvider(criteria, true);
						final Location location = locationManager.getLastKnownLocation(best);

						if (location == null) {
							Toast.makeText(context, "Ultima ubicacion no disponible, presione el boton para encontrar tu ubicacion", Toast.LENGTH_LONG).show();
						} else {
							latitude = location.getLatitude();
							longitude = location.getLongitude();


							//llamo a la base de datos con las zonas
							dataZonas = new DB_zonas(context);

							//Obtengo solo los nombres de la zona
							List<Zone> zonas = dataZonas.getNameZonas();

							//parseo los datos a la tabla
							for (Zone cn : zonas) {

								//obtengo los datos internos de cada zona
								List<Zone> ubicaciones = dataZonas.getLatLongZonas(cn.getNombre());


								//construyo un objeto
								LatLngBounds.Builder builder = new LatLngBounds.Builder();

								//recorro la tabla
								for (Zone latlong : ubicaciones) {

									//parseo los datos
									double x_lat = Double.parseDouble(latlong.getLatitud().toString());
									double x_long = Double.parseDouble(latlong.getLongitud().toString());
									builder.include(new LatLng (x_lat,x_long));

								}

								//armo los datos
								LatLngBounds bound = builder.build();

								//verifico si el dispositivo esta dentro del objeto recien creado
								if (bound.contains(new LatLng (latitude,longitude))){
									//mensaje de alerta
									Toast.makeText(context, "Estas en una zona de peligrosidad " +cn.getPeligrosidad(), Toast.LENGTH_LONG).show();
									//se activa el vibrador
									Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
									vibrator.vibrate(1000);
								}

							}

						}

					} else {
						Toast.makeText(context, "GPS no esta activo", Toast.LENGTH_SHORT).show();
						Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
						vibrator.vibrate(500);
						vibrator.vibrate(500);
					}

					break;
				case ConnectionResult.SERVICE_MISSING:
					Toast.makeText(context,"SERVICE_MISSING", Toast.LENGTH_SHORT).show();
					break;
				case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
					Toast.makeText(context,"SERVICE_VERSION_UPDATE_REQUIRED", Toast.LENGTH_SHORT).show();
					break;
				default:

			}
		} catch (Exception e) {
			Toast.makeText(context,"Exception 2" + e.toString(), Toast.LENGTH_SHORT).show();
		}






	}
}
