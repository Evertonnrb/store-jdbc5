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
import java.util.List;

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
                Seller seller = createSeller(rs,department);
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
        return null;
    }

    private Seller createSeller(ResultSet rs, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDay(rs.getDate("BirthDate"));
        department = createDepartment(rs);
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
