package com.studybuddy.user_identity_service.repository;

import com.studybuddy.user_identity_service.entity.Enums.Role;
import com.studybuddy.user_identity_service.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository {

    private final JdbcTemplate jdbc;

    /* ---------- ROW MAPPER ---------- */

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            User.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .build();

    /* ---------- EXISTS BY EMAIL ---------- */

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    /* ---------- SAVE USER ---------- */

    @Transactional
    public User save(User user) {

        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

        String userSql = """
            INSERT INTO users (id, first_name, last_name, email, password)
            VALUES (?, ?, ?, ?, ?)
        """;

        jdbc.update(
                userSql,
                user.getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword()
        );

        saveRoles(user.getId(), user.getRoles());

        return user;
    }

    /* ---------- SAVE ROLES ---------- */

    private void saveRoles(UUID userId, List<Role> roles) {

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
        }

        String roleSql = """
            INSERT INTO user_roles (user_id, role_name)
            VALUES (?, ?)
        """;

        jdbc.batchUpdate(
                roleSql,
                roles,
                roles.size(),
                (ps, role) -> {
                    ps.setString(1, userId.toString());
                    ps.setString(2, role.name());
                }
        );
    }

    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return Optional.ofNullable(jdbc.queryForObject(sql, USER_ROW_MAPPER, id));
    }

    /* ---------- FIND BY EMAIL ---------- */

    public Optional<User> findByEmail(String email) {

        String userSql = """
            SELECT id, first_name, last_name, email, password
            FROM users
            WHERE email = ?
        """;

        String roleSql = """
            SELECT role_name
            FROM user_roles
            WHERE user_id = ?
        """;

        try {
            User user = jdbc.queryForObject(userSql, USER_ROW_MAPPER, email);

            List<Role> roles = jdbc.query(
                    roleSql,
                    (rs, rowNum) -> Role.valueOf(rs.getString("role_name")),
                    user.getId().toString()
            );

            user.setRoles(roles);
            return Optional.of(user);

        } catch (EmptyResultDataAccessException ex) {
            log.debug("No user found with email: {}", email);
            return Optional.empty();
        }
    }
}
