package com.cyberguard.controller;

import com.cyberguard.model.CaseStatus;
import com.cyberguard.util.IncidentFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.List;

/** Publishes shared lists to every JSP. */
@WebListener
public class AppInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        ServletContext ctx = e.getServletContext();
        ctx.setAttribute("statuses", CaseStatus.values());
        ctx.setAttribute("incidentTypes", IncidentFactory.TYPES);
        ctx.setAttribute("priorities", List.of("Low", "Medium", "High"));
    }
}
