/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.solomax;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class MainPanel extends Panel {
	private static final long serialVersionUID = 1L;
	public static final String CONTAINER_PANEL_ID = "container";
	private final WebMarkupContainer container = new WebMarkupContainer(CONTAINER_PANEL_ID);
	private static final String PANEL_ID = "panel";

	public MainPanel(String id) {
		super(id);
		setAuto(true);
		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);
	}

	@Override
	protected void onInitialize() {
		add(container.add(new TestListPanel(PANEL_ID)).setOutputMarkupId(true));
		super.onInitialize();
	}
}
