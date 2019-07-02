package il.ac.hit.todolist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.*;

import com.example.todolist.R;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    WebView browser;
    public DatabaseHandler myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHandler(this);

        browser = (WebView) findViewById(R.id.webView);
        browser.setWebViewClient(new WebViewClient());
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().getAllowUniversalAccessFromFileURLs();
        browser.setWebContentsDebuggingEnabled(true);
        if (isConnected()) {
            browser.loadUrl("http://10.0.2.2:8080/todolistcontroller/user/login");
        } else {
            browser.loadUrl("file:///android_asset/userinterface.html");
            browser.getSettings().setDomStorageEnabled(true);
            browser.addJavascriptInterface(new addInteraction(), "addToDb");                   //add todo
            browser.addJavascriptInterface(new getItems(), "getDataFromDB");           //get items
            browser.addJavascriptInterface(new deleteInteraction(), "deleteToDoFromDb");  //delete item
            browser.addJavascriptInterface(new updateInteraction(), "updateDB"); //update item
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        browser.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        browser.restoreState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public class addInteraction {       //call to DB function -- adding
        @JavascriptInterface
        public void addItem(String date, String name) {
            myDb.addItem(1, date, name);       //db

        }
    }

    public class getItems {       //call to DB function   -- get all data
        @JavascriptInterface
        public String getItems() throws JSONException {

            return myDb.getItems();
        }
    }

    public class deleteInteraction {       //call to DB function   -- delete item

        @JavascriptInterface
        public void deleteToDo(String date, String name) {
            //TODO add filter to filter out other accounts (user_id)
            myDb.deleteToDo(date, name);
        }
    }

    public class updateInteraction {       //call to DB function     -- edit todo from list

        @JavascriptInterface
        public void updateToDo(String date, String name, String updateDate, String updateName) {
            //TODO add filter to filter out other accounts (user_id)
            myDb.updateToDo(date, name, updateDate, updateName);
        }
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
