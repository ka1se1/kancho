
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
@WebServlet("/HanteiAns")
public class HanteiServletAns extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// -------- 文字コード設定 --------
		request.setCharacterEncoding("UTF-8");

		// -------- 占い結果生成  --------
		String numString = request.getParameter("num");
		// 入力値を数値に変換する
		int num = Integer.parseInt(numString);

		// 画面に返却する文字列
		String returnStr = "大凶";

		if (num % 3 == 0) {
			returnStr = "大吉";
		} else if (num % 3 == 1) {
			returnStr = "中吉";
		} else if (num % 3 == 2) {
			returnStr = "小吉";
		}

		// -------- リクエストに占い結果を追加（setAttribute）--------
		request.setAttribute("result", returnStr);

		// -------- JSPへ転送（forward）--------
		RequestDispatcher rd = request.getRequestDispatcher("/UranaiResult.jsp");
		rd.forward(request, response);

	}

}
