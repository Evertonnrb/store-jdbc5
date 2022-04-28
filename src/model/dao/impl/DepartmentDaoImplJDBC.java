package model.dao.impl;

import db.exception.DBException;
import db.exception.DBIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoImplJDBC implements DepartmentDao {

    private Connection conn;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    public DepartmentDaoImplJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        try {
            pst = conn.prepareStatement("insert into department (Name) values (?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, obj.getName());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            } else {
                throw new DBException("Unexpected error! No rows affected.");
            }
        } catch (SQLException e) {
            throw new DBIntegrityException(e.getMessage());
        }
    }

    @Override
    public void update(Department obj) {
        try {
            pst = conn.prepareStatement("update department set Name=? where Id = ?");
            pst.setString(1, obj.getName());
            pst.setLong(2, obj.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DBIntegrityException(e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            pst = conn.prepareStatement("delete from department where id=?");
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DBIntegrityException(e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        try {
            pst = conn.prepareStatement("select Id,Name from department where Id=?");
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                Department department = createDepartment(rs);
                return department;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DBIntegrityException(e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        try {
            pst = conn.prepareStatement("select * from department");
            rs = pst.executeQuery();
            List<Department> list = new ArrayList<>();
            Department department = null;
            while (rs.next()) {
                department = createDepartment(rs);
                list.add(department);
            }
            return list;
        } catch (SQLException e) {
            throw new DBIntegrityException(e.getMessage());
        }
    }

    public Department createDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("Id"));
        department.setName(rs.getString("Name"));
        return department;
    }

}
