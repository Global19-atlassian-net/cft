/*******************************************************************************
 * Copyright (c) 2015, 2016 Pivotal Software, Inc. and others
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
package org.eclipse.cft.server.standalone.core.internal.pivotal;

import org.eclipse.cft.server.core.ApplicationDeploymentInfo;
import org.eclipse.cft.server.core.internal.application.ICloudFoundryServerApplicationDelegate;
import org.eclipse.cft.server.core.internal.client.CloudFoundryApplicationModule;
import org.eclipse.cft.server.core.internal.pivotal.PivotalConstants;
import org.eclipse.cft.server.standalone.core.internal.application.StandaloneApplicationDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

/**
 * 
 * Determines if a give module is a Java standalone application. Also provides
 * an archiving mechanism that is specific to Java standalone applications.
 * 
 */
public class PivotalStandaloneApplicationDelegate extends StandaloneApplicationDelegate
		implements ICloudFoundryServerApplicationDelegate {

	public PivotalStandaloneApplicationDelegate() {

	}

	@Override
	public ApplicationDeploymentInfo getDefaultApplicationDeploymentInfo(IModule module, IServer server,
			IProgressMonitor monitor) throws CoreException {
		CloudFoundryApplicationModule appModule = getCloudFoundryApplicationModule(module, server);
		// Set default values.
		String appName = appModule.getDeployedApplicationName();
		ApplicationDeploymentInfo deploymentInfo = new ApplicationDeploymentInfo(appName);
		deploymentInfo.setBuildpack(PivotalConstants.PIVOTAL_WEB_SERVICES_JAVA_BUILDPACK);
		deploymentInfo.setMemory(PivotalConstants.PIVOTAL_DEFAULT_MEMORY);

		return deploymentInfo;
	}

	@Override
	public String getServerUri() {
		return PivotalConstants.PIVOTAL_WEB_SERVICES_URI;
	}

}
