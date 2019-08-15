package info.androidhive.loginandregistration.app;

public class AppConfig {

	public static String ID_SISTEMA          = "1";                                                                         //Sistema o empresa
	public static String URL_LOGIN           = "http://190.98.210.36/app_seguridad/app_android/login.php";                  //Ingreso de usuario
	public static String URL_REGISTER        = "http://190.98.210.36/app_seguridad/app_android/register.php";               //Registro de usuario
	public static String URL_USER_UPDATE     = "http://190.98.210.36/app_seguridad/app_android/usrupdate.php";              //actualizacion de usuario
	public static String URL_CONTACT_INSERT  = "http://190.98.210.36/app_seguridad/app_android/contact_insert.php";         //creacion de un contacto
	public static String URL_CONTACT_DELETE  = "http://190.98.210.36/app_seguridad/app_android/contact_delete.php";         //eliminacion de un contacto
	public static String URL_ALERTAS         = "http://190.98.210.36/app_seguridad/app_android/alertas.php";                //crear una alarma
	public static String URL_ALERTAS_JOVEN   = "http://190.98.210.36/app_seguridad/app_android/alertas_joven.php";          //crear una alarma de sosjoven
	public static String URL_NOTIFICATIONS   = "http://190.98.210.36/app_seguridad/app_android/notificaciones.php";         //se actualizan las notificaciones
	public static String URL_MAPA_UBICACION  = "http://190.98.210.36/app_seguridad/app_android/mapa_ubicacion.php";         //se solicita la ultima ubicacion
	public static String URL_MAPA_PEDIR      = "http://190.98.210.36/app_seguridad/app_android/notificacion_unitaria.php";  //se solicita la ubiccion actualizada
	public static String URL_EVENTO          = "http://190.98.210.36/app_seguridad/app_android/eventos.php";                //se envia evento
	public static String URL_RECUPERAR       = "http://190.98.210.36/app_seguridad/app_android/recover.php";                //Se recupera la contraseña
	public static String URL_ENVIO_UBICACION = "http://190.98.210.36/app_seguridad/app_android/envio_ubicacion.php";        //Se envia la ubicacion
	public static String URL_PUNTOS          = "http://190.98.210.36/app_seguridad/app_android/puntos.php";                 //Obtengo los eventos a mi alrededor


	//Sistema de notificaciones
	public static final String GCM_SENDER_ID = "905685951610";

}
