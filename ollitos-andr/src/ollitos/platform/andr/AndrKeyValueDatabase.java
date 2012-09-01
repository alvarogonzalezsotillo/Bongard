package ollitos.platform.andr;

import java.util.HashMap;

import ollitos.platform.state.IBKeyValueDatabase;
import ollitos.platform.state.IBKeyValueTable;
import ollitos.util.BException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AndrKeyValueDatabase extends SQLiteOpenHelper implements IBKeyValueDatabase{
	
	private static final int VERSION = 1;
	private static final String TABLE = "tables";
	private static final String TABLE_COLUMN = "table_c";
	private static final String KEY_COLUMN = "key_c";
	private static final String VALUE_COLUMN = "value_c";
	
	private static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE + " ( " +
					TABLE_COLUMN +" TEXT, " +
					KEY_COLUMN + " TEXT, " +
					VALUE_COLUMN + " BLOB)";
	;
	private String _name;

	private AndrKeyValueDatabase(String name){
		super(AndrPlatform.context(), name, null, VERSION);
		_name = name;
	}

	private static HashMap<String, AndrKeyValueDatabase> _databases;
	
	public static AndrKeyValueDatabase open(String s){
		if( _databases == null ){
			_databases = new HashMap<String, AndrKeyValueDatabase>();
		}
		AndrKeyValueDatabase ret = _databases.get(s);
		if( ret == null ){
			ret = new AndrKeyValueDatabase(s);
			_databases.put(s, ret);
		}
		return ret;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL( CREATE_TABLE_SQL );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setUp() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean disposed() {
		return false;
	}

	@Override
	public String name() {
		return _name;
	}

	@Override
	public IBKeyValueTable table(Object... name) {
		return new KeyValueTable( Util.concatenate(name) );
	}
	
	private class KeyValueTable implements IBKeyValueTable{

		private static final String SELECTOR = TABLE_COLUMN + "=?1 AND " + KEY_COLUMN + "=?2";
		private String _tableName;
		public KeyValueTable(String name) {
			_tableName = name;
		}

		@Override
		public String name() {
			return _tableName;
		}

		@Override
		public IBKeyValueDatabase database() {
			return AndrKeyValueDatabase.this;
		}

		@Override
		public boolean putBytes(byte[] b, Object... key) {
			SQLiteDatabase db = getWritableDatabase();
			String k = Util.concatenate(key);
			db.beginTransaction();
			db.delete(TABLE, SELECTOR, new String[]{name(), k} );
			ContentValues cv = new ContentValues(2);
			cv.put( KEY_COLUMN, k);
			cv.put( VALUE_COLUMN, b);
			db.insert(TABLE, null, cv);
			db.endTransaction();
			db.close();
			return true;
		}

		@Override
		public byte[] getBytes(Object... key) {
			SQLiteDatabase db = getWritableDatabase();
			String k = Util.concatenate(key);
			db.beginTransaction();
			Cursor c = db.query(TABLE, new String[]{VALUE_COLUMN}, SELECTOR, new String[]{name(),k}, null, null, null);
			int count = c.getCount();
			if( count == 0 ){
				return null;
			}
			if( count > 1 ){
				throw new BException( "More than one row:" + k, null );
			}
			
			c.moveToFirst();
			byte[] blob = c.getBlob(2);
			c.close();
			db.endTransaction();
			db.close();
			return blob;
		}
		
	}
}
