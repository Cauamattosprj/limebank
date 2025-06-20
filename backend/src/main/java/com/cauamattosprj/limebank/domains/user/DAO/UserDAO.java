package com.cauamattosprj.limebank.domains.user.DAO;

import com.cauamattosprj.limebank.domains.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> result = jdbcTemplate.query(sql, new UserRowMapper(), username);
        return result.getFirst();
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
