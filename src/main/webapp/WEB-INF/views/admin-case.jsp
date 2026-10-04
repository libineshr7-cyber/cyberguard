<c:set var="pageTitle" value="Case Management"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<p><a href="${ctx}/admin/dashboard">&larr; Dashboard</a></p>
<section class="panel">
  <div class="split"><h1><c:out value="${incident.caseId}"/><c:if test="${incident.demo}"> <span class="demo-tag">DEMO</span></c:if></h1>
    <div><span class="badge p-${fn:toLowerCase(incident.priority)}"><c:out value="${incident.priority}"/> priority</span> <span class="badge s-${incident.status.cssClass}"><c:out value="${incident.status.label}"/></span></div></div>
  <%@ include file="/WEB-INF/views/_timeline.jspf" %>
  <dl class="facts">
    <dt>Incident type</dt><dd><c:out value="${incident.incidentType}"/></dd>
    <dt>Reported date</dt><dd><fmt:formatDate value="${incident.createdAt}" pattern="dd MMM yyyy, HH:mm"/></dd>
    <dt>Incident date</dt><dd><fmt:formatDate value="${incident.incidentDate}" pattern="dd MMM yyyy"/></dd>
    <dt>Financial loss</dt><dd><c:out value="${incident.financialLoss}"/></dd>
    <dt>Suspicious URL</dt><dd><c:choose><c:when test="${empty incident.suspiciousUrl}">-</c:when><c:otherwise><code><c:out value="${incident.suspiciousUrl}"/></code> <small class="muted">(not clickable - do not open directly)</small></c:otherwise></c:choose></dd>
    <dt>Assigned investigator</dt><dd><c:out value="${empty incident.assignedToName ? 'Unassigned' : incident.assignedToName}"/></dd>
    <dt>Reporter</dt><dd><c:out value="${incident.reporterName}"/> &middot; <c:out value="${incident.reporterEmail}"/> &middot; <c:out value="${incident.reporterPhone}"/> <c:if test="${empty incident.userId}"><small class="muted">(guest report)</small></c:if></dd>
  </dl>
  <h3>Description</h3><p class="pre"><c:out value="${incident.description}"/></p>
  <c:if test="${not empty incident.additionalInfo}"><h3>Additional information</h3><p class="pre"><c:out value="${incident.additionalInfo}"/></p></c:if>
  <h3>Handling guidance (<c:out value="${incident.class.simpleName}"/>)</h3><p class="muted"><c:out value="${incident.categoryGuidance}"/></p>
</section>

<form class="panel form" id="update" method="post" action="${ctx}/admin/case">
  <h2 id="assign">Update case</h2>
  <input type="hidden" name="id" value="${incident.incidentId}">
  <div class="row">
    <label>Status<select name="status"><c:forEach var="s" items="${statuses}"><option value="${s}" ${incident.status eq s ? 'selected' : ''}><c:out value="${s.label}"/></option></c:forEach></select></label>
    <label>Assigned investigator<select name="assignedTo"><option value="">Unassigned</option>
      <c:forEach var="u" items="${staffList}"><option value="${u.userId}" ${incident.assignedTo eq u.userId ? 'selected' : ''}><c:out value="${u.name}"/> (<c:out value="${u.role}"/>)</option></c:forEach></select></label>
    <label>Priority<select name="priority"><c:forEach var="p" items="${priorities}"><option value="${p}" ${incident.priority eq p ? 'selected' : ''}><c:out value="${p}"/></option></c:forEach></select></label>
  </div>
  <label>Investigation note<textarea name="note" rows="4" maxlength="2000" placeholder="Add an update (optional)"></textarea></label>
  <label class="check-row"><input type="checkbox" name="publicNote" checked> Visible to the reporter (uncheck for internal-only notes)</label>
  <div class="actions">
    <button class="btn" type="submit" name="action" value="save">Save changes</button>
    <button class="btn btn-ghost" type="submit" name="action" value="resolve">Mark as Resolved</button>
    <button class="btn btn-danger" type="submit" name="action" value="close">Close Case</button>
  </div>
</form>

<div class="grid g2">
  <section class="panel"><h3>Evidence</h3>
    <ul><c:forEach var="e" items="${incident.evidenceList}"><li><a href="${ctx}/evidence?id=${e.evidenceId}"><c:out value="${e.fileName}"/></a> <small class="muted"><c:out value="${e.fileType}"/></small></li></c:forEach></ul>
    <c:if test="${empty incident.evidenceList}"><p class="muted">No files attached.</p></c:if>
  </section>
  <section class="panel"><h3>Status history</h3>
    <ul class="hist"><c:forEach var="h" items="${incident.history}"><li><c:out value="${empty h.oldStatus ? 'NEW' : fn:replace(h.oldStatus, '_', ' ')}"/> &rarr; <strong><c:out value="${fn:replace(h.newStatus, '_', ' ')}"/></strong><br><span class="muted"><fmt:formatDate value="${h.updatedAt}" pattern="dd MMM yyyy, HH:mm"/> &middot; <c:out value="${h.updatedBy}"/></span></li></c:forEach></ul>
  </section>
</div>
<section class="panel"><h3>Investigation notes</h3>
  <c:forEach var="n" items="${incident.investigations}">
    <div class="note"><div class="muted"><fmt:formatDate value="${n.createdAt}" pattern="dd MMM yyyy, HH:mm"/> &middot; <c:out value="${n.investigatorName}"/> <span class="badge ${n.publicNote ? 's-resolved' : 's-closed'}"><c:out value="${n.publicNote ? 'Public' : 'Internal'}"/></span></div><p class="pre"><c:out value="${n.notes}"/></p></div>
  </c:forEach>
  <c:if test="${empty incident.investigations}"><p class="muted">No notes yet.</p></c:if>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
