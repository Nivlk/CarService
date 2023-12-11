package com.tac.car.car;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;


@Service
@RequiredArgsConstructor
public class reparationService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    protected final Log logger = LogFactory.getLog(this.getClass());
    public void insertRep(RepaRequestDTO repaRequestDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        final StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO reparations (category, subs, fechain, fechasal, status, priority, comments, id_auto) VALUES (?, ?, ?, ?, ?,?,?,?)");
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, repaRequestDTO.getCategory());
            ps.setString(2, repaRequestDTO.getSubs());
            ps.setDate(3, new java.sql.Date(repaRequestDTO.getFechain().getTime()));  // Convert to java.sql.Date
            ps.setDate(4, new java.sql.Date(repaRequestDTO.getFechasal().getTime()));  // Convert to java.sql.Date
            ps.setString(5, repaRequestDTO.getStatus());
            ps.setString(6, repaRequestDTO.getPriorioty());
            ps.setString(7, repaRequestDTO.getComments());
            ps.setLong(8, repaRequestDTO.getId_auto());
            return ps;
        }, keyHolder);

        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        if (keyList != null && !keyList.isEmpty()) {
            Integer autoIdInteger = (Integer) keyList.get(0).get("id");
            Long autoId = autoIdInteger != null ? autoIdInteger.longValue() : null;

            if (autoId != null) {
                for (String imageUrl : repaRequestDTO.getImages()) {
                    jdbcTemplate.update("INSERT INTO media_rep (id_repa, url) VALUES (?, ?)", autoId, imageUrl);
                }
            }
        }
    }

    public List<Map<String, Object>> getRepDataByCarId(long user_id) {
        String sql = "SELECT\n" +
                "    c.*,\n" +
                "    STRING_AGG(CAST(m.id AS VARCHAR), ',') as media_ids,\n" +
                "    STRING_AGG(m.url, ',') as image_urls\n" +
                "FROM\n" +
                "    reparations c\n" +
                "LEFT JOIN\n" +
                "    media_rep m ON m.id_repa = c.id\n" +
                "WHERE\n" +
                "    id_auto = ?\n" +
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


}
