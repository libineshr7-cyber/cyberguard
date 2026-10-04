<c:set var="pageTitle" value="Report Incident"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Report a Cyber Incident</h1>
<p class="muted">Fields marked * are required. Do not include passwords, OTPs or card numbers.</p>
<form class="form" method="post" action="${ctx}/report" enctype="multipart/form-data">
  <fieldset class="panel">
    <legend>Your details</legend>
    <div class="row">
      <label>Full name *<input name="fullName" required minlength="2" maxlength="100" value="<c:out value='${empty param.fullName ? sessionScope.user.name : param.fullName}'/>"></label>
      <label>Email *<input type="email" name="email" required maxlength="150" value="<c:out value='${empty param.email ? sessionScope.user.email : param.email}'/>"></label>
      <label>Phone *<input type="tel" name="phone" required pattern="[0-9+\- ]{7,15}" title="7-15 digits" value="<c:out value='${empty param.phone ? sessionScope.user.phone : param.phone}'/>"></label>
    </div>
    <c:if test="${empty sessionScope.user}"><p class="muted">Tip: <a href="${ctx}/login">log in</a> to see all your reports in your dashboard.</p></c:if>
  </fieldset>

  <fieldset class="panel">
    <legend>Incident details</legend>
    <div class="row">
      <label>Incident type *
        <select name="incidentType" required>
          <option value="">Select a category</option>
          <c:forEach var="t" items="${incidentTypes}"><option value="${t}" ${param.incidentType eq t ? 'selected' : ''}><c:out value="${t}"/></option></c:forEach>
        </select></label>
      <label>Date of incident *<input type="date" name="incidentDate" required data-max-today value="<c:out value='${param.incidentDate}'/>"></label>
      <label>Approximate financial loss (if any)<input type="number" name="financialLoss" min="0" step="0.01" placeholder="0.00" value="<c:out value='${param.financialLoss}'/>"></label>
    </div>
    <label>Description * <small>(at least 20 characters)</small><textarea name="description" rows="6" required minlength="20" maxlength="5000"><c:out value="${param.description}"/></textarea></label>
    <label>Suspicious website / URL<input type="url" name="suspiciousUrl" maxlength="500" placeholder="https://" value="<c:out value='${param.suspiciousUrl}'/>"><small>Do not visit the site again. It will be shown as plain text only.</small></label>
  </fieldset>

  <fieldset class="panel">
    <legend>Evidence &amp; extra information</legend>
    <label>Evidence attachment <small>(JPG, PNG, PDF or TXT &middot; max 3 files &middot; 5 MB each)</small>
      <input type="file" id="evidence" name="evidence" multiple accept=".jpg,.jpeg,.png,.pdf,.txt"></label>
    <label>Additional information<textarea name="additionalInfo" rows="3" maxlength="2000"><c:out value="${param.additionalInfo}"/></textarea></label>
  </fieldset>
  <button class="btn" type="submit">Submit Report</button>
</form>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
