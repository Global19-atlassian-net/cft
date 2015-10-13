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
package org.eclipse.cft.server.standalone.internal.application;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.server.core.util.ProjectModule;

/**
 * Use a regular project module delegate for Java applications as the Java
 * applications are archived based on Java launch configurations and other
 * application repackaging only on deployment.
 * 
 */
public class JavaLauncherModuleDelegate extends ProjectModule {

	public JavaLauncherModuleDelegate(IProject project) {
		super(project);
	}

}
