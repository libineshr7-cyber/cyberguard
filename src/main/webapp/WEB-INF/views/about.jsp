<c:set var="pageTitle" value="About"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<h1>About CyberGuard</h1>
<div class="panel">
  <p>CyberGuard is a <strong>college mini-project</strong> for the Java OOP subject. It demonstrates how a cyber incident reporting and case tracking website can be built with Java Servlets, JSP, JDBC and MySQL running on Apache Tomcat.</p>
  <p><strong>This is not a real or government cybercrime portal.</strong> Records marked <span class="demo-tag">DEMO</span> are sample data created for demonstration only.</p>
</div>
<div class="grid g2">
  <article class="card"><h3>How it works</h3><ol><li>Report an incident (login is optional).</li><li>Receive a unique Case ID such as <code>CYB-2026-00001</code>.</li><li>An investigator reviews and updates the case.</li><li>Track progress with your Case ID.</li></ol></article>
  <article class="card"><h3>Technology</h3><ul><li>HTML5, CSS3, JavaScript</li><li>Java Servlets &amp; JSP (MVC)</li><li>JDBC with DAO pattern</li><li>MySQL database</li><li>Apache Tomcat server</li></ul></article>
</div>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
