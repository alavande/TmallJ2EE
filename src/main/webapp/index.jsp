<%@page pageEncoding="UTF-8" isELIgnored="false" import="java.util.*"
        contentType="text/html;charset=UTF-8" language="java" %>

<%
    String contextPath = request.getServletContext().getContextPath();
    String home = contextPath + "/forehome";
    response.sendRedirect(home);
%>
