<c:set var="pageTitle" value="Case Details"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<p><a href="${ctx}/user/reports">&larr; My reports</a></p>
<section class="panel">
  <div class="split"><h1><c:out value="${incident.caseId}"/><c:if test="${incident.demo}"> <span class="demo-tag">DEMO</span></c:if></h1>
    <span class="badge s-${incident.status.cssClass}"><c:out value="${incident.status.label}"/></span></div>
  <%@ include file="/WEB-INF/views/_timeline.jspf" %>
  <dl class="facts">
    <dt>Incident type</dt><dd><c:out value="${incident.incidentType}"/></dd>
    <dt>Reported on</dt><dd><fmt:formatDate value="${incident.createdAt}" pattern="dd MMM yyyy, HH:mm"/></dd>
    <dt>Incident date</dt><dd><fmt:formatDate value="${incident.incidentDate}" pattern="dd MMM yyyy"/></dd>
    <dt>Assigned investigator</dt><dd><c:out value="${empty incident.assignedToName ? 'Not assigned yet' : incident.assignedToName}"/></dd>
    <dt>Last updated</dt><dd><fmt:formatDate value="${incident.updatedAt}" pattern="dd MMM yyyy, HH:mm"/></dd>
    <c:if test="${incident.financialLoss.signum() > 0}"><dt>Reported loss</dt><dd><c:out value="${incident.financialLoss}"/></dd></c:if>
    <c:if test="${not empty incident.suspiciousUrl}"><dt>Suspicious URL</dt><dd><code><c:out value="${incident.suspiciousUrl}"/></code> <small class="muted">(shown as text for your safety)</small></dd></c:if>
  </dl>
  <h3>Description</h3><p class="pre"><c:out value="${incident.description}"/></p>
  <c:if test="${not empty incident.additionalInfo}"><h3>Additional information</h3><p class="pre"><c:out value="${incident.additionalInfo}"/></p></c:if>
</section>

<section class="panel"><h3>Safety guidance</h3><p><c:out value="${incident.categoryGuidance}"/></p></section>

<div class="grid g2">
  <section class="panel"><h3>Evidence</h3>
    <ul><c:forEach var="e" items="${incident.evidenceList}"><li><a href="${ctx}/evidence?id=${e.evidenceId}"><c:out value="${e.fileName}"/></a></li></c:forEach></ul>
    <c:if test="${empty incident.evidenceList}"><p class="muted">No files attached.</p></c:if>
  </section>
  <section class="panel"><h3>Case history</h3>
    <ul class="hist"><c:forEach var="h" items="${incident.history}"><li><strong><c:out value="${fn:replace(h.newStatus, '_', ' ')}"/></strong> <span class="muted">&middot; <fmt:formatDate value="${h.updatedAt}" pattern="dd MMM yyyy, HH:mm"/></span></li></c:forEach></ul>
  </section>
</div>
<section class="panel"><h3>Investigator updates</h3>
  <c:forEach var="n" items="${incident.investigations}">
    <div class="note"><div class="muted"><fmt:formatDate value="${n.createdAt}" pattern="dd MMM yyyy, HH:mm"/> &middot; <c:out value="${n.investigatorName}"/></div><p class="pre"><c:out value="${n.notes}"/></p></div>
  </c:forEach>
  <c:if test="${empty incident.investigations}"><p class="muted">No updates have been shared yet.</p></c:if>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
