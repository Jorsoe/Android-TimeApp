package com.rfstudio.timeapp.utilsModel;

import com.rfstudio.timeapp.utils.FileManager;

/**
 * 本应用的相关配置
 * 可自行添加配置信息
 *
 * 注意： 一旦修改/添加/删除 相关属性，需要到FileManager 中更改相关的逻辑代码
 *
 * 推荐使用： FileManager.buildConfigInfoModel（filename） 来构建
 */
public class ConfigInfoModel {
    public boolean isNewAccount;

    public ConfigInfoModel() {
        isNewAccount = true;

    }
}
