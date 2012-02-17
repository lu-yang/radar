package com.solidstategroup.radar.web.pages.admin;

import com.solidstategroup.radar.model.user.User;
import com.solidstategroup.radar.web.pages.BasePage;
import com.solidstategroup.radar.web.panels.navigation.AdminNavigationPanel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.Page;

@AuthorizeInstantiation({User.ROLE_ADMIN})
public class AdminsBasePage extends BasePage {
    public AdminsBasePage() {
        super();
    }

    protected void addNavigation(Class<? extends Page> pageClass) {
        add(new AdminNavigationPanel());
    }
}
