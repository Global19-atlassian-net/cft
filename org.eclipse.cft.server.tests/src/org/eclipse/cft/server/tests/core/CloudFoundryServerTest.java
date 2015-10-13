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
package org.eclipse.cft.server.tests.core;

import org.eclipse.cft.server.core.internal.CloudFoundryServer;
import org.eclipse.cft.server.core.internal.ServerCredentialsStore;
import org.eclipse.cft.server.ui.internal.ServerDescriptor;
import org.eclipse.cft.server.ui.internal.ServerHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.internal.ServerWorkingCopy;

import junit.framework.TestCase;

/**
 * @author Steffen Pingel
 */
public class CloudFoundryServerTest extends TestCase {

	private CloudFoundryServer cloudFoundryServer;

	private IServer server;

	private IServerWorkingCopy serverWC;

	// there is no longer just one default url for a server type
	// public void testGetServerIdDefaultUrl() throws CoreException {
	// assertEquals(null, CloudFoundryServer.getServerId());
	//
	// CloudFoundryServer.setUrl(null);
	// assertEquals("null@http://api.cloudfoundry.com",
	// CloudFoundryServer.getServerId());
	// }

	public void testGetServerId() throws CoreException {
		// assertEquals(null, CloudFoundryServer.getServerId());

		cloudFoundryServer.setUsername("user");
		assertEquals("user@http://api.cloudfoundry.com", cloudFoundryServer.getServerId());

		cloudFoundryServer.setPassword("pass");
		assertEquals("user@http://api.cloudfoundry.com", cloudFoundryServer.getServerId());

		cloudFoundryServer.setUrl("http://url");
		assertEquals("user@http://url", cloudFoundryServer.getServerId());
	}

	public void testGetPasswordLegacy() throws CoreException {
		// create legacy password attribute
		((ServerWorkingCopy) serverWC).setAttribute("org.eclipse.cft.password", "pwd");
		serverWC.save(true, null);

		// assertEquals(null, CloudFoundryServer.getServerId());

		// create new server instance
		serverWC = server.createWorkingCopy();
		cloudFoundryServer = (CloudFoundryServer) serverWC.loadAdapter(CloudFoundryServer.class, null);

		assertEquals("pwd", cloudFoundryServer.getPassword());

		ServerCredentialsStore store = cloudFoundryServer.getCredentialsStore();
		assertEquals(null, store.getPassword());

		serverWC.save(true, null);
		assertEquals("Unexpected migration of password to secure store although it was not changed", null,
				store.getPassword());
	}

	public void testSetPasswordMigrateLegacy() throws CoreException {
		// create legacy password attribute
		((ServerWorkingCopy) serverWC).setAttribute("org.eclipse.cft.password", "pwd");
		serverWC.save(true, null);

		// create new server instance
		serverWC = server.createWorkingCopy();
		cloudFoundryServer = (CloudFoundryServer) serverWC.loadAdapter(CloudFoundryServer.class, null);

		cloudFoundryServer.setPassword("newpwd");
		assertEquals("newpwd", cloudFoundryServer.getPassword());

		serverWC.save(true, null);
		ServerCredentialsStore store = cloudFoundryServer.getCredentialsStore();
		assertEquals("newpwd", store.getPassword());
	}

	public void testSetPasswordMigrateChangePasswordOnly() throws CoreException {
		// create legacy password attribute
		((ServerWorkingCopy) serverWC).setAttribute("org.eclipse.cft.url", "http://url");
		((ServerWorkingCopy) serverWC).setAttribute("org.eclipse.cft.username", "user");
		((ServerWorkingCopy) serverWC).setAttribute("org.eclipse.cft.password", "pwd");
		serverWC.save(true, null);

		// create new server instance
		serverWC = server.createWorkingCopy();
		cloudFoundryServer = (CloudFoundryServer) serverWC.loadAdapter(CloudFoundryServer.class, null);
		cloudFoundryServer.setPassword("newpwd");
		assertEquals("newpwd", cloudFoundryServer.getPassword());
		assertEquals("user@http://url", cloudFoundryServer.getServerId());

		serverWC.save(true, null);
		cloudFoundryServer = (CloudFoundryServer) server.loadAdapter(CloudFoundryServer.class, null);
		assertEquals("user@http://url", cloudFoundryServer.getServerId());
		assertEquals("newpwd", cloudFoundryServer.getPassword());
	}

	// FIXNS: Commented out for now due to STS-3280 which is an issue in Eclipse
	// 3.7 and higher. However, CF plugin has been installed in 3.7 and 4.2
	// without
	// issues
	// and passing functional verification tests, so this unit test case is not
	// a show stopper.

	// public void testSetPasswordMigrateUpdatesClient() throws CoreException {
	// // create legacy password attribute
	// ((ServerWorkingCopy)
	// serverWC).setAttribute("org.eclipse.cft.password", "pwd");
	// ((ServerWorkingCopy)
	// serverWC).setAttribute("org.eclipse.cft.username", "user");
	// // ((ServerWorkingCopy)
	// // serverWC).setAttribute("org.eclipse.cft.url", "url");
	// serverWC.save(true, null);
	//
	// assertEquals("user", cloudFoundryServer.getUsername());
	// assertEquals("pwd", cloudFoundryServer.getPassword());
	// CloudFoundryServerBehaviour serverBehaviour =
	// (CloudFoundryServerBehaviour) serverWC.loadAdapter(
	// CloudFoundryServerBehaviour.class, null);
	// CloudFoundryOperations client = serverBehaviour.getClient(null);
	//
	// // create new server instance
	// serverWC = server.createWorkingCopy();
	// cloudFoundryServer.setPassword("newpwd");
	// cloudFoundryServer.setUsername("newuser");
	// cloudFoundryServer.setUrl("http://api.cloudfoundry.com");
	// serverWC.save(true, null);
	//
	// // verify that old instance is updated
	// assertEquals("newuser", cloudFoundryServer.getUsername());
	// assertEquals("newpwd", cloudFoundryServer.getPassword());
	// assertNotSame("Expected new client instance due to password change",
	// client, serverBehaviour.getClient(null));
	// }

	public void testSaveCredentials() throws CoreException {
		cloudFoundryServer.setUsername("user");
		cloudFoundryServer.setPassword("pass");
		assertEquals("user", cloudFoundryServer.getUsername());
		assertEquals("pass", cloudFoundryServer.getPassword());

		serverWC.save(true, null);
		assertEquals("user", cloudFoundryServer.getUsername());
		assertEquals("pass", cloudFoundryServer.getPassword());

		cloudFoundryServer = (CloudFoundryServer) server.loadAdapter(CloudFoundryServer.class, null);
		assertEquals("user", cloudFoundryServer.getUsername());
		assertEquals("pass", cloudFoundryServer.getPassword());
	}

	@Override
	protected void setUp() throws Exception {
		ServerDescriptor descriptor = new ServerDescriptor("server") {
			{
				setRuntimeTypeId("org.cloudfoundry.cloudfoundryserver.test.runtime.10");
				setServerTypeId("org.cloudfoundry.cloudfoundryserver.test.10");
				setRuntimeName("Cloud Foundry Test Runtime");
				setServerName("Cloud Foundry Test Server");
				setForceCreateRuntime(true);
			}
		};

		ServerHandler handler = new ServerHandler(descriptor);
		server = handler.createServer(new NullProgressMonitor(), ServerHandler.ALWAYS_OVERWRITE);
		serverWC = server.createWorkingCopy();
		cloudFoundryServer = (CloudFoundryServer) serverWC.loadAdapter(CloudFoundryServer.class, null);

		// there is no longer a singer default URL for a server type
		cloudFoundryServer.setUrl("http://api.cloudfoundry.com");
	}

}
