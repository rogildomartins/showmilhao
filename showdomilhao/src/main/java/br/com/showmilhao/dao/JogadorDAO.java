package br.com.showmilhao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.showmilhao.connection.ConnectionFactory;
import br.com.showmilhao.model.Jogador;
import br.com.showmilhao.util.LogUtil;

public class JogadorDAO {

	private Connection connection;

	private final static String QUERY_INSERT = "INSERT INTO jogador (id, nome, pontuacao) VALUES ($nex_id, ?, ?)";
	private final static String QUERY_UPDATE = "UPDATE jogador SET nome = ?, pontuacao = ? WHERE id = ?";
	private final static String QUERY_ZERAR_RANKING = "DELETE FROM jogador";
	private final static String QUERY_CONSULTAR_TODOS = "SELECT * FROM jogador";
	private final static String QUERY_LISTAR_RANKING = "SELECT * FROM jogador ORDER BY pontuacao DESC LIMIT 10";

	public JogadorDAO() {
		// TODO Auto-generated constructor stub
		connection = ConnectionFactory.getConnection();
	}

	public boolean adicionar(Jogador jogador) {
		try {
			try (PreparedStatement stmt = connection.prepareStatement(QUERY_INSERT)) {
				stmt.setString(2, jogador.getNome());
				stmt.setInt(3, jogador.getPontuacao());
				stmt.executeUpdate();
				connection.commit();
				return true;
			}

		} catch (Exception e) {
			LogUtil.getLogger(JogadorDAO.class).error(e.getCause().toString());
			return false;
		}

	}

	public void atualizar(Jogador jogador) {
		try {
			try (PreparedStatement stmt = connection.prepareStatement(QUERY_UPDATE)) {

				stmt.setString(1, jogador.getNome());
				stmt.setInt(2, jogador.getPontuacao());
				stmt.setInt(3, jogador.getId());
				stmt.executeUpdate();
				connection.commit();
			}

		} catch (Exception e) {
			LogUtil.getLogger(JogadorDAO.class).error(e.getCause().toString());

		}

	}

	private List<Jogador> buscar(String sql) {
		List<Jogador> jogadores = new ArrayList<>();
		try {
			try (PreparedStatement smtm = connection.prepareStatement(sql)) {
				try (ResultSet rs = smtm.executeQuery()) {
					while (rs.next()) {
						Jogador jogador = new Jogador();
						jogador.setId(rs.getInt("id"));
						jogador.setLinha(rs.getRow());
						jogador.setNome(rs.getString("nome"));
						jogador.setPontuacao(rs.getInt("pontuacao"));
						jogadores.add(jogador);
					}

				}
				smtm.close();
			}

		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getLogger(JogadorDAO.class).error(e.getCause().toString());
		}

		return jogadores;

	}

	public List<Jogador> listar() {
		return buscar(QUERY_CONSULTAR_TODOS);
	}

	public List<Jogador> listarRanking() {
		return buscar(QUERY_LISTAR_RANKING); 

	}

	public void zerarRanking() {
		try {
			try (PreparedStatement stmt = connection.prepareStatement(QUERY_ZERAR_RANKING)) {
				stmt.execute();
				connection.commit();

			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getLogger(JogadorDAO.class).error(e.getCause().toString());
		}

	}

}
