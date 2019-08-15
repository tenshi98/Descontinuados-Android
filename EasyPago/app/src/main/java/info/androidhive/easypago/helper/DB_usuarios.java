package info.androidhive.easypago.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DB_usuarios extends SQLiteOpenHelper {

	//private static final String TAG = DB_usuarios.class.getSimpleName();
	//private static final String TAG = DB_contactos.class.getSimpleName();

	/**********************************************************************************************/
	/*                                Configuracion general                                       */
	/**********************************************************************************************/
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "seguridad";
	/**********************************************************************************************/
	/*                          Tablas dentro de la base de datos                                 */
	/**********************************************************************************************/
	private static final String TABLE_USER      = "usuarios";
	/**********************************************************************************************/
	/*                       Configuracion de las columnas de la tabla                            */
	/**********************************************************************************************/
	public static final String KEY_ID              = "idUser";
	public static final String KEY_IDCLIENTE       = "idCliente";
	public static final String KEY_EMAIL           = "email";
	public static final String KEY_NOMBRE          = "Nombre";
	public static final String KEY_RUT             = "Rut";
	public static final String KEY_FNACIMIENTO     = "fNacimiento";
	public static final String KEY_DIRECCION       = "Direccion";
	public static final String KEY_FONO1           = "Fono1";
	public static final String KEY_FONO2           = "Fono2";
	public static final String KEY_DEPTO           = "Departamento";
	public static final String KEY_PROV            = "Provincia";
	public static final String KEY_DIST            = "Distrito";
	public static final String KEY_FCREACION       = "fcreacion";
	public static final String KEY_FACTUALIZACION  = "factualizacion";
	public static final String KEY_LATITUD         = "latitud";
	public static final String KEY_LONGITUD        = "longitud";
	public static final String KEY_SALDO           = "Saldo";
	/**********************************************************************************************/
	/*                                Creacion de las tablas                                      */
	/**********************************************************************************************/
	//Tabla de usuarios
	private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_IDCLIENTE + " TEXT,"
			+ KEY_EMAIL + " TEXT,"
			+ KEY_NOMBRE + " TEXT,"
			+ KEY_RUT + " TEXT,"
			+ KEY_FNACIMIENTO + " TEXT,"
			+ KEY_DIRECCION + " TEXT,"
			+ KEY_FONO1 + " TEXT,"
			+ KEY_FONO2 + " TEXT,"
			+ KEY_DEPTO + " TEXT,"
			+ KEY_PROV + " TEXT,"
			+ KEY_DIST + " TEXT,"
			+ KEY_FCREACION + " TEXT,"
			+ KEY_FACTUALIZACION + " TEXT,"
			+ KEY_LATITUD + " TEXT,"
			+ KEY_LONGITUD + " TEXT,"
			+ KEY_SALDO + " TEXT"
			+ ")";

	/**********************************************************************************************/
	/*                                 El resto de los datos                                      */
	/**********************************************************************************************/
	public DB_usuarios(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	// Se crea la tabla
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_LOGIN_TABLE);

		//Log.d(TAG, "Database tables created");
	}

	// Se actualiza la tabla
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Se eliminan las tablas si es que estas existen
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// Create tables again
		onCreate(db);
	}

	/**********************************************************************************************/
	/*                          Operaciones en la tabla de los usuarios                           */
	/**********************************************************************************************/
	//Se agrega el usuario
	public void addUser(String idCliente, String email, String Nombre, String Rut, String fNacimiento, String Direccion, String Fono1, String Fono2,
						String Departamento, String Provincia, String Distrito, String fcreacion, String factualizacion, String Saldo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IDCLIENTE, idCliente);
		values.put(KEY_EMAIL, email);
		values.put(KEY_NOMBRE, Nombre);
		values.put(KEY_RUT, Rut);
		values.put(KEY_FNACIMIENTO, fNacimiento);
		values.put(KEY_DIRECCION, Direccion);
		values.put(KEY_FONO1, Fono1);
		values.put(KEY_FONO2, Fono2);
		values.put(KEY_DEPTO, Departamento);
		values.put(KEY_PROV, Provincia);
		values.put(KEY_DIST, Distrito);
		values.put(KEY_FCREACION, fcreacion);
		values.put(KEY_FACTUALIZACION, factualizacion);
		values.put(KEY_SALDO, Saldo);

		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection


	}

	//Obtengo toda la informacion del usuario
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("idUser", cursor.getString(0));
			user.put("idCliente", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("Nombre", cursor.getString(3));
			user.put("Rut", cursor.getString(4));
			user.put("fNacimiento", cursor.getString(5));
			user.put("Direccion", cursor.getString(6));
			user.put("Fono1", cursor.getString(7));
			user.put("Fono2", cursor.getString(8));
			user.put("Departamento", cursor.getString(9));
			user.put("Provincia", cursor.getString(10));
			user.put("Distrito", cursor.getString(11));
			user.put("fcreacion", cursor.getString(12));
			user.put("factualizacion", cursor.getString(13));
			user.put("latitud", cursor.getString(14));
			user.put("longitud", cursor.getString(15));
			user.put("Saldo", cursor.getString(16));

		}
		cursor.close();
		db.close();

		return user;
	}

	//borro al usuario
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

	}

	// code to update the single contact
	public int updateUser(String idUser,String idCliente, String email, String Nombre, String Rut, String fNacimiento, String Direccion, String Fono1
			,String Fono2,String Departamento, String Provincia, String Distrito, String fcreacion, String factualizacion) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IDCLIENTE, idCliente);
		values.put(KEY_EMAIL, email);
		values.put(KEY_NOMBRE, Nombre);
		values.put(KEY_RUT, Rut);
		values.put(KEY_FNACIMIENTO, fNacimiento);
		values.put(KEY_DIRECCION, Direccion);
		values.put(KEY_FONO1, Fono1);
		values.put(KEY_FONO2, Fono2);
		values.put(KEY_DEPTO, Departamento);
		values.put(KEY_PROV, Provincia);
		values.put(KEY_DIST, Distrito);
		values.put(KEY_FCREACION, fcreacion);
		values.put(KEY_FACTUALIZACION, factualizacion);


		// updating row
		return db.update(TABLE_USER, values, KEY_ID + " = ?",
				new String[] { String.valueOf(idUser) });
	}



	// code to update the single contact
	public int updateUserLatLong(String idUser, String latitud, String longitud) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LATITUD, latitud);
		values.put(KEY_LONGITUD, longitud);


		// updating row
		return db.update(TABLE_USER, values, KEY_ID + " = ?",
				new String[] { String.valueOf(idUser) });
	}

	// code to update the single contact
	public int updateUserSaldo(String idUser, String Saldo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SALDO, Saldo);


		// updating row
		return db.update(TABLE_USER, values, KEY_ID + " = ?",
				new String[] { String.valueOf(idUser) });
	}














}
