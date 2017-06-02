package be.vdab.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import be.vdab.entities.Pizza;
import be.vdab.repositories.PizzaRepository;
import be.vdab.util.StringUtils;

/**
 * Servlet implementation class PizzaToevoegenServlet
 */
@WebServlet("/pizzas/toevoegen.htm")
@MultipartConfig
public class PizzaToevoegenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/pizzatoevoegen.jsp";
	// private static final String SUCCESS_VIEW = "/WEB-INF/JSP/pizzas.jsp";
	private static final String REDIRECT_URL = "%s/pizzas.htm";
	private final transient PizzaRepository pizzaRepository = new PizzaRepository();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> fouten = new HashMap<>();
		String naam = request.getParameter("naam");
		if (!Pizza.isNaamValid(naam)) {
			fouten.put("naam", "verplicht");
		}
		String prijsString = request.getParameter("prijs");
		BigDecimal prijs = null;
		if (StringUtils.isBigDecimal(prijsString)) {
			prijs = new BigDecimal(prijsString);
			if (!Pizza.isPrijsValid(prijs)) {

				fouten.put("prijs", "tik een positief getal");
			}
		} else {
			fouten.put("prijs", "tik een getal");
		}

		Part fotoPart = request.getPart("foto");
		boolean fotoIsOpgeladen = fotoPart != null && fotoPart.getSize() != 0;
		if (fotoIsOpgeladen && !fotoPart.getContentType().contains("jpeg")) {
			fouten.put("foto", "geen JPEG foto");
		}
		if (fouten.isEmpty()) {
			boolean pikant = "pikant".equals(request.getParameter("pikant"));
			Pizza pizza = new Pizza(naam, prijs, pikant);
			pizzaRepository.create(pizza);
			if (fotoIsOpgeladen) {
				String pizzaFotosPad = this.getServletContext().getRealPath("/pizzafotos");
				System.out.println("pizzaFotosPad= " + pizzaFotosPad);
				fotoPart.write(String.format("%s/%d.jpg", pizzaFotosPad, pizza.getId()));
			}
			String url = String.format(REDIRECT_URL, request.getContextPath());
			System.out.println("redirecturl= " + url);
//			response.sendRedirect(url);
			response.sendRedirect(response.encodeRedirectURL(url));
		} else {
			request.setAttribute("fouten", fouten);
			request.getRequestDispatcher(VIEW).forward(request, response);
		}
	}
	
	@Resource(name = PizzaRepository.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		pizzaRepository.setDataSource(dataSource);
	}

}
