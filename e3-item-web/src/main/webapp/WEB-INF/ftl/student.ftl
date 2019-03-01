<html>
    <head>
        <title>freeMarker语法</title>
    </head>
    <body>
        <#--基础变量-->
        变量:${key}<br>
        <#--POJO-->
        学生信息:<br>
        学号:${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
        姓名:${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
        年龄:${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
        地址:${student.adrre}&nbsp;&nbsp;&nbsp;&nbsp;<br>
        <#--List-->
        学生列表:<br>
        <table border="1">
            <tr>
               <th>序号</th>
               <th>学号</th>
               <th>姓名</th>
               <th>年龄</th>
               <th>地址</th>
            </tr>
            <#list list as stu>
                <#if stu_index %2 ==0 >
                    <tr bgcolor="#f0f8ff">
                <#else >
                    <tr bgcolor="#faebd7">
                </#if>
                    <td>${stu_index}</td>
                    <td>${stu.id}</td>
                    <td>${stu.name}</td>
                    <td>${stu.age}</td>
                    <td>${stu.adrre}</td>
                </tr>
            </#list>
        </table><br>
        <#--date?date,?time,?datetime,?string(yyyy/MM/dd HH:MM:SS)-->
        当前时间:${date?date}<br>
        <#--null值-->
        val:${val!"null值"}<br>
        <#if val??>
            不是null
        <#else >
            null值
        </#if>
        <#--include-->
        <#include "hello.ftl">
    </body>
</html>