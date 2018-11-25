package tw.com.lccnet.app.designateddriving.API.Analyze;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_BIRTHDAY;
import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_CMP;
import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_CONTACT;
import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_EMAIL;
import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_PICTURE;
import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_SEX;
import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_TOKEN;
import static tw.com.lccnet.app.designateddriving.Utils.SQLiteDatabaseHandler.KEY_UNAME;

public class AnalyzeCustomer {

    /**
     * 1.1.3	登入
     */
    public static ContentValues getLogin(JSONObject json) {
        try {
            ContentValues cv = new ContentValues();
            if (json.getBoolean("Success")) {
                JSONObject jsonObject = json.getJSONObject("Data");
                cv.put(KEY_TOKEN, (jsonObject.getString(KEY_TOKEN)));
                cv.put(KEY_UNAME, (jsonObject.getString(KEY_UNAME)));
                cv.put(KEY_PICTURE, (jsonObject.getString(KEY_PICTURE)));
                cv.put(KEY_SEX, (jsonObject.getString(KEY_SEX)));
                cv.put(KEY_BIRTHDAY, (jsonObject.getString(KEY_BIRTHDAY)));
                cv.put(KEY_EMAIL, (jsonObject.getString(KEY_EMAIL)));
                cv.put(KEY_CONTACT, (jsonObject.getString(KEY_CONTACT)));
                cv.put(KEY_CMP, (jsonObject.getString(KEY_CMP)));
                return cv;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}