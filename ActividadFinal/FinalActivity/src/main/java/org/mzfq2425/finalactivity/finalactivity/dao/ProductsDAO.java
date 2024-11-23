package org.mzfq2425.finalactivity.finalactivity.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.mzfq2425.finalactivity.finalactivity.model.Products;
import org.mzfq2425.finalactivity.finalactivity.model.Sellers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAO {
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

    //function to fetch all products from seller id
    public static List<Products> getProductsFromSeller(int id){
        List<Products> res = new ArrayList<>();
        try {
            openCon();
            String query = "SELECT p FROM Products p INNER JOIN Seller_products s ON s.product = p WHERE s.seller.sellerId = :seller_id";

            res = session.createQuery(query, Products.class)
                    .setParameter("seller_id", id)
                    .getResultList();
        }catch(Exception e){
            System.out.println(e.getMessage());
        } finally{
            closeCon();
        }

        return res;
    }

    //function to check if discount is active using the PostgreSQL function
    public static boolean isDiscountActive(int productId, LocalDate offerStartDate, LocalDate offerEndDate) {
        boolean isActive = false;
        try {
            openCon();

            String sql = "SELECT is_discount_active(:productId, :offerStartDate, :offerEndDate)";

            isActive = (Boolean) session.createNativeQuery(sql)
                    .setParameter("productId", productId)
                    .setParameter("offerStartDate", java.sql.Date.valueOf(offerStartDate))
                    .setParameter("offerEndDate", java.sql.Date.valueOf(offerEndDate))
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCon();
        }
        return isActive;
    }
}
