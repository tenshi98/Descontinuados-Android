package sosamerica.sosclick.cl;


import sosamerica.sosclick.cl.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class cerrar extends Activity {
    private WebView cerrar;
 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cerrar);
				    	String finalurl = "http://sosamerica.sosclick.cl/cierre.php";
				        cerrar = (WebView) findViewById(R.id.cerrar);
				    	cerrar.loadUrl(finalurl);		
				    	cerrar.setWebViewClient(new WebViewClient() {

					        @Override
					        public boolean shouldOverrideUrlLoading(WebView view, String url){
					            return false;
					        } 
				        });
			
				    	
    } 
    
    //BOTON VOLVER ATRAS
   	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        	// Esto es lo que hace mi botón al pulsar ir a atrás
	        	finish();
	        }
	        return super.onKeyDown(keyCode, event);
	    }
  //BOTON VOLVER ATRAS
    
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
//            finish();
            return true; 
       case R.id.ulaerta:
            Intent i1 = new Intent(this, ultimaalerta.class );
            startActivity(i1);
         //   finish();
            return true;            
        case R.id.perfil:
            Intent i2 = new Intent(this, perfil.class );
            startActivity(i2);
         //   finish();
            return true;  
        case R.id.contactos:
            Intent i21 = new Intent(this, contactos.class );
            startActivity(i21);
         //   finish();
            return true;              
        case R.id.llamadas:
            Intent i3 = new Intent(this, llamadas.class );
            startActivity(i3);
         //   finish();
            return true;               
        case R.id.cerrar:
        	  // Intent i4 = new Intent(this, cerrar.class );
        	  // startActivity(i4);
                  finish();
                 // return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
  
}
    
       
    
    
    
    
    
    
    
    
    //DESDE AQUI HASTA ACA    
//}