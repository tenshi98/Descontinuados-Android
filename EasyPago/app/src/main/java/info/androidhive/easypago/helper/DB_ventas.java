package info.androidhive.easypago.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.easypago.model.Ventas;

public class DB_ventas extends SQLiteOpenHelper {

	/**********************************************************************************************/
	/*                                Configuracion general                                       */
	/**********************************************************************************************/
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "ventas";
	/**********************************************************************************************/
	/*                          Tablas dentro de la base de datos                                 */
	/**********************************************************************************************/
	public static final String TABLE_SELL = "ventas";
	/**********************************************************************************************/
	/*                       Configuracion de las columnas de la tabla                            */
	/**********************************************************************************************/
	public static final String KEY_ID     = "id";
	public static final String KEY_DATE   = "fecha";
	public static final String KEY_MOUNT   = "monto";
	public static final String KEY_PHONE    = "fono";
	/**********************************************************************************************/
	/*                                Creacion de las tablas                                      */
	/**********************************************************************************************/
	public DB_ventas(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//3rd argument to be passed is CursorFactory instance
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_SELL_TABLE = "CREATE TABLE " + TABLE_SELL + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_DATE + " TEXT,"
				+ KEY_MOUNT + " TEXT,"
				+ KEY_PHONE + " TEXT"
				+ ")";
		db.execSQL(CREATE_SELL_TABLE);
	}
	/**********************************************************************************************/
	/*                                 El resto de los datos                                      */
	/**********************************************************************************************/
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELL);

		// Create tables again
		onCreate(db);
	}
	/**********************************************************************************************/
	/*                          Operaciones en la tabla de los contactos                          */
	/**********************************************************************************************/
	// code to add the new contact
	public void addVenta(Ventas ventas) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, ventas.getFecha());
		values.put(KEY_MOUNT, ventas.getMonto());
		values.put(KEY_PHONE, ventas.getFono());

		// Inserting Row
		db.insert(TABLE_SELL, null, values);
		//2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection



	}

	// code to get the single contact
	public Ventas getVenta(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SELL, new String[]{KEY_ID,
						KEY_DATE, KEY_MOUNT, KEY_PHONE}, KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Ventas ventas = new Ventas(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3));
		// return contact
		return ventas;
	}

	// code to get all contacts in a list view
	public List<Ventas> getAllVenta() {
		List<Ventas> ventasList = new ArrayList<Ventas>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SELL;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Ventas ventas = new Ventas();
				ventas.setID(Integer.parseInt(cursor.getString(0)));
				ventas.setFecha(cursor.getString(1));
				ventas.setMonto(cursor.getString(2));
				ventas.setFono(cursor.getString(3));
				// Adding contact to list
				ventasList.add(ventas);
			} while (cursor.moveToNext());
		}

		// return contact list
		return ventasList;
	}

	// code to get all contacts in a list view
	public List<Ventas> getAllVentaSorted() {
		List<Ventas> ventasList = new ArrayList<Ventas>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SELL + " ORDER BY " + KEY_ID + " DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Ventas ventas = new Ventas();
				ventas.setID(Integer.parseInt(cursor.getString(0)));
				ventas.setFecha(cursor.getString(1));
				ventas.setMonto(cursor.getString(2));
				ventas.setFono(cursor.getString(3));
				// Adding contact to list
				ventasList.add(ventas);
			} while (cursor.moveToNext());
		}

		// return contact list
		return ventasList;
	}

	// code to get all contacts in a list view
	public Cursor getCursorVenta() {
		//List<Alert> alertaList = new ArrayList<Alert>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SELL;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// return contact list
		return cursor;
	}




	// code to update the single contact
	public int updateVenta(Ventas ventas) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, ventas.getFecha());
		values.put(KEY_MOUNT, ventas.getMonto());
		values.put(KEY_PHONE, ventas.getFono());

		// updating row
		return db.update(TABLE_SELL, values, KEY_ID + " = ?",
				new String[] { String.valueOf(ventas.getID()) });
	}

	// Deleting single contact
	public void deleteVenta(int ventas) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SELL, KEY_ID + " = ?",
				new String[]{String.valueOf(ventas)});
		db.close();
	}

	// Deleting all contact
	public void deleteAllVenta() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SELL, null, null);
		db.close();
	}



	// Getting contacts Count
	public int getVentaCount() {
		String countQuery = "SELECT  * FROM " + TABLE_SELL;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}