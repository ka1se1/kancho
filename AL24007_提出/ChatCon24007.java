import java.io.IOException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChatCon24007 extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        String message = request.getParameter("MYMESSAGE");
        String url;

        if (message != null && message.equals("Q")) {
            DbSelectBean selectBean = new DbSelectBean();
            selectBean.execute();
            ArrayList<String[]> messages = selectBean.getMessages();
            request.setAttribute("messages", messages);

            // Today's date in JST (Docker runs UTC, timestamps stored with +9h offset)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date(System.currentTimeMillis() + 32400000L));
            request.setAttribute("today", today);

            url = "/chatView24007.jsp";
        } else {
            DbInsertBean insertBean = new DbInsertBean();
            insertBean.setMessage(message);
            insertBean.execute();
            request.setAttribute("message", message);
            url = "/chatResult24007.jsp";
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doGet(request, response);
    }
}
