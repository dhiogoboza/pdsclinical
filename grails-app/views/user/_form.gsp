<%@ page import="br.ufrn.pds.usersregister.User" %>

<div>
	<div style="width: 48%; float: left">
		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'name', 'error')} ">
			<label for="name">
				<g:message code="user.name.label" default="Name" />

			</label>
			<g:textField name="name" value="${userInstance?.name}"/>
		</div>

		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
			<label for="password">
				<g:message code="user.password.label" default="Password" />
				<span class="required-indicator">*</span>
			</label>
			<g:field type="password" name="password" required="" value="${userInstance?.password}"/>
		</div>

		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'username', 'error')} required">
			<label for="username">
				<g:message code="user.username.label" default="Username" />
				<span class="required-indicator">*</span>
			</label>
			<g:textField name="username" required="" value="${userInstance?.username}"/>
		</div>

		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'email', 'error')} required">
			<label for="email">
				<g:message code="user.email.label" default="Email" />
				<span class="required-indicator">*</span>
			</label>
			<g:textField name="email" required="" value="${userInstance?.email}"/>
		</div>

		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'cellPhone', 'error')} ">
			<label for="cellPhone">
				<g:message code="user.cellPhone.label" default="Cell Phone" />

			</label>
			<g:textField name="cellPhone" value="${userInstance?.cellPhone}"/>
		</div>

		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'phone', 'error')} ">
			<label for="phone">
				<g:message code="user.phone.label" default="Phone" />

			</label>
			<g:textField name="phone" value="${userInstance?.phone}"/>
		</div>

		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'birthDate', 'error')} ">
			<label for="birthDate">
				<g:message code="user.birthDate.label" default="Birth Date" />

			</label>
			<g:datePicker name="birthDate" precision="day"  value="${userInstance?.birthDate}" default="none" noSelection="['': '']" />
		</div>

		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'photo', 'error')} ">
			<label for="photo">
				<g:message code="user.photo.label" default="Photo" />

			</label>
			<input type="file" name="photo_" id="photo_"/>
		</div>	
	</div>


	<div style="width: 48%; float: left">
		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'street', 'error')} ">
			<label for="street">
				<g:message code="user.street.label" default="Street" />

			</label>
			<g:textField name="street" value="${userInstance?.street}"/>
		</div>

		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'number', 'error')} required">
			<label for="number">
				<g:message code="user.number.label" default="Number" />
			</label>
			<g:textField name="number" value="${userInstance?.number}"/>
		</div>
		
		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'complement', 'error')} required">
			<label for="complement">
				<g:message code="user.complement.label" default="Complement" />
			</label>
			<g:textField name="complement" value="${userInstance?.complement}"/>
		</div>
		
		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'district', 'error')} required">
			<label for="district">
				<g:message code="user.district.label" default="District" />
			</label>
			<g:textField name="district" value="${userInstance?.district}"/>
		</div>
		
		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'zip', 'error')} required">
			<label for="zip">
				<g:message code="user.zip.label" default="Zip" />
			</label>
			<g:textField name="zip" value="${userInstance?.zip}"/>
		</div>
		
		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'city', 'error')} required">
			<label for="city">
				<g:message code="user.city.label" default="City" />
			</label>
			<g:textField name="city" value="${userInstance?.city}"/>
		</div>
		
		<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'state', 'error')} required">
			<label for="state">
				<g:message code="user.state.label" default="State" />
			</label>
			<g:textField name="state" value="${userInstance?.state}"/>
		</div>
		
	</div>
</div>
