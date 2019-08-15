package info.androidhive.supegatrab.app;

public class AppConfig {

	public static String ID_SISTEMA          = "1";                                                                  //Sistema o empresa
	public static String URL_LOGIN           = "http://190.98.210.37/supega/app_trabajador/login.php";                  //Ingreso de usuario
	public static String URL_USER_UPDATE     = "http://190.98.210.37/supega/app_trabajador/usrupdate.php";              //actualizacion de usuario
	public static String URL_NOTIFICATIONS   = "http://190.98.210.37/supega/app_trabajador/notificaciones.php";         //se actualizan las notificaciones
	public static String URL_RECUPERAR       = "http://190.98.210.37/supega/app_trabajador/recover.php";                //Se recupera la contraseña
	public static String URL_UPLOAD          = "http://190.98.210.37/supega/app_trabajador/upload_img.php";             //subir imagen
	public static String URL_CALLBACK        = "http://190.98.210.37/supega/app_trabajador/callback.php";               //subir imagen
	public static String URL_OFERDIA         = "http://190.98.210.37/supega/app_trabajador/ofertas_diarias.php";        //subir imagen
	public static String URL_OFERDIAVIEW     = "http://190.98.210.37/supega/app_trabajador/ofertas_diarias_view.php";   //subir imagen
	public static String URL_CONTACT_INSERT  = "http://190.98.210.37/supega/app_trabajador/contact_insert.php";         //creacion de un contacto
	public static String URL_CONTACT_DELETE  = "http://190.98.210.37/supega/app_trabajador/contact_delete.php";         //eliminacion de un contacto
	public static String URL_ALERTS          = "http://190.98.210.37/supega/app_trabajador/alertas.php";                //eliminacion de un contacto


	public static String FOLDER_UPLOAD       = "http://190.98.210.37/supega/app_trabajador/upload/";                    //carpeta subidas
	public static String FOLDER_PHOTOS       = "http://190.98.210.37/supega/administracion/upload/";                 //carpeta subidas


	//Sistema de notificaciones
	public static final String GCM_SENDER_ID = "43602073106";

}
