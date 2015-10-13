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
package org.eclipse.cft.server.ui.internal.actions;

import org.eclipse.cft.server.core.internal.CloudFoundryPlugin;
import org.eclipse.cft.server.core.internal.CloudFoundryServer;
import org.eclipse.cft.server.core.internal.client.CloudFoundryApplicationModule;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.IServerModule;

public abstract class AbstractCloudFoundryServerAction implements IObjectActionDelegate {

	private IModule selectedModule;

	private IServer selectedServer;

	public void selectionChanged(IAction action, ISelection selection) {
		selectedServer = getSelectedServer(selection);
		CloudFoundryServer cloudServer = selectedServer != null ? (CloudFoundryServer) selectedServer.loadAdapter(
				CloudFoundryServer.class, null) : null;
		CloudFoundryApplicationModule appModule = cloudServer != null && selectedModule != null ? cloudServer
				.getExistingCloudModule(selectedModule) : null;
		serverSelectionChanged(cloudServer, appModule, action);
	}

	/**
	 * Subclasses can override if they want to perform some behaviour on a valid
	 * server selection, like enabling/disabling the action.
	 * @param action
	 */
	protected void serverSelectionChanged(CloudFoundryServer cloudServer, CloudFoundryApplicationModule appModule,
			IAction action) {
		// Do nothing
	}

	public void run(IAction action) {

	
		String error = null;
		CloudFoundryServer cloudServer = selectedServer != null ? (CloudFoundryServer) selectedServer.loadAdapter(
				CloudFoundryServer.class, null) : null;
		CloudFoundryApplicationModule appModule = cloudServer != null && selectedModule != null ? cloudServer
				.getExistingCloudModule(selectedModule) : null;
		if (selectedServer == null) {
			error = "No Cloud Foundry server instance available to run the selected action."; //$NON-NLS-1$
		}

		if (error == null) {
			doRun(cloudServer, appModule, action);
		}
		else {
			error += " - " + action.getText(); //$NON-NLS-1$
			CloudFoundryPlugin.logError(error);
		}
	}

	abstract void doRun(CloudFoundryServer cloudServer, CloudFoundryApplicationModule appModule, IAction action);



	protected IServer getSelectedServer(ISelection selection) {
		IServer server = null;
		selectedModule = null;
		if (!selection.isEmpty()) {
			if (selection instanceof IStructuredSelection) {
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				if (obj instanceof IServer) {
					server = (IServer) obj;
				}
				else if (obj instanceof IServerModule) {
					IServerModule sm = (IServerModule) obj;
					IModule[] module = sm.getModule();
					selectedModule = module[module.length - 1];
					if (selectedModule != null) {
						server = sm.getServer();
					}
				}
			}
		}
		return server;
	}

}
