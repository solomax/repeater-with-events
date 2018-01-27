package org.apache.solomax;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.util.time.Duration;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;
	private static final String MAIN_PANEL_ID = "main";
	private final WebMarkupContainer mainContainer = new WebMarkupContainer("main-container");
	private final AbstractAjaxTimerBehavior areaBehavior = new AbstractAjaxTimerBehavior(Duration.ONE_SECOND) {
		private static final long serialVersionUID = 1L;

		@Override
		protected void onTimer(AjaxRequestTarget target) {
			main.updateContents(target);
			stop(target);
		}
	};
	private final MainPanel main = new MainPanel(MAIN_PANEL_ID);
	private final AbstractDefaultAjaxBehavior delayedLoad = new AbstractDefaultAjaxBehavior() {
		private static final long serialVersionUID = 1L;

		@Override
		protected void respond(AjaxRequestTarget target) {
			target.add(
				mainContainer.replace(main)
				.add(areaBehavior));
		}
	};

	public HomePage() {
		super();
		add(mainContainer.add(new EmptyPanel(MAIN_PANEL_ID)).setOutputMarkupId(true));
		add(delayedLoad);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(OnDomReadyHeaderItem.forScript(delayedLoad.getCallbackScript()));
	}
}
