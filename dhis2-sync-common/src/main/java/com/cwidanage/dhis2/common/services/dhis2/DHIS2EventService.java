package com.cwidanage.dhis2.common.services.dhis2;

import com.cwidanage.dhis2.common.models.Event;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

@Component
public class DHIS2EventService {

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    public DHIS2EventService() {
        //this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Transactional
    public Event loadEventAttributes(String eventId) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Session session = sessionFactory.openSession();
        Event event = session.load(Event.class, eventId);
        Hibernate.initialize(event.getDataValues());
        Hibernate.initialize(event.getCoordinate());
        session.close();
        return event;
    }
}
