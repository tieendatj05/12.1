# SQL Gateway (Java + Tomcat 10)

Web app chạy trên Tomcat 10 (Jakarta EE) với giao diện giống screenshot. Database dùng **H2 embedded**.

## Cách build & deploy với Maven
1. Cài JDK 17+, Maven, Tomcat 10.1+.
2. Build:
   ```bash
   mvn clean package
   ```
   File WAR sẽ là: `target/sql-gateway.war`

3. Deploy:
   - Copy `target/sql-gateway.war` vào `TOMCAT_HOME/webapps/`
   - Start Tomcat, mở: `http://localhost:8080/sql-gateway/`

## NetBeans
- File → Open Project → chọn thư mục `sql-gateway-tomcat` (Maven).  
- Right-click project → **Run** (NetBeans sẽ deploy vào Tomcat server bạn cấu hình).
- Nếu bạn đã add Tomcat 10 trong Services → Servers, chọn đúng server đó.

## Notes
- API: `POST /sql-gateway/api/query` body: `{"sql": "SELECT * FROM patients"}`
- Chỉ cho phép **1 câu lệnh**/request. Lệnh `SELECT` trả về bảng, `INSERT/UPDATE/DELETE` trả về số rows thay đổi.
- Dùng H2 lưu file ở `./data/sqlgateway` (cùng thư mục chạy Tomcat).
- Đây là demo; không nên cho người lạ chạy SQL tùy ý trên server thật.
