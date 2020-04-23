package com.rfstudio.timeapp.utils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.rfstudio.timeapp.application.MyApplication;
import com.rfstudio.timeapp.utilsModel.ConfigInfoModel;
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

    private enum CONFIG_PARAM_ENUM{
        Config,
        Content,
        isNewAccount,
        ringtoneIndex
    }

    private Context context;

    /**
     * 初始化，需要applicationContext
     * 在可使用 getApplicationContext（）情况下 作为参数传入
     * 建议 可以使用其静态方法
     * @param context 请使用ApplicationContext
     */
    public FileManager(Context context) {
        this.context = context;
        Log.e("File", "来不: " + context.getExternalFilesDir(null).getPath());

    }

    /**
     *  获取应用根目录
     *
     *  @param context 请使用ApplicationContext
     * @return 返回 应用数据 根目录
     */
    public static String getDiskDirPath(Context context){

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
     * @param context 请使用ApplicationContext
     * @return true/false
     */
    public static boolean fileIsExisted(String fileName ,Context context){
        try{

            File file = new File(getDiskDirPath(context), fileName);
            if (!file.exists()){
                // 如果不存在，会自行创建

                return false;
            }
        }catch (Exception e){
            Log.e("文件存储："+Log.getStackTraceString(e) , "出现异常： " + e.getMessage());
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
     * @param context 请使用ApplicationContext
     */
    public static void addPlanListInXmlFile (ArrayList<PlanModel> planlist , String fileName,Context context){
        if (fileIsExisted(fileName,context))
            Log.i(FileManager.class.getName(),"文件存在");
        else
            Log.e(FileManager.class.getName(),"文件不存在");

        try {
            // 获取文件
            File file = new File(getDiskDirPath(context),fileName);
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
                Log.e(FileManager.class.getName(),"看来出错了");
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
     * @param context 请使用ApplicationContext
     * @return planList : ArrayList<PlanModel>
     */
    public static ArrayList<PlanModel> getPlanlistInXmlFile(String fileName , Context context){
        if (fileIsExisted(fileName,context))
            Log.i(FileManager.class.getName(),"文件存在");
        else{
            Log.e(FileManager.class.getName(),"文件不存在");
            Toast.makeText(context,"抱歉文件不存在,为空",Toast.LENGTH_LONG);
        }

        ArrayList<PlanModel> planList = new ArrayList<PlanModel>();

        try{
            // 获取文件
            File file = new File(getDiskDirPath(context),fileName);
            // 打开流
            FileInputStream fis = new FileInputStream(file);
            // 获得pull解析器
            XmlPullParser parser = Xml.newPullParser();
            // 指定编码
            parser.setInput(fis,"utf-8");
            // 获取事件类型
            int eventType = parser.getEventType();

            PlanModel planItem = null;
            String st = null;
            String et = null;
            TimeStruct ts = null;

            // 开始获取
            while (eventType != XmlPullParser.END_DOCUMENT){ // 如果当前没有到文档底部
                // 获取当前节点名
                String tagName = parser.getName();
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:  // 头部
                        break;
                    case XmlPullParser.START_TAG : // 开始节点
                        // 匹配赋值
                        if ("PlanItem".equals(tagName)){ // 如果是一个项目开始，则属性赋值
                            // 空间
                            planItem = new PlanModel();
                            // 赋值
                            planItem.setDoThing(parser.getAttributeValue(null,PLANMODEL_PARAM_ENUM.doThing.name()));         //doThing
                            // 时间段
                            st = parser.getAttributeValue(null,PLANMODEL_PARAM_ENUM.startTime.name());
                            et = parser.getAttributeValue(null,PLANMODEL_PARAM_ENUM.endTime.name());
                            ts = new TimeStruct(st,et);
                            planItem.setDuring(ts);
                            // 赋值boolean
                            planItem.isBellAlert = Boolean.valueOf(parser.getAttributeValue(null,PLANMODEL_PARAM_ENUM.isBellAlert.name())); // isBellAlert
                            planItem.isShock = Boolean.valueOf(parser.getAttributeValue(null,PLANMODEL_PARAM_ENUM.isShock.name())); // isShock
                            planItem.isOpen = Boolean.valueOf(parser.getAttributeValue(null,PLANMODEL_PARAM_ENUM.isOpen.name())); // isOpen
                            planItem.isExecuted = Boolean.valueOf(parser.getAttributeValue(null,PLANMODEL_PARAM_ENUM.isExecuted.name())); // isExecuted

                            // 完毕后 在结尾tag添加到list
                        }
                        break;
                    case XmlPullParser.END_TAG: // 结束tag
                         if ("PlanItem".equals(tagName)){
                             // 读完了一项
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
    private static boolean writeListInFor(ArrayList<PlanModel> planlist, XmlSerializer serializer){

        boolean isOK = false;
        try{
            PlanModel planItem = null;
            for (int i = 0 ; i < planlist.size() ; i++ ){
                serializer.text("\t");
                planItem = planlist.get(i);
                // <PlanItem>
                serializer.startTag(null,"PlanItem");
                serializer.attribute(null,PLANMODEL_PARAM_ENUM.id.name(),String.valueOf(i));        // 不是planmode 的id
                //serializer.text("\n\t\t");
                serializer.attribute(null,PLANMODEL_PARAM_ENUM.doThing.name(),planItem.getDoThing());
                serializer.attribute(null,PLANMODEL_PARAM_ENUM.startTime.name(),planItem.getDuring().getStartTime());
                serializer.attribute(null,PLANMODEL_PARAM_ENUM.endTime.name(),planItem.getDuring().getEndTime());
                serializer.attribute(null,PLANMODEL_PARAM_ENUM.isBellAlert.name(),String.valueOf(planItem.isBellAlert));
                serializer.attribute(null,PLANMODEL_PARAM_ENUM.isShock.name(),String.valueOf(planItem.isShock));
                serializer.attribute(null,PLANMODEL_PARAM_ENUM.isOpen.name(),String.valueOf(planItem.isOpen));
                serializer.attribute(null,PLANMODEL_PARAM_ENUM.isExecuted.name(),String.valueOf(planItem.isExecuted));

                /*
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

                 */
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


    /**
     * 更新/写入 配置文件 config.xml
     * 前提文件已存在
     * @param configInfoModel 可以是个空白configInfoModel 对象
     * @param fileName 最好为 FileManager.configFileName
     * @param context 请使用ApplicationContext
     * @return true：更新成功 ，false：更新失败
     */
    public static boolean updateConfigFile(ConfigInfoModel configInfoModel,String fileName,Context context){
        boolean isOk = false;

        try {
            // 获取文件
            File file = new File(getDiskDirPath(context),fileName);
            // 打开流
            FileOutputStream fos = new FileOutputStream(file);
            // 序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos,"utf-8");
            // 设置头
            serializer.startDocument("utf-8",true);
            serializer.text("\n");
            // 文档开始 <Config>
            serializer.startTag(null,CONFIG_PARAM_ENUM.Config.name());
            serializer.text("\n");

            // 内容
            serializer.startTag(null,CONFIG_PARAM_ENUM.Content.name());

            serializer.attribute(null,CONFIG_PARAM_ENUM.isNewAccount.name(),String.valueOf(configInfoModel.isNewAccount));
            serializer.attribute(null,CONFIG_PARAM_ENUM.ringtoneIndex.name(),String.valueOf(configInfoModel.ringtoneIndex));

            serializer.endTag(null,CONFIG_PARAM_ENUM.Content.name());


            // 结尾
            serializer.text("\n");
            serializer.endTag(null,CONFIG_PARAM_ENUM.Config.name());
            serializer.endDocument();
            Toast.makeText(context,"写入config到xml成功！",Toast.LENGTH_LONG).show();


            // 结束
            serializer.flush();
            fos.close();
            isOk = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isOk;

    }

    /**
     *  从文件读取config，并返回ConfigInfoModel
     *  前提 文件中已有数据
     * @param fileName 最好为 FileManager.configFileName
     * @param context 请使用ApplicationContext
     * @return 返回构建好的ConfigInfoModel
     */
    public static ConfigInfoModel getConfigInfoModel (String fileName,Context context){
        ConfigInfoModel configInfoModel = null;

        try{
            // 获取文件
            File file = new File(getDiskDirPath(context),fileName);
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
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:  // 头部
                        break;
                    case XmlPullParser.START_TAG : // 开始节点
                        // 内容
                        if (CONFIG_PARAM_ENUM.Content.name().equals(tagName)){ // 如果是content 标签，取值
                            configInfoModel = new ConfigInfoModel();
                            configInfoModel.isNewAccount = Boolean.valueOf(parser.getAttributeValue(null,CONFIG_PARAM_ENUM.isNewAccount.name()));
                            configInfoModel.ringtoneIndex = Integer.parseInt(parser.getAttributeValue(null,CONFIG_PARAM_ENUM.ringtoneIndex.name()));
                        }
                        break;
                    case XmlPullParser.END_TAG: // 结束tag
                        break;
                }// switch
                eventType = parser.next();
            }

            fis.close();
            Log.e("读取完毕","Config.xml读取完毕");

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return configInfoModel;
    }


    /**
     *  根据config文件 来构建一个 ConfigInfoModel,
     *  无文件/失败 将会是默认属性，
     *
     *  最终会生成一个有内容的文件
     * @param fileName
     * @param context 请使用ApplicationContext
     * @return ConfigInfoModel
     */
    public static ConfigInfoModel buildConfigInfoModel(String fileName,Context context){
        ConfigInfoModel configInfoModel = null;

        if (!fileIsExisted(fileName,context)){
            // 文件不存在，则为标准的ConfigInfoModel
            configInfoModel = new ConfigInfoModel();
            updateConfigFile(configInfoModel,fileName,context);
        }else {
            // 读取文件 生成对象
            configInfoModel = getConfigInfoModel(fileName,context);

            if (configInfoModel == null ){
                // 对象为空 表示读取失败，则 为标准
                configInfoModel = new ConfigInfoModel();
                updateConfigFile(configInfoModel,fileName,context);
            }
        }

        return configInfoModel;
    }
}
