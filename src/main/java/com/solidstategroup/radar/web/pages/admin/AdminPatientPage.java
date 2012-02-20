package com.solidstategroup.radar.web.pages.admin;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import com.solidstategroup.radar.service.DemographicsManager;
import com.solidstategroup.radar.service.UserManager;
import com.solidstategroup.radar.model.Demographics;
import com.solidstategroup.radar.model.user.PatientUser;
import com.solidstategroup.radar.util.TripleDes;
import org.apache.wicket.util.string.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class AdminPatientPage extends AdminsBasePage {

    @SpringBean
    private DemographicsManager demographicsManager;
    @SpringBean
    private UserManager userManager;

    private static final String PARAM_ID = "ID";

    public AdminPatientPage(PageParameters parameters) {
        super();

        final Demographics demographics;
        final PatientUser patientUser;

        StringValue idValue = parameters.get(PARAM_ID);
        patientUser = userManager.getPatientUser(idValue.toLong());
        demographics = demographicsManager.getDemographicsByRadarNumber(patientUser.getRadarNumber());

        CompoundPropertyModel<PatientUser> patientUserModel =
                new CompoundPropertyModel<PatientUser>(patientUser);

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        feedback.setOutputMarkupPlaceholderTag(true);
        add(feedback);

        final Form<PatientUser> userForm = new Form<PatientUser>("patientForm", patientUserModel) {
            protected void onSubmit() {
                try {
                    userManager.savePatientUser(getModelObject());
                } catch (Exception e) {
                    error("Could not save patient: " + e.toString());
                }
            }
        };
        add(userForm);

        userForm.add(new Label("radarNo", patientUser.getRadarNumber().toString()));
        userForm.add(new Label("forename", demographics.getForename()));
        userForm.add(new Label("surname", demographics.getSurname()));
        userForm.add(new RequiredTextField("username"));

        // the password is encrypted in the property passwordhash so unenctypt and set the password prop
        // so the model can automatically map it to the textfield
        try {
            patientUser.setPassword(TripleDes.decrypt(patientUser.getPasswordHash()));
        } catch (Exception e) {
            // dunno
        }

        userForm.add(new RequiredTextField("password"));
        userForm.add(new Label("dob", patientUser.getDateOfBirth().toString()));
        userForm.add(new Label("dateRegistered", patientUser.getDateRegistered().toString()));

        userForm.add(new AjaxSubmitLink("update") {
            protected void onSubmit(AjaxRequestTarget ajaxRequestTarget, Form<?> form) {
                setResponsePage(AdminPatientsPage.class);
            }

            protected void onError(AjaxRequestTarget ajaxRequestTarget, Form<?> form) {
                ajaxRequestTarget.add(feedback);
            }
        });

        userForm.add(new AjaxLink("cancel") {
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                setResponsePage(AdminPatientsPage.class);
            }
        });
    }

    public static PageParameters getPageParameters(PatientUser patientUser) {
        return new PageParameters().set(PARAM_ID, patientUser.getId());
    }
}