# 50道必做sql面试题

##  创建四张表 

###  1.学生表 

Student(SId,Sname,Sage,Ssex)

SId 学生编号,Sname 学生姓名,Sage 出生年月,Ssex 学生性别

```text
create table Student(SId varchar(10),Sname varchar(10),Sage datetime,Ssex varchar(10));
insert into Student values('01' , '赵雷' , '1990-01-01' , '男');
insert into Student values('02' , '钱电' , '1990-12-21' , '男');
insert into Student values('03' , '孙风' , '1990-12-20' , '男');
insert into Student values('04' , '李云' , '1990-12-06' , '男');
insert into Student values('05' , '周梅' , '1991-12-01' , '女');
insert into Student values('06' , '吴兰' , '1992-01-01' , '女');
insert into Student values('07' , '郑竹' , '1989-01-01' , '女');
insert into Student values('09' , '张三' , '2017-12-20' , '女');
insert into Student values('10' , '李四' , '2017-12-25' , '女');
insert into Student values('11' , '李四' , '2012-06-06' , '女');
insert into Student values('12' , '赵六' , '2013-06-13' , '女');
insert into Student values('13' , '孙七' , '2014-06-01' , '女');
```

###  2.课程表 

 Course(CId,Cname,TId)
CId 课程编号,Cname 课程名称,TId 教师编号 

```text
create table Course(CId varchar(10),Cname nvarchar(10),TId varchar(10));
insert into Course values('01' , '语文' , '02');
insert into Course values('02' , '数学' , '01');
insert into Course values('03' , '英语' , '03');
```

###  3.教师表 

 Teacher(TId,Tname)
TId 教师编号,Tname 教师姓名 

```text
create table Teacher(TId varchar(10),Tname varchar(10));
insert into Teacher values('01' , '张三');
insert into Teacher values('02' , '李四');
insert into Teacher values('03' , '王五');
```

###  4.成绩表 

 SC(SId,CId,score)
SId 学生编号,CId 课程编号,score 分数 

```text
create table SC(SId varchar(10),CId varchar(10),score decimal(18,1));
insert into SC values('01' , '01' , 80);
insert into SC values('01' , '02' , 90);
insert into SC values('01' , '03' , 99);
insert into SC values('02' , '01' , 70);
insert into SC values('02' , '02' , 60);
insert into SC values('02' , '03' , 80);
insert into SC values('03' , '01' , 80);
insert into SC values('03' , '02' , 80);
insert into SC values('03' , '03' , 80);
insert into SC values('04' , '01' , 50);
insert into SC values('04' , '02' , 30);
insert into SC values('04' , '03' , 20);
insert into SC values('05' , '01' , 76);
insert into SC values('05' , '02' , 87);
insert into SC values('06' , '01' , 31);
insert into SC values('06' , '03' , 34);
insert into SC values('07' , '02' , 89);
insert into SC values('07' , '03' , 98);
```

## Question

 1、查询"01"课程比"02"课程成绩高的学生的信息及课程分数 

```text
-- 查询出01课程所有学生的成绩 */
SELECT *
FROM sc
WHERE cid ='01'

-- 查询出02课程所有学生的成绩
SELECT *
FROM sc
WHERE cid ='02'

-- 此处注意，from表中可以有两张表，将两张表连接在一起
SELECT *
FROM (SELECT *
FROM sc
WHERE cid ='01') AS a,
(SELECT *
FROM sc
WHERE cid ='02') AS b

-- 01课程成绩大于02课程进行筛选
SELECT *
FROM (SELECT *
FROM sc
WHERE cid ='01') AS a,
(SELECT *
FROM sc
WHERE cid ='02') AS b
WHERE a.sid = b.sid 
AND a.score > b.score

-- 有了一个新表结果，现在取出其中需要的列
SELECT a.sid,a.score as class1,b.score as class2
FROM (SELECT *
FROM sc
WHERE cid ='01') AS a,
(SELECT *
FROM sc
WHERE cid ='02') AS b
WHERE a.sid = b.sid 
AND a.score > b.score

-- 最后一步，原始题目的要求是学生信息以及课程成绩
SELECT *
FROM student
RIGHT JOIN 
(SELECT a.sid,a.score as class1,b.score as class2
FROM (SELECT *
FROM sc
WHERE cid ='01') AS a,
(SELECT *
FROM sc
WHERE cid ='02') AS b
WHERE a.sid = b.sid 
AND a.score > b.score)t1 -- 命名为新表t1
ON student.sid = t1.sid
```

 -- 法二，更简单的做法，其中先进行自联结 

```text
SELECT *
FROM
(SELECT s1.sid, s1.score as class1,s2.score as class2
FROM sc s1
JOIN sc s2
ON s1.sid = s2.sid
WHERE s1.cid ='01' AND s2.cid ='02'
AND s1.score > s2.score) r
LEFT JOIN student
ON r.sid = student.sid
```

 1.1 查询同时存在" 01 "课程和" 02 "课程的情况 

```text
-- 翻译成大白话：意思是选择01和02课程的学生学号以及分数
SELECT a.sid,a.score,b.score
FROM (SELECT *
FROM sc
WHERE cid = '01') AS a,
(SELECT *
FROM sc
WHERE cid = '02') AS b
WHERE a.sid = b.sid
```

 -- 法二 

```text
SELECT s1.sid, s1.score, s2.score
FROM sc s1
LEFT JOIN sc s2
ON s1.sid = s2.sid
WHERE s1.cid = '01' AND s2.cid = '02'
```

--法三 自写

```
select * 
from (select * from sc where cid='01')s1
join (select * from sc where cid='02') s2
on s1.sid=s2.sid
```

 1.2 查询存在" 01 "课程但可能不存在" 02 "课程的情况(不存在时显示为 null ) 

```text
/* 大白话:哪些同学选了01课程，但是可能没有选02课程
显示为null用left join 处理*/
SELECT *
FROM (SELECT *
FROM sc
WHERE cid = '01') AS a
LEFT JOIN(SELECT *
FROM sc
WHERE cid = '02') AS b
ON a.sid = b.sid 
```

1.3 查询不存在" 01 "课程但存在" 02 "课程的情况 

```text
/*题目看似语言平实，实际上会产生歧义 
此处我理解成选了02课程但是没有选01课程的学生*/

-- 首先找出哪些人选了01课程
SELECT sid
FROM sc
WHERE cid = '01';

-- 以上这些学号排除再外
SELECT *
FROM sc
WHERE sid NOT IN 
(SELECT sid
FROM sc
WHERE cid = '01')
AND cid = '02';
```

 2.查询平均成绩大于等于 60 分的同学的学生编号和学生姓名和平均成绩 

 -- 法二 

```text
SELECT s1.sid, s1.sname, avg(s2.score) as 平均成绩
FROM student s1
JOIN sc s2
ON s1.sid = s2.sid
GROUP BY s1.sid
HAVING avg(s2.score) >= 60
```

3. 查询在 SC 表存在成绩的学生信息 

```text
 -- 首先查找出成绩表中存在的学生学号
SELECT DISTINCT sid
FROM sc
WHERE  score is not NULL

-- 根据上述查找出的学号，结合学生表，得出学生所有信息
SELECT *
FROM student
WHERE sid IN
(SELECT DISTINCT sid
FROM sc
WHERE  score is not NULL) 
```

4. 查询所有同学的学生编号、学生姓名、选课总数、所有课程的总成绩(没成绩的显示为 null ) 

```text
-- 大白话：学生编号和学生姓名在学生表中，选课总数用count在成绩表中，总成绩也在成绩表中,实际将两张表联结即可
SELECT student.sid,sname, COUNT(cid) as 选课总数, SUM(score) as 总成绩
FROM student
LEFT JOIN sc -- 因为需要显示没有成绩的，所以要用左联结
ON student.sid = sc.sid
GROUP BY sid
```

 4.1 查有成绩的学生信息 

```text
-- 首先查找出那些有成绩的学生学号
SELECT sid
FROM sc
WHERE scor is not NULL

-- 根据以上学号，结合学生表查找出所有相关信息
SELECT *
FROM student
WHERE sid  IN 
(SELECT sid
FROM sc
WHERE score is not NULL)  
```

5. 查询「李」姓老师的数量 

```text
SELECT COUNT(tname) 
FROM teacher
WHERE tname LIKE '李%'
```

6. 查询学过「张三」老师授课的同学的信息 

```text
-- 首先根据教师表得出张三老师的教师编号
SELECT tid
FROM teacher
WHERE tname = '张三'

-- 根据张三老师的教师编号，结合课程表找出张三老师所授课程的课程编号
SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三')

-- 根据上述得出的课程编号得出那些上过张三老师课的学生编号
SELECT sid
FROM sc
WHERE cid =
(SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三'))

-- 再根据学生学号，结合学生表找出所有学生的信息
SELECT *
FROM student
WHERE sid IN
(SELECT sid
FROM sc
WHERE cid =
(SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三')))
```


  -- 我习惯性拆解思路，一张一张表写，可能会非常麻烦，如下的写法效率很高 

```text
select distinct s.*
from student s
inner join sc sc on s.SId=sc.SId
inner join course c on sc.CId=c.CId
inner join teacher t on c.TId=t.TId
where t.Tname='张三'
```

7. 查询没有学全所有课程的同学的信息 

```text
-- 首先根据课程表计算出所有课程的数量
SELECT count(cid)
FROM course

-- 结合成绩表，按照学号分组，只要选修的课程数量小于上述子查询，得出学号
SELECT sid
FROM sc
GROUP BY sid
HAVING count(cid) <
(SELECT count(cid)
FROM course)

-- 根据上述得出的学生学号，结合学生表，得出所有学生信息
SELECT *
FROM student
WHERE sid IN 
(SELECT sid
FROM sc
GROUP BY sid
HAVING count(cid) <
(SELECT count(cid)
FROM course))
```

 **这道题目我看到别人写出的答案，把学号9-13号的同学的信息也加进去了，我觉得没必要，如果觉得应该这么做的人可以在评论区写答案，下面也有小部分题目也出现了类似情况，把其他学号加进去** 

8. 查询至少有一门课与学号为" 01 "的同学所学相同的同学的信息 

```text
SELECT DISTINCT s.*,s.sid
FROM student s, sc s2
WHERE cid IN
(SELECT cid
FROM sc
WHERE sid = '01')
AND s.sid = s2.sid
AND s2.sid <> '01'
```

9. 查询和" 01 "号的同学学习的课程完全相同的其他同学的信息 

```text
-- 首先查找出01同学学习的课程
SELECT cid
FROM sc
WHERE sid ='01'

-- 哪些学生学习的课程和01号同学相似
SELECT DISTINCT sid
FROM sc
WHERE cid IN
(SELECT cid
FROM sc
WHERE sid ='01')
AND sid <> '01'
GROUP BY sid -- 这里一定要按学号分组，否则查询结果只会返回第一个符合条件的学号
HAVING COUNT(cid) >=3

-- 根据上述查询的学号，结合学生表，找到相关信息
SELECT student.*
FROM student
WHERE sid IN
(SELECT DISTINCT sid
FROM sc
WHERE cid IN
(SELECT cid
FROM sc
WHERE sid ='01')
AND sid <> '01'
GROUP BY sid 
HAVING COUNT(cid) >=3
```

-- 法二

有时需要几张表的信息时可以同时卸载FROM 子句中

```text
SELECT DISTINCT s2.*
FROM sc s1, student s2,
(SELECT cid
FROM sc
WHERE sid ='01')r
WHERE s1.sid = s2.sid
AND s1.cid = r.cid
AND s2.sid <> '01'
GROUP BY s2.sid
HAVING COUNT(s1.cid) >2
```

10. 查询没学过"张三"老师讲授的任一门课程的学生姓名 

```text
-- 首先查询出张三老师所授课程的教师号 
SELECT tid
FROM teacher
WHERE tname = '张三'

-- 结合课程表，根据上述教师号找到相应的课程号
SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三')

-- 结合成绩表，查询哪些学生上过张三老师的课
SELECT  sid
FROM sc
WHERE cid  IN 
(SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三'))-- 换个思维，因为找没上过的人学号，结果会把学号和课程号当成一条记录来判断

-- 查找有哪些学号没有在上述子查询中出现
SELECT DISTINCT sid
FROM sc
WHERE sid not in 
(SELECT  sid
FROM sc
WHERE cid  IN 
(SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三')))

-- 最后根据上述查询学号找到相应的学生姓名
SELECT sname
FROM student
WHERE sid IN
(SELECT DISTINCT sid
FROM sc
WHERE sid not in 
(SELECT  sid
FROM sc
WHERE cid  IN 
(SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三'))))
```

11. 查询两门及其以上不及格课程的同学的学号，姓名及其平均成绩 

```text
-- 查询两门以上不及格的同学学号及其平均成绩
SELECT sid,avg(score) as avg_score
FROM sc
WHERE score <60
GROUP BY sid
HAVING COUNT(cid) >= 2

-- 根据上述查询的学号，结合学生表得出相关姓名
SELECT s.sid,sname,t1.avg_score
FROM student s
RIGHT JOIN
(SELECT sc.sid,avg(score) as avg_score
FROM sc
WHERE score <60
GROUP BY sc.sid
HAVING COUNT(sc.cid) >= 2)t1
ON s.sid = t1.sid  
```

12. 检索" 01 "课程分数小于 60，按分数降序排列的学生信息 

```text
-- 找出01课程分数小于60的学生学号
SELECT sid,score
FROM sc
WHERE cid ='01'
AND score <60

-- 根据上述查询出的学号，结合学生表找到相关信息
SELECT *,score
FROM student s
RIGHT JOIN
(SELECT sid,score
FROM sc
WHERE cid ='01'
AND score <60)t1
ON s.sid = t1.sid
ORDER BY score DESC
```

 -- 法二 非常精炼的做法 

```text
select student.*
from student,sc
where sc.CId ='01'
and   sc.score<60
and   student.SId=sc.SId
```

13. 按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩 

```text
--  首先按照学号分类，查询每个学生的平均成绩
SELECT sid, AVG(score) as 平均成绩
FROM sc
GROUP BY sid
ORDER BY AVG(score) DESC

-- 再添加学生的每门课程的成绩
SELECT sc.sid, sc.cid, sc.score,t1.平均成绩
FROM sc
LEFT JOIN 
(SELECT sid, AVG(score) as 平均成绩
FROM sc
GROUP BY sid)t1
ON sc.sid = t1.sid
ORDER BY t1.平均成绩 DESC
```

 14、查询没学过"张三"老师讲授的任一门课程的学生姓名 

```text
-- 根据教师表，查询张三老师的教师编号
SELECT tid
FROM teacher
WHERE tname = '张三'

-- 结合课程表，查询张三老师的课程编号
SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三')

-- 结合成绩表，查询上过张三老师课程的学生学号
SELECT sid
FROM sc
WHERE cid =
(SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三'))

-- 根据上述学号结果，查询没上过张三老师课程的学生学号
SELECT DISTINCT sid
FROM sc 
WHERE sid NOT IN 
(SELECT sid
FROM sc
WHERE cid =
(SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三')))

-- 结合学生表，查询上述学号的学生姓名
SELECT sname
FROM student
WHERE sid =
(SELECT DISTINCT sid
FROM sc 
WHERE sid NOT IN 
(SELECT sid
FROM sc
WHERE cid =
(SELECT cid
FROM course
WHERE tid =
(SELECT tid
FROM teacher
WHERE tname = '张三'))))
```

