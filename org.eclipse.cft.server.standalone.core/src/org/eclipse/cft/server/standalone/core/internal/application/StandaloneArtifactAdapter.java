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
package org.eclipse.cft.server.standalone.core.internal.application;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IModuleArtifact;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.model.ModuleArtifactAdapterDelegate;
import org.eclipse.wst.server.core.util.WebResource;

/**
 * Artifact adapters get invoked by RunOnServer framework.
 * 
 */
public class StandaloneArtifactAdapter extends ModuleArtifactAdapterDelegate {

	public IModuleArtifact getModuleArtifact(Object obj) {
		IProject project = (IProject) ((IAdaptable) obj).getAdapter(IProject.class);

		if (!StandAloneModuleFactory.canHandle(project)) {
			return null;
		}
		// Bypass WTP to load registered module factories as to possibly avoid
		// invoking the module factory on other components that support Java
		// type modules. See org.eclipse.wst.server.core.ServerUtil on how
		// to get modules through the WTP framework.

		IModule[] modules = ServerUtil.getModules(project);
		if (modules == null || modules.length == 0) {
			return null;
		}
		return new WebResource(modules[0], Path.EMPTY);
	}

}
