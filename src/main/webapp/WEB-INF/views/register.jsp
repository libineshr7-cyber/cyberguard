<c:set var="pageTitle" value="Register"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="narrow">
  <form class="panel form" method="post" action="${ctx}/register">
    <h1>Create account</h1>
    <label>Full name<input name="name" required minlength="2" maxlength="100" value="<c:out value='${param.name}'/>"></label>
    <label>Email<input type="email" name="email" required maxlength="150" value="<c:out value='${param.email}'/>"></label>
    <label>Phone<input type="tel" name="phone" required pattern="[0-9+\- ]{7,15}" title="7-15 digits" value="<c:out value='${param.phone}'/>"></label>
    <label>Password<input type="password" id="password" name="password" required minlength="8" maxlength="72" autocomplete="new-password"><small>At least 8 characters with a letter and a number.</small></label>
    <label>Confirm password<input type="password" id="confirm" name="confirm" required minlength="8" maxlength="72" autocomplete="new-password"></label>
    <button class="btn block" type="submit">Register</button>
    <p class="muted">Already registered? <a href="${ctx}/login">Login</a></p>
  </form>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
