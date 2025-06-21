package com.tp.APP1.dao;

import com.tp.APP1.models.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    boolean add(User user) throws SQLException;

    boolean update(User user) throws SQLException;
    

    boolean delete(int id) throws SQLException;
    

    User getById(int id) throws SQLException;
    

    User getByUsername(String username) throws SQLException;
    

    List<User> getAll() throws SQLException;

    User authenticate(String username, String password) throws SQLException;
    

    boolean usernameExists(String username) throws SQLException;
}