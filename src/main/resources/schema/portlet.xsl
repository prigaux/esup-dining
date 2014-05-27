<?xml version="1.0" ?>
<!--

    Licensed to ESUP-Portail under one or more contributor license
    agreements. See the NOTICE file distributed with this work for
    additional information regarding copyright ownership.

    ESUP-Portail licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file except in
    compliance with the License. You may obtain a copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text"/>
	<xsl:template match="/">
		<json>
			{
				"restaurants" : [
					<xsl:for-each select="//restaurant">
						{
							<xsl:variable name="restaurantId" select="@id"/>
							"id" : <xsl:value-of select="substring('r6', 2)"/>,
							"title" : "<xsl:value-of select='@nom'/>",
							"lat" : <xsl:value-of select='@lat'/>,
							"lon" : <xsl:value-of select='@lon'/>,
							"area" : "<xsl:value-of select='@zone'/>",
							<xsl:if test="@opening">
							"opening" : "<xsl:value-of select="@opening"/>",
							</xsl:if>
							<xsl:if test="@closing">
							"closing" : "<xsl:value-of select="@closing"/>",
							</xsl:if>
							<xsl:if test="@type">
							"type" : "<xsl:value-of select="@type"/>",
							</xsl:if>
							<xsl:if test="@accessibilite">
							"accessibility" : <xsl:value-of select="@accessibilite"/>,
							</xsl:if>
							<xsl:if test="@wifi">
							"wifi" : <xsl:value-of select="@wifi"/>,
							</xsl:if>
							<xsl:if test="./shortdesc">
							"shortdesc" : "<xsl:value-of select="/shortdesc/text()"/>",
							</xsl:if>
							<xsl:if test="./description">
							"description" : "<xsl:value-of select="./description/text()"/>",
							</xsl:if>
							<xsl:if test="./acces">
							"access" : "<xsl:value-of select="./acces/text()"/>",
							</xsl:if>
							<xsl:if test="./horaires">
							"operationalhours" : "<xsl:value-of select="./horaires/text()"/>",
							</xsl:if>
							<xsl:if test="./contact">
							"contact" : {
								"tel" : "<xsl:value-of select="contact/@tel"/>",
								"email" : "<xsl:value-of select="contact/@email"/>"
							},
							</xsl:if>
							<xsl:if test="./photo">
							"photo" : {
								"src" : "<xsl:value-of select="photo/@src"/>",
								"alt" : "<xsl:value-of select="photo/@alt"/>"
							},
							</xsl:if>
							<xsl:if test="./paiement">
							"payment" : [
								<xsl:for-each select="./paiement/moyen">
									{
										"name" : "<xsl:value-of select="@nom"/>"
									}<xsl:if test="position() != last()">,</xsl:if>
								</xsl:for-each>
							],
							</xsl:if>
							"menus" : [
								<xsl:for-each select="//menu[@restaurant=$restaurantId]">
									{
										"date" : "<xsl:value-of select='@date'/>",
										"meal" : [
										<xsl:for-each select="./repas">	
											{
												"name" : "<xsl:value-of select='@nom'/>",
												"foodcategory" : [
													<xsl:for-each select="./plats">
														{
															"name" : "<xsl:value-of select="@nom"/>",
															"dishes" : [
																<xsl:for-each select="plat">
																	{
																		<xsl:if test="./code">
																			"code" : [
																				<xsl:for-each select="code">
																					<xsl:value-of select="@valeur"/><xsl:if test="position() != last()">,</xsl:if>
																				</xsl:for-each>
																			],
																		</xsl:if>
																		<xsl:if test="./ingredients">
																		"ingredients" : "<xsl:value-of select="ingredients/text()"/>",
																		</xsl:if>
																		"name" : "<xsl:value-of select="nom/text()"/>"
																	}<xsl:if test="position() != last()">,</xsl:if>
																</xsl:for-each>
															]
														}<xsl:if test="position() != last()">,</xsl:if>
													</xsl:for-each>
												]
											}<xsl:if test="position() != last()">,</xsl:if>
										</xsl:for-each>
										]
									}<xsl:if test="position() != last()">,</xsl:if>
								</xsl:for-each>
							]
						}<xsl:if test="position() != last()">,</xsl:if>
					</xsl:for-each>
				]
			}
		</json>
	</xsl:template>
</xsl:stylesheet>