package il.ac.hit.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DatabaseHandler extends SQLiteOpenHelper {
    // create DB
    private SQLiteDatabase db;
    private Context context;
    public static final String DATABASE_NAME = "Todo.db";
    private static final String TABLE_ITEMS = "to_do_items";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_TODO = "CREATE TABLE " + TABLE_ITEMS + "("+Columns.I_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+Columns.I_COL_2+" TEXT,"+Columns.I_COL_3+" TEXT,"+Columns.I_COL_4+" INTEGER)";

    abstract class Columns{
        //DB columns
        private static final String I_COL_1 = "serialnum";
        private static final String I_COL_2 = "date";
        private static final String I_COL_3 = "name";
        private static final String I_COL_4 = "userid";

    }

    /***
     * Constructor
     * @param context
     */
    public DatabaseHandler(Context context) {

        //calling SQLiteOpenHelper constructor
        super(context,DATABASE_NAME,null,DATABASE_VERSION );

        //set which activity is using the
        this.context = context;

        /*  must be last line in the constructor,
            in order to make sure all variables are initialized prior to onCreate() */
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        //create todo table
        db.execSQL(CREATE_TABLE_TODO);


        this.showToast(this.context,"initialized");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ITEMS +"'");
        onCreate(db);
    }

    private void showToast(Context context, String t) {
        Toast.makeText(context,t,Toast.LENGTH_LONG).show();
    }

    public void deleteToDo(String date, String name)
    {
        //todo to be removed from the DB
        String whereClause = "date=? AND name =?" ;
        String whereArgs[] = new String[] {date, name};
        //make the deletion - show toast with msg
        if (db.delete(TABLE_ITEMS,whereClause,whereArgs)==0) {
            showToast(context,"Error: activity doesnt appear in the db");
        } else {
            showToast(context, "Successfully deleted");
        }
    }


    public void updateToDo(String date, String name,String updateDate, String updateName)
    {
        ContentValues values = new ContentValues();
        String whereArgs[] = new String[] {date, name};
        String whereClause = "date=? AND name =?" ;

        //fill in the values
        values.put(Columns.I_COL_2, updateDate);
        values.put(Columns.I_COL_3, updateName);
        if (db.update(TABLE_ITEMS,values,whereClause,whereArgs)==0) {
            showToast(context,"Error: activity doesnt appear in the db");
        } else {
            showToast(context, "Successfully updated");
        }
    }


    public void addItem(int userId, String date,  String name )
    {


        //initialize ContentValues object
        ContentValues values = new ContentValues();


        values.put(Columns.I_COL_2, date);
        values.put(Columns.I_COL_3, name);
        values.put(Columns.I_COL_4, userId);

        //make the insertion to the db
        if (db.insert(TABLE_ITEMS,null,values)!=-1)
        {
            this.showToast(this.context,"Item added");
        } else {
            this.showToast(this.context,"Error: try again to add");
        }
    }
    public String getItems() throws JSONException
    {       //load todo from DB
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();

        json.put("toDoItems",arr);

        String query = "SELECT * FROM "+TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        try{
            while (cursor.moveToNext())
            {
                JSONObject item = new JSONObject();
                item.put("serial",cursor.getString(cursor.getColumnIndex("serialnum")));
                item.put("id",cursor.getString(cursor.getColumnIndex("userid")));
                item.put("item",cursor.getString(cursor.getColumnIndex("name")));
                item.put("date",cursor.getString(cursor.getColumnIndex("date")));
                arr.put(item);


            }
        }
        finally {
            cursor.close();
        }
        return json.toString();
    }
}
