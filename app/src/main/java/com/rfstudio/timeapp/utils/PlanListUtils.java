package com.rfstudio.timeapp.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.rfstudio.timeapp.utilsModel.PlanModel;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * 处理与 planlist 相关的 工具类
 * 包括，排序，查找
 *
 * 静态的
 */
public class PlanListUtils {

    /**
     *  将无需List y依照id 递增排序
     *
     * @param planlist
     * @return
     */
    public static ArrayList<PlanModel> sortList(ArrayList<PlanModel> planlist){
        Collections.sort(planlist, new Comparator<PlanModel>() {
            @Override
            public int compare(PlanModel o1, PlanModel o2) {
                if (o1.getId()== null | o2.getId() == null)
                    return 0;
                return o1.getId().compareTo(o2.getId());

            }
        });

        showList(planlist);
        return planlist;

    }

    /**
     * 获取当前 开始/正在运行的Item
     * @return  nowPlanItem
     */
    public static PlanModel getNowPlanItem(ArrayList<PlanModel> planlist){

        // 先排序
        planlist = sortList(planlist);
        // 查找已开始的项目 直到找到 开始中的最大的
        Long nowTime = System.currentTimeMillis();
        PlanModel planModel = null;
        // 没有数据，就返回null
        if (planlist.size() == 0)
            return null;
        // 有数据，返回已开始时间中最大的
        for (PlanModel temp :planlist
             ) {
            if (temp.getDuring().getStartTimeMillis() <= nowTime){
                if (temp.getDuring().getStartTimeMillis() == nowTime)
                    return temp;
                if (temp.getDuring().getEndTimeMillis() > nowTime)  // 已开始但 没有结束
                    planModel = temp;
            }else {
                // 大于的时候，就不用再找了
                break;
            }

        }

        return planModel;

    }

    /**
     * 测试 排序后的结果
     * @param planlist
     */
    public  static  void showList(ArrayList<PlanModel> planlist){
        Iterator i = planlist.iterator();
        while (i.hasNext()){
            Log.d("PlanList 排序结果------",i.next().toString());
        }
    }

}
