

暂不支持父类字段获取



###版本更新日志：

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

