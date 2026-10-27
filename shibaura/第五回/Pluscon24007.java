import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Pluscon24007 extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        String url;
        try {
            int num1 = Integer.parseInt(request.getParameter("num1"));
            int num2 = Integer.parseInt(request.getParameter("num2"));
            int result = num1 + num2;
            request.setAttribute("result", result);
            url = "/plus24007.jsp";
        } catch (NumberFormatException e) {
            url = "/error.html";
        }
        RequestDispatcher dispatcher
                   = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }
}
