package jbase;

import java.sql.*;
import java.util.Scanner;

public class Utils {
	
	static Scanner teclado = new Scanner(System.in);

	public static Connection conectar() {

		String CLASSE_DRIVER = "com.mysql.cj.jdbc.Driver";
		String USUARIO = "root";
		String SENHA= "admin123";
		String URL_SERVIDOR= "jdbc:mysql://localhost:3306/jmysql?useSSL=false";

		try{
			Class.forName(CLASSE_DRIVER);
			return DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA);

		} catch (Exception e) {
			if(e instanceof ClassNotFoundException){
				System.out.println("Verifique o driver de conexão...");
			}else{
				System.out.println("Verifique se o servidor está ativo...");
			}
			System.exit(-42);
			return null;
		}


		//System.out.println("Conectando ao banco de dados...");
	}

	public static void desconectar(Connection conn) throws SQLException {
		if(conn !=null){
			conn.close();
		}

	}

	public static void listar() {

		String BUSCAR_TODOS = "SELECT * FROM produtos";
		try {
			Connection conn = conectar();
			PreparedStatement produtos = conn.prepareStatement(BUSCAR_TODOS);
			ResultSet res = produtos.executeQuery();

			boolean hasProducts = false;

			System.out.println("Listando produtos...");
			System.out.println("....................");

			while (res.next()) {
				hasProducts = true;
				System.out.println("ID: " + res.getInt(1));
				System.out.println("Produto: " + res.getString(2));
				System.out.println("Preço: " + res.getFloat(3));
				System.out.println("Estoque: " + res.getInt(4));
				System.out.println("..............");
			}

			if (!hasProducts) {
				System.out.println("Nenhum produto encontrado.");
			}

			produtos.close();
			desconectar(conn);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro buscando produtos...");
			System.exit(-42);
		}
	}
	
	public static void inserir() {
		System.out.println("Informe o nome do produto...");
		String nome = teclado.nextLine();

		System.out.println("Informe o preço do produto...");
		float preco = teclado.nextFloat();

		System.out.println("Informe a quantidade em estoque do produto...");
		int estoque = teclado.nextInt();

		String INSERIR = "INSERT INTO produtos(nome,preco, estoque) VALUES (?,?,?) ";

		try{

			Connection conn = conectar();
			PreparedStatement salvar=conn.prepareStatement(INSERIR);
			salvar.setString(1, nome);
			salvar.setFloat(2, preco);
			salvar.setInt(3,estoque );
			salvar.executeUpdate();
			salvar.close();
			desconectar(conn);
			System.out.println("O produto " + nome + "foi inserido com sucesso");

		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro salvando produto");
			System.exit(-42);
		}

	}
	
	public static void atualizar() {

		System.out.println("Informe o código do produto.");
		int id =Integer.parseInt(teclado.nextLine());
		String BUSCAR_POR_ID = "SELECT * FROM produtos WHERE id=?";

		try{

			Connection conn = conectar();
			PreparedStatement produto=conn.prepareStatement(BUSCAR_POR_ID);
			produto.setInt(1,id );
			ResultSet res = produto.executeQuery();

			if(res.next() ){

				System.out.println("Informe o nome do produto...");
				String nome = teclado.nextLine();

				System.out.println("Informe o preço do produto...");
				float preco = teclado.nextFloat();

				System.out.println("Informe a quantidade em estoque...");
				int estoque = teclado.nextInt();

				String ATUALIZAR= "UPDATE produtos SET nome=?, preco=?, estoque=? WHERE id=?";
				PreparedStatement upd = conn.prepareStatement(ATUALIZAR);
				upd.setString(1, nome);
				upd.setFloat(2, preco);
				upd.setInt(3, estoque);
				upd.setInt(4, id);

				upd.executeUpdate();
				upd.close();
				desconectar(conn);
				System.out.println("O produto " + nome + " foi atualizado");

			}else{
				System.out.println("Não existe produto com o id informado");
			}


		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro salvando produto");
			System.exit(-42);
		}


		System.out.println("Atualizando produtos...");
	}
	
	public static void deletar() {

		System.out.println("Informe o código do produto.");
		int id =Integer.parseInt(teclado.nextLine());
		String BUSCAR_POR_ID = "SELECT * FROM produtos WHERE id=?";

		try{

			Connection conn = conectar();
			PreparedStatement produto=conn.prepareStatement(BUSCAR_POR_ID);
			produto.setInt(1,id );
			ResultSet res = produto.executeQuery();

			if(res.next() ){

				String DELETAR= "DELETE  FROM produtos WHERE id=?";
				PreparedStatement upd = conn.prepareStatement(DELETAR);

				upd.setInt(1, id);

				upd.execute();
				upd.close();
				desconectar(conn);
				System.out.println("O produto " + id + " foi atualizado");

			}else{
				System.out.println("Não existe produto com o id informado");
			}


		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro deletando produto");
			System.exit(-42);
		}


		System.out.println("Deletando produtos...");
	}
	
	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma opção: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");
		
		int opcao = Integer.parseInt(teclado.nextLine());
		if(opcao == 1) {
			listar();
		}else if(opcao == 2) {
			inserir();
		}else if(opcao == 3) {
			atualizar();
		}else if(opcao == 4) {
			deletar();
		}else {
			System.out.println("Opção inválida.");
		}
	}
}
