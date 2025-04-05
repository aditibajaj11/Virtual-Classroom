package jakarta;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect("admin_login.jsp");
            return;
        }
        
        // Fetch admin-related data from the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/virtualclassroom", "root", "password");
            String query = "SELECT * FROM admin WHERE id=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, (int) session.getAttribute("admin"));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                request.setAttribute("adminName", rs.getString("name"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Database connection error");
        }
        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }
}
