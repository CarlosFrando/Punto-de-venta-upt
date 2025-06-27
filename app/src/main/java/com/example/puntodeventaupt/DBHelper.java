package com.example.puntodeventaupt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "puntoventa.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla de usuarios
        String sqlUsuarios = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario TEXT, " +
                "contrasena TEXT)";
        db.execSQL(sqlUsuarios);

        // Tabla de productos
        String sqlProductos = "CREATE TABLE productos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "folio TEXT, " +
                "nombre TEXT, " +
                "cantidad INTEGER, " +
                "precio_compra REAL, " +
                "precio_venta REAL)";
        db.execSQL(sqlProductos);
        db.execSQL("INSERT INTO usuarios (usuario, contrasena) VALUES ('admin', '12345')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Elimina tablas si existen (para desarrollo, en producción se debe migrar)
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS productos");
        onCreate(db);
    }

    // Método para insertar productos
    public boolean insertarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("folio", producto.getFolio());
        values.put("nombre", producto.getNombre());
        values.put("cantidad", producto.getCantidad());
        values.put("precio_compra", producto.getPrecioCompra());
        values.put("precio_venta", producto.getPrecioVenta());

        long resultado = db.insert("productos", null, values);
        db.close();

        return resultado != -1;
    }
    public Cursor getProductoPorFolio(String folio) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM productos WHERE folio = ?", new String[]{folio});
    }

    public Cursor getInventario() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Selecciona las columnas que usas en FragmentInventario: nombre, cantidad, precio_venta
        return db.rawQuery("SELECT nombre, cantidad, precio_venta FROM productos", null);
    }



}
