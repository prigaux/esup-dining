<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h1><spring:message code="edit.title"/></h1>

<a href="${baseURL}/restaurants" class="icn-fam icn-fam-back">
	<spring:message code="go.back.home"/>
</a>

<c:if test="${sessionScope.isAdmin}">
	<h2>
		<c:set value="${baseURL}/admin" var="adminSettings" />
	
		<a href="${adminSettings}">
			<spring:message code="menu.editadmin"/>
		</a>
	</h2>
</c:if>

<c:if test="${not empty areaList}">
	
	<c:set value="${baseURL}/settings?action=setUserArea" var="setUserArea"/>

	<form method="post" action="${setUserArea}">
		<fieldset>
			<legend><spring:message code="edit.form.zone.legend"/></legend>
			
			
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
			
			<%--<label for="field-zone"><spring:message code="edit.form.zone.label"/></label>
			<select id="field-zone" name="zone">
				<c:forEach var="area" items="${areas}">
					<option value="${area}"
						<c:if test="${area == defaultArea}">
							selected="selected"
						</c:if>
					>
						${area}
					</option>
				</c:forEach>
			</select>--%>
				<c:if test="${zoneSubmit == 'true'}">
					<label class="is-valid icn-fam icn-fam-valid">
						<spring:message code="edit.msg.success"/>
					</label>
				</c:if>
			<input type="submit" value="<spring:message code="edit.form.submit"/>"/>
		</fieldset>
	</form>
</c:if>

<c:if test="${not empty listFavRestaurant}">
<h3><spring:message code="view.favorite.title"/></h3>

<table class="table table-responsive table-striped">
	<c:forEach var="restaurant" items="${listFavRestaurant}">
		<tr>
			<td>
				${restaurant.title}
			</td>
			<td class="ta-right">
			 	<c:set value="${baseURL}/settings?action=removeFavorite&restaurant-id=${restaurant.id}" var="removeFavorite" />
				<a href="${removeFavorite}" class="btn btn-danger" style="color: white;">
					<spring:message code="edit.delete"/> <span class="glyphicon glyphicon-remove"></span>
				</a>
			</td>
		</tr>
	</c:forEach>
</table>

</c:if>

<h3><spring:message code="edit.nutritive.prefs"/></h3>
<c:set value="${baseURL}/settings?action=nutritionPreferences" var="nutritionPreferences"/>
<form method="POST" action="${nutritionPreferences}">
    <fieldset>
        <legend>
            <spring:message code="edit.nutritive.allergens"/>
        </legend>
        <c:forEach var="code" items="${allergenCodes}" varStatus="status">

            <c:if test="${status.index % 2 == 0}">
                <c:if test="${!status.first}">
                    </div>
                </c:if>
                <c:if test="${!status.last}">
                    <div class="row">
                </c:if>
            </c:if>

            <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
                <label style="float: none;">
                    <spring:message code="meal.code.${fn:trim(code)}.name" />
                    <input type="checkbox" name="code-${code}" id="code-${code}"
                        <c:forEach var="userPrefCode" items="${nutritionPrefs}">
                            <c:if test="${userPrefCode == code}">
                                checked="checked"
                            </c:if>
                        </c:forEach>			
                    />
                </label>	
            </div>
	</c:forEach>
    </fieldset>
    
    <fieldset>
        <legend>
            <spring:message code="edit.nutritive.preferences"/>
        </legend>
        <c:forEach var="code" items="${preferenceCodes}" varStatus="status">

            <c:if test="${status.index % 2 == 0}">
                <c:if test="${!status.first}">
                    </div>
                </c:if>
                <c:if test="${!status.last}">
                    <div class="row">
                </c:if>
            </c:if>

            <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
                <label style="float: none;">
                    <spring:message code="meal.code.${fn:trim(code)}.name" />
                    <input type="checkbox" name="code-${code}" id="code-${code}"
                        <c:forEach var="userPrefCode" items="${nutritionPrefs}">
                            <c:if test="${userPrefCode == code}">
                                checked="checked"
                            </c:if>
                        </c:forEach>			
                    />
                </label>	
            </div>
	</c:forEach>
    </fieldset>
	
	<c:if test="${nutritSubmit == 'true'}">
		<label class="is-valid icn-fam icn-fam-valid" style="float:none;">
			<spring:message code="edit.msg.success"/>
		</label>
	</c:if>
	
	<input type="submit" value="<spring:message code="edit.form.submit"/>"/>
</form>
	
<%@ include file="/WEB-INF/jsp/footer.jsp"%>