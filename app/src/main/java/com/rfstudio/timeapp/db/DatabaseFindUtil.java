package com.rfstudio.timeapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rfstudio.timeapp.R;
import com.rfstudio.timeapp.utils.TimeStruct;
import com.rfstudio.timeapp.utilsModel.PlanModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class DatabaseFindUtil {
    Context context;
    ImportDatabaseUtil importDatabaseUtil;
    //构造函数
    public DatabaseFindUtil(Context context) {
        this.context = context;
        importDatabaseUtil = new ImportDatabaseUtil(context);
    }
    //检查listAll是否可用
    String guideSQL_1 = "INSERT INTO DailyPlanTable(planType,dothing,startTime,endTime) SELECT DothingType.type,Dothing.thing,startTime,endTime FROM Dothing,During,DothingType INNER JOIN ( SELECT during_id,dothing_id FROM During_Dothing WHERE dothing_id IN ( SELECT dothing_id FROM Dothing WHERE type_id ==";
    String guideSQL_2 = "AND id not in(1,2,3,6,7)) AND during_id in (";
    String guideSQL_3 = ") GROUP BY dothing_id ORDER BY percentage DESC LIMIT 5 ) AS b ON During.id = b.during_id AND Dothing.id = b.dothing_id WHERE DothingType.id ==";
    String referralSQL = "SELECT DothingType.type,Dothing.thing,startTime,endTime FROM DothingType,Dothing,During INNER JOIN( SELECT during_id,dothing_id FROM During_Dothing ORDER BY percentage DESC) AS All_During_Dothing ON During.id = All_During_Dothing.during_id AND Dothing.id = All_During_Dothing.dothing_id";

    //引导页查询
    public ArrayList<PlanModel> guidePageQuery(int dothingTypeId , char[] during){
        StringBuffer guideSQL = new StringBuffer();
        //以下操作粘接SQL语句
        guideSQL.append(guideSQL_1);
        guideSQL.append(dothingTypeId+"\n");
        guideSQL.append(guideSQL_2);
        for (int i=0;i<during.length;i++){
            guideSQL.append(during[i]);
            if (i<during.length-1){
                guideSQL.append(',');
            }
        }
        guideSQL.append(guideSQL_3);
        guideSQL.append((dothingTypeId));
        String guideSQLResult = guideSQL.toString();
        //以上操作粘接SQL语句
        ArrayList<PlanModel> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = importDatabaseUtil.getDatabase();       //获取数据库
        sqLiteDatabase.execSQL(guideSQLResult);                                 //执行查询语句（insert）
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM DailyPlanTable",null);
        while(cursor.moveToNext()){
            PlanModel planModel = new PlanModel();
            planModel.setDoThing(cursor.getString(cursor.getColumnIndex("dothing")));
            planModel.setDuring(new TimeStruct(cursor.getString(cursor.getColumnIndex("startTime")),cursor.getString(cursor.getColumnIndex("endTime"))));
            list.add(planModel);
        }
        return list;
    }

    //生成全部推荐内容
    public ArrayList<PlanModel> referralPageQuery(int count){
        ArrayList<PlanModel> listAll = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = importDatabaseUtil.getDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(referralSQL,null);
        while (cursor.moveToNext()){
            PlanModel planModel = new PlanModel();
            planModel.setDuring(new TimeStruct(cursor.getString(cursor.getColumnIndex("startTime")),cursor.getString(cursor.getColumnIndex("endTime"))));
            planModel.setDoThing(cursor.getString(1));
            listAll.add(planModel);
        }
        ArrayList<PlanModel> list = new ArrayList<>();
        Random random = new Random();       //随机数获取对象
        for (int i=0;i<count;i++){
            list.add(listAll.get(random.nextInt(listAll.size())));
        }
        return list;
    }
}
