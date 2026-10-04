<c:set var="pageTitle" value="My Reports"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<div class="split"><h1>My Reports</h1><a class="btn btn-sm" href="${ctx}/report">New report</a></div>
<%@ include file="/WEB-INF/views/_case-table.jspf" %>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
