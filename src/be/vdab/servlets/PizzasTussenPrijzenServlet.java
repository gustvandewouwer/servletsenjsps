package be.vdab.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.vdab.repositories.PizzaRepository;
import be.vdab.util.StringUtils;

/**
 * Servlet implementation class PizzasTussenPrijzenServlet
 */
@WebServlet("/pizzas/tussenprijzen.htm")
public class PizzasTussenPrijzenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/pizzastussenprijzen.jsp";
	private final PizzaRepository pizzaRepository = new PizzaRepository();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getQueryString() != null) {
			Map<String, String> fouten = new HashMap<>();
			String vanString = request.getParameter("van");
			BigDecimal van = null;
			if (StringUtils.isBigDecimal(vanString)) {
				van = new BigDecimal(vanString);
			} else {
				fouten.put("van", "tik een getal");
			}
			String totString = request.getParameter("tot");
			BigDecimal tot = null;
			if (StringUtils.isBigDecimal(totString)) {
				tot = new BigDecimal(totString);
			} else {
				fouten.put("tot", "tik een getal");
			}
			if (fouten.isEmpty()) {
				request.setAttribute("pizzas", pizzaRepository.findByPrijsBetween(van, tot));
			} else {
				request.setAttribute("fouten", fouten);
			}
		}
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

}
