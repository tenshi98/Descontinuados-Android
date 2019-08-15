package info.androidhive.loginandregistration.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.loginandregistration.model.Zone;

public class DB_zonas extends SQLiteOpenHelper {

	/**********************************************************************************************/
	/*                                Configuracion general                                       */
	/**********************************************************************************************/
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "zonas";
	/**********************************************************************************************/
	/*                          Tablas dentro de la base de datos                                 */
	/**********************************************************************************************/
	public static final String TABLE_ZONA = "zonas";
	/**********************************************************************************************/
	/*                       Configuracion de las columnas de la tabla                            */
	/**********************************************************************************************/
	public static final String KEY_ID         = "id";
	public static final String KEY_IDZONA     = "idZona";
	public static final String KEY_NOMBRE     = "Nombre";
	public static final String KEY_COLOR      = "ColorCode";
	public static final String KEY_PELIGRO    = "Peligrosidad";
	public static final String KEY_LATITUD    = "Latitud";
	public static final String KEY_LONGITUD   = "Longitud";
	/**********************************************************************************************/
	/*                                Creacion de las tablas                                      */
	/**********************************************************************************************/
	public DB_zonas(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//3rd argument to be passed is CursorFactory instance
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ZONA + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_IDZONA + " TEXT,"
				+ KEY_NOMBRE + " TEXT,"
				+ KEY_COLOR + " TEXT,"
				+ KEY_PELIGRO + " TEXT,"
				+ KEY_LATITUD + " TEXT,"
				+ KEY_LONGITUD + " TEXT"
				+ ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}
	/**********************************************************************************************/
	/*                                 El resto de los datos                                      */
	/**********************************************************************************************/
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZONA);

		// Create tables again
		onCreate(db);
	}
	/**********************************************************************************************/
	/*                          Operaciones en la tabla de los contactos                          */
	/**********************************************************************************************/
	// code to add the new contact
	public void addZona(Zone zona) {
		SQLiteDatabase db = this.getWritableDatabase();


		ContentValues values = new ContentValues();
		values.put(KEY_IDZONA, zona.getidZona());        // id de la zona
		values.put(KEY_NOMBRE, zona.getNombre());        // nombre
		values.put(KEY_COLOR, zona.getColorCode());      // color
		values.put(KEY_PELIGRO, zona.getPeligrosidad());      // Peligrosidad
		values.put(KEY_LATITUD, zona.getLatitud());      // latitud
		values.put(KEY_LONGITUD, zona.getLongitud());    // longitud

		// Inserting Row
		db.insert(TABLE_ZONA, null, values);
		//2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection

	}



	public void addZonaNuevo(String idZona, String Nombrez, String ColorCode, String Peligrosidad, String Latitud, String Longitud) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IDZONA, idZona);        // id de la zona
		values.put(KEY_NOMBRE, Nombrez);        // nombre
		values.put(KEY_COLOR, ColorCode);      // color
		values.put(KEY_PELIGRO, Peligrosidad);      // Peligrosidad
		values.put(KEY_LATITUD, Latitud);      // latitud
		values.put(KEY_LONGITUD, Longitud);    // longitud



		// Inserting Row
		long id = db.insert(TABLE_ZONA, null, values);
		db.close(); // Closing database connection

		//Log.d(TAG, "New user inserted into sqlite: " + id);
	}



	// code to get the single contact
	public Zone getZona(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ZONA, new String[]{KEY_ID, KEY_IDZONA, KEY_NOMBRE, KEY_COLOR, KEY_PELIGRO, KEY_LATITUD, KEY_LONGITUD}, KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Zone zona = new Zone(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6) );
		// return contact
		return zona;
	}
/*
	// code to get the single contact
	public Zone getZonaPeligro(String nombre) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ZONA, new String[]{KEY_ID, KEY_IDZONA, KEY_NOMBRE, KEY_COLOR, KEY_PELIGRO, KEY_LATITUD, KEY_LONGITUD}, KEY_NOMBRE + "=?",
				new String[]{String.valueOf(nombre)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Zone zona = new Zone(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6) );
		// return contact
		return zona;
	}*/
	// code to get all contacts in a list view
	public List<Zone> getZonaPeligro(String nombre) {
		List<Zone> zonaList = new ArrayList<Zone>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ZONA +" WHERE "+KEY_NOMBRE + "='" +nombre+"' LIMIT 1";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Zone zona = new Zone();
				zona.setID(Integer.parseInt(cursor.getString(0)));
				zona.setidZona(cursor.getString(1));
				zona.setNombre(cursor.getString(2));
				zona.setColorCode(cursor.getString(3));
				zona.setPeligrosidad(cursor.getString(4));
				zona.setLatitud(cursor.getString(5));
				zona.setLongitud(cursor.getString(6));

				// Adding contact to list
				zonaList.add(zona);
			} while (cursor.moveToNext());
		}

		// return contact list
		return zonaList;
	}





	// code to get all contacts in a list view
	public List<Zone> getAllZonas() {
		List<Zone> zonaList = new ArrayList<Zone>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ZONA;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Zone zona = new Zone();
				zona.setID(Integer.parseInt(cursor.getString(0)));
				zona.setidZona(cursor.getString(1));
				zona.setNombre(cursor.getString(2));
				zona.setColorCode(cursor.getString(3));
				zona.setPeligrosidad(cursor.getString(4));
				zona.setLatitud(cursor.getString(5));
				zona.setLongitud(cursor.getString(6));

				// Adding contact to list
				zonaList.add(zona);
			} while (cursor.moveToNext());
		}

		// return contact list
		return zonaList;
	}

	// code to get all contacts in a list view
	public List<Zone> getNameZonas() {
		List<Zone> zonaList = new ArrayList<Zone>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ZONA +" GROUP BY "+KEY_NOMBRE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Zone zona = new Zone();
				zona.setID(Integer.parseInt(cursor.getString(0)));
				zona.setidZona(cursor.getString(1));
				zona.setNombre(cursor.getString(2));
				zona.setColorCode(cursor.getString(3));
				zona.setPeligrosidad(cursor.getString(4));
				zona.setLatitud(cursor.getString(5));
				zona.setLongitud(cursor.getString(6));

				// Adding contact to list
				zonaList.add(zona);
			} while (cursor.moveToNext());
		}

		// return contact list
		return zonaList;
	}

	// code to get all contacts in a list view
	public List<Zone> getLatLongZonas(String Dato) {
		List<Zone> zonaList = new ArrayList<Zone>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ZONA +" WHERE "+KEY_NOMBRE + "='" +Dato+"' ORDER BY " +KEY_IDZONA+" ASC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Zone zona = new Zone();
				zona.setID(Integer.parseInt(cursor.getString(0)));
				zona.setidZona(cursor.getString(1));
				zona.setNombre(cursor.getString(2));
				zona.setColorCode(cursor.getString(3));
				zona.setPeligrosidad(cursor.getString(4));
				zona.setLatitud(cursor.getString(5));
				zona.setLongitud(cursor.getString(6));

				// Adding contact to list
				zonaList.add(zona);
			} while (cursor.moveToNext());
		}

		// return contact list
		return zonaList;
	}

	// code to get all contacts in a list view
	public Cursor getCursorZona() {
		List<Zone> contactList = new ArrayList<Zone>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ZONA;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// return contact list
		return cursor;
	}




	// code to update the single contact
	public int updateZona(Zone zona) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IDZONA, zona.getidZona());        // id de la zona
		values.put(KEY_NOMBRE, zona.getNombre());        // nombre
		values.put(KEY_COLOR, zona.getColorCode());      // color
		values.put(KEY_PELIGRO, zona.getPeligrosidad());      // Peligrosidad
		values.put(KEY_LATITUD, zona.getLatitud());      // latitud
		values.put(KEY_LONGITUD, zona.getLongitud());    // longitud


		// updating row
		return db.update(TABLE_ZONA, values, KEY_ID + " = ?",
				new String[] { String.valueOf(zona.getID()) });
	}

	// Deleting single contact
	public void deleteZona(int zona) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ZONA, KEY_ID + " = ?",
				new String[]{String.valueOf(zona)});
		db.close();
	}

	// Deleting all contact
	public void deleteAllZonas() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ZONA, null, null);
		db.close();
	}



	// Getting contacts Count
	public int getZonaCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ZONA;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}