package com.example.sqlgateway;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.Statement;

@WebListener
public class AppInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection conn = DB.getConnection(); Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS patients (
                  id IDENTITY PRIMARY KEY,
                  name VARCHAR(100) NOT NULL,
                  age INT,
                  diagnosis VARCHAR(200)
                );
            """);
            st.execute("""
                CREATE TABLE IF NOT EXISTS visits (
                  id IDENTITY PRIMARY KEY,
                  patient_id BIGINT,
                  visit_date DATE,
                  note VARCHAR(255)
                );
            """);
            st.execute("""
                MERGE INTO patients (id, name, age, diagnosis) KEY(id) VALUES
                (1,'Alice',30,'Sprain'),
                (2,'Bob',44,'ACL tear'),
                (3,'Cathy',19,'Fracture');
            """);
            st.execute("""
                MERGE INTO visits (id, patient_id, visit_date, note) KEY(id) VALUES
                (1,1,DATE '2025-09-01','Follow-up OK'),
                (2,2,DATE '2025-09-10','Physio needed'),
                (3,3,DATE '2025-09-15','Cast applied');
            """);
            System.out.println("H2 database initialized.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
