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
package org.eclipse.cft.server.tests.server;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Terry Denney
 * @author Steffen Pingel
 */
public class TestServlet

// FIXNS: Commented out because of STS-3159
// extends HttpServlet

{

	public static class Response {

		private final int status;

		private final String message;

		private final String body;

		public Response(int status, String message, String body) {
			this.status = status;
			this.message = message;
			this.body = body;
		}

	}

	private static final long serialVersionUID = 1L;

	private static TestServlet instance;

	public static TestServlet getInstance() {
		return instance;
	}

	private final List<Response> responses = new LinkedList<TestServlet.Response>();

	public TestServlet() {
		instance = this;
	}

	public void addResponse(Response response) {
		responses.add(response);
	}

	// FIXNS: Commented out because of STS-3159
	// @Override
	// protected void doGet(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	// }
	//
	// @Override
	// protected void doPost(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException,
	// IOException {
	// response.setContentType("application/json;charset=utf-8");
	// if (responses.size() > 0) {
	// Response mockReposonse = responses.remove(0);
	// response.sendError(mockReposonse.status, mockReposonse.message);
	// response.getWriter().append(mockReposonse.body);
	// }
	// else {
	// response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
	// }
	// }

}
