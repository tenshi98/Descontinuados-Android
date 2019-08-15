package info.androidhive.loginandregistration.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.loginandregistration.model.Eventos;

public class DB_eventos extends SQLiteOpenHelper {

	/**********************************************************************************************/
	/*                                Configuracion general                                       */
	/**********************************************************************************************/
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "eventos";
	/**********************************************************************************************/
	/*                          Tablas dentro de la base de datos                                 */
	/**********************************************************************************************/
	public static final String TABLE_ALERTS = "eventos";
	/**********************************************************************************************/
	/*                       Configuracion de las columnas de la tabla                            */
	/**********************************************************************************************/
	public static final String KEY_ID     = "id";
	public static final String KEY_DATE   = "fecha";
	public static final String KEY_HOUR   = "hora";
	public static final String KEY_LAT    = "latitud";
	public static final String KEY_LONG   = "longitud";
	public static final String KEY_NAME   = "nombre";
	public static final String KEY_TYPE   = "tipo_evento";
	/**********************************************************************************************/
	/*                                Creacion de las tablas                                      */
	/**********************************************************************************************/
	public DB_eventos(Context context) {
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
				+ KEY_NAME + " TEXT,"
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
	public void addEvent(Eventos eventos) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, eventos.getFecha());
		values.put(KEY_HOUR, eventos.getHora());
		values.put(KEY_LAT, eventos.getLatitud());
		values.put(KEY_LONG, eventos.getLongitud());
		values.put(KEY_LONG, eventos.getLongitud());
		values.put(KEY_NAME, eventos.getNombre());
		values.put(KEY_TYPE, eventos.getTipoEvento());

		// Inserting Row
		db.insert(TABLE_ALERTS, null, values);
		//2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection



	}

	// code to get the single contact
	public Eventos getEvent(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ALERTS, new String[]{KEY_ID,
						KEY_DATE, KEY_HOUR, KEY_LAT, KEY_LONG, KEY_NAME, KEY_TYPE}, KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Eventos eventos = new Eventos(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
		// return contact
		return eventos;
	}

	// code to get all contacts in a list view
	public List<Eventos> getAllEvent() {
		List<Eventos> eventoList = new ArrayList<Eventos>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ALERTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Eventos eventos = new Eventos();
				eventos.setID(Integer.parseInt(cursor.getString(0)));
				eventos.setFecha(cursor.getString(1));
				eventos.setHora(cursor.getString(2));
				eventos.setLatitud(cursor.getString(3));
				eventos.setLongitud(cursor.getString(4));
				eventos.setEvento(cursor.getString(5));
				eventos.setTipoEvento(cursor.getString(6));
				// Adding contact to list
				eventoList.add(eventos);
			} while (cursor.moveToNext());
		}

		// return contact list
		return eventoList;
	}

	// code to get all contacts in a list view
	public List<Eventos> getAllEventSorted() {
		List<Eventos> eventoList = new ArrayList<Eventos>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ALERTS + " ORDER BY " + KEY_ID + " DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Eventos evento = new Eventos();
				evento.setID(Integer.parseInt(cursor.getString(0)));
				evento.setFecha(cursor.getString(1));
				evento.setHora(cursor.getString(2));
				evento.setLatitud(cursor.getString(3));
				evento.setLongitud(cursor.getString(4));
				evento.setEvento(cursor.getString(5));
				evento.setTipoEvento(cursor.getString(6));
				// Adding contact to list
				eventoList.add(evento);
			} while (cursor.moveToNext());
		}

		// return contact list
		return eventoList;
	}

	// code to get all contacts in a list view
	public Cursor getCursorEvent() {
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ALERTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// return contact list
		return cursor;
	}




	// code to update the single contact
	public int updateEvent(Eventos evento) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, evento.getFecha());
		values.put(KEY_HOUR, evento.getHora());
		values.put(KEY_LAT, evento.getLatitud());
		values.put(KEY_LONG, evento.getLongitud());
		values.put(KEY_NAME, evento.getNombre());
		values.put(KEY_TYPE, evento.getTipoEvento());

		// updating row
		return db.update(TABLE_ALERTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(evento.getID()) });
	}

	// Deleting single contact
	public void deleteEvent(int evento) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALERTS, KEY_ID + " = ?",
				new String[]{String.valueOf(evento)});
		db.close();
	}

	// Deleting all contact
	public void deleteAllEvent() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALERTS, null, null);
		db.close();
	}



	// Getting contacts Count
	public int getEventCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ALERTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}