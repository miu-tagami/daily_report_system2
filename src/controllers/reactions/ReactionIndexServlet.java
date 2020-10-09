package controllers.reactions;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Reaction;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReactionIndexServlet
 */
@WebServlet("/reactions/index")
public class ReactionIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReactionIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        }catch(Exception e){
            page = 1;
        }

        Report r = em.find(Report.class,Integer.parseInt(request.getParameter("id")));


        List<Reaction> reactions = (List<Reaction>) em.createNamedQuery("getReportIdReactions",Reaction.class)
                                                       .setParameter("report", r )
                                                       .setFirstResult(15 * (page - 1))
                                                       .setMaxResults(15)
                                                       .getResultList();

        long reactions_count = (long) em.createNamedQuery("getReactionsCount",Long.class)
                                           .setParameter("report", r)
                                           .getSingleResult();

        em.close();

        request.setAttribute("reactions", reactions);
        request.setAttribute("reactions_count", reactions_count);
        request.setAttribute("page",page);
        if(request.getSession().getAttribute("flush") != null){
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reactions/index.jsp");
        rd.forward(request, response);
    }



}
