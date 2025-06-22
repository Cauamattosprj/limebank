package com.cauamattosprj.limebank.domains.user.DAO;

import com.cauamattosprj.limebank.domains.user.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user) {
        String sql = "INSERT INTO users (id, email, password, role_id) VALUES (?, ?, ?, ?)";
        int roleId = getUserRoleId();
        UUID userId = UUID.randomUUID();
        user.setId(userId);

        jdbcTemplate.update(sql, userId, user.getEmail(), user.getPassword(), roleId);

        return user;
    }


    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND is_deleted = false";
        List<User> result = jdbcTemplate.query(sql, new UserRowMapper(), username);
        return result.getFirst();
    }

    public int getUserRoleId() {
        String sql = "SELECT id FROM roles WHERE role = 'USER'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }


    private static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .email(rs.getString("email"))
                    .role(rs.getString("role"))
                    .created_at(rs.getTimestamp("created_at").toInstant())
                    .updated_at(rs.getTimestamp("updated_at").toInstant())
                    .is_deleted(rs.getBoolean("is_deleted"))
                    .build();

        }
    }
}
