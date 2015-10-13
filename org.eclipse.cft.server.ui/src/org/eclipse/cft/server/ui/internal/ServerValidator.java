/*******************************************************************************
 * Copyright (c) 2013, 2014 Pivotal Software, Inc. 
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution. 
 * 
 * The Eclipse Public License is available at 
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * and the Apache License v2.0 is available at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * You may elect to redistribute this code under either of these licenses.
 *  
 *  Contributors:
 *     Pivotal Software, Inc. - initial API and implementation
 ********************************************************************************/
package org.eclipse.cft.server.ui.internal;

import org.eclipse.jface.operation.IRunnableContext;

public interface ServerValidator {

	/**
	 * Validates server credentials and orgs and spaces selection. The contract
	 * should be: if not validating against the server, validate locally (e.g.
	 * ensure password and username are entered and have valid characters). If
	 * validating against a server, do a full validation and ensure an org and
	 * space are selected for the credentials.
	 * @param validateAgainstServer true if validation should be local, or false
	 * if requires server validation.
	 * @param validateSpace true if cloud space should be validated.
	 * @param runnableContext. May be null. If null, a default runnable context
	 * should be used.
	 * @return non-null validation status
	 */
	public abstract ValidationStatus validate(boolean validateAgainstServer, boolean validateSpace,
			IRunnableContext runnableContext);

}