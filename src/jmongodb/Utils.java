package jmongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientURI;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.diagnostics.logging.Logger;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.bson.Document;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;


public class Utils {


	
	static Scanner teclado = new Scanner(System.in);


	public static MongoCollection<Document> conectar() {
		try {
			MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017");
			MongoClient conn = new MongoClient(uri);
			MongoDatabase database = conn.getDatabase("java_mongodb");
			MongoCollection<Document> collection = database.getCollection("produtos");

			return collection;
		} catch (Exception e) {
			if (e instanceof ClassNotFoundException) {
				System.out.println("Verifique o driver de conexão...");
			} else {
				System.out.println("Verifique se o servidor está ativo...");
				e.printStackTrace();
			}
			System.exit(-42);
			return null;
		}
	}


	public static void desconectar(MongoCursor<Document> cursor) throws SQLException {
		cursor.close();

	}

	public static void listar() throws SQLException {

		MongoCollection <Document>collection=conectar();

		if(collection.countDocuments() > 0){
			MongoCursor<Document> cursor= collection.find().iterator();

			try{
				System.out.println("Listando produtos...");
				System.out.println("---------------------");
				while(cursor.hasNext()){
					String json= cursor.next().toJson();
					JSONObject obj = new JSONObject(json);
					JSONObject id = obj.getJSONObject("_id");

					System.out.println("ID:" + id.get("$oid"));
					System.out.println("Produto:" + obj.get("nome"));
					System.out.println("Preço:" + obj.get("preco"));
					System.out.println("Estoque:" + obj.get("estoque"));

					System.out.println("---------------------");

				}


			}catch(Exception e){
				e.printStackTrace();
			}

			desconectar(cursor);


	}else{
			System.out.println("Não existem documentos cadastrados");
		}
	}
	
	public static void inserir() {

		MongoCollection <Document> collection=conectar();

		System.out.println("Informe o nome do produto...");
		String nome = teclado.nextLine();


		System.out.println("Informe o preço do produto...");
		float preco = teclado.nextFloat();

		System.out.println("Informe a quantidade em estoque do produto...");
		int estoque = teclado.nextInt();

		JSONObject nproduto= new JSONObject();
		nproduto.put("nome", nome);
		nproduto.put("preco", preco);
		nproduto.put("estoque", estoque);

		collection.insertOne(Document.parse(nproduto.toString()));
		System.out.println("O produto " + nome +" foi inserido com sucesso");

	}
	
	public static void atualizar() {
		MongoCollection <Document>collection=conectar();
		System.out.println("Informe o código do produto.");
		String _id =teclado.nextLine();

		System.out.println("Informe o nome do produto...");
		String nome = teclado.nextLine();
		System.out.println("Informe o preço do produto...");
		float preco = teclado.nextFloat();

		System.out.println("Informe a quantidade em estoque do produto...");
		int estoque = teclado.nextInt();


		Bson query= combine( set("nome", nome),  set ("preco",  preco),  set("estoque", estoque));

		UpdateResult res= collection.updateOne(new Document("_id", new ObjectId(_id)),
				 query );

		if(res.getModifiedCount()==1){
			System.out.println("O produto " + nome +" foi atualizado com sucesso");
		}else{
			System.out.println("O produto não foi atualizado");
		}

	}
	
	public static void deletar() {

		MongoCollection <Document>collection=conectar();
		System.out.println("Informe o código do produto.");
		String _id =teclado.nextLine();


		DeleteResult res= collection.deleteOne(new Document("_id", new ObjectId(_id) ));

		if(res.getDeletedCount()==1){
			System.out.println("O produto foi deletado com sucesso");
		}else{
			System.out.println("O produto não foi deletado");
		}

	}
	
	public static void menu() throws SQLException {
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
