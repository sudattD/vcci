package vcci.android.consumer.app_update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

public class GooglePlayStoreAppVersionNameLoader extends AsyncTask<String, Void, String> {

    String newVersion = "";
    String currentVersion = "";
    WSCallerVersionListener mWsCallerVersionListener;
    boolean isVersionAvailabel;
    boolean isAvailableInPlayStore;
    Context mContext;
    String mStringCheckUpdate = "";

    public GooglePlayStoreAppVersionNameLoader(Context mContext, WSCallerVersionListener callback) {
        mWsCallerVersionListener = callback;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            isAvailableInPlayStore = true;
            if (isNetworkAvailable(mContext)) {
                mStringCheckUpdate = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.ibphub.gcciapp" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
                return mStringCheckUpdate;
            }

        } catch (Exception e) {
            isAvailableInPlayStore = false;
            return mStringCheckUpdate;
        } catch (Throwable e) {
            isAvailableInPlayStore = false;
            return mStringCheckUpdate;
        }
        return mStringCheckUpdate;
    }

    @Override
    protected void onPostExecute(String newVersionNo) {
        isVersionAvailabel = false;
        if (isAvailableInPlayStore) {
            newVersion = newVersionNo;
            Log.e("new Version", newVersion);
            checkApplicationCurrentVersion();
            if (newVersion != null && !newVersion.isEmpty()) {
                isVersionAvailabel = !currentVersion.equalsIgnoreCase(newVersion);
            }
            Log.d("isVersionAvailabel", "isVersionAvailabel: " + isVersionAvailabel);
            mWsCallerVersionListener.onGetResponse(isVersionAvailabel);
        }
    }

    /**
     * Method to check current app version
     */
    public void checkApplicationCurrentVersion() {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = packageInfo.versionName;
        Log.e("currentVersion", currentVersion);
    }

    /**
     * Method to check internet connection
     *
     * @param context
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
