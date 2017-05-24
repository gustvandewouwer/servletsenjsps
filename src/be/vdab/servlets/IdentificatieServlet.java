package be.vdab.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class IdentificatieServlet
 */
@WebServlet("/identificatie.htm")
public class IdentificatieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/identificatie.jsp";
//	private static final int COOKIE_MAXIMUM_LEEFTIJD = 60 /* seconden */ * 30 /* minuten */;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			System.out.println("identificatie - doGET - session is niet null");
			String gebruikersnaam = (String) session.getAttribute("gebruikersnaam");
			if (gebruikersnaam != null) {
				System.out.println("identificatie - doGET - gebruikersnaam is niet null");
				request.setAttribute("gebruikersnaam", gebruikersnaam);
			} else {
				System.out.println("identificatie - doGET - gebruikersnaam is null");
			}
		} else {
			System.out.println("identificatie - doGET - session is null");
		}
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("identificatie - doPOST");
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		session.setAttribute("gebruikersnaam", request.getParameter("gebruikersnaam"));
		response.sendRedirect(request.getRequestURI()); // redirect->huidige
	}
}
