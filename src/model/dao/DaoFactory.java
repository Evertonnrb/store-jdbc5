package model.dao;

import db.DB;
import model.dao.impl.SellerDaoImplJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao(){
        return new SellerDaoImplJDBC(DB.getConnection());
    }
}
