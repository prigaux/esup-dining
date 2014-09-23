<%@ include file="/WEB-INF/jsp/header.jsp"%>

<c:if test="${not empty user}">

	<h1><spring:message code="edit.title"/></h1>

	<a href="${baseURL}/settings" class="icn-fam icn-fam-back">
		<spring:message code="edit.admin.back"/>
	</a> - 	
	<a href="${baseURL}/restaurants" class="icn-fam icn-fam-back">
		<spring:message code="go.back.home"/>
	</a> - 	
	<c:set value="${baseURL}/admin/stats" var="statsAdmin" />
	<a href="${statsAdmin}" class="icn-fam icn-fam-stats">
		Stats
	</a>

<br/>
	
<c:set value="${baseURL}/admin?action=setDefaultArea" var="setAreas"/>

<h1>
Choose your feed
</h1>		
		<c:set value="${baseURL}/admin?action=urlFeed" var="urlFlux"/>

		<form method="post" action="${urlFlux}" class="clearfix">		
			<c:if test="${not empty feedList}">
				
				<label>
					Feed to choose :
				</label>
				
				<select id="field-feed" name="feedId">
					<c:forEach var="feedInfo" items="${feedList}">
						<c:choose>
							<c:when test="${feedInfo.isDefault == true}">
								<c:set var="selected" value="selected=\"selected\""/>
							</c:when>
							<c:otherwise>
								<c:set var="selected" value=""/>
							</c:otherwise>
						</c:choose>
						<option value="${feedInfo.id}" ${selected}>
							${feedInfo.name}
						</option>
					</c:forEach>
				</select>
				
				<input type="submit"/>
												
			</c:if>
			
				
		</form>
		<hr/>
		
		<div>
	<form method="post" action="${setAreas}" class="clearfix"> 
	
		<c:forEach var="areaValue" items="${areaList}" varStatus="status">
			<label for="field-areas-${status.index}">
				<input type="checkbox" value="${areaValue}" name="chkArea[]" id="field-areas-${status.index}"
					<c:forEach var="areaDb" items="${defaultArea}">
						<c:if test="${areaDb == areaValue}">checked="checked"</c:if>
					</c:forEach>
				>
				${areaValue}
			</label>
		</c:forEach>
		
		<br/>
		<c:if test="${areaSubmit}">
			<label class="is-valid icn-fam icn-fam-valid">
				<spring:message code="edit.msg.success"/>
			</label>
		</c:if>
		
		<input type="submit"/>
	</form>
</div>

<hr/>
		
		<c:set value="${baseURL}/admin?action=forceFeedUpdate" var="forceFeedUpdate" />
		
		<p>
			<a href="${forceFeedUpdate}" class="btn btn-primary">
				<spring:message code="edit.admin.forceupdate"/>
			</a>
		</p>
		<c:if test="${not empty updateFeed}">
			${updateFeed}
		</c:if>
		
</c:if>
<c:if test="${not empty user}">
	<spring:message code="admin.notallowed"/>
</c:if>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>