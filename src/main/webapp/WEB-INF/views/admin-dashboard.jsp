<c:set var="pageTitle" value="Admin Dashboard"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Investigator Dashboard</h1>
<p class="muted">Signed in as <c:out value="${sessionScope.user.name}"/> (<c:out value="${sessionScope.user.role}"/>)</p>
<div class="grid g6 stats">
  <div class="stat"><span class="num">${stats.total}</span><span>Total Cases</span></div>
  <div class="stat"><span class="num">${stats.newCases}</span><span>New Cases</span></div>
  <div class="stat"><span class="num">${stats.underReview}</span><span>Under Review</span></div>
  <div class="stat"><span class="num">${stats.investigating}</span><span>Investigating</span></div>
  <div class="stat"><span class="num">${stats.resolved}</span><span>Resolved</span></div>
  <div class="stat"><span class="num">${stats.closed}</span><span>Closed</span></div>
</div>

<form class="panel form filters" method="get" action="${ctx}/admin/dashboard">
  <label>Search Case ID<input name="q" maxlength="20" placeholder="CYB-2026-" value="<c:out value='${param.q}'/>"></label>
  <label>Category<select name="type"><option value="">All</option>
    <c:forEach var="t" items="${incidentTypes}"><option value="${t}" ${param.type eq t ? 'selected' : ''}><c:out value="${t}"/></option></c:forEach></select></label>
  <label>Status<select name="status"><option value="">All</option>
    <c:forEach var="s" items="${statuses}"><option value="${s}" ${param.status eq s.name() ? 'selected' : ''}><c:out value="${s.label}"/></option></c:forEach></select></label>
  <label>Reported on<input type="date" name="date" value="<c:out value='${param.date}'/>"></label>
  <div class="actions"><button class="btn" type="submit">Filter</button><a class="btn btn-ghost" href="${ctx}/admin/dashboard">Reset</a></div>
</form>

<div class="table-wrap">
<table class="tbl">
  <thead><tr><th>Case ID</th><th>Type</th><th>Date</th><th>Priority</th><th>Status</th><th>Investigator</th><th>Action</th></tr></thead>
  <tbody>
  <c:forEach var="i" items="${incidents}">
    <tr>
      <td><strong><c:out value="${i.caseId}"/></strong><c:if test="${i.demo}"> <span class="demo-tag">DEMO</span></c:if></td>
      <td><c:out value="${i.incidentType}"/></td>
      <td><fmt:formatDate value="${i.createdAt}" pattern="dd MMM yyyy"/></td>
      <td><span class="badge p-${fn:toLowerCase(i.priority)}"><c:out value="${i.priority}"/></span></td>
      <td><span class="badge s-${i.status.cssClass}"><c:out value="${i.status.label}"/></span></td>
      <td><c:out value="${empty i.assignedToName ? '-' : i.assignedToName}"/></td>
      <td class="actions-cell">
        <a class="btn btn-xs btn-ghost" href="${ctx}/admin/case?id=${i.incidentId}">View</a>
        <a class="btn btn-xs btn-ghost" href="${ctx}/admin/case?id=${i.incidentId}#assign">Assign</a>
        <a class="btn btn-xs btn-ghost" href="${ctx}/admin/case?id=${i.incidentId}#update">Update</a>
        <c:if test="${i.status.name() ne 'CLOSED'}">
          <form class="inline-form" method="post" action="${ctx}/admin/case" data-confirm="Close case ${i.caseId}?">
            <input type="hidden" name="id" value="${i.incidentId}"><input type="hidden" name="action" value="close"><input type="hidden" name="from" value="dashboard">
            <button class="btn btn-xs btn-danger" type="submit">Close</button>
          </form>
        </c:if>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty incidents}"><tr><td colspan="7" class="muted">No cases match your filters.</td></tr></c:if>
  </tbody>
</table>
</div>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
