/*******************************************************************************
 * Copyright (c) 2014 Pivotal Software, Inc. 
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

import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;

/**
 * Provides a wizard context for UI Parts, that includes getting a runnable
 * context for performing operations in a wizard.
 */
public class WizardHandleContext {

	private final IWizardHandle wizardHandle;

	public WizardHandleContext(IWizardHandle wizardHandle) {
		this.wizardHandle = wizardHandle;
	}

	public IWizardHandle getWizardHandle() {
		return wizardHandle;
	}

	public IRunnableContext getRunnableContext() {
		return new IRunnableContext() {
			public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable)
					throws InvocationTargetException, InterruptedException {
				wizardHandle.run(fork, cancelable, runnable);
			}
		};
	}

}
