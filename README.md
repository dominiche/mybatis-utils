

暂不支持父类字段获取



###版本更新日志：
####2.1.0 Restriction改造
    （1）Restriction添加notIn支持
    
####2.0.4: RestrictionsUtils, fix @DatePolicy begin and end 
    预编译参数名互相覆盖的问题（临时解决方案）
    
####2.0.3: TransformUtils, fix number type convert exception
    修复number类型的字段转换出错，如：Integer转成Long等

####2.0.0: 2018-1-11 15:10:32 SQL语句拼接预编译参数化改造
    
####1.5.0: InsertNull注解改名HandleNull，添加scope（see HandleNullScope）
    HandleNullScope.ALL 对INSERT、UPDATE、CONDITION都有效，//todo CONDITION有待完善
    
####1.4.0: update bean 支持InsertNull注解
    没有InsertNull注解时，默认处理方式改为null值不处理，需要则加@InsertNull(true)

####1.3.4: 修改null值插入的处理方式
    没有InsertNull注解时，默认处理方式改为null值不处理，需要则加@InsertNull(true)

####1.3.3: 修改null值插入的处理方式
    没有InsertNull注解时，默认处理方式改为null值插入为NULL(之前是null值忽略)，
    如果某字段为not null，但是数据库会给默认值，则bean的该字段值可以为nul但要加上注解InsertNull(false)
####1.3.1:
    修复dominic.mybatis.service.AbstractUpdateService.insert(java.util.List<T>)对字段NULL值插入null，而表中该字段是not null但有默认值的情况：
        添加InsertNull注解，默认不插入null的字段，如果需要可以加上该注解
####1.3.0:
    insertList支持一条sql批量插入
####1.2.0:
    architecture reform:
        consists of mybatis-utils-service and mybatis-utils-tool
####1.1.1:
    (1)修复PageParam的pageIndex、pageSize可能被赋予负值
####1.1.0： 
    （1）UpdateFieldsUtils支持根据bean构建成UpdateFieldUnit
    （2）支持根据bean的值来更新，null值的字段忽略，非空的更新

