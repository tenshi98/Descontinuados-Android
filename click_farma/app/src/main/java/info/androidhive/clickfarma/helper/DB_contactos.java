package info.androidhive.clickfarma.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.clickfarma.model.Contact;

public class DB_contactos extends SQLiteOpenHelper {

	/**********************************************************************************************/
	/*                                Configuracion general                                       */
	/**********************************************************************************************/
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "contactos";
	/**********************************************************************************************/
	/*                          Tablas dentro de la base de datos                                 */
	/**********************************************************************************************/
	public static final String TABLE_CONTACTS = "contacts";
	/**********************************************************************************************/
	/*                       Configuracion de las columnas de la tabla                            */
	/**********************************************************************************************/
	public static final String KEY_ID             = "id";
	public static final String KEY_NAME           = "name";
	public static final String KEY_PHONE          = "phone_number";
	public static final String KEY_ESTADO         = "estado";
	public static final String KEY_GSM            = "gsm";
	public static final String KEY_IDCLIENTE      = "idcliente";
	public static final String KEY_TIPOCONTACTO   = "tipoContacto";
	/**********************************************************************************************/
	/*                                Creacion de las tablas                                      */
	/**********************************************************************************************/
	public DB_contactos(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//3rd argument to be passed is CursorFactory instance
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT,"
				+ KEY_PHONE + " TEXT,"
				+ KEY_ESTADO + " TEXT,"
				+ KEY_GSM + " TEXT,"
				+ KEY_IDCLIENTE + " TEXT,"
				+ KEY_TIPOCONTACTO + " TEXT"
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
	public void addContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		//verifico si el numero seleccionado ya existe dentro de la base
		Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID}, KEY_PHONE + "=?",
				new String[]{String.valueOf(contact.getPhoneNumber())}, null, null, null, null);
		cursor.getCount();

		//si no existe se guarda
		if (cursor.getCount() <= 0){
			ContentValues values = new ContentValues();
			values.put(KEY_NAME, contact.getName());                  // Nombre
			values.put(KEY_PHONE, contact.getPhoneNumber());          // Telefono
			values.put(KEY_ESTADO, contact.getEstado());              // Estado
			values.put(KEY_GSM, contact.getGSM());                    // GSM
			values.put(KEY_IDCLIENTE, contact.getidCliente());        // idCliente
			values.put(KEY_TIPOCONTACTO, contact.getTipoContacto());  // tipo de contacto


			// Inserting Row
			db.insert(TABLE_CONTACTS, null, values);
			//2nd argument is String containing nullColumnHack
			db.close(); // Closing database connection

		}

	}

	// code to get the single contact
	public Contact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
						KEY_NAME, KEY_PHONE, KEY_ESTADO, KEY_GSM, KEY_IDCLIENTE, KEY_TIPOCONTACTO}, KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
		// return contact
		return contact;
	}

	// code to get all contacts in a list view
	public List<Contact> getAllContacts() {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhoneNumber(cursor.getString(2));
				contact.setEstado(cursor.getString(3));
				contact.setGSM(cursor.getString(4));
				contact.setidCliente(cursor.getString(5));
				contact.setTipoContacto(cursor.getString(6));

				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// code to get all contacts in a list view
	public List<Contact> getAllContactsRegister() {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_ESTADO + "='Registrado'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhoneNumber(cursor.getString(2));
				contact.setEstado(cursor.getString(3));
				contact.setGSM(cursor.getString(4));
				contact.setidCliente(cursor.getString(5));
				contact.setTipoContacto(cursor.getString(6));

				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// code to get all contacts in a list view
	public Cursor getCursorContacts() {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// return contact list
		return cursor;
	}




	// code to update the single contact
	public int updateContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_PHONE, contact.getPhoneNumber());
		values.put(KEY_ESTADO, contact.getEstado());
		values.put(KEY_GSM, contact.getGSM());
		values.put(KEY_IDCLIENTE, contact.getidCliente());
		values.put(KEY_TIPOCONTACTO, contact.getTipoContacto());

		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
	}

	// Deleting single contact
	public void deleteContact(int contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[]{String.valueOf(contact)});
		db.close();
	}

	// Deleting all contact
	public void deleteAllContact() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, null, null);
		db.close();
	}



	// Getting contacts Count
	public int getContactsCount() {

		//String countQuery = "SELECT  COUNT("+KEY_ID+") FROM " + TABLE_CONTACTS;

		final String DATABASE_COMPARE = "SELECT  COUNT("+KEY_ID+") FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getWritableDatabase();

		int sometotal = (int) DatabaseUtils.longForQuery(db, DATABASE_COMPARE, null);


		// return count
		return sometotal;

		//return DatabaseUtils.queryNumEntries(readableDatabase, TABLE_CONTACTS);



	}

}