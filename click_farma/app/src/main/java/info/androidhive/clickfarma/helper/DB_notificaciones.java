package info.androidhive.clickfarma.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import info.androidhive.clickfarma.model.Notifications;

public class DB_notificaciones extends SQLiteOpenHelper {

	/**********************************************************************************************/
	/*                                Configuracion general                                       */
	/**********************************************************************************************/
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "notificaciones";
	/**********************************************************************************************/
	/*                          Tablas dentro de la base de datos                                 */
	/**********************************************************************************************/
	public static final String TABLE_NOTIFICATIONS = "notificaciones";
	/**********************************************************************************************/
	/*                       Configuracion de las columnas de la tabla                            */
	/**********************************************************************************************/
	public static final String KEY_ID         = "id";
	public static final String KEY_MSG        = "mensaje";
	public static final String KEY_TXT        = "texto";
	public static final String KEY_DATE       = "fecha";
	public static final String KEY_HOUR       = "hora";
	public static final String KEY_WEB        = "Web";
	public static final String KEY_TIPO       = "tipoNoti";
	public static final String KEY_ROOM       = "room";
	/**********************************************************************************************/
	/*                                Creacion de las tablas                                      */
	/**********************************************************************************************/
	public DB_notificaciones(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//3rd argument to be passed is CursorFactory instance
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_NOTI_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_MSG + " TEXT,"
				+ KEY_TXT + " TEXT,"
				+ KEY_DATE + " TEXT,"
				+ KEY_HOUR + " TEXT,"
				+ KEY_WEB + " TEXT,"
				+ KEY_TIPO + " TEXT,"
				+ KEY_ROOM + " TEXT"
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);

		// Create tables again
		onCreate(db);
	}
	/**********************************************************************************************/
	/*                          Operaciones en la tabla de los contactos                          */
	/**********************************************************************************************/
	// code to add the new contact
	public void addNoti(Notifications notificaciones) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MSG, notificaciones.getMensaje());
		values.put(KEY_TXT, notificaciones.getTexto());
		values.put(KEY_DATE, notificaciones.getFecha());
		values.put(KEY_HOUR, notificaciones.getHora());
		values.put(KEY_WEB, notificaciones.getWeb());
		values.put(KEY_TIPO, notificaciones.getTipo());
		values.put(KEY_ROOM, notificaciones.getRoom());


		// Inserting Row
		db.insert(TABLE_NOTIFICATIONS, null, values);
		//2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection

	}

	// code to get the single contact
	public Notifications getNoti(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NOTIFICATIONS, new String[]{KEY_ID,
						KEY_MSG,KEY_TXT,KEY_DATE, KEY_HOUR, KEY_WEB, KEY_TIPO, KEY_ROOM}, KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Notifications notificaciones = new Notifications(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)
				, cursor.getString(6), cursor.getString(7));
		// return contact
		return notificaciones;
	}

	// code to get all contacts in a list view
	public List<Notifications> getAllNoti() {
		List<Notifications> notiList = new ArrayList<Notifications>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS + " ORDER BY " + KEY_ID + " DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Notifications notificacion = new Notifications();
				notificacion.setID(Integer.parseInt(cursor.getString(0)));
				notificacion.setMensaje(cursor.getString(1));
				notificacion.setTexto(cursor.getString(2));
				notificacion.setFecha(cursor.getString(3));
				notificacion.setHora(cursor.getString(4));
				notificacion.setWeb(cursor.getString(5));
				notificacion.setTipo(cursor.getString(6));
				notificacion.setRoom(cursor.getString(7));

				// Adding contact to list
				notiList.add(notificacion);
			} while (cursor.moveToNext());
		}

		// return contact list
		return notiList;
	}

	// code to get all contacts in a list view
	public Cursor getCursorNoti() {
		//List<Notifications> contactList = new ArrayList<Notifications>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// return contact list
		return cursor;
	}




	// code to update the single contact
	public int updateNoti(Notifications notificacion) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MSG, notificacion.getMensaje());
		values.put(KEY_TXT, notificacion.getTexto());
		values.put(KEY_DATE, notificacion.getFecha());
		values.put(KEY_HOUR, notificacion.getHora());
		values.put(KEY_WEB, notificacion.getWeb());
		values.put(KEY_TIPO, notificacion.getTipo());
		values.put(KEY_ROOM, notificacion.getRoom());


		// updating row
		return db.update(TABLE_NOTIFICATIONS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(notificacion.getID()) });
	}

	// Deleting single contact
	public void deleteNoti(int noti) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTIFICATIONS, KEY_ID + " = ?",
				new String[]{String.valueOf(noti)});
		db.close();
	}

	// Deleting all contact
	public void deleteAllNoti() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTIFICATIONS, null, null);
		db.close();
	}



	// Getting contacts Count
	public int getNotiCount() {
		String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}