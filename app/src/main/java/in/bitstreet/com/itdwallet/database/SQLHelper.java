package in.bitstreet.com.itdwallet.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import in.bitstreet.com.itdwallet.utills.Constants;

public class SQLHelper extends SQLiteOpenHelper {

	public SQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABSE_VERSION);
	}

	// datatbase version
	public static final int DATABSE_VERSION = 1;

	// database name
	public static final String DATABASE_NAME = "itdwallet.db";

	// table name
	String createTableLogin = "CREATE TABLE "
			+ Constants.LOGIN_TABLE + "(" + Constants.ID

			+ " TEXT PRIMARY KEY ," + Constants.FIRST_NAME
			+ " TEXT ," + Constants.LAST_NAME
			+ " TEXT " + "," + Constants.HANNDLE
			+ " TEXT " + "," + Constants.EMAIL_ID
			+ " TEXT " + "," + Constants.WALLET_ADDRESS
			+ " TEXT " + "," + Constants.SIGNUP_TOKEN
			+ " TEXT " + "," + Constants.REFERRAL_CODE
			+ " TEXT " + "," + Constants.DOB
			+ " TEXT " + "," + Constants.IMAGE_URL
			+ " TEXT " + "," + Constants.GENDER
			+ " TEXT " + "," + Constants.USER_ID

			/*//changes on json structure and field on 27/12/2017 so below value is commented
			+ " TEXT " + "," + Constants.BALANCE
			+ " TEXT " + "," + Constants.PENDING_PAID
			+ " TEXT " + "," + Constants.CREATION_TIME
			+ " TEXT " + "," + Constants.SECURITY_MODE
			+ " TEXT " + "," + Constants.IS_VALID_INVITECODE
			+ " TEXT " + "," + Constants.BTC_BALANCE*/

			+ " TEXT " + "," + Constants.UPDATED_AT
			+ " TEXT);";


	String createTableAdminSettings = "CREATE TABLE "
			+ Constants.LOGIN_TABLE + "(" + Constants.DOB
			+ " TEXT " + "," + Constants.IMAGE_URL
			+ " TEXT " + "," + Constants.GENDER
			+ " TEXT " + "," + Constants.BALANCE
			+ " TEXT " + "," + Constants.UPDATED_AT
			+ " TEXT);";








	@Override
	public void onCreate(SQLiteDatabase db) {
		// /////////////////////////////////////////////////////////////////////////////////////////////
		db.execSQL(createTableLogin);


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.LOGIN_TABLE);
		onCreate(db);

	}

}
