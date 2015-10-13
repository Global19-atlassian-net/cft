/*******************************************************************************
 * Copyright (c) 2012, 2014 Pivotal Software, Inc. 
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
package org.eclipse.cft.server.rse.internal;

import org.eclipse.rse.subsystems.files.core.servicesubsystem.AbstractRemoteFile;
import org.eclipse.rse.subsystems.files.core.servicesubsystem.FileServiceSubSystem;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFileContext;

/**
 * @author Leo Dos Santos
 */
public class CloudFoundryRemoteFile extends AbstractRemoteFile {

	private CloudFoundryHostFile hostFile;

	public CloudFoundryRemoteFile(FileServiceSubSystem subSystem, IRemoteFileContext context, IRemoteFile parent,
			CloudFoundryHostFile hostFile) {
		super(subSystem, context, parent, hostFile);
		this.hostFile = hostFile;
	}

	public String getCanonicalPath() {
		return getAbsolutePath();
	}

	public String getClassification() {
		return hostFile.getClassification();
	}

}
