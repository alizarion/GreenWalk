package com.project.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = "GROUP")
public class GroupEvent extends Event {


}
