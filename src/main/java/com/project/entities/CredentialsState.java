/*
 * Copyright NewWorks 2009.
 * Created on 28 janv. 2009 at 11:03:22.
 */
package com.project.entities;

import java.io.Serializable;

/**
 * @author selim.bensenouci
 */
public enum CredentialsState implements Serializable {
    /** Pending credentials, need activation. Person can't log in. */
    P,
    /** Active credentials. Person can log in. */
    A,
    /** Disabled credentials. Person can't log in. */
    D;

}
