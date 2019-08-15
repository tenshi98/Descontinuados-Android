package sosamerica.sosclick.cl;

import java.util.Calendar;

import sosamerica.sosclick.cl.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AndroidAlarmService extends Activity {
public static String  parametro_imei;
private PendingIntent pendingIntent;
public String best;//Para el nombre del proveedor que vamos a utilizar.
public static double latitude;
public static double longitude;
public String deviceid;

 /** Called when the activity is first created. */
 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.gps_alerta);

   Button buttonStart = (Button)findViewById(R.id.startalarm);
     Button buttonCancel = (Button)findViewById(R.id.cancelalarm);
     

  	
     buttonStart.setOnClickListener(new Button.OnClickListener(){

  @Override
  public void onClick(View arg0) {
   Intent myIntent = new Intent(AndroidAlarmService.this, MyAlarmService.class);
   pendingIntent = PendingIntent.getService(AndroidAlarmService.this, 0, myIntent, 0);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 30);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),30000, pendingIntent);
            Toast.makeText(AndroidAlarmService.this, "Iniciando Aplicacion", Toast.LENGTH_LONG).show();
            volver();
             }});
  
     buttonCancel.setOnClickListener(new Button.OnClickListener(){

  @Override
  public void onClick(View arg0) {
   Intent myIntent = new Intent(AndroidAlarmService.this, MyAlarmService.class);
   pendingIntent = PendingIntent.getService(AndroidAlarmService.this, 0, myIntent, 0);
   AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
   alarmManager.cancel(pendingIntent);
   pendingIntent.cancel();
   finish();
   Toast.makeText(AndroidAlarmService.this, "Cancelando!", Toast.LENGTH_LONG).show();
   volver();
  }});
           
 }
 private void volver()
 {
	Intent i20 = new Intent(this, llamadas.class );
	startActivity(i20);
	//PROBANDO
	finish();
	//PROBANDO		
 }
  private void activa()
  {
	   Intent myIntent = new Intent(AndroidAlarmService.this, MyAlarmService.class);
	   pendingIntent = PendingIntent.getService(AndroidAlarmService.this, 0, myIntent, 0);

	            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

	            Calendar calendar = Calendar.getInstance();
	            calendar.setTimeInMillis(System.currentTimeMillis());
	            calendar.add(Calendar.SECOND, 10);
	            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),10000, pendingIntent);
	            Toast.makeText(AndroidAlarmService.this, "Iniciando Aplicacion", Toast.LENGTH_LONG).show();		
  }

  private void desactiva()
  {
	   Intent myIntent = new Intent(AndroidAlarmService.this, MyAlarmService.class);
	   pendingIntent = PendingIntent.getService(AndroidAlarmService.this, 0, myIntent, 0);
	   AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	   alarmManager.cancel(pendingIntent);
	   pendingIntent.cancel();
	   finish();
       Toast.makeText(AndroidAlarmService.this, "Cancelando!", Toast.LENGTH_LONG).show();	
  }
  
  
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
          finish();
          return true; 
     case R.id.ulaerta:
          Intent i1 = new Intent(this, ultimaalerta.class );
          startActivity(i1);
          finish();
          return true;            
      case R.id.perfil:
          Intent i2 = new Intent(this, perfil.class );
          startActivity(i2);
          finish();
          return true;  
      case R.id.contactos:
          Intent i21 = new Intent(this, contactos.class );
          startActivity(i21);
          finish();
          return true;              
      case R.id.llamadas:
          Intent i3 = new Intent(this, llamadas.class );
          startActivity(i3);
          finish();
          return true;               
      case R.id.cerrar:
              Intent i4 = new Intent(this, cerrar.class );
              startActivity(i4);
              finish();
              return true; 
          default:
              return super.onOptionsItemSelected(item);
      }
  }
  
  
}