package info.androidhive.jootes.app;

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


public class ServiceLocation extends BroadcastReceiver {


	public LocationManager locationManager;
	private double longitude;
	private double latitude;



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
