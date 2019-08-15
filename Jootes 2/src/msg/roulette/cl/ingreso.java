package msg.roulette.cl;
import static msg.roulette.cl.CommonUtilities.SENDER_ID;
import msg.roulette.cl.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import msg.roulette.cl.ingreso;
import msg.roulette.cl.iralweb;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;





public class ingreso extends Activity  {
    private EditText login;
	private EditText password1;
	private String password2;
	private Button registrate;
	public String USERNAME = "";
	public String PASSWORD = "";
     /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        setContentView(R.layout.ingreso);
 
        try
		{
			BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("datos.txt")), 8192);
			String serverUrl = fin.readLine();
			String[] separated = serverUrl.split("&");  
			if (separated[0]=="login=") { 
		            Intent i00 = new Intent(this, ingreso.class );
		            startActivity(i00);
			 } else {
				 
				 USERNAME = separated[0].replace("login=", "");
			     TextView login = (TextView)findViewById(R.id.login);
			     login.setText(USERNAME);
			     
				 PASSWORD  = separated[1].replace("password=", "");
			     TextView password1 = (TextView)findViewById(R.id.password);
			     password1.setText(PASSWORD);
			 }
			
		}
		catch (Exception ex)
		{ 
	        login = (EditText)findViewById(R.id.login);
	        password1 = (EditText)findViewById(R.id.password);
		} 
        
        login = (EditText)findViewById(R.id.login);
        password1 = (EditText)findViewById(R.id.password);
        registrate = (Button)findViewById(R.id.registrate);   

        registrate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
            	password2  = password1.getText().toString().replace(" ", "_");
            	final String str = "login="+login.getText().toString()+"&password="+password2;
    			//Clase que permite grabar texto en un archivo
    			
    			try
    			{
    			    OutputStreamWriter fout=
    			        new OutputStreamWriter(
    			            openFileOutput("datos.txt", Context.MODE_PRIVATE));
    			    		fout.write(str);
    			    		fout.close();
    			    		volver();
    			}
    			catch (Exception ex)
    			{ 

    			}
    			
            }
        });
    }
    
    private void volver()
    {
    GCMRegistrar.register(this, SENDER_ID);
	Intent i20 = new Intent(this, iralweb.class );
	startActivity(i20);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_llamar:
            	Intent i0 = new Intent(this, llamadas.class );
            	startActivityForResult(i0,0);
                return true;
            case R.id.ver_notificaciones:
            	Intent i1 = new Intent(this, iralweb.class );
            	startActivityForResult(i1,0);
                return true;
            case R.id.options_perfil:
            	Intent i2 = new Intent(this, ingreso.class );
            	startActivityForResult(i2,0);
                return true;
            case R.id.options_exit:
            	System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }  

    

}
