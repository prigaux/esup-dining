<%@ include file="/WEB-INF/jsp/header.jsp"%>

  <h1>Lexique</h1>

  <ul>
    <c:forEach var="codeNumber" items="${code}">
      <li>
        <strong>
          <img src="<%= renderRequest.getContextPath() %><spring:message code="meal.code.${fn:trim(codeNumber)}.img" />"
           alt="<spring:message code="meal.code.${fn:trim(codeNumber)}.description" />"
         title="<spring:message code="meal.code.${fn:trim(codeNumber)}.name" />"
      /> <spring:message code="meal.code.${fn:trim(codeNumber)}.name"/>  </strong>
       <br>
      <spring:message code="meal.code.${fn:trim(codeNumber)}.description" />
        
      </li>
    </c:forEach>
  </ul>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>