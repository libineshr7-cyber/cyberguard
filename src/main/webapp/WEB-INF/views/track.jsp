<c:set var="pageTitle" value="Track Case"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Track Your Case</h1>
<form class="panel form inline" method="get" action="${ctx}/track">
  <label class="grow">Case ID<input name="caseId" required maxlength="20" placeholder="CYB-2026-00001" value="<c:out value='${param.caseId}'/>"></label>
  <button class="btn" type="submit">Search</button>
</form>
<c:if test="${not empty incident}">
  <section class="panel">
    <div class="split"><h2><c:out value="${incident.caseId}"/><c:if test="${incident.demo}"> <span class="demo-tag">DEMO</span></c:if></h2>
      <span class="badge s-${incident.status.cssClass}"><c:out value="${incident.status.label}"/></span></div>
    <%@ include file="/WEB-INF/views/_timeline.jspf" %>
    <dl class="facts">
      <dt>Incident category</dt><dd><c:out value="${incident.incidentType}"/></dd>
      <dt>Date reported</dt><dd><fmt:formatDate value="${incident.createdAt}" pattern="dd MMM yyyy"/></dd>
      <dt>Assigned investigator</dt><dd><c:out value="${empty incident.assignedToName ? 'Not assigned yet' : incident.assignedToName}"/></dd>
      <dt>Current status</dt><dd><c:out value="${incident.status.label}"/></dd>
      <dt>Last updated</dt><dd><fmt:formatDate value="${incident.updatedAt}" pattern="dd MMM yyyy, HH:mm"/></dd>
    </dl>
    <h3>Investigation updates</h3>
    <c:forEach var="n" items="${incident.investigations}">
      <div class="note"><div class="muted"><fmt:formatDate value="${n.createdAt}" pattern="dd MMM yyyy, HH:mm"/> &middot; <c:out value="${n.investigatorName}"/></div><p class="pre"><c:out value="${n.notes}"/></p></div>
    </c:forEach>
    <c:if test="${empty incident.investigations}"><p class="muted">No updates have been shared yet.</p></c:if>
  </section>
</c:if>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
