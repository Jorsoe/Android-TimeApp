package com.rfstudio.timeapp.utils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.rfstudio.timeapp.application.MyApplication;
import com.rfstudio.timeapp.utilsModel.PlanModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于对 存档 的管理
 * 包含文件数据的 读 和 写 ，
 *
 * 以pullParser 来实现
 */
public class FileManager {
    public static String savefileName = "SaveFile.xml";
    public static String configfileName = "config.xml";

    private enum PLANMODEL_PARAM_ENUM{
        id,
        doThing,
        startTime,
        endTime,
        isBellAlert,
        isShock,
        isOpen,
        isExecuted

    }

    private Context context;

    /**
     * 初始化，需要applicationContext
     * 在可使用 getApplicationContext（）情况下 作为参数传入
     * @param context 请使用ApplicationContext
     */
    public FileManager(Context context) {
        MyApplication application = new MyApplication();
        this.context = context;
        Log.e("File", "来不: " + context.getExternalFilesDir(null).getPath());

    }

    /**
     *  获取应用根目录
     *  （自身有Context）
     * @return 返回 应用数据 根目录
     */
    public  String getDiskDirPath(){

        String dataPath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || ! Environment.isExternalStorageRemovable()){
            // 如果外存卡 存在
            dataPath = context.getExternalFilesDir(null).getPath();
        }else{
            dataPath = context.getFilesDir().getPath();
        }

        return dataPath;
    }

    /**
     *  判定 指定文件 是否存在
     *  当不存在时，会自行生成
     * @param fileName
     * @return true/false
     */
    public boolean fileIsExisted(String fileName){
        try{

            File file = new File(getDiskDirPath(), fileName);
            if (!file.exists()){
                // 如果不存在，会自行创建

                return false;
            }
        }catch (Exception e){
            Log.e("文件存储："+getClass().getName().toString() , "出现异常： " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     *
     * 添加list到文件
     * 增删改 只需要改变planlist即可
     * @param planlist 计划列表
     * @param fileName 要存储的文件名
     */
    public void addPlanListInXmlFile (ArrayList<PlanModel> planlist , String fileName){
        if (fileIsExisted(fileName))
            Log.i(getClass().getName(),"文件存在");
        else
            Log.e(getClass().getName(),"文件不存在");

        try {
            // 获取文件
            File file = new File(getDiskDirPath(),fileName);
            // 打开流
            FileOutputStream fos = new FileOutputStream(file);
            // 序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos,"utf-8");
            // 设置头
            serializer.startDocument("utf-8",true);
            serializer.text("\n");
            // 文档开始 <PlanList>
            serializer.startTag(null,"PlanList");
            serializer.text("\n");
            // 内部元素
            if (writeListInFor(planlist,serializer)){

                // 结尾
                serializer.endTag(null,"PlanList");
                serializer.endDocument();
                Toast.makeText(context,"写入List到xml成功！",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(context,"写入List到xml出错了！",Toast.LENGTH_LONG).show();
                Log.e(getClass().getName(),"看来出错了");
            }


            serializer.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取Planlist的文件，并返回planlist
     * @param fileName 同样的savefile
     * @return planList : ArrayList<PlanModel>
     */
    public ArrayList<PlanModel> getPlanlistInXmlFile(String fileName){
        if (fileIsExisted(fileName))
            Log.i(getClass().getName(),"文件存在");
        else{
            Log.e(getClass().getName(),"文件不存在");
            Toast.makeText(context,"抱歉文件不存在,为空",Toast.LENGTH_LONG);
        }

        ArrayList<PlanModel> planList = new ArrayList<PlanModel>();

        try{
            // 获取文件
            File file = new File(getDiskDirPath(),fileName);
            // 打开流
            FileInputStream fis = new FileInputStream(file);
            // 获得pull解析器
            XmlPullParser parser = Xml.newPullParser();
            // 指定编码
            parser.setInput(fis,"utf-8");
            // 获取事件类型
            int eventType = parser.getEventType();
            // 开始获取
            while (eventType != XmlPullParser.END_DOCUMENT){ // 如果当前没有到文档底部
                // 获取当前节点名
                String tagName = parser.getName();
                String st = null;
                String et = null;
                PlanModel planItem = null;
                switch (eventType){
                    case XmlPullParser.START_TAG : // 开始节点
                        // 匹配赋值
                        switch (tagName){
                            case "PlanList":
                                break;
                            case "PlanItem":
                                planItem = new PlanModel();
                                planItem.setId(Integer.parseInt(parser.getAttributeValue(null,"id")));
                                break;
                            case "dothing":
                                parser.nextText();
                                planItem.setDoThing(parser.nextText());
                                break;
                            case "startTime":
                                parser.nextText();
                                st = parser.nextText();
                                break;
                            case "endTime":
                                parser.nextText();
                                et = parser.nextText();
                                break;
                            case "isBellAlert":
                                parser.nextText();
                                planItem.isBellAlert = Boolean.parseBoolean(parser.nextText());
                                break;
                            case "isShock":
                                parser.nextText();
                                planItem.isShock = Boolean.parseBoolean(parser.nextText());
                                break;
                            case "isOpen":
                                parser.nextText();
                                planItem.isOpen = Boolean.parseBoolean(parser.nextText());
                                break;
                            case "isExecuted":
                                parser.nextText();
                                planItem.isExecuted = Boolean.parseBoolean(parser.nextText());
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG: // 结束tag
                         if ("PlanItem".equals(tagName)){
                             // 读完了一项
                             TimeStruct ts = new TimeStruct(st,et);
                             planItem.setDuring(ts);
                             planList.add(planItem);
                         }
                         break;
                }// switch
                eventType = parser.next();
            }

            fis.close();
            Log.e("读取完毕","xml读取完毕");

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return planList;
    }



    /**
     *  单纯的用来循环添加 planlist
     *  仅包含planlist的部分，无文档的上下标签
     *  外面需要完成 头设置 以及 文档头标签
     * @param planlist
     * @param serializer 已设置过头，以及 文档开始标签
     * @return 成功返回 true
     */
    private boolean writeListInFor(ArrayList<PlanModel> planlist, XmlSerializer serializer){

        boolean isOK = false;
        try{
            PlanModel planItem = null;
            for (int i = 0 ; i < planlist.size() ; i++ ){
                serializer.text("\t");
                planItem = planlist.get(i);
                // <PlanItem>
                serializer.startTag(null,"PlanItem");
                serializer.attribute(null,"id",String.valueOf(i));
                serializer.text("\n\t\t");
                // 内容
                serializer.startTag(null,PLANMODEL_PARAM_ENUM.doThing.name());
                serializer.text(planItem.getDoThing());
                serializer.endTag(null,PLANMODEL_PARAM_ENUM.doThing.name());
                serializer.text("\n\t\t");
                // 开始时间
                serializer.startTag(null,PLANMODEL_PARAM_ENUM.startTime.name());
                serializer.text(planItem.getDuring().startTime);
                serializer.endTag(null,PLANMODEL_PARAM_ENUM.startTime.name());
                serializer.text("\n\t\t");
                // 结束时间
                serializer.startTag(null,PLANMODEL_PARAM_ENUM.endTime.name());
                serializer.text(planItem.getDuring().endTime);
                serializer.endTag(null,PLANMODEL_PARAM_ENUM.endTime.name());
                serializer.text("\n\t\t");
                // 是否响铃
                serializer.startTag(null,PLANMODEL_PARAM_ENUM.isBellAlert.name());
                serializer.text(String.valueOf(planItem.isBellAlert));
                serializer.endTag(null,PLANMODEL_PARAM_ENUM.isBellAlert.name());
                serializer.text("\n\t\t");
                // 是否震动
                serializer.startTag(null,PLANMODEL_PARAM_ENUM.isShock.name());
                serializer.text(String.valueOf(planItem.isShock));
                serializer.endTag(null,PLANMODEL_PARAM_ENUM.isShock.name());
                serializer.text("\n\t\t");
                // 是否开启
                serializer.startTag(null,PLANMODEL_PARAM_ENUM.isOpen.name());
                serializer.text(String.valueOf(planItem.isOpen));
                serializer.endTag(null,PLANMODEL_PARAM_ENUM.isOpen.name());
                serializer.text("\n\t\t");
                // 是否已执行
                serializer.startTag(null,PLANMODEL_PARAM_ENUM.isExecuted.name());
                serializer.text(String.valueOf(planItem.isExecuted));
                serializer.endTag(null,PLANMODEL_PARAM_ENUM.isExecuted.name());
                serializer.text("\n\t\t");
                // </PlanItem>
                serializer.endTag(null,"PlanItem");
                serializer.text("\n");
            }

            isOK = true;
        }catch (IOException e) {
            e.printStackTrace();
        }

        return isOK;
    }
}
