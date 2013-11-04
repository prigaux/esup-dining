<%@ include file="/WEB-INF/jsp/header.jsp"%>
	
	<h2>
    	<spring:message code="exception.title"/>
  	</h2>
	<p>
		<span class="exceptionMessage">
			<spring:message code="${exceptionMessage}"/>
		</span>

	    <a href="#" id="exception-details-link">
	    	<spring:message code="exception.details"/>
	    </a>		
	</p>
	
	<div class="exception-details">
		<pre>${exceptionStackTrace}</pre>
	</div>


<%@ include file="/WEB-INF/jsp/footer.jsp"%>