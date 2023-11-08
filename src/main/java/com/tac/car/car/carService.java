package com.tac.car.car;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class carService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    protected final Log logger = LogFactory.getLog(this.getClass());
    public void insertCar(AutoRequestDTO autoRequestDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        final StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO car (user_id, registration, model, year, color) VALUES (?, ?, ?, ?, ?)");
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, autoRequestDTO.getUser_id());
            ps.setString(2, autoRequestDTO.getRegistration());
            ps.setString(3, autoRequestDTO.getModel());
            ps.setInt(4, autoRequestDTO.getYear());
            ps.setString(5, autoRequestDTO.getColor());
            return ps;
        }, keyHolder);

        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        if (keyList != null && !keyList.isEmpty()) {
            Integer autoIdInteger = (Integer) keyList.get(0).get("id");
            Long autoId = autoIdInteger != null ? autoIdInteger.longValue() : null;

            if (autoId != null) {
                for (String imageUrl : autoRequestDTO.getImages()) {
                    jdbcTemplate.update("INSERT INTO media (id_auto, url) VALUES (?, ?)", autoId, imageUrl);
                }
            } else {
                // Manejar el caso cuando el ID del auto es nulo
            }
        }
    }



}
