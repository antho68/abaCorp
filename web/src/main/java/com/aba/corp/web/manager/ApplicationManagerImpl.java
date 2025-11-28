package com.aba.corp.web.manager;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;


/**
 * Common manager for the application
 *
 * @author mala
 */
@Named
@ApplicationScoped
public class ApplicationManagerImpl implements Serializable, ApplicationManager
{
}
