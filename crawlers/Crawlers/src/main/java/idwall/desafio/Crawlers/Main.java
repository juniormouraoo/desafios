package idwall.desafio.Crawlers;

public class Main {

	String pesquisa;

	public static void main(String[] args) {

		// Input do que sera pesquisado
		String pesquisa = "worldnews;askreddit;cats;dogs";
		HtmlParserCrawlers htmlParserCrawlers = new HtmlParserCrawlers(null);
		htmlParserCrawlers.conexion(pesquisa);

	}

}
