## Mysql

------

数据库三大范式

```
第一范式：1NF是对属性的原子性约束，要求属性(列)具有原子性，不可再分解；(只要是关系型数据库都满足1NF)
第二范式：2NF是对记录的惟一性约束，表中的记录是唯一的, 就满足2NF, 通常我们设计一个主键来实现，主键不能包含业务逻辑。
第三范式：3NF是对字段冗余性的约束，它要求字段没有冗余。 没有冗余的数据库设计可以做到。
但是，没有冗余的数据库未必是最好的数据库，有时为了提高运行效率，就必须降低范式标准，适当保留冗余数据。具体做法是： 在概念数据模型设计时遵守第三范式，降低范式标准的工作放到物理数据模型设计时考虑。降低范式就是增加字段，允许冗余。
```

sql优化

```
表的设计合理化(符合3NF)
添加适当索引(index) [四种: 普通索引、主键索引、唯一索引unique、全文索引]
SQL语句优化
分表技术(水平分割、垂直分割)
读写[写: update/delete/add]分离
存储过程 [模块化编程，可以提高速度]
对mysql配置优化 [配置最大并发数my.ini, 调整缓存大小 ]
mysql服务器硬件升级
定时的去清除不需要的数据,定时进行碎片整理(MyISAM)
```

创建存储过程

无参

```sql
create procedure user_porced () 
begin 
  select name from users; 
end; 
-- 调用
call user_porced();
```

有参

```sql
create PROCEDURE user_porcedPa(
in a int(10)
)
BEGIN
 select * from users where age>a;
END;
-- 调用
call user_porcedPa(10);
```

常用配置查看：

```sql
-- 查看MySQL服务器状态信息
show status;
-- mysql数据库启动了多少时间
show status like 'uptime';
--（显示数据库的查询，更新，添加，删除的次数）
show status like 'com_select' ;
show status like 'com_update' ;
show status like 'com_insert' ;
show status like 'com_delete' ;
-- 查询数据库当前设置的最大连接数
show variables like '%max_connection%';
-- 显示慢查询次数
show status like 'slow_queries';
-- 查询慢查询时间
show variables like 'long_query_time';
-- 修改慢查询时间
set long_query_time=1; ---但是重启mysql之后，long_query_time依然是my.ini中的值
-- 查看数据库数据文件存放位置
SELECT @@datadir;
-- 这个参数设置为ON，可以捕获执行时间超过一定数值的SQL语句
slow_query_log
-- 查看是否开启缓存
show variables like 'have_query_cache'
-- 查看连接状态
show status like 'Threads%'; 
+-------------------+-------+  
    | Variable_name     | Value |  
    +-------------------+-------+  
    | Threads_cached    | 58    |  
    | Threads_connected | 57    |   ###这个数值指的是打开的连接数  
    | Threads_created   | 3676  |  
    | Threads_running   | 4     |   ###这个数值指的是激活的连接数，这个数值一般远低于connected数值  
    +-------------------+-------+  
    Threads_connected 跟show processlist结果相同，表示当前连接数。准确的来说，Threads_running是代表当前并发数  

```

业务sql:

```sql
-- 截取日期
SELECT * from user  WHERE status=0 and DATE_FORMAT(record_date, '%Y-%m-%d')="2019-07-31"
-- 修改自增序列
alter table user AUTO_INCREMENT=1;
-- 查看表索引
show index  from user;
```

随笔

```
MySQL保存boolean值时用1代表TRUE，0代表FALSE；

timestamp 类型根据当前时间戳更新日期  CURRENT_TIMESTAMP;
```

