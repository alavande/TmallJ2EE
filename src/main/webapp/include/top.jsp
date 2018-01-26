<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<script>
	$(function () {
	    root = '<%=request.getSession().getServletContext().getContextPath()%>';
        $('.my').click(function () {
            var page = "forecheckLogin";
            var path = $(this).attr('href');
            $.get(
                page,
                function (result) {
                    if ('true' == result) {
                        location.href = path;
                        return true;
                    } else {
                        location.href = "login.jsp";
					}
                });
            return false;
        });
    });
</script>

<nav class="top ">
		<a href="${contextPath}">
			<span style="color:#C40000;margin:0px" class="glyphicon glyphicon-home redColor"></span>
			天猫首页
		</a>	
		
		<span>喵，欢迎来天猫</span>
		
		<c:if test="${!empty user}">
			<a href="login.jsp">${user.name}</a>
			<a href="forelogout">退出</a>		
		</c:if>
		
		<c:if test="${empty user}">
			<a href="login.jsp">请登录</a>
			<a href="register.jsp">免费注册</a>
		</c:if>

		<span class="pull-right">
			<a href="forebought" class="my">我的订单</a>
			<a href="forecart" class="my">
			<span style="color:#C40000;margin:0px" class="glyphicon glyphicon-shopping-cart redColor"></span>
			购物车<strong id="cartNum">${cartTotalItemNumber}</strong>件</a>
		</span>
</nav>



