# mybatis-utils

## 项目由来
做web项目时，大多都是单表的增删改查需求，遇到页面有很多的查询条件的场景时（很多这样的场景），
构建sql语句就变的很麻烦，不是在provider里动态构建就是在注解里写动态标签，
这些工作都是类似的，做多了这种需求就感觉到很累，尤其需求改动时超级麻烦。特别是在注解中用动态标签时，
老是担心哪里改少了什么或者忘了什么没删掉，导致出现莫名奇妙的错误，而且这些错误还很难debug出来。
所以就想能不能写个工具出来自动生成这部分sql，到时产品说要加一个查询字段，我也只需在dto中相应加一个字段就行，
简单方便。所以就有了这个项目。

## 使用说明

### mybatis-utils配置
1. 依赖
需要添加的pom依赖(请自行下载源码install到本地仓库，有私服的可以推送到私服)
```xml
<dependency>
    <groupId>dominic</groupId>
    <artifactId>mybatis-utils-service</artifactId>
    <version>2.1.2-SNAPSHOT</version> <!-- 版本有改动请自行改用最新版本 -->
</dependency>
````
2. 配置
在mybatis扫描配置中加入"dominic.mybatis.dao"即可，即basePackage属性中加入"dominic.mybatis.dao"
    * 如果是spring boot项目，可以这样配置
    (这里还可以细分为主从配置，即配两个sqlSessionFactory，一主一从，分别扫描"dominic.mybatis.dao.update"、"dominic.mybatis.dao.query"。mybatis-utils-test就是这样配置的)
    ```java
    @MapperScan(basePackages={/*"其他的dao包",*/"dominic.mybatis.dao"}, sqlSessionFactoryRef="sqlSessionFactory")
    ```
    * 如果是传统spring xml配置，也是在basePackage属性中加入"dominic.mybatis.dao"，如：
    ```xml
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="dataSource" ref="dataSource" />
        <property name="sqlSessionFactory" value="sqlSessionFactory" />
        <property name="basePackage" value="dominic.mybatis.dao" /> <!-- 还有其他的dao包需要扫描，只需用英文逗号分隔即可 -->
        <!-- other properties -->
    </bean>
    ```
3. 使用
* 查询，只需要继承dominic.mybatis.service.AbstractQueryService即可，如：
```java
@Service
public class SubEntityQueryService extends AbstractQueryService<Sub> {
}
```
然后在需要使用的地方Autowired或Resource进去即可对表做查询操作
* 更新，跟查询类似，只需继承dominic.mybatis.service.AbstractUpdateService即可，如：
```java
@Service
public class SubEntityUpdateService extends AbstractUpdateService<Sub> {
}
```

### 使用举例
mybatis-utils-test项目是mybatis-utils的使用demo，下面的例子都可以在test项目中找到源码
#### 1. 查询
* 根据DTO查询
    ```java
    SubEntityQueryDTO subQueryDTO = SubEntityQueryDTO.builder()
            .userNames(Lists.newArrayList("406226662983"))
            .payDateStart(DateTime.parse("2017-06-20").toDate())
            .payDateEnd(DateTime.parse("2017-06-30").toDate())
            .status(Lists.newArrayList(3,4))
            .build();

    List<Sub> subDTOList = subEntityQueryService.queryByBean(subQueryDTO);
    ```
    日志级别为debug时，mybatis打印出来的sql是这样的：
    ```
    2018-02-07 14:06:42.942 [main] DEBUG d.m.dao.query.BaseQueryDAO.query - ==>  Preparing: select `sub_id`,`user_name`,`user_id`,`sub_date`,`pay_date`,`sub_number`,`actual_total`,`status`,`section_on_way`,`sub_settlement_sn`,`addr_order_sn`,`sub_type` from `order` where `user_name` IN (?) AND `pay_date`>=? AND `pay_date`<=? AND `status` IN (?,?)
    2018-02-07 14:06:42.973 [main] DEBUG d.m.dao.query.BaseQueryDAO.query - ==> Parameters: 406226662983(String), 2017-06-20 00:00:00(String), 2017-06-30 00:00:00(String), 3(Integer), 4(Integer)
    2018-02-07 14:06:42.997 [main] DEBUG d.m.dao.query.BaseQueryDAO.query - <==      Total: 1
    ```
    mybatis-utils支持mybatis的预编译执行，能有效的防止SQL注入。
    queryByBean方法根据DTO来查询，这样增减查询条件就能通过增减相应字段来实现，
    而且能支持时间区段查询和集合查询。

* 根据DTO分页查询，并排序
    ```java
    PageParam pageParam = PageParam.builder().pageIndex(0).pageSize(10).build();
    AbstractAppender<OrderSupport> orderSupportAppender = OrderSupportAppender.newInstance()
            .append(OrderSupport.DESC("sub_date")).append(OrderSupport.DESC("actual_total"));
    List<Sub> subDTOList = subEntityQueryService.queryByBean(subQueryDTO, pageParam, orderSupportAppender);
    ```
    日志打印的sql：
    ```
    2018-02-07 14:42:16.350 [main] DEBUG d.m.dao.query.BaseQueryDAO.query - ==>  Preparing: select `sub_id`,`user_name`,`user_id`,`sub_date`,`pay_date`,`sub_number`,`actual_total`,`status`,`sub_type` from `order` where `user_name` IN (?) AND `pay_date`<=? AND `status` IN (?,?) order by sub_date DESC,actual_total DESC limit 0,10
    2018-02-07 14:42:16.373 [main] DEBUG d.m.dao.query.BaseQueryDAO.query - ==> Parameters: 406226662983(String), 2017-06-30 00:00:00(String), 3(Integer), 4(Integer)
    2018-02-07 14:42:16.400 [main] DEBUG d.m.dao.query.BaseQueryDAO.query - <==      Total: 10
    ```

* 根据某些条件灵活查询
    ```java
    //Restriction: 查询用户名为406226662983的所有订单
    List<Sub> subDTOList = subEntityQueryService.query(Restriction.eq("user_name", "406226662983"));

    //Restrictions: 查询用户名为406226662983的所有在2017-06-30前支付的状态为3或4的订单
    Restrictions restrictions = Restrictions.builder().eq("user_name", "406226662983")
                    .in("status", Lists.newArrayList(3,4))
                    .lessEqual("pay_date", "2017-06-30").build();
            List<Sub> subDTOList = subEntityQueryService.query(restrictions);

    ```

#### 2. 更新
##### (1) 插入数据
* insert插入数据，没有回填主键
    ```java
    Sub order = Sub.builder()
            .userName("dominic")
            .userId("123456")
            .subDate(new Date())
            .subNumber("O1234")
            .actualTotal(BigDecimal.valueOf(0.01))
            .status(0)
            .build();
    int result = subEntityUpdateService.insert(order);
    System.out.println(order);
    ```
    结果日志打印：
    ```
    2018-02-07 15:36:07.711 [main] DEBUG d.m.dao.update.BaseUpdateDAO.insert - ==>  Preparing: INSERT INTO `order` (`user_name`, `user_id`, `sub_date`, `sub_number`, `actual_total`, `status`) VALUES (?, ?, ?, ?, ?, ?)
    2018-02-07 15:36:07.737 [main] DEBUG d.m.dao.update.BaseUpdateDAO.insert - ==> Parameters: dominic(String), 123456(String), 2018-02-07 15:36:07.419(Timestamp), O1234(String), 0.01(BigDecimal), 0(Integer)
    2018-02-07 15:36:07.748 [main] DEBUG d.m.dao.update.BaseUpdateDAO.insert - <==    Updates: 1
    Sub(subId=null, userName=dominic, userId=123456, subDate=Wed Feb 07 15:36:07 CST 2018, payDate=null, subNumber=O1234, actualTotal=0.01, status=0, subType=null, createTime=null)
    ```
* save插入数据，会回填主键
    ```java
    int result = subEntityUpdateService.save(order);
    System.out.println(order);
    ```
    结果日志打印（注意subId的值，已经回填了主键subId的值）：
    ```
    2018-02-07 15:43:18.932 [main] DEBUG d.m.dao.update.BaseUpdateDAO.save - ==>  Preparing: INSERT INTO `order` (`user_name`, `user_id`, `sub_date`, `sub_number`, `actual_total`, `status`) VALUES (?, ?, ?, ?, ?, ?)
    2018-02-07 15:43:18.959 [main] DEBUG d.m.dao.update.BaseUpdateDAO.save - ==> Parameters: dominic(String), 123456(String), 2018-02-07 15:43:18.595(Timestamp), O1235(String), 0.01(BigDecimal), 0(Integer)
    2018-02-07 15:43:18.963 [main] DEBUG d.m.dao.update.BaseUpdateDAO.save - <==    Updates: 1
    Sub(subId=2, userName=dominic, userId=123456, subDate=Wed Feb 07 15:43:18 CST 2018, payDate=null, subNumber=O1235, actualTotal=0.01, status=0, subType=null, createTime=null)
    ```
* 批量insert
    ```java
    Sub order1 = Sub.builder().userName("dominic").userId("123456").subDate(new Date())
                    .subNumber("O1211").actualTotal(BigDecimal.valueOf(0.01)).status(0).build();
    Sub order2 = Sub.builder().userName("dominic").userId("123456").subDate(new Date())
            .subNumber("O1212").actualTotal(BigDecimal.valueOf(0.01)).status(0).build();
    ArrayList<Sub> list = Lists.newArrayList(order1, order2);
    int result = subEntityUpdateService.insert(list);
    System.out.println(result);
    ```
    结果日志打印：
    ```
    2018-02-07 15:50:45.981 [main] DEBUG d.m.d.u.BaseUpdateDAO.insertList - ==>  Preparing: INSERT INTO `order`(`sub_id`,`user_name`,`user_id`,`sub_date`,`pay_date`,`sub_number`,`actual_total`,`status`) VALUES (?,?,?,?,?,?,?,?),(?,?,?,?,?,?,?,?)
    2018-02-07 15:50:46.009 [main] DEBUG d.m.d.u.BaseUpdateDAO.insertList - ==> Parameters: null, dominic(String), 123456(String), 2018-02-07 15:50:45.675(Timestamp), null, O1211(String), 0.01(BigDecimal), 0(Integer), null, dominic(String), 123456(String), 2018-02-07 15:50:45.675(Timestamp), null, O1212(String), 0.01(BigDecimal), 0(Integer)
    2018-02-07 15:50:46.011 [main] DEBUG d.m.d.u.BaseUpdateDAO.insertList - <==    Updates: 2
    2
    ```

##### (2) 更新数据
* 根据dto更新
    ```java
    Sub order = Sub.builder().payDate(new Date()).status(1).build();
    int result = subEntityUpdateService.update(order, Restriction.eq("sub_id", 1));
    ```
    结果日志打印：
    ```
    2018-02-07 16:19:47.580 [main] DEBUG d.m.dao.update.BaseUpdateDAO.update - ==>  Preparing: update `order` set `pay_date`=?,`status`=? where `sub_id`=?
    2018-02-07 16:19:47.603 [main] DEBUG d.m.dao.update.BaseUpdateDAO.update - ==> Parameters: 2018-02-07 16:19:47.28(Timestamp), 1(Integer), 1(Integer)
    2018-02-07 16:19:47.609 [main] DEBUG d.m.dao.update.BaseUpdateDAO.update - <==    Updates: 1
    ```
* 根据更新dto和条件dto更新
    ```java
    Sub order = Sub.builder().payDate(new Date()).status(1).build();
    Sub condition = Sub.builder().userName("dominic").subNumber("O1235").build();
    int result = subEntityUpdateService.updateByQueryBean(order, condition);
    ```
    结果日志打印：
    ```
    2018-02-07 16:32:51.250 [main] DEBUG d.m.dao.update.BaseUpdateDAO.update - ==>  Preparing: update `order` set `pay_date`=?,`status`=? where `user_name`=? AND `sub_number`=?
    2018-02-07 16:32:51.273 [main] DEBUG d.m.dao.update.BaseUpdateDAO.update - ==> Parameters: 2018-02-07 16:32:50.933(Timestamp), 1(Integer), dominic(String), O1235(String)
    2018-02-07 16:32:51.276 [main] DEBUG d.m.dao.update.BaseUpdateDAO.update - <==    Updates: 1
    ```
* 用UpdateField和Restriction来更新
    ```java
    int result = subEntityUpdateService.update(UpdateField.set("status", 2), Restriction.eq("sub_id", 1));
    ```
    ```java
    int result = subEntityUpdateService.update(UpdateFields.builder().set("status", 2).set("pay_date", new Date()).build(), Restriction.eq("sub_id", 1));
    ```
    ```java
    UpdateFields updateFields = UpdateFields.builder().set("status", 2).set("pay_date", new Date()).build();
    Restrictions restrictions = Restrictions.builder().eq("user_name", "dominic").eq("sub_number", "O1235").build();
    int result = subEntityUpdateService.update(updateFields, restrictions);
    ```

相关源码直通车：
[SubEntityQueryServiceTest](https://github.com/dominiche/mybatis-utils/blob/master/mybatis-utils-test/src/test/java/dominic/test/SubEntityQueryServiceTest.java)
[SubEntityUpdateServiceTest](https://github.com/dominiche/mybatis-utils/blob/master/mybatis-utils-test/src/test/java/dominic/test/SubEntityUpdateServiceTest.java)

### 注意事项

* 本项目是基于mybatis构建的，SQL语句适用于MySQL
* 构建SQL时，暂不支持父类字段获取、原始类型字段获取 (todo)
* 批量insert时要注意，因为这里为了不遗漏数据，所以是不会忽略为null的字段的，
即批量insert时默认处理null，普通情况可以不用考虑这里的区别；只有表的某个字段有默认值，
想让数据库使用表的默认值时，才需要注意，这时可以显式设置null的处理方式来解决，即加上注解：
`@HandleNull(value = false)`。这里的典型场景是create_time和update_time。
