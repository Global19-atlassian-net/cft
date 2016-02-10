/*******************************************************************************
 * Copyright (c) 2012, 2016 Pivotal Software, Inc.
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
package org.eclipse.cft.server.tests.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.cft.server.core.internal.CloudUtil;
import org.eclipse.cft.server.tests.util.CloudFoundryTestFixture;
import org.eclipse.cft.server.tests.util.CloudFoundryTestFixture.Harness;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.internal.Server;

import junit.framework.TestCase;

/**
 * @author Steffen Pingel
 */
public class CloudUtilTest extends TestCase {

	private Harness harness;

	private IServer server;

	private IProject project;

	@Override
	protected void setUp() throws Exception {
		harness = CloudFoundryTestFixture.getSafeTestFixture().createHarness();
		server = harness.createServer();
	}

	@Override
	protected void tearDown() throws Exception {
		harness.dispose();
	}

	public void testCreateWarFile() throws Exception {
		harness.createProject("appclient-module");
		project = harness.createProject("dynamic-webapp-with-appclient-module");
		harness.addModule(project);

		IModule[] modules = ServerUtil.getModules(project);
		File file = CloudUtil.createWarFile(modules, (Server) server, new NullProgressMonitor());

		List<String> files = new ArrayList<String>();
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> en = zipFile.entries();
		while (en.hasMoreElements()) {
			ZipEntry entry = en.nextElement();
			files.add(entry.getName());
		}
		// the directory entry is not always present, remove to avoid test
		// failure
		files.remove("WEB-INF/lib/");
		Collections.sort(files);
		List<String> expected = Arrays.asList("META-INF/", "META-INF/MANIFEST.MF", "WEB-INF/", "WEB-INF/classes/",
				"WEB-INF/classes/TestServlet.class", "WEB-INF/lib/appclient-module.jar", "WEB-INF/web.xml",
				"index.html");
		assertEquals(expected, files);
	}

}
