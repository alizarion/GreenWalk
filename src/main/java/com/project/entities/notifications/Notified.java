package com.project.entities.notifications;

import com.project.entities.PrivateMessage;

import javax.persistence.PrePersist;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 24/09/13
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */
public interface Notified  {

   public List<Notification> pushNotifications();
}
