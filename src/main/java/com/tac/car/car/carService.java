package com.tac.car.car;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Service
@RequiredArgsConstructor
public class carService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    protected final Log logger = LogFactory.getLog(this.getClass());
    public void insertCar(AutoRequestDTO autoRequestDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        final StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO car (user_id, registration, model, year, color,status) VALUES (?, ?, ?, ?, ?,?)");
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, autoRequestDTO.getUser_id());
            ps.setString(2, autoRequestDTO.getRegistration());
            ps.setString(3, autoRequestDTO.getModel());
            ps.setInt(4, autoRequestDTO.getYear());
            ps.setString(5, autoRequestDTO.getColor());
            ps.setString(6, autoRequestDTO.getStatus());
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
            }
        }
    }

    public List<Map<String, Object>> getCarDataByUserId(long user_id) {
        String sql = "SELECT\n" +
                "    c.*,\n" +
                "    STRING_AGG(CAST(m.id AS VARCHAR), ',') as media_ids,\n" +
                "    STRING_AGG(m.url, ',') as image_urls\n" +
                "FROM\n" +
                "    car c\n" +
                "LEFT JOIN\n" +
                "    media m ON m.id_auto = c.id\n" +
                "WHERE\n" +
                "    user_id = ?\n" +
                "GROUP BY\n" +
                "    c.id;\n";

        try {
            List<Map<String, Object>> results = this.jdbcTemplate.queryForList(sql, user_id);

            for (Map<String, Object> result : results) {
                String mediaIds = (String) result.get("media_ids");
                String imageUrls = (String) result.get("image_urls");
                List<Map<String, String>> mediaDataList = new ArrayList<>();
                List<String> mediaIdsList = Arrays.asList(mediaIds.split(","));
                List<String> imageUrlsList = Arrays.asList(imageUrls.split(","));
                for (int i = 0; i < mediaIdsList.size(); i++) {
                    Map<String, String> mediaData = new HashMap<>();
                    mediaData.put("id", mediaIdsList.get(i));
                    mediaData.put("url", imageUrlsList.get(i));
                    mediaDataList.add(mediaData);
                }
                result.put("media_data", mediaDataList);
                result.remove("media_ids");
                result.remove("image_urls");
            }

            return results;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public void updateCar(AutoRequestDTO request) {
        // Verifica si el id es válido
        if (request.getId() <= 0) {
            throw new IllegalArgumentException("El ID del automóvil no es válido.");
        }

        final StringBuilder updateCarSql = new StringBuilder();
        updateCarSql.append("UPDATE car SET " +
                "user_id=?, " +
                "registration=?, " +
                "model=?, " +
                "year=?, " +
                "color=?, " +
                "status=? " +
                "WHERE id=?;");

        logger.debug("Executing:" + updateCarSql + " ; param: " + request);

        jdbcTemplate.update(updateCarSql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, request.getUser_id());
                ps.setString(2, request.getRegistration());
                ps.setString(3, request.getModel());
                ps.setInt(4, request.getYear());
                ps.setString(5, request.getColor());
                ps.setString(6, request.getStatus());
                ps.setLong(7, request.getId());
            }
        });

        // Actualiza las URLs de las imágenes si la lista no es nula o vacía
        List<String> images = request.getImages();
        if (images != null && !images.isEmpty()) {
            for (String imageUrl : images) {
                // Suponiendo que hay un campo id_media en la tabla media
                jdbcTemplate.update("UPDATE media SET url = ? WHERE id_auto = ? AND url = ?", imageUrl, request.getId(), imageUrl);
            }
        }
    }


}
