package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        // persistence unit name: persistence.xml 의 persistence-unit name
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-basic");

        EntityManager em = emf.createEntityManager();

        em.close();

        emf.close();
    }
}
