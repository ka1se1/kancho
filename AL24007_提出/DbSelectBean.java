import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

public class DbSelectBean {

    // Each element: { id, name, message, writtendate_string }
    private ArrayList<String[]> messages = new ArrayList<>();

    public void execute() {
        String url  = "jdbc:postgresql://172.21.87.24:5434/test1";
        String user = "shibaura";
        String pass = "toyosu";

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT ID, NAME, MESSAGE, WRITTENDATE FROM T_LOG ORDER BY WRITTENDATE DESC LIMIT 20";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] row = new String[4];
                row[0] = rs.getString("ID");
                row[1] = rs.getString("NAME");
                row[2] = rs.getString("MESSAGE");
                // Timestamp.toString() format: "yyyy-MM-dd HH:mm:ss.n"
                row[3] = rs.getTimestamp("WRITTENDATE").toString();
                messages.add(row);
            }

            rs.close();
            pstmt.close();
            conn.close();

            // Reverse to display oldest first (ascending order)
            Collections.reverse(messages);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> getMessages() {
        return messages;
    }
}
