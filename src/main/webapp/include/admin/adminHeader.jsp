<%-- 使用HTML5技术--%>
<!DOCTYPE html>
<%-- 浏览器进行中文编码识别， jsp上的中文使用UTF-8编码，jsp中使用EL表达式--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"
         language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%-- 引入JSTL， 使用c和fmt两种标准标签库--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%-- 公共头部--%>
<html>
<head>
    <script src="resources/js/jquery/2.0.0/jquery.min.js"></script>
    <link href="resources/css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="resources/js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link href="resources/css/back/style.css" rel="stylesheet">
    <%-- 预定义判断输入框函数--%>
    <script>
        function checkEmpty(id, name) {
            var value = $("#" + id).val();
            if (value.length == 0){
                alert(name + "不能为空");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }
        function checkNumber(id, name) {
            if (!checkEmpty(id, name))
                return false;
            if (isNaN($("#" + id).val())){
                alert(name + "必须是数字");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }
        function checkInt(id, name) {
            if (!checkEmpty(id, name))
                return false;
            var value = $("#" + id).val();
            if (parseInt(value) != value){
                alert(name + "必须是整数");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }
        // 删除操作需要进行确认
        $(function(){
            $("a").click(function () {
                var deleteLink = $(this).attr("deleteLink");
                console.log(deleteLink);
                if ("true" == deleteLink){
                    var confirmDelete = confirm("确认要删除？");
                    if (confirmDelete)
                        return true;
                    return false;
                }
            });
        });
    </script>
</head>
<body>
