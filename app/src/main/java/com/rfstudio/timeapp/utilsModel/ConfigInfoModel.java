package com.rfstudio.timeapp.utilsModel;

import com.rfstudio.timeapp.utils.FileManager;

/**
 * 本应用的相关配置
 * 可自行添加配置信息
 *
 * 注意： 一旦修改/添加/删除 相关属性，需要到FileManager 中更改相关的逻辑代码
 * 修改 ：1.enum 2. updateConfig...  3. getConfig.....
 *
 * 推荐使用： FileManager.buildConfigInfoModel（filename） 来构建
 */
public class ConfigInfoModel {
    public boolean isNewAccount;

    // 应用方面
    public int ringtoneIndex;

    public ConfigInfoModel() {
        // 默认值
        isNewAccount = true;
        ringtoneIndex = 0;
    }
}
