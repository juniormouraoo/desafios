package idwall.desafio.Crawlers;

public class Main {
	
	String pesquisa;

	public static void main(String[] args) {

		String pesquisa = "askreddit;cats";
		HtmlParserCrawlers htmlParserCrawlers = new HtmlParserCrawlers(null);
		htmlParserCrawlers.conexion(pesquisa);

	}

}
