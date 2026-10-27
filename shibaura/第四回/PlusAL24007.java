import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PlusAL24007 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        // パラメータ取得
        String num1Str = request.getParameter("num1");
        String num2Str = request.getParameter("num2");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("  <meta charset=\"UTF-8\">");
        out.println("  <title>Calculation</title>");
        out.println("</head>");
        out.println("<body>");

        try {
            int num1 = Integer.parseInt(num1Str);
            int num2 = Integer.parseInt(num2Str);
            int result = num1 + num2;
            out.println("  <p>計算結果は、" + result + "です。</p>");
        } catch (NumberFormatException e) {
            out.println("  <p>エラー：整数を入力してください。</p>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}
