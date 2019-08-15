package info.androidhive.easypago.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.easypago.model.Redes;

public class DB_redes extends SQLiteOpenHelper {

	/**********************************************************************************************/
	/*                                Configuracion general                                       */
	/**********************************************************************************************/
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "redes";
	/**********************************************************************************************/
	/*                          Tablas dentro de la base de datos                                 */
	/**********************************************************************************************/
	public static final String TABLE_CONTACTS = "redes";
	/**********************************************************************************************/
	/*                       Configuracion de las columnas de la tabla                            */
	/**********************************************************************************************/
	public static final String KEY_ID             = "id";
	public static final String KEY_NAME           = "name";
	public static final String KEY_PHONE          = "phone_number";
	/**********************************************************************************************/
	/*                                Creacion de las tablas                                      */
	/**********************************************************************************************/
	public DB_redes(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//3rd argument to be passed is CursorFactory instance
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT,"
				+ KEY_PHONE + " TEXT"
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}
	/**********************************************************************************************/
	/*                          Operaciones en la tabla de los contactos                          */
	/**********************************************************************************************/
	// code to add the new contact
	public void addRed(Redes redes) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, redes.getName());                  // Nombre
		values.put(KEY_PHONE, redes.getPhoneNumber());          // Telefono

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		//2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection


	}

	// code to get the single contact
	public Redes getRed(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
						KEY_NAME, KEY_PHONE}, KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Redes contact = new Redes(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return contact;
	}

	// code to get all contacts in a list view
	public List<Redes> getAllRedes() {
		List<Redes> contactList = new ArrayList<Redes>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Redes contact = new Redes();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhoneNumber(cursor.getString(2));

				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}



	// code to get all contacts in a list view
	public Cursor getCursorRed() {
		List<Redes> contactList = new ArrayList<Redes>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// return contact list
		return cursor;
	}




	// code to update the single contact
	public int updateRed(Redes contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_PHONE, contact.getPhoneNumber());

		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
	}

	// Deleting single contact
	public void deleteRed(int contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[]{String.valueOf(contact)});
		db.close();
	}

	// Deleting all contact
	public void deleteAllRedes() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, null, null);
		db.close();
	}



	// Getting contacts Count
	public int getRedesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}