<c:set var="pageTitle" value="Login"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="narrow">
  <form class="panel form" method="post" action="${ctx}/login">
    <h1>Login</h1>
    <label>Email<input type="email" name="email" required maxlength="150" autocomplete="username" value="<c:out value='${param.email}'/>"></label>
    <label>Password<input type="password" name="password" required maxlength="72" autocomplete="current-password"></label>
    <button class="btn block" type="submit">Login</button>
    <p class="muted">New here? <a href="${ctx}/register">Create an account</a> &middot; <a href="${ctx}/admin-login">Admin / Investigator login</a></p>
  </form>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
