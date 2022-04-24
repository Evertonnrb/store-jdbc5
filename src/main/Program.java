package main;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
    public static void main(String[] args) {
        System.out.println(DB.getConnection());
        Seller s = new Seller();
        Department department = new Department(1,"Testx'");
        SellerDao sellerDao = DaoFactory.createSellerDao();
        

    }
}
