/*******************************************************************************
 * Copyright (c) 2012, 2015 Pivotal Software, Inc. 
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
package org.eclipse.cft.server.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.cft.server.core.internal.CloudFoundryPlugin;
import org.eclipse.cft.server.core.internal.CloudFoundryServer;
import org.eclipse.cft.server.ui.internal.Messages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * @author Terry Denney
 */
public class DeleteServicesWizard extends Wizard {

	private final CloudFoundryServer cloudServer;

	private DeleteServicesWizardPage page;

	private final List<String> services;

	public DeleteServicesWizard(CloudFoundryServer cloudServer, List<String> services) {
		this.cloudServer = cloudServer;
		this.services = services;
		setWindowTitle(Messages.DeleteServicesWizard_TITLE_DELETE_SERVICE);
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page = new DeleteServicesWizardPage(cloudServer, services);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						List<String> selectedServices = page.getSelectedServices();
						if (selectedServices.size() > 0) {
							cloudServer.getBehaviour().operations().deleteServices(selectedServices).run(monitor);
						}
					}
					catch (CoreException e) {
						throw new InvocationTargetException(e);
					}
					catch (OperationCanceledException e) {
						throw new InterruptedException();
					}
					finally {
						monitor.done();
					}
				}
			});
			return true;
		}
		catch (InvocationTargetException e) {
			if (e.getCause() instanceof CoreException) {
				Status status = new Status(IStatus.ERROR, CloudFoundryPlugin.PLUGIN_ID, NLS.bind(
						Messages.DeleteServicesWizard_ERROR_DELETE_SERVICE, cloudServer.getServer().getName(), e
								.getCause().getMessage()), e);
				StatusManager.getManager().handle(status, StatusManager.SHOW | StatusManager.BLOCK | StatusManager.LOG);
			}
		}
		catch (InterruptedException e) {
			// ignore
		}
		return false;
	}

}
