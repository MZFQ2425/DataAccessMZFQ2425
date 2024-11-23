package org.mzfq2425.finalactivity.finalactivity.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.mzfq2425.finalactivity.finalactivity.model.Sellers;

public class SellersDAO {

    static SessionFactory sessionFactory = null;
    static Session session = null;

    //function to open connection
    public static void openCon(){
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .buildSessionFactory();

            session = sessionFactory.openSession();

            if (session != null) {
                System.out.println("Session successfully opened!");
            } else {
                System.out.println("Error opening session!");
            }
        } catch (Exception e) {
            closeCon();
            System.err.println("Error initializing Hibernate: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    //function to close connection
    public static void closeCon(){
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //function to check if seller info from login is correct
    public Sellers getSellerFromUserAndPass(String user, String password){
        Sellers userRes = null;

        try {
            openCon();
            String query = "SELECT s FROM Sellers s WHERE s.cif = :cif AND s.password = :password";

            userRes = session.createQuery(query, Sellers.class)
                    .setParameter("cif", user)
                    .setParameter("password", password)
                    .uniqueResult();
        }catch(Exception e){
            System.out.println(e.getMessage());
        } finally{
            closeCon();
        }

        return userRes;
    }

    //function to update the seller with the values entered
    public static Sellers updateSeller(Sellers seller) {
        try {
            openCon();
            session.beginTransaction();
            session.update(seller);
            session.getTransaction().commit();
            return seller;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            return null;
        } finally {
            closeCon();
        }
    }
}
