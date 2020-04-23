# RFTimeApp

#### Description
RF工作室项目开发仓库

#### Software Architecture
Software architecture description

### 项目结构说明
#### 目录
+ application ： 自定义MyApplication
+ db : 数据库各种逻辑
+ service : 自定义service
+ utils : 一些通用的构件，比如可能是弹窗什么的
+ utilsModel ： 通用bean，一些情况可能是通用的
+ utilsViewModel : 通用viewModel ，有时可能是共享的viewmodel 
+ work : 一下为所有业务，业务包的命名 以 Work 结尾

### 注意事项
+ gitee 无法上传空目录，需要在空目录下添加 .gitignore 内容如下:
```
# Ignore everything in this directory
*
# Except this file
!.gitignore
```

+ 对于需要使用 apllication 共享数据时，自行到 application/ 的相关类中增添属性
+ 业务结构并不绝对，有些并不需要 model
+ 使用自定义的application 的方法为
```java
    MyApplication application ; // 可为局部变量，也可为全局成员变量
    application = (MyApplication)getApplication();          // 这样获取到了对应的application
    // 使用get/set 方法 来存取数据
    application.getxxx();
```
+ 1.getApplication()：虽然它返回的是Application对象，但Application类继承自Context，所以它可以用来提供Application Context；
+ 2.getApplicationContext()：返回Application Context；
+ 3.getBaseContext()：返回Activity Context；
+ 4.MainActivity.this：表示MainActivity对象，一般用在内部类中指示外面的this，如果在内部类直接用this，指示的是内部类本身。因为MainActivity继承Activity，而Activity继承Context，所以它也可以用来提供Activity Contex；
+ 5.this：表示当前对象；当它表示MainActivity时，也可以用来提供Activity Context，原因同上。
+ 6.getContext()：这个是View类中提供的方法，在继承了View的类中才可以调用，返回的是当前View运行在哪个Activity Context中。前面的3个方法可以在Activity中调用。
+ 对于没有代码描述的情况， 可以在AS的设置中 勾选 show quick doc on mouse move 以及 show the documentation popup in,(用搜索框能快速找到此选项)
+ 对于 push 提示警告 可在 Terminal 使用
```doc
git pull origin master --allow-unrelated-histories
```
+ Broadcast 的生命周期是 当`onRecive()` 执行完毕后消除

#### 提一下
+ list 的排序依据就是 requestCode （TimeStruct 的属性）
+ 添加/修改/删除 一个时间计划，简单粗暴方法，只需要改application中的list在启动AlarmService即可； 高级办法：对哪个改了，就找那个再修改/删除，重设定
+ 出现弹窗两个选择 “现在开始” “今日不再提醒” (不管选哪个 最后这个计划都是 Exected 状态,区别在于 是否可进入计时界面)
+ 计时界面在主界面右上角，点击后提示是否有正在执行的计划，有：进入计时界面；无：没啥反应
#### Installation

1.  xxxx
2.  xxxx
3.  xxxx

#### Instructions

1.  xxxx
2.  xxxx
3.  xxxx

#### Contribution

1.  Fork the repository
2.  Create Feat_xxx branch
3.  Commit your code
4.  Create Pull Request


#### Gitee Feature

1.  You can use Readme\_XXX.md to support different languages, such as Readme\_en.md, Readme\_zh.md
2.  Gitee blog [blog.gitee.com](https://blog.gitee.com)
3.  Explore open source project [https://gitee.com/explore](https://gitee.com/explore)
4.  The most valuable open source project [GVP](https://gitee.com/gvp)
5.  The manual of Gitee [https://gitee.com/help](https://gitee.com/help)
6.  The most popular members  [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
