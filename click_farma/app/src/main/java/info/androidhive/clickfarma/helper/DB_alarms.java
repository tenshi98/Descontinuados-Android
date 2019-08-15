package info.androidhive.clickfarma.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.clickfarma.model.Alarms;

public class DB_alarms extends SQLiteOpenHelper {

	/**********************************************************************************************/
	/*                                Configuracion general                                       */
	/**********************************************************************************************/
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "alarmas";
	/**********************************************************************************************/
	/*                          Tablas dentro de la base de datos                                 */
	/**********************************************************************************************/
	public static final String TABLE_ALARMS = "alarmas";
	/**********************************************************************************************/
	/*                       Configuracion de las columnas de la tabla                            */
	/**********************************************************************************************/
	public static final String KEY_ID         = "id";
	public static final String KEY_TXT        = "texto";
	public static final String KEY_HOUR       = "hora";
	public static final String KEY_HOUR_INI   = "hora_ini";
	/**********************************************************************************************/
	/*                                Creacion de las tablas                                      */
	/**********************************************************************************************/
	public DB_alarms(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//3rd argument to be passed is CursorFactory instance
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_NOTI_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_TXT + " TEXT,"
				+ KEY_HOUR + " TEXT,"
				+ KEY_HOUR_INI + " TEXT"
				+ ")";
		db.execSQL(CREATE_NOTI_TABLE);
	}
	/**********************************************************************************************/
	/*                                 El resto de los datos                                      */
	/**********************************************************************************************/
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);

		// Create tables again
		onCreate(db);
	}
	/**********************************************************************************************/
	/*                          Operaciones en la tabla de los contactos                          */
	/**********************************************************************************************/
	// code to add the new contact
	public void addNoti(Alarms notificaciones) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TXT, notificaciones.getTexto());
		values.put(KEY_HOUR, notificaciones.getHora());
		values.put(KEY_HOUR_INI, notificaciones.getHoraini());


		// Inserting Row
		db.insert(TABLE_ALARMS, null, values);
		//2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection

	}
	public void addNoti2(String texto, String hora, String horaini) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TXT, texto);
		values.put(KEY_HOUR, hora);
		values.put(KEY_HOUR_INI, horaini);

		// Inserting Row
		long id = db.insert(TABLE_ALARMS, null, values);
		db.close(); // Closing database connection


	}

	// code to get the single contact
	public Alarms getNoti(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ALARMS, new String[]{KEY_ID,
						KEY_TXT,KEY_HOUR, KEY_HOUR_INI}, KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Alarms notificaciones = new Alarms(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3));
		// return contact
		return notificaciones;
	}

	// code to get all contacts in a list view
	public List<Alarms> getAllNoti() {
		List<Alarms> notiList = new ArrayList<Alarms>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ALARMS + " ORDER BY " + KEY_ID + " DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Alarms notificacion = new Alarms();
				notificacion.setID(Integer.parseInt(cursor.getString(0)));
				notificacion.setTexto(cursor.getString(1));
				notificacion.setHora(cursor.getString(2));
				notificacion.setHoraini(cursor.getString(3));

				// Adding contact to list
				notiList.add(notificacion);
			} while (cursor.moveToNext());
		}

		// return contact list
		return notiList;
	}




	// code to update the single contact
	public int updateNoti(Alarms notificacion) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TXT, notificacion.getTexto());
		values.put(KEY_HOUR, notificacion.getHora());
		values.put(KEY_HOUR_INI, notificacion.getHoraini());


		// updating row
		return db.update(TABLE_ALARMS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(notificacion.getID()) });
	}

	// Deleting single contact
	public void deleteNoti(int noti) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALARMS, KEY_ID + " = ?",
				new String[]{String.valueOf(noti)});
		db.close();
	}

	// Deleting all contact
	public void deleteAllNoti() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALARMS, null, null);
		db.close();
	}

	public HashMap<String, String> lastNoti() {

		HashMap<String, String> user = new HashMap<String, String>();

		String selectQuery = "SELECT  * FROM " + TABLE_ALARMS + " ORDER BY " + KEY_ID + " DESC LIMIT 1";


		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("id", cursor.getString(0));
		}
		cursor.close();
		db.close();

		return user;

	}

	// Getting contacts Count
	public int getNotiCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ALARMS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}