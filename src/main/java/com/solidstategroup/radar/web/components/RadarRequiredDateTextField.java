package com.solidstategroup.radar.web.components;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.IModel;

import java.util.Date;
import java.util.List;

public class RadarRequiredDateTextField extends DateTextField{
    public RadarRequiredDateTextField(String id, String datePattern, Form form, List<Component> componentsToUpdate) {
        super(id, datePattern);
        init(form, componentsToUpdate);
    }

    public RadarRequiredDateTextField(String id, IModel<Date> model, String datePattern, Form form, List<Component> componentsToUpdate) {
        super(id, model, datePattern);
        init(form, componentsToUpdate);
    }

    private void init(Form form, List<Component> componentsToUpdate) {
        add(new RadarDatePicker());
        final ComponentFeedbackPanel feedbackPanel = new ComponentFeedbackPanel(getId() + "Feedback", this) {

            @Override
            public boolean isVisible() {
                List<FeedbackMessage> feedbackMessages = getCurrentMessages();
                for(FeedbackMessage feedbackMessage : feedbackMessages) {
                    if (feedbackMessage.getMessage().toString().contains("required")) {
                        return false;
                    }
                }
                return super.isVisible();
            }
        };
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.setOutputMarkupPlaceholderTag(true);
        form.add(feedbackPanel);
        componentsToUpdate.add(feedbackPanel);

        setRequired(true);
        RadarFormComponentFeedbackIndicator radarFormComponentFeedbackIndicator =
                new RadarFormComponentFeedbackIndicator(getId() + "FeedbackIndicator", this){
                    @Override
                    public boolean isVisible() {
                        if(feedbackPanel.isVisible()) {
                          return false;
                        }
                        return super.isVisible();    //To change body of overridden methods use File | Settings | File Templates.
                    }
                };
        form.add(radarFormComponentFeedbackIndicator);
        radarFormComponentFeedbackIndicator.setOutputMarkupId(true);
        radarFormComponentFeedbackIndicator.setOutputMarkupPlaceholderTag(true);
        componentsToUpdate.add(radarFormComponentFeedbackIndicator);
    }


}