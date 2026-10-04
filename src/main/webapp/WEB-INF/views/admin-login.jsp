<c:set var="pageTitle" value="Admin Login"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="narrow">
  <form class="panel form" method="post" action="${ctx}/admin-login">
    <h1>Admin / Investigator Login</h1>
    <p class="muted">Authorized staff only.</p>
    <label>Email<input type="email" name="email" required maxlength="150" autocomplete="username" value="<c:out value='${param.email}'/>"></label>
    <label>Password<input type="password" name="password" required maxlength="72" autocomplete="current-password"></label>
    <button class="btn block" type="submit">Login</button>
    <p class="muted"><a href="${ctx}/login">Back to citizen login</a></p>
  </form>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
