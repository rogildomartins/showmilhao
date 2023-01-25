package br.com.showmilhao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import br.com.showmilhao.connection.ConnectionFactory;
import br.com.showmilhao.model.Pergunta;
import br.com.showmilhao.util.LogUtil;

public class PerguntaDAO {

	private Connection connection;

	private final static String QUERY_INSERT = "INSERT INTO perguntas (id, nivel, enunciado, alternativa1, alternativa2, alternativa3, resposta) VALUES ($next_id, ?, ?, ?, ?, ?, ?)";
	private final static String QUERY_UPDATE = "UPDATE perguntas SET nivel = ?, enunciado = ?, alternativa1 = ?, alternativa2 = ?, alternativa3 = ?, resposta = ? WHERE id = ?";
	private final static String QUERY_DELETE = "DELETE FROM perguntas WHERE id = ?";
	private final static String OK = "Processo Concluido !";
	private final static int MESSAGE_TYPE = JOptionPane.INFORMATION_MESSAGE;

	public PerguntaDAO() {
		connection = ConnectionFactory.getConnection();
	}

	// Metodo de adcionar
	public void adcionar(Pergunta pergunta) {

		try {
			try (PreparedStatement stmt = connection.prepareStatement(QUERY_INSERT)) {

				stmt.setString(2, pergunta.getNivel());
				stmt.setString(3, pergunta.getEnunciado());
				stmt.setString(4, pergunta.getAlternativa1());
				stmt.setString(5, pergunta.getAlternativa2());
				stmt.setString(6, pergunta.getAlternativa3());
				stmt.setString(7, pergunta.getResposta());
				stmt.executeUpdate();
				connection.commit();

			}
			JOptionPane.showMessageDialog(new JFrame(), "Pergunta adicionada com sucesso ! !", OK, MESSAGE_TYPE);
		} catch (Exception e) {
			LogUtil.getLogger(PerguntaDAO.class).error(e.getCause().toString());
		}
	}

	// Metodo de atualizar
	public void atualizar(Pergunta pergunta) {

		try {
			try (PreparedStatement stmt = connection.prepareStatement(QUERY_UPDATE)) {

				stmt.setString(1, pergunta.getNivel());
				stmt.setString(2, pergunta.getEnunciado());
				stmt.setString(3, pergunta.getAlternativa1());
				stmt.setString(4, pergunta.getAlternativa2());
				stmt.setString(5, pergunta.getAlternativa3());
				stmt.setString(6, pergunta.getResposta());
				stmt.setInt(7, pergunta.getId());
				stmt.executeUpdate();
				connection.commit();

			}
			JOptionPane.showMessageDialog(new JFrame(), "Alterações realizada com sucesso !", OK, MESSAGE_TYPE);
		} catch (Exception e) {
			LogUtil.getLogger(PerguntaDAO.class).error(e.getCause().toString());
		}
	}

	// Metodo de excluir
	public void remover(Integer idpergunta) {
		try {
			try (PreparedStatement stmt = connection.prepareStatement(QUERY_DELETE)) {
				stmt.setInt(1, idpergunta);
				stmt.execute();
				connection.commit();
			}
			JOptionPane.showMessageDialog(new JFrame(), "Pergunta removido com sucesso !", OK, MESSAGE_TYPE);

		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getLogger(PerguntaDAO.class).error(e.getCause().toString());
		}
	}

	private List<Pergunta> buscar(String sql, String nivel) {
		List<Pergunta> perguntas = new ArrayList<>();
		try {
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				if (Objects.nonNull(nivel))
					stmt.setString(1, nivel);
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						Pergunta pergunta = new Pergunta();
						pergunta.setId(rs.getInt("id"));
						pergunta.setNivel(rs.getString("nivel"));
						pergunta.setEnunciado(rs.getString("enunciado"));
						pergunta.setAlternativa1(rs.getString("alternativa1"));
						pergunta.setAlternativa2(rs.getString("alternativa2"));
						pergunta.setAlternativa3(rs.getString("alternativa3"));
						pergunta.setResposta(rs.getString("resposta"));
						perguntas.add(pergunta);

					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.getLogger(PerguntaDAO.class).error(e.getCause().toString());
		}

		return perguntas;
	}
	
	public List<Pergunta> listar(){
		return buscar("SELECT * FROM perguntas", null);
	}
	
	public List<Pergunta> listar(String nivel){
		return buscar("SELECT * FROM perguntas WHERE nivel = ?", nivel);
	}
	
	public List<Pergunta> listar(String idsPerguntasFeitas, String nivel){
		String sql = "SELECT * FROM perguntas WHERE nivel = ? ORDER BY RANDOM() LIMITE 1";
		return buscar("SELECT * FROM perguntas WHERE nivel = ?", nivel);
	}

}




























































