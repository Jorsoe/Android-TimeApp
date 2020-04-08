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
