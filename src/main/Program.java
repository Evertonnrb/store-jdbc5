package main;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.dao.impl.DepartmentDaoImplJDBC;
import model.entities.Department;
import model.entities.Seller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) throws ParseException {
        /*SellerDao sellerDao = DaoFactory.createSellerDao();
          System.out.println(sellerDao.findById(3));
        Department department = new Department(2,null);
        System.out.println("Find all seller by deparment");
        List<Seller> allSellerByDepartment = sellerDao.findByDepartment(department);
        for(Seller s : allSellerByDepartment){
            System.out.println(s);
        }
        System.out.println("Find all");
        List<Seller> all = sellerDao.findAll();
        for (Seller s : all) {
            System.out.println(s);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Department department = new Department(1,null);
        Date birthDate = dateFormat.parse("19/11/1985");
        Seller seller = new Seller(null,"Brant Jouli ","brant_clin@gmail.com",birthDate,new BigDecimal("4000.0"),department);
        sellerDao.insert(seller);
        Seller seller1 = sellerDao.findById(9);
        System.out.println(seller1);
        seller1.setName("Martha Wayne");
        seller1.setEmail("martha_wy@gmail.com");
        seller1.setBirthDay(birthDate);
        sellerDao.update(seller1);
        sellerDao.delete(11); */
        Department dp = new Department(null,"Development");
        DepartmentDao dpDao = DaoFactory.createDepartmentDao();
        //dpDao.insert(dp);
        //System.out.println(dpDao.findById(6));
        //Department dpUPdate = dpDao.findById(6);
        //dpUPdate.setName("Dev");
        //dpDao.update(dpUPdate);
        //dpDao.delete(6);
        for(Department d : dpDao.findAll()){
            System.out.println(d);
        }
    }
}
