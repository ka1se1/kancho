import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class DbInsertBean {

    // ★要変更: 自分の名前に書き換えてください
    private static final String MY_NAME = "卜部颯斗";

    private String message = "";

    public void setMessage(String message) {
        this.message = message;
    }

    public void execute() {
        String url  = "jdbc:postgresql://172.21.87.24:5434/test1";
        String user = "shibaura";
        String pass = "toyosu";

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "INSERT INTO T_LOG (ID, NAME, MESSAGE, WRITTENDATE) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "AL24007");
            pstmt.setString(2, MY_NAME);
            pstmt.setString(3, message);
            // currentTimeMillis() is UTC; +32400000 converts to JST for Docker (UTC) environment
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis() + 32400000L));

            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
