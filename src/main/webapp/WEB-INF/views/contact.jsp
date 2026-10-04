<c:set var="pageTitle" value="Contact"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>Contact</h1>
<div class="grid g2">
  <div class="panel">
    <h3>Project team</h3>
    <p>Email: <code>team@cyberguard.example</code><br>Department of Computer Science (college mini-project)</p>
    <p class="muted">For real cyber emergencies please contact your local cybercrime authority or your bank immediately.</p>
  </div>
  <form class="panel form" method="post" action="${ctx}/contact">
    <h3>Send a message (demo)</h3>
    <label>Name<input name="name" required maxlength="100"></label>
    <label>Email<input type="email" name="email" required maxlength="150"></label>
    <label>Message<textarea name="message" rows="4" required maxlength="1000"></textarea></label>
    <button class="btn" type="submit">Send</button>
  </form>
</div>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
