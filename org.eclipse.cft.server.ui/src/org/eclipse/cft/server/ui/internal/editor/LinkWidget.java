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
package org.eclipse.cft.server.ui.internal.editor;

import org.eclipse.cft.server.ui.internal.UIWebNavigationHelper;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;


public class LinkWidget {

	private String label;

	private String location;

	private Label labelControl;

	private Hyperlink linkControl;

	private FormToolkit toolkit;

	private Composite parent;

	public LinkWidget(Composite parent, String label, String location, FormToolkit toolkit) {
		this.label = label;
		this.location = location;
		this.toolkit = toolkit;
		this.parent = parent;
	}

	public void createControl() {
		Composite linkComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 5).applyTo(linkComposite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(linkComposite);

		if (toolkit != null) {
			labelControl = toolkit.createLabel(linkComposite, label + ":"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(labelControl);
			labelControl.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));

			linkControl = toolkit.createHyperlink(linkComposite, location, SWT.MULTI);
			GridDataFactory.fillDefaults().grab(true, false).align(SWT.LEFT, SWT.TOP).applyTo(linkControl);
			linkControl.setBackground(toolkit.getColors().getBackground());
		}
		else {
			labelControl = new Label(linkComposite, SWT.NONE);
			labelControl.setText(label);
			GridDataFactory.fillDefaults().grab(false, false).applyTo(labelControl);
			linkControl = new Hyperlink(linkComposite, SWT.MULTI);
			GridDataFactory.fillDefaults().grab(true, false).hint(250, SWT.DEFAULT).applyTo(linkControl);
			linkControl.setText(location);
		}

		linkControl.setForeground(JFaceColors.getActiveHyperlinkText(linkControl.getDisplay()));

		linkControl.addHyperlinkListener(new HyperlinkAdapter() {

			public void linkActivated(HyperlinkEvent e) {
				navigate();
			}
		});

	}

	protected void navigate() {
		new UIWebNavigationHelper(location, label).navigate();
	}

	public void setVisible(boolean show) {
		if (labelControl != null) {
			labelControl.setVisible(show);
		}
		if (linkControl != null) {
			linkControl.setVisible(show);
		}
	}

}
