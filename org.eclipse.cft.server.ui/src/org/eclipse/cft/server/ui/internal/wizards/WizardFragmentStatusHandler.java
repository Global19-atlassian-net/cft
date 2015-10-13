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
package org.eclipse.cft.server.ui.internal.wizards;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;

/**
 * Updates status of a wizard if the wizard components are defined by WST
 * {@link IWizardHandle}
 *
 */
public class WizardFragmentStatusHandler extends WizardStatusHandler {

	private final IWizardHandle wizardHandle;

	public WizardFragmentStatusHandler(IWizardHandle wizardHandle) {
		this.wizardHandle = wizardHandle;
	}

	protected void update() {
		if (canUpdate()) {
			wizardHandle.update();
		}
	}

	protected void setWizardError(String message) {
		doSetMessage(message, DialogPage.ERROR);
	}

	protected void setWizardInformation(String message) {
		doSetMessage(message, DialogPage.INFORMATION);
	}

	protected void setWizardMessage(String message) {
		doSetMessage(message, DialogPage.NONE);
	}

	protected void doSetMessage(String message, int type) {
		if (canUpdate()) {
			wizardHandle.setMessage(message, type);
		}
	}

	/**
	 * Determines if the message can be set. This is to guard against possible
	 * NPE if attempting to set a message when the handle is a wizard page and
	 * the wizard container is not yet available.
	 * @return true if it can be set. False if it is not possible (e.g, wizard
	 * container is not available yet)
	 */
	protected boolean canUpdate() {
		if (!(wizardHandle instanceof IWizardPage)) {
			return true;
		}
		IWizardPage page = (IWizardPage) wizardHandle;
		return page.getWizard() != null && page.getWizard().getContainer() != null;
	}
}
