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
package org.eclipse.cft.server.core.internal;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;

/**
 * Wraps around a module resource delta so that the delta is accessible to
 * operations that need to check if the resource has been removed
 * 
 */
public class ModuleResourceDeltaWrapper implements IModuleResource {

	private final IModuleResourceDelta resourceDelta;

	public ModuleResourceDeltaWrapper(IModuleResourceDelta resourceDelta) {
		this.resourceDelta = resourceDelta;
	}

	public Object getAdapter(Class clazz) {

		return resourceDelta.getModuleResource().getAdapter(clazz);
	}

	public IPath getModuleRelativePath() {

		return resourceDelta.getModuleResource().getModuleRelativePath();
	}

	public String getName() {
		return resourceDelta.getModuleResource().getName();
	}

	public IModuleResourceDelta getResourceDelta() {
		return resourceDelta;
	}
}