package jdbc;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myusers (id, firstName,lastName,age) VALUES (?, ?, ?, ?)";
    private static final String updateUserSQL = "UPDATE myusers SET firstName=?, lastName=?, age=? WHERE id=?";
    private static final String deleteUser = "DELETE FROM myusers WHERE id=?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id=?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstName=?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser() throws SQLException {
        User newUser = User.builder().id(1L).firstName("John").lastName("Doe").age(30).build();
        try(Connection connection = CustomDataSource.getInstance().getConnection()){
            ps = connection.prepareStatement(createUserSQL);
            ps.setLong(1, newUser.getId());
            ps.setString(2,newUser.getFirstName());
            ps.setString(3,newUser.getLastName());
            ps.setInt(4,newUser.getAge());
            ps.executeUpdate();
            return newUser.getId();
        }
    }

    public User findUserById(Long userId) throws SQLException {
        try(Connection connection = CustomDataSource.getInstance().getConnection()){
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return User.builder().id(rs.getLong(1)).firstName(rs.getString(2)).lastName(rs.getString(3)).age(rs.getInt(4)).build();
        }
    }

    public User findUserByName(String userName) throws SQLException {
        try(Connection connection = CustomDataSource.getInstance().getConnection()){
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return User.builder().id(rs.getLong(1)).firstName(rs.getString(2)).lastName(rs.getString(3)).age(rs.getInt(4)).build();
        }
    }

    public List<User> findAllUser() throws SQLException {
        try(Connection connection = CustomDataSource.getInstance().getConnection()){
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(findAllUserSQL);
            List<User> users = new ArrayList<>();
            while(rs.next()){
                User user = User.builder().id(rs.getLong(1)).firstName(rs.getString(2)).lastName(rs.getString(3)).age(rs.getInt(4)).build();
                users.add(user);
            }
            return users;
        }
    }

    public User updateUser() throws SQLException {
        User newUser = User.builder().id(1L).firstName("Mike").lastName("Ray").age(26).build();
        try(Connection connection = CustomDataSource.getInstance().getConnection()){
            ps = connection.prepareStatement(updateUserSQL);
            ps.setString(1,newUser.getFirstName());
            ps.setString(2,newUser.getLastName());
            ps.setInt(3,newUser.getAge());
            ps.setLong(4,newUser.getId());
            ps.executeUpdate();
            return newUser;
        }
    }

    private void deleteUser(Long userId) throws SQLException {
        try(Connection connection = CustomDataSource.getInstance().getConnection()){
            ps = connection.prepareStatement(deleteUser);
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }
}
