<c:set var="pageTitle" value="Something went wrong"/>
<%@ include file="/WEB-INF/views/_header.jspf" %>
<section class="center narrow panel">
  <h1>Oops!</h1>
  <c:if test="${empty error}"><p>The page you requested could not be found or an unexpected problem occurred.</p></c:if>
  <p><a class="btn" href="${ctx}/home">Back to Home</a></p>
</section>
<%@ include file="/WEB-INF/views/_footer.jspf" %>
