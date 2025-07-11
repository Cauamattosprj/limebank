package com.cauamattosprj.limebank.domains.user.DAO;

import com.cauamattosprj.limebank.domains.user.models.User;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
public class  UserDAO {
    private final JdbcTemplate jdbcTemplate;


    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user) {
        String sql = "INSERT INTO users (id, email, password, role_id) VALUES (?, ?, ?, ?)";
        int roleId = getDefaultRoleId();
        UUID userId = UUID.randomUUID();
        user.setId(userId);

        System.out.println("userId = " + userId);
        System.out.println("password = " + user.getPassword());
        System.out.println("email = " + user.getEmail());
        System.out.println("roleId = " + user.getRole());

        jdbcTemplate.update(sql, userId, user.getEmail(), user.getPassword(), roleId);

        return user;
    }

    public User getUserByEmail(String email) {
        System.out.println("UserDAO.findByEmail");
        String sql = """
                SELECT u.*, r.role
                FROM users u
                JOIN roles r ON u.role_id = r.id
                WHERE u.email = ? AND u.is_deleted = false
                """;
        List<User> result = jdbcTemplate.query(sql, new UserRowMapper(), email);
        System.out.println("findByEmail chamado. Result.getFirst: " + result.getFirst());
        return result.getFirst();
    }

    public int getDefaultRoleId() {
        String sql = "SELECT id FROM roles WHERE role = 'USER'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public UUID getUserIdByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, UUID.class, email);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                Timestamp createdAtTs = rs.getTimestamp("created_at");
                Timestamp updatedAtTs = rs.getTimestamp("updated_at");
                Timestamp deletedAtTs = rs.getTimestamp("deleted_at");

                return User.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .email(rs.getString("email"))
                        .password(rs.getString("password"))
                        .role(rs.getString("role_id"))
                        .created_at(createdAtTs != null ? createdAtTs.toInstant() : null)
                        .updated_at(updatedAtTs != null ? updatedAtTs.toInstant() : null)
                        .deleted_at(deletedAtTs != null ? deletedAtTs.toInstant() : null)
                        .is_deleted(rs.getBoolean("is_deleted"))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                throw new SQLException("Erro ao mapear User", e);
            }
        }
    }

}
