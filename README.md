# TreasureBox
## 大数据百宝箱
![](http://i1.piimg.com/567571/9e7506dc7d7ad028.png)
### 大纲
这个框架封装了日常大数据工作的数据集处理，监控，评估等任务，并将大数据工作流程化，以此提高开发时间
因为数据体量的原因，大数据集的开发调试工作较其他开发而言很不一样，在总结多次开发实践之后，将调试工作单独抽出来，以方便调试
在大数据工作没有任何规范，约束的日子里，调试和开发的代码可读性很差，这个框架的目的也有提供一个可行的规范，使得代码可读性更强
### 快速开始
1 安装 java8 在本地
2 安装 scala 在本地
3 下载并打包本程序
4 执行example中的示例main方法
### 处理流程
本框架的总体流程如下图所示
![](http://i2.muimg.com/567571/632207be0b9e980f.png)
1 DataLoader负责原始数据集的载入工作，因为这部分工作很多重复性劳动，所以可以抽离出来。目前共封装了3个数据源：hbase,hive,redis
2 DataCleaner负责数据清洗，目前是以组件的形式封装在DataLoader中，主要以BigDataVO的数据传输类进行约束
3 DataCutter负责数据集切分，这一块主要是评估任务用到，将训练集和测试集分离
4 DataConverter负责多个模型数据集之间的转换，这个也是大量重复性的劳动，因此也可以单独抽离出来
5 DataDebuger主要是将调试任务流程化，将每次调试都常用的操作固定出来
6 DataMoniter负责数据集任务的监控，正在开发中
7 DataProcessor 大数据工作开始的入口，封装了以上组件
### 当前计划
1 数据源的完善
2 切分器的通用性
3 修复评估处理器的部分bug
4 增加通用工具类
5 维护