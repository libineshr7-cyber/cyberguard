<c:set var="pageTitle" value="Report Submitted"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="center narrow panel">
  <div class="check">&#10003;</div>
  <h1>Your cyber incident has been successfully reported.</h1>
  <p class="muted">Case ID</p>
  <p class="caseid"><c:out value="${caseId}"/></p>
  <p>Keep this Case ID to track your report. Save it somewhere safe &mdash; you will need it on the Track Case page.</p>
  <div class="actions"><a class="btn" href="${ctx}/track?caseId=${caseId}">Track this case</a><a class="btn btn-ghost" href="${ctx}/home">Back to Home</a></div>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
