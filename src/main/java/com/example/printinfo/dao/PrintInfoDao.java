package com.example.printinfo.dao;

import com.example.printinfo.model.PrintInfo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PrintInfoDao {
  private final JdbcTemplate jdbc;
  public PrintInfoDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  public Map<String, Object> findPage(String q, String sort, String order, int page, int size) {
    String base = "FROM 인쇄정보 WHERE 1=1";
    String where = "";
    Object[] params;

    if (StringUtils.hasText(q)) {
      // LIKE search on selective text columns
      where = " AND (인쇄방법 LIKE ? OR 품목명 LIKE ? OR 인쇄담당팀 LIKE ? OR 쇼핑백색상 LIKE ? OR 사이즈 LIKE ?)";
      String like = "%" + q + "%";
      params = new Object[]{ like, like, like, like, like };
    } else {
      params = new Object[]{};
    }

    int total = jdbc.queryForObject("SELECT COUNT(*) " + base + where, params, Integer.class);

    String orderBy;
    switch (sort == null ? "printId" : sort) {
      case "printMethod": orderBy = "인쇄방법"; break;
      case "itemName": orderBy = "품목명"; break;
      case "bagColor": orderBy = "쇼핑백색상"; break;
      case "sizeText": orderBy = "사이즈"; break;
      case "quantity": orderBy = "제작장수"; break;
      case "team": orderBy = "인쇄담당팀"; break;
      case "sides": orderBy = "인쇄면"; break;
      default: orderBy = "인쇄ID";
    }
    String dir = ("desc".equalsIgnoreCase(order)) ? "DESC" : "ASC";

    int offset = page * size;

    String sql = "SELECT 인쇄ID AS printId, 인쇄방법 AS printMethod, 품목명 AS itemName, " +
                 "쇼핑백색상 AS bagColor, 사이즈 AS sizeText, 제작장수 AS quantity, " +
                 "인쇄담당팀 AS team, 인쇄면 AS sides " + base + where +
                 " ORDER BY " + orderBy + " " + dir +
                 " LIMIT " + size + " OFFSET " + offset;

    List<PrintInfo> content = jdbc.query(sql, params, new BeanPropertyRowMapper<>(PrintInfo.class));

    Map<String, Object> result = new HashMap<>();
    result.put("content", content);
    result.put("page", page);
    result.put("size", size);
    result.put("totalElements", total);
    result.put("totalPages", (int) Math.ceil(total / (double) size));
    return result;
  }
}
