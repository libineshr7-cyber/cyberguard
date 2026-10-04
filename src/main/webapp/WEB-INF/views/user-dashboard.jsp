<c:set var="pageTitle" value="Dashboard"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Welcome, <c:out value="${sessionScope.user.name}"/></h1>
<div class="grid g4 stats">
  <div class="stat"><span class="num">${total}</span><span>Total Reports</span></div>
  <div class="stat"><span class="num">${underReview}</span><span>Under Review</span></div>
  <div class="stat"><span class="num">${investigating}</span><span>Investigating</span></div>
  <div class="stat"><span class="num">${resolved}</span><span>Resolved</span></div>
</div>
<div class="split"><h2>Recent reports</h2><div class="actions"><a class="btn btn-sm" href="${ctx}/report">New report</a><a class="btn btn-sm btn-ghost" href="${ctx}/user/reports">All reports</a></div></div>
<%@ include file="/WEB-INF/views/_case-table.jspf" %>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
