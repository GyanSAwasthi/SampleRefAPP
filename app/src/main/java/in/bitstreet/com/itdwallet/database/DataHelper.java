
package in.bitstreet.com.itdwallet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import in.bitstreet.com.itdwallet.utills.Constants;

public class DataHelper {
    private Context mContext;
    private static SQLiteDatabase dbinstance;
    private static SQLHelper dbhelper;

    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;
    String userId;

    public DataHelper(Context con, SQLiteDatabase db) {
        mContext = con;
        dbinstance = db;
    }

    public DataHelper(Context context) {
        dbhelper = new SQLHelper(context);
    }

    public void open() throws SQLException {
        dbinstance = dbhelper.getWritableDatabase();
    }

    public void close() {
         dbinstance.close();
    }

  /* // changes on json structure and field on 27/12/2017 so below value is commented
    public void insertLoginUserInfo(String id, String firstName, String lastName, String handle,
                                    String email, String walletAddress, String signup_mode,
                                    String email_token, String pendingPaidInteractions,
                                    String creationTime, String securityMode, String isValid_inviteCode, String btc_balance,
                                    String balance, String updatedAt) {


        dbinstance.execSQL("delete from "
                + Constants.LOGIN_TABLE + ";");
        ContentValues values = new ContentValues();
        values.put(Constants.ID, id);
        values.put(Constants.FIRST_NAME, firstName);
        values.put(Constants.LAST_NAME, lastName);
        values.put(Constants.HANNDLE, handle);
        values.put(Constants.EMAIL_ID, email);
        values.put(Constants.WALLET_ADDRESS, walletAddress);
        values.put(Constants.SIGNUP_MODE, signup_mode);

        values.put(Constants.EMAIL_TOKEN, email_token);
        values.put(Constants.PENDING_PAID, pendingPaidInteractions);
        values.put(Constants.CREATION_TIME, creationTime);
        values.put(Constants.SECURITY_MODE, securityMode);
        //values.put(Constants.IS_VALID_INVITECODE, isValid_inviteCode);
        values.put(Constants.BTC_BALANCE, btc_balance);
        values.put(Constants.BALANCE, balance);

        values.put(Constants.UPDATED_AT, updatedAt);
        dbinstance.insert(Constants.LOGIN_TABLE, null,
                values);
    }
*/

   // changes on json structure and field on 27/12/2017 so below value is commented
    public void insertLoginUserInfo(String id, String firstName, String lastName, String handle,
                                    String email, String signup_token, String userId,
                                    String referralCode, String dob,
                                    String imgurl, String gender, String walletAddress, String updatedAt) {


        dbinstance.execSQL("delete from "
                + Constants.LOGIN_TABLE + ";");
        ContentValues values = new ContentValues();
        values.put(Constants.ID, id);
        values.put(Constants.FIRST_NAME, firstName);
        values.put(Constants.LAST_NAME, lastName);
        values.put(Constants.HANNDLE, handle);
        values.put(Constants.EMAIL_ID, email);
        values.put(Constants.SIGNUP_TOKEN, signup_token);
        values.put(Constants.USER_ID, userId);
        values.put(Constants.REFERRAL_CODE, referralCode);
        values.put(Constants.DOB, dob);
        values.put(Constants.IMAGE_URL, imgurl);
        values.put(Constants.GENDER, gender);
        values.put(Constants.WALLET_ADDRESS, walletAddress);
        values.put(Constants.UPDATED_AT, updatedAt);

        dbinstance.insert(Constants.LOGIN_TABLE, null,
                values);
    }


    public Cursor getUserInfo() {
        String sql = " select * from " + Constants.LOGIN_TABLE + " ;";
        return dbinstance.rawQuery(sql, null);

    }

    public void insertAdminSettingInfo(String sentAmt, String withDrawAmt, String depositAmt, String handle) {

        dbinstance.execSQL("delete from "
                + Constants.ADMIN_SETTINGS_TABLE + ";");
        ContentValues values = new ContentValues();

        values.put(Constants.MAX_SENT_AMT, sentAmt);
        values.put(Constants.MAX_WIDRAWAL_AMT, withDrawAmt);
        values.put(Constants.DEPOSIT_AMT, depositAmt);

        dbinstance.insert(Constants.ADMIN_SETTINGS_TABLE, null,
                values);
    }

    public Cursor geAdminSettingInfo() {
        String sql = " select * from " + Constants.ADMIN_SETTINGS_TABLE + " ;";
        return dbinstance.rawQuery(sql, null);

    }

    public void updateUserInfo( String firstName, String lastName, String handle,
                               String email, String walletAddress, String signup_mode, String signup_token,
                               String dob, String imgurl, String gender, String referralcode, String userID, String updatedAt) {

        try {
            String sql = "Update " + Constants.LOGIN_TABLE
                    + " set first_name ='" + firstName + "' , "
                    + " last_name = '" + lastName + "' , "
                    + " handle = '" + handle + "' , "
                    + " email_id = '" + email + "' , "
                    + " wallet_address = '" + walletAddress + "' , "
                    + " signup_token = '" + signup_token + "' , "
                    + " referral_code = '" + referralcode + "' , "
                    + " dob = '" + dob + "' , "
                    + " image_url = '" + imgurl + "' , "
                    + " gender = '" + gender + "' , "
                    + " updated_at = '" + updatedAt + "' "



                    + " where "
                    + Constants.USER_ID + " = '" + userID + "' ;";

            dbinstance.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList getUserInfoFromDB(Context ctx) {
        ArrayList userInfo = new ArrayList();
        db = new DataHelper(ctx);
        db.open();
        Cursor c = db.getUserInfo();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    userInfo.add(c.getString(c.getColumnIndex(Constants.USER_ID)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.FIRST_NAME)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.LAST_NAME)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.HANNDLE)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.EMAIL_ID)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.WALLET_ADDRESS)));




                } while (c.moveToNext());
            }

        }
        return userInfo;
    }


    public ArrayList getEditedUserInfoFromDB(Context ctx) {
        ArrayList userInfo = new ArrayList();
        db = new DataHelper(ctx);
        db.open();
        Cursor c = db.getUserInfo();
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    userInfo.add(c.getString(c.getColumnIndex(Constants.USER_ID)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.FIRST_NAME)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.LAST_NAME)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.HANNDLE)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.EMAIL_ID)));
                    userInfo.add(c.getString(c.getColumnIndex(Constants.WALLET_ADDRESS)));

                    if (c.getString(c.getColumnIndex(Constants.DOB))!=null && c.getString(c.getColumnIndex(Constants.DOB)).length()>0) {
                        userInfo.add(c.getString(c.getColumnIndex(Constants.DOB)));
                    }
                    if (c.getString(c.getColumnIndex(Constants.GENDER))!=null && c.getString(c.getColumnIndex(Constants.GENDER)).length()>0) {
                        userInfo.add(c.getString(c.getColumnIndex(Constants.GENDER)));
                    }
                    if (c.getString(c.getColumnIndex(Constants.IMAGE_URL))!=null && c.getString(c.getColumnIndex(Constants.IMAGE_URL)).length()>0) {
                        userInfo.add(c.getString(c.getColumnIndex(Constants.IMAGE_URL)));
                    }


                } while (c.moveToNext());
            }

        }

        return userInfo;
    }

    public String getUserImageFromDB(Context ctx) {
     String userInfoImg="";
        db = new DataHelper(ctx);
        db.open();
        Cursor c = db.getUserInfo();
        if (c != null) {
            if (c.moveToFirst()) {
                do {


                    if (c.getString(c.getColumnIndex(Constants.IMAGE_URL))!=null && c.getString(c.getColumnIndex(Constants.IMAGE_URL)).length()>0) {
                        userInfoImg = c.getString(c.getColumnIndex(Constants.IMAGE_URL));
                    }


                } while (c.moveToNext());
            }

        }

        return userInfoImg;
    }


/*
    public Bundle getAllMediaInfo(Cursor mCursor) {
		int i = 0;
		*/
/*
		 * Cursor mCursor; mCursor =
		 * getfilemaster();//MylanProviderRequest.getFilemasterFromMylanProvider
		 * (mContext,null,null,"FileId ASC,BrandId DESC");
		 *//*

		// String[][] medialist = new String[4][mCursor.getCount()];
		ArrayList<String> id = new ArrayList<String>();
		ArrayList<String> url = new ArrayList<String>();
		ArrayList<String> sdcardPath = new ArrayList<String>();
		ArrayList<String> localSize = new ArrayList<String>();
		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					try {
						id.add(mCursor.getString(mCursor
								.getColumnIndex(Constants.MEDIA_ID)));
						url.add(mCursor.getString(mCursor
								.getColumnIndex(Constants.HTTPURL)));
						sdcardPath.add(mCursor.getString(mCursor
								.getColumnIndex(Constants.SDCARDURL)));
						localSize.add(mCursor.getString(mCursor
								.getColumnIndex(Constants.LOCALFILESIZE)));
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
				} while (mCursor.moveToNext());
			}
		}
		mCursor.close();
		Bundle mediaList = new Bundle();
		mediaList.putStringArrayList("id", id);
		mediaList.putStringArrayList("url", url);
		mediaList.putStringArrayList("sdcardPath", sdcardPath);
		mediaList.putStringArrayList("localSize", localSize);
		return mediaList;
	}

	public Cursor getfilemaster(SQLiteDatabase db) {
		Cursor mCursor = null;
		try {
			String sql = "select * from MEDIA_URLS;";
			mCursor = db.rawQuery(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCursor;

	}

	public Cursor getfilemaster_downloadLog(SQLiteDatabase db) {
		Cursor mCursor = null;
		try {
			String sql = "select id,httpurl,sdcardurl,localsize,max(isdownloaded) as isdownloaded from MEDIA_URLS group by sdcardurl;";
			mCursor = db.rawQuery(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCursor;

	}

	public Cursor getUndownloadedFilesForDashboard(SQLiteDatabase db) {
		Cursor mCursor = null;
		try {
			String sql = "select * from " + Constants.TABLE_DOWNLOAD_MEDIA
					+ " where " + Constants.ISDOWNDLOADED
					+ " = 0 and slide_type in('5','6');";
			mCursor = db.rawQuery(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCursor;

	}

	public Cursor getUndownloadedFilesForBarnd(SQLiteDatabase db, String brandId) {
		Cursor mCursor = null;
		try {
			String sql = "select * from " + Constants.TABLE_DOWNLOAD_MEDIA
					+ " where " + Constants.ISDOWNDLOADED + " = 0;";
			mCursor = db.rawQuery(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCursor;

	}

	public void updatefilesizecoloumn(String fileid, String filesize,
			SQLiteDatabase db) {
		try {
			String sql = "Update " + Constants.TABLE_DOWNLOAD_MEDIA
					+ " set local_file_size ='" + filesize + "' where "
					+ Constants.MEDIA_ID + " = " + fileid + ";";
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ketan pareek
	public void setisdownloadedflag(String fileid, SQLiteDatabase db) {
		try {
			String sql = "Update " + Constants.TABLE_DOWNLOAD_MEDIA + " SET "
					+ Constants.ISDOWNDLOADED + " =1  where "
					+ Constants.MEDIA_ID + " = " + fileid + ";";
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setisundownloadedflag(String fileid, SQLiteDatabase db) {
		try {
			String sql = "Update MEDIA_URLS SET " + Constants.ISDOWNDLOADED
					+ " =0  where " + Constants.MEDIA_ID + " = " + fileid + ";";
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getURLCount(SQLiteDatabase db) {
		try {
			String sql = "select count(*) from "
					+ Constants.TABLE_DOWNLOAD_MEDIA + ";";
			Cursor mCursor = db.rawQuery(sql, null);
			if (mCursor.moveToFirst()) {
				return Integer.parseInt(mCursor.getString(0));
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}



	public Cursor getBrandsName() {
		String sql = " select * from " + Constants.TABLE_BRAND_MASTER + " ;";
		return dbinstance.rawQuery(sql, null);

	}

	// public void insertUserInfo(String name, String email, String city,
	// String phoneNo) {
	// dbinstance.execSQL("delete from "+Constants.TABLE_USER_INFO_MASTER+";");
	// ContentValues values = new ContentValues();
	// values.put(Constants.USER_NAME, name);
	// values.put(Constants.CITY, city);
	// values.put(Constants.EMAIL_ID, email);
	// values.put(Constants.PHONE_NO, phoneNo);
	// dbinstance.insert(Constants.TABLE_USER_INFO_MASTER, null, values);
	//
	// }

	// public Cursor selectAllValuesFromUserInfoMaster() {
	// String sql = " select * from "
	// + Constants.TABLE_USER_INFO_MASTER + " ;";
	// return dbinstance.rawQuery(sql, null);
	// }

	public void insertUserInfo(String name, String email, String city,
			String phoneNo) {

		dbinstance.execSQL("delete from "
				+ Constants.TABLE_REGISTRATION_INFO_MASTER + ";");
		ContentValues values = new ContentValues();
		values.put(Constants.USER_NAME, name);
		values.put(Constants.CITY, city);
		values.put(Constants.EMAIL_ID, email);
		values.put(Constants.PHONE_NO, phoneNo);
		dbinstance.insert(Constants.TABLE_REGISTRATION_INFO_MASTER, null,
				values);

	}

	public Cursor getFilterName() {
		String sql = " select " + Constants.TAG_NAME + " , " + Constants.TAG_ID
				+ " from " + Constants.TABLE_TAG_MASTER + " where "
				+ Constants.PARENT + " is  null  ;";
		return dbinstance.rawQuery(sql, null);
	}

	public Cursor getSubFilterName(int id) {
		String sql = " select " + Constants.TAG_NAME + " , " + Constants.TAG_ID
				+ " from " + Constants.TABLE_TAG_MASTER + " where "
				+ Constants.PARENT + " = " + id + " ;";
		return dbinstance.rawQuery(sql, null);
	}

	public Cursor setfilteredbrandsIds(String tagIdClicked) {

		String sql = " select " + Constants.SLIDE_ID + " from "
				+ Constants.TABLE_SLIDE_TAG_TYPE_RELATION_MASTER + " where "
				+ Constants.TAG_ID + " in ( " + tagIdClicked + " ) ;";
		return dbinstance.rawQuery(sql, null);

	}

	public Cursor setfilteredbrands(String slide_id) {

		String sql = " select bm.brand_id, brand_name,brand_info,sm.slide_id,sm.slide_name from brand_master bm join slide_brand_relation_master sbrm on ( sbrm.brand_id=bm.brand_id) join slide_master sm on ( sbrm.slide_id=sm.slide_id) where sm.slide_id in ( "
				+ slide_id + " );";
		return dbinstance.rawQuery(sql, null);

	}

	// to get the StoredSelection

	// to get count of all media files.
	public Cursor getCountFromDownloadMedia(SQLiteDatabase db) {

		String sql = "SELECT COUNT(*) FROM " + Constants.TABLE_DOWNLOAD_MEDIA
				+ ";";
		Cursor mCursor = db.rawQuery(sql, null);
		return mCursor;
	}

	// to get count of all downloaded files
	public Cursor getCountDownloadedFiles(SQLiteDatabase db) {

		String sql = "select COUNT(*) from " + Constants.TABLE_DOWNLOAD_MEDIA
				+ " where " + Constants.ISDOWNDLOADED + " = 1;";
		Cursor mCursor = db.rawQuery(sql, null);
		return mCursor;
	}

	public Cursor selectAllValuesFromUserInfoMaster() {
		String sql = " select * from "
				+ Constants.TABLE_REGISTRATION_INFO_MASTER + " ;";
		return dbinstance.rawQuery(sql, null);
	}

	public Cursor getslidedetails(Integer brandId) {
		String sql = " select * from " + Constants.TABLE_SLIDE_MASTER + " ;";
		return dbinstance.rawQuery(sql, null);
	}

	public Cursor getslidedetailsofFilter(Integer brandid, String slideid) {
		// TODO Auto-generated method stub
		String sql = " select sm.slide_id,sm.slide_name,sm.slide_url from  slide_brand_relation_master sbrm  join slide_master sm on ( sbrm.slide_id=sm.slide_id) where sm.slide_id in ( "
				+ slideid + ") and sbrm.brand_id in(" + brandid + ");";
		return dbinstance.rawQuery(sql, null);
	}

	public Cursor getFocusedSlides() {
		String sql = "select * from slide_master where slide_type ='5';";
		return dbinstance.rawQuery(sql, null);
	}

	public Cursor getschemeSlies() {
		// String sql = "select * from " + Constants.TABLE_SLIDE_MASTER +
		// " where " + Constants.SLIDE_TYPE +" = '"+ 3 +"';";
		String sql = "select * from slide_master where slide_type ='6';";
		return dbinstance.rawQuery(sql, null);
	}

	public void insertLoginUserInfo(String name, String pwd) {
		dbinstance.execSQL("delete from "
				+ Constants.TABLE_USER_LOGIN_INFO_MASTER + ";");
		ContentValues values = new ContentValues();
		values.put(Constants.NAME, name);
		values.put(Constants.PASSWORD, pwd);
		dbinstance.insert(Constants.TABLE_USER_LOGIN_INFO_MASTER, null, values);

	}

	public String getEmailName() {
		Cursor cur = selectAllValuesFromUserInfoMaster();
		String email = "";
		if (cur != null) {
			if (cur.moveToNext()) {
				do {
					email = cur.getString(cur
							.getColumnIndex(Constants.EMAIL_ID));
				} while (cur.moveToNext());
			}
		}

		return email;
	}

	public Cursor getSlideMrp() {
		String sql = " select * from " + Constants.TABLE_SLIDE_MASTER + " ;";
		return dbinstance.rawQuery(sql, null);
	}

	public Cursor getUndownloadedFilesForSlides(SQLiteDatabase db,
			String slideIds) {

		String sql = "select * from " + Constants.TABLE_DOWNLOAD_MEDIA
				+ " where " + Constants.ISDOWNDLOADED + " = 0 and "
				+ Constants.MEDIA_ID + " in(" + slideIds + ");";
		return db.rawQuery(sql, null);

	}
	*/
}




