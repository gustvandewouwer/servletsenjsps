package be.vdab.listeners;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Application Lifecycle Listener implementation class MandjeListener
 *
 */
@WebListener
public class MandjeListener implements ServletContextListener, HttpSessionAttributeListener {

	private static final String MANDJE = "mandje";
	private static final String AANTAL_MANDJES = "aantalMandjes";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("contextInitialized");
		event.getServletContext().setAttribute(AANTAL_MANDJES, new AtomicInteger());
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		System.out.println("attributeAdded " + event.getName());
		if (MANDJE.equals(event.getName())) {
			((AtomicInteger) event.getSession().getServletContext().getAttribute(AANTAL_MANDJES)).incrementAndGet();
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		System.out.println("attributeRemoved " + event.getName());
		if (MANDJE.equals(event.getName())) {
			((AtomicInteger) event.getSession().getServletContext().getAttribute(AANTAL_MANDJES)).decrementAndGet();
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		System.out.println("attributeReplaced " + event.getName());
	}
}
