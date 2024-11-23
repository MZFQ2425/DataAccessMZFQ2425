package org.mzfq2425.finalactivity.finalactivity.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.mzfq2425.finalactivity.finalactivity.model.Seller_products;

public class Seller_ProductsDAO {
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

    //function to get seller_product from seller id and product id
    public static Seller_products getFromSellerAndProduct(int sellerId, int productId){
        Seller_products sellerProducts = new Seller_products();
        try {
            openCon();
            session.beginTransaction();

            String hql = "SELECT sp FROM Seller_products sp WHERE sp.product.productId = :productId AND sp.seller.sellerId = :sellerId";
            sellerProducts = (Seller_products) session.createQuery(hql)
                    .setParameter("productId", productId)
                    .setParameter("sellerId", sellerId)
                    .uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCon();
        }

        return sellerProducts;
    }

    //function to update the seller with the values entered
    public static Seller_products updateSeller(Seller_products seller_products) {
        try {
            openCon();
            session.beginTransaction();
            session.update(seller_products);
            session.getTransaction().commit();
            return seller_products;
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
