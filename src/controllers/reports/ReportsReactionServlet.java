package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Reaction;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsReactionServlet
 */
@WebServlet("/reports/reaction")
public class ReportsReactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsReactionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        //いいね数を+1する
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        int count = r.getLike_reaction();
        count ++;
        r.setLike_reaction(count);

        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();

        //いいねした人の情報を保存する
        Reaction re = new Reaction();

        re.setReport((Report)em.find(Report.class, Integer.parseInt(request.getParameter("id"))));
        re.setEmployee((Employee)request.getSession().getAttribute("login_employee"));

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        re.setCreated_at(currentTime);
        re.setUpdated_at(currentTime);


        em.getTransaction().begin();
        em.persist(re);
        em.getTransaction().commit();
        em.close();
        request.getSession().setAttribute("flush", "いいね！しました。");

        response.sendRedirect(request.getContextPath() + "/reports/index");


    }



}
