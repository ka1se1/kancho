import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tricon24007 extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        String url = "/triangle24007.jsp";
        RequestDispatcher dispatcher
                   = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }
}
