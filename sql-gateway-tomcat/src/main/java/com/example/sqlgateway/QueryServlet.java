package com.example.sqlgateway;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

public class QueryServlet extends HttpServlet {

    static class ReqBody { String sql; }
    static class ResOk {
        boolean ok = true;
        String type;
        List<Map<String, Object>> rows;
        Integer changes;
        Long lastID;
    }
    static class ResErr {
        boolean ok = false;
        String error;
        ResErr(String e) { error = e; }
    }

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try (BufferedReader reader = req.getReader(); PrintWriter out = resp.getWriter()) {
            ReqBody body = gson.fromJson(reader, ReqBody.class);
            if (body == null || body.sql == null || body.sql.trim().isEmpty()) {
                out.print(gson.toJson(new ResErr("Empty SQL.")));
                return;
            }

            String sql = body.sql.trim();

            // Basic guard: allow only single statement
            String[] parts = Arrays.stream(sql.split(";")).map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
            if (parts.length != 1) {
                out.print(gson.toJson(new ResErr("Only one statement per request is allowed.")));
                return;
            }
            sql = parts[0];

            try (Connection conn = DB.getConnection()) {
                if (sql.toLowerCase().startsWith("select")) {
                    try (PreparedStatement ps = conn.prepareStatement(sql);
                         ResultSet rs = ps.executeQuery()) {
                        List<Map<String, Object>> list = new ArrayList<>();
                        ResultSetMetaData md = rs.getMetaData();
                        int n = md.getColumnCount();
                        while (rs.next()) {
                            Map<String, Object> row = new LinkedHashMap<>();
                            for (int i = 1; i <= n; i++) {
                                Object val = rs.getObject(i);
                                row.put(md.getColumnLabel(i), val);
                            }
                            list.add(row);
                        }
                        ResOk ok = new ResOk();
                        ok.type = "rows";
                        ok.rows = list;
                        out.print(gson.toJson(ok));
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        int changed = ps.executeUpdate();
                        Long lastId = null;
                        try (ResultSet gk = ps.getGeneratedKeys()) {
                            if (gk.next()) lastId = gk.getLong(1);
                        }
                        ResOk ok = new ResOk();
                        ok.type = "result";
                        ok.changes = changed;
                        ok.lastID = lastId;
                        out.print(gson.toJson(ok));
                    }
                }
            } catch (SQLException e) {
                out.print(gson.toJson(new ResErr(e.getMessage())));
            }
        }
    }
}
