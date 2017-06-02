package be.vdab.repositories;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import be.vdab.entities.Pizza;

public class PizzaRepository extends AbstractRepository {

	private final static Logger LOGGER = Logger.getLogger(PizzaRepository.class.getName());
	
	private static final String BEGIN_SELECT = "select id, naam, prijs, pikant from pizzas ";
	private static final String FIND_ALL = BEGIN_SELECT + "order by naam";
	private static final String READ = BEGIN_SELECT + "where id=?";
	private static final String FIND_BY_PRIJS_BETWEEN = BEGIN_SELECT + "where prijs between ? and ? order by prijs";
	private static final String CREATE = "insert into pizzas(naam, prijs, pikant) values (?, ?, ?)";

	public List<Pizza> findAll() {
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(FIND_ALL)) {
			List<Pizza> pizzas = new ArrayList<>();
			while (resultSet.next()) {
				pizzas.add(resultSetRijNaarPizza(resultSet));
			}
			return pizzas;
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "Probleem met database pizzaluigi", ex);
			throw new RepositoryException(ex);
		}
	}

	private Pizza resultSetRijNaarPizza(ResultSet resultSet) throws SQLException {
		return new Pizza(resultSet.getLong("id"), resultSet.getString("naam"), resultSet.getBigDecimal("prijs"),
				resultSet.getBoolean("pikant"));
	}

	public Optional<Pizza> read(long id) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(READ)) {
			statement.setLong(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(resultSetRijNaarPizza(resultSet));
				}
				return Optional.empty();
			}
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "Probleem met database pizzaluigi", ex);
			throw new RepositoryException(ex);
		}
	}

	public List<Pizza> findByPrijsBetween(BigDecimal van, BigDecimal tot) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(FIND_BY_PRIJS_BETWEEN)) {
			List<Pizza> pizzas = new ArrayList<>();
			statement.setBigDecimal(1, van);
			statement.setBigDecimal(2, tot);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					pizzas.add(resultSetRijNaarPizza(resultSet));
				}
				return pizzas;
			}
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "Probleem met database pizzaluigi", ex);
			throw new RepositoryException(ex);
		}
	}

	public void create(Pizza pizza) { // pizza toevoegen
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, pizza.getNaam());
			statement.setBigDecimal(2, pizza.getPrijs());
			statement.setBoolean(3, pizza.isPikant());
			statement.executeUpdate();
			try (ResultSet resultSet = statement.getGeneratedKeys()) {
				resultSet.next();
				pizza.setId(resultSet.getLong(1));
			}
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "Probleem met database pizzaluigi", ex);
			throw new RepositoryException(ex);
		}
	}
}
