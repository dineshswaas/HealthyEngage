package Repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import DataBase.DatabaseHandler;

public class CarePlanHeaderRepository {
    private DatabaseHandler dbHandler=null;
    private SQLiteDatabase database = null;
    private Context mContext;
    String id,plan_no,title,goal,problem,plan_start_date,plan_end_date,current_cycle_start_date,current_cycle_end_date;
    String name,color,dosage,instructions,total_days,frequency;
    public CarePlanHeaderRepository (Context context){
        dbHandler = new DatabaseHandler(context);
        mContext = context;
    }

    public void DBConnectionOpen()  {
        database = dbHandler.getWritableDatabase();
    }

    public void DBConnectionClose(){
        if(database.isOpen()) {
            database.close();
        }
    }
    public static String CreateCarePlanHeaderTable() {
        String CREATE_WORK_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS tbl_sfa_care_plan_header ( _Id  INTEGER PRIMARY KEY," +
                " plan_no TEXT," +
                "title TEXT," +
                "goal TEXT," +
                "problem TEXT," +
                "plan_start_date TEXT," +
                "plan_end_date TEXT," +
                "current_cycle_start_date TEXT" +
                ",current_cycle_end_date TEXT)";
        return CREATE_WORK_CATEGORY_TABLE;
    }
}
