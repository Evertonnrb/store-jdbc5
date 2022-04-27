package model.dao.impl;

import db.DB;
import db.exception.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoImplJDBC implements SellerDao {

    private Connection connection;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    public SellerDaoImplJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {

        try {
            pst = connection.prepareStatement(
                    "select seller.*,department.Name as DepName from seller\n" +
                            "inner join department on seller.DepartmentId = department.Id\n" +
                            "where seller.Id  = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                Department department = createDepartment(rs);
                Seller seller = createSeller(rs, department);
                return seller;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            closeConnections();
        }
    }


    @Override
    public List<Seller> findAll() {
        List<Seller> list = new ArrayList<>();
        try {
            pst = connection.prepareStatement("select * from seller");
            rs = pst.executeQuery();
            while (rs.next()) {
                Department department = creteDepartmentForList(rs);
                Seller s = createSeller(rs, department);
                list.add(s);
            }
            return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            closeConnections();
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = connection.prepareStatement("select seller.*,department.Name as DepName from seller\n" +
                    "inner join department on seller.DepartmentId = departmentId\n" +
                    "where departmentId = ?\n" +
                    "order by name");
            pst.setInt(1, department.getId());
            rs = pst.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> mapDepartment = new HashMap<>();
            while (rs.next()) {
                Department dp = mapDepartment.get(rs.getInt("DepartmentId"));
                if (dp == null){
                    dp = createDepartment(rs);
                    mapDepartment.put(rs.getInt("DepartmentId"),dp);
                }
                Seller seller = createSeller(rs, dp);
                list.add(seller);
            }
            return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    private Department creteDepartmentForList(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("Id"));
        department.setName(rs.getString("Name"));
        return department;
    }

    private Seller createSeller(ResultSet rs, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDay(rs.getDate("BirthDate"));
        //department = createDepartment(rs);
        seller.setDepartment(department);
        return seller;
    }

    private Department createDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("DepName"));
        return department;
    }

    private void closeConnections() {
        DB.closeStatements(pst);
        DB.closeResultSet(rs);
        DB.closeConnection();
    }
}
