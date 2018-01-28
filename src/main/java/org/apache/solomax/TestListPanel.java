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

import static org.apache.wicket.ajax.attributes.CallbackParameter.explicit;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

public class TestListPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private final FeedbackPanel feedback = new FeedbackPanel("feedback");
	private final AbstractDefaultAjaxBehavior wbAction = new AbstractDefaultAjaxBehavior() {
		private static final long serialVersionUID = 1L;

		@Override
		protected void respond(AjaxRequestTarget target) {
		}
	};

	public TestListPanel(String id) {
		super(id);
		List<String> list = Arrays.asList(new String[] { "a", "b", "c" });
		DataView<String> view = new DataView<String>("view", new ListDataProvider<>(list)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<String> item) {
				item.add(new Label("label", item.getModel()));
				item.add(new AjaxEventBehavior("click") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onEvent(AjaxRequestTarget target) {
						feedback.info("Item clicked: " + item.getModelObject());
						target.add(feedback);
					}
				});
			}
		};
		add(feedback.setOutputMarkupId(true), view);
		add(wbAction);
	}

	private static StringBuilder getNamedFunctionStr(String name, AbstractDefaultAjaxBehavior b, CallbackParameter... extraParameters) {
		StringBuilder sb = new StringBuilder();
		sb.append("function ").append(name).append("(");
		boolean first = true;
		for (CallbackParameter curExtraParameter : extraParameters) {
			if (curExtraParameter.getFunctionParameterName() != null) {
				if (first) {
					first = false;
				} else {
					sb.append(',');
				}
				sb.append(curExtraParameter.getFunctionParameterName());
			}
		}
		sb.append(") {\n");
		sb.append(b.getCallbackFunctionBody(extraParameters));
		sb.append("}\n");
		return sb;
	}

	public static JavaScriptHeaderItem getNamedFunction(String name, AbstractDefaultAjaxBehavior b, CallbackParameter... extraParameters) {
		String uid = UUID.randomUUID().toString();
		return JavaScriptHeaderItem.forScript(getNamedFunctionStr(name, b, extraParameters), String.format("%s-%s", name, uid));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(new PriorityHeaderItem(getNamedFunction("wbAction", wbAction, explicit("action"), explicit("obj"))));
	}
}
