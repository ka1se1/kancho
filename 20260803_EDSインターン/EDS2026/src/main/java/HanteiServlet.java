
import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Hantei")
public class HanteiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// -------- 文字コード設定 --------
		request.setCharacterEncoding("UTF-8");

		// -------- 占い結果生成  --------

		// -------- リクエストに占い結果を追加（setAttribute）--------
		request.setAttribute("result", "こんばんは金曜日");

		// -------- JSPへ転送（forward）--------
		RequestDispatcher rd = request.getRequestDispatcher("/UranaiResult.jsp");
		rd.forward(request, response);

	}

}
