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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.cft.server.core.internal.CloudFoundryServer;
import org.eclipse.cft.server.ui.internal.editor.CloudFoundryApplicationsEditorPage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.internal.ServerUIPlugin;
import org.eclipse.wst.server.ui.internal.editor.IServerEditorInput;
import org.eclipse.wst.server.ui.internal.editor.ServerEditor;
import org.eclipse.wst.server.ui.internal.editor.ServerEditorInput;
import org.eclipse.wst.server.ui.internal.view.servers.ModuleServer;



/**
 * @author Steffen Pingel
 * @author Terry Denney
 * @author Christian Dupuis
 */
@SuppressWarnings("restriction")
public class CloudFoundryActionProvider extends CommonActionProvider {

	public void init(ICommonActionExtensionSite actionSite) {
		super.init(actionSite);
		ICommonViewerSite site = actionSite.getViewSite();
		if (site instanceof ICommonViewerWorkbenchSite) {
			StructuredViewer viewer = actionSite.getStructuredViewer();
			if (viewer instanceof CommonViewer) {
				CommonViewer serversViewer = (CommonViewer) viewer;
				serversViewer.addOpenListener(new IOpenListener() {
					public void open(OpenEvent event) {
						ISelection s = event.getSelection();
						if (s instanceof IStructuredSelection) {
							IStructuredSelection selection = (IStructuredSelection) s;
							Object[] selectedObjects = selection.toArray();
							if (selectedObjects.length == 1 && selectedObjects[0] instanceof ModuleServer) {
								ModuleServer moduleServer = (ModuleServer) selectedObjects[0];
								openApplicationPage(moduleServer);
							}
						}
					}
				});
			}
		}
	}

	private void openApplicationPage(ModuleServer moduleServer) {
		final IModule[] modules = moduleServer.getModule();
		IServer server = moduleServer.getServer();
		CloudFoundryServer cloudServer = (CloudFoundryServer) server.loadAdapter(CloudFoundryServer.class, null);
		if (cloudServer != null && modules != null && modules.length == 1) {
			IWorkbenchWindow workbenchWindow = ServerUIPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();

			try {
				// open server editor
				ServerEditor editor = (ServerEditor) page.openEditor(new ServerEditorInput(server.getId()),
						IServerEditorInput.EDITOR_ID);

				// set applications page to active
				Method method = MultiPageEditorPart.class.getDeclaredMethod("setActivePage", int.class); //$NON-NLS-1$
				method.setAccessible(true);
				method.invoke(editor, 1);

				CloudFoundryApplicationsEditorPage editorPage = (CloudFoundryApplicationsEditorPage) editor.getSelectedPage();
				editorPage.selectAndReveal(modules[0]);
			}
			catch (CoreException e) {
				StatusManager.getManager().handle(e.getStatus(), StatusManager.LOG);
			}
			catch (SecurityException e) {
				StatusManager.getManager().handle(
						new Status(IStatus.ERROR, CloudFoundryServerUiPlugin.PLUGIN_ID,
								Messages.CloudFoundryActionProvider_ERROR_REFLECTION_CF_APP_PAGE, e), StatusManager.LOG);
			}
			catch (NoSuchMethodException e) {
				StatusManager.getManager().handle(
						new Status(IStatus.ERROR, CloudFoundryServerUiPlugin.PLUGIN_ID,
								Messages.CloudFoundryActionProvider_ERROR_REFLECTION_CF_APP_PAGE, e), StatusManager.LOG);
			}
			catch (IllegalArgumentException e) {
				StatusManager.getManager().handle(
						new Status(IStatus.ERROR, CloudFoundryServerUiPlugin.PLUGIN_ID,
								Messages.CloudFoundryActionProvider_ERROR_REFLECTION_CF_APP_PAGE, e), StatusManager.LOG);
			}
			catch (IllegalAccessException e) {
				StatusManager.getManager().handle(
						new Status(IStatus.ERROR, CloudFoundryServerUiPlugin.PLUGIN_ID,
								Messages.CloudFoundryActionProvider_ERROR_REFLECTION_CF_APP_PAGE, e), StatusManager.LOG);
			}
			catch (InvocationTargetException e) {
				StatusManager.getManager().handle(
						new Status(IStatus.ERROR, CloudFoundryServerUiPlugin.PLUGIN_ID,
								Messages.CloudFoundryActionProvider_ERROR_REFLECTION_CF_APP_PAGE, e), StatusManager.LOG);
			}
		}

	}

}
