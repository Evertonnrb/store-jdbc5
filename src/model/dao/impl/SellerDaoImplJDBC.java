package model.dao.impl;

import db.DB;
import db.exception.DBException;
import db.exception.DBIntegrityException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
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
        try {
            pst = connection.prepareStatement("insert into seller(Name,Email,BirthDate," +
                            "BaseSalary,DepartmentId) values (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getEmail());
            pst.setDate(3, new java.sql.Date(obj.getBirthDay().getTime()));
            pst.setBigDecimal(4, obj.getBaseSalary());
            pst.setInt(5, obj.getDepartment().getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            } else {
                throw new DBIntegrityException("Unexpected error! No rows affeceted.");
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void update(Seller obj) {
        try{
            pst = connection.prepareStatement("update seller set Name= ?, Email=?, BirthDate=?," +
                    " BaseSalary=?, DepartmentId=? where Id=?");
            pst.setString(1,obj.getName());
            pst.setString(2,obj.getEmail());
            pst.setDate(3,new java.sql.Date(obj.getBirthDay().getDate()));
            pst.setBigDecimal(4,obj.getBaseSalary());
            pst.setInt(5,obj.getDepartment().getId());
            pst.setInt(6,obj.getId());
            pst.executeUpdate();
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }

    }

    @Override
    public void delete(Integer id) {
        try {
            pst = connection.prepareStatement("delete from seller where Id = ?");
            pst.setInt(1,id);
            pst.executeUpdate();
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }
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
                if (dp == null) {
                    dp = createDepartment(rs);
                    mapDepartment.put(rs.getInt("DepartmentId"), dp);
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
        seller.updateSalary(rs.getBigDecimal("BaseSalary"));
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
