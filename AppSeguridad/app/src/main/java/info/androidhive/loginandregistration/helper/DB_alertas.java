package info.androidhive.loginandregistration.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.loginandregistration.model.Alert;

public class DB_alertas extends SQLiteOpenHelper {

	/**********************************************************************************************/
	/*                                Configuracion general                                       */
	/**********************************************************************************************/
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "alertas";
	/**********************************************************************************************/
	/*                          Tablas dentro de la base de datos                                 */
	/**********************************************************************************************/
	public static final String TABLE_ALERTS = "alertas";
	/**********************************************************************************************/
	/*                       Configuracion de las columnas de la tabla                            */
	/**********************************************************************************************/
	public static final String KEY_ID     = "id";
	public static final String KEY_DATE   = "fecha";
	public static final String KEY_HOUR   = "hora";
	public static final String KEY_LAT    = "latitud";
	public static final String KEY_LONG   = "longitud";
	public static final String KEY_TYPE   = "tipo_alerta";
	/**********************************************************************************************/
	/*                                Creacion de las tablas                                      */
	/**********************************************************************************************/
	public DB_alertas(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//3rd argument to be passed is CursorFactory instance
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_ALERTS_TABLE = "CREATE TABLE " + TABLE_ALERTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_DATE + " TEXT,"
				+ KEY_HOUR + " TEXT,"
				+ KEY_LAT + " TEXT,"
				+ KEY_LONG + " TEXT,"
				+ KEY_TYPE + " TEXT"
				+ ")";
		db.execSQL(CREATE_ALERTS_TABLE);
	}
	/**********************************************************************************************/
	/*                                 El resto de los datos                                      */
	/**********************************************************************************************/
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTS);

		// Create tables again
		onCreate(db);
	}
	/**********************************************************************************************/
	/*                          Operaciones en la tabla de los contactos                          */
	/**********************************************************************************************/
	// code to add the new contact
	public void addAlert(Alert alerta) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, alerta.getFecha());
		values.put(KEY_HOUR, alerta.getHora());
		values.put(KEY_LAT, alerta.getLatitud());
		values.put(KEY_LONG, alerta.getLongitud());
		values.put(KEY_TYPE, alerta.getTipoAlerta());

		// Inserting Row
		db.insert(TABLE_ALERTS, null, values);
		//2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection



	}

	// code to get the single contact
	public Alert getAlert(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ALERTS, new String[]{KEY_ID,
						KEY_DATE, KEY_HOUR, KEY_LAT, KEY_LONG, KEY_TYPE}, KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Alert alertas = new Alert(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
		// return contact
		return alertas;
	}

	// code to get all contacts in a list view
	public List<Alert> getAllAlert() {
		List<Alert> alertaList = new ArrayList<Alert>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ALERTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Alert alerta = new Alert();
				alerta.setID(Integer.parseInt(cursor.getString(0)));
				alerta.setFecha(cursor.getString(1));
				alerta.setHora(cursor.getString(2));
				alerta.setLatitud(cursor.getString(3));
				alerta.setLongitud(cursor.getString(4));
				alerta.setTipoAlerta(cursor.getString(5));
				// Adding contact to list
				alertaList.add(alerta);
			} while (cursor.moveToNext());
		}

		// return contact list
		return alertaList;
	}

	// code to get all contacts in a list view
	public List<Alert> getAllAlertSorted() {
		List<Alert> alertaList = new ArrayList<Alert>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ALERTS + " ORDER BY " + KEY_ID + " DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Alert alerta = new Alert();
				alerta.setID(Integer.parseInt(cursor.getString(0)));
				alerta.setFecha(cursor.getString(1));
				alerta.setHora(cursor.getString(2));
				alerta.setLatitud(cursor.getString(3));
				alerta.setLongitud(cursor.getString(4));
				alerta.setTipoAlerta(cursor.getString(5));
				// Adding contact to list
				alertaList.add(alerta);
			} while (cursor.moveToNext());
		}

		// return contact list
		return alertaList;
	}

	// code to get all contacts in a list view
	public Cursor getCursorAlert() {
		//List<Alert> alertaList = new ArrayList<Alert>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ALERTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// return contact list
		return cursor;
	}




	// code to update the single contact
	public int updateAlert(Alert alerta) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, alerta.getFecha());
		values.put(KEY_HOUR, alerta.getHora());
		values.put(KEY_LAT, alerta.getLatitud());
		values.put(KEY_LONG, alerta.getLongitud());
		values.put(KEY_TYPE, alerta.getTipoAlerta());

		// updating row
		return db.update(TABLE_ALERTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(alerta.getID()) });
	}

	// Deleting single contact
	public void deleteAlert(int alerta) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALERTS, KEY_ID + " = ?",
				new String[]{String.valueOf(alerta)});
		db.close();
	}

	// Deleting all contact
	public void deleteAllAlert() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALERTS, null, null);
		db.close();
	}



	// Getting contacts Count
	public int getAlertCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ALERTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}