
<%@ page import="br.ufrn.pds.usersregister.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-user" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<div style="">
				<div style="width: 48%; float: left">
					<ol class="property-list user">

						<li class="fieldcontain">
							<span id="name-label" class="property-label"><g:message code="user.name.label" default="Name" /></span>

							<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${userInstance}" field="name"/></span>

						</li>
						
						<li class="fieldcontain">
							<span id="username-label" class="property-label"><g:message code="user.username.label" default="Username" /></span>

								<span class="property-value" aria-labelledby="username-label"><g:fieldValue bean="${userInstance}" field="username"/></span>

						</li>
						
						<li class="fieldcontain">
							<span id="email-label" class="property-label"><g:message code="user.email.label" default="Email" /></span>

								<span class="property-value" aria-labelledby="email-label"><g:fieldValue bean="${userInstance}" field="email"/></span>

						</li>
						
						<li class="fieldcontain">
							<span id="cellPhone-label" class="property-label"><g:message code="user.cellPhone.label" default="Cell Phone" /></span>

								<span class="property-value" aria-labelledby="cellPhone-label"><g:fieldValue bean="${userInstance}" field="cellPhone"/></span>

						</li>
						
						<li class="fieldcontain">
							<span id="phone-label" class="property-label"><g:message code="user.phone.label" default="Phone" /></span>

								<span class="property-value" aria-labelledby="phone-label"><g:fieldValue bean="${userInstance}" field="phone"/></span>

						</li>
						
						<li class="fieldcontain">
							<span id="birthDate-label" class="property-label"><g:message code="user.birthDate.label" default="Birth Date" /></span>

								<span class="property-value" aria-labelledby="birthDate-label"><g:formatDate date="${userInstance?.birthDate}" /></span>

						</li>

						<g:if test="${userInstance?.photo}">
						<li class="fieldcontain">
							<span id="photo-label" class="property-label"><g:message code="user.photo.label" default="Photo" /></span>

							<span class="property-value" aria-labelledby="photo-label">
								<g:image afile="${userInstance?.photo}"/>
							</span>
						</li>
						</g:if>
					</ol>
				</div>
				<div style="width: 48%; float: left">
					<ol class="property-list user">
						<li class="fieldcontain">
							<span id="street-label" class="property-label"><g:message code="user.street.label" default="Street" /></span>

							<span class="property-value" aria-labelledby="street-label"><g:fieldValue bean="${userInstance}" field="street"/></span>
						</li>
						
						<li class="fieldcontain">
							<span id="number-label" class="property-label"><g:message code="user.number.label" default="Number" /></span>

							<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${userInstance}" field="number"/></span>
						</li>
						
						<li class="fieldcontain">
							<span id="complement-label" class="property-label"><g:message code="user.complement.label" default="Complement" /></span>

							<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${userInstance}" field="complement"/></span>
						</li>
						
						<li class="fieldcontain">
							<span id="district-label" class="property-label"><g:message code="user.district.label" default="District" /></span>

							<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${userInstance}" field="district"/></span>
						</li>
						
						<li class="fieldcontain">
							<span id="zip-label" class="property-label"><g:message code="user.zip.label" default="Zip" /></span>

							<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${userInstance}" field="zip"/></span>
						</li>
						
						<li class="fieldcontain">
							<span id="city-label" class="property-label"><g:message code="user.city.label" default="City" /></span>

							<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${userInstance}" field="city"/></span>
						</li>
						
						<li class="fieldcontain">
							<span id="state-label" class="property-label"><g:message code="user.state.label" default="State" /></span>

							<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${userInstance}" field="state"/></span>
						</li>
					</ol>
					
				</div>
			</div>
			
			<div style="clear: both"></div>
			
			<g:form url="[resource:userInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${userInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
