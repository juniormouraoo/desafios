package idwall.desafio.Crawlers;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParserCrawlers {

	private Document document;

	public HtmlParserCrawlers(Document document) {
		this.document = document;
	}

	public void conexion(String pesquisa) {

		// Separa as palavras passadas por parametro
		String[] p = pesquisa.split(";");
		String url = "https://old.reddit.com/r/";
		for (String palavra : p) {

			try {
				// Conexao e captura das informacoes no old.reddit
				Document document = Jsoup.connect(url + palavra + "/top/").get();
				HtmlParserCrawlers parserCrawlers = new HtmlParserCrawlers(document);
				parserCrawlers.getCrawlers(palavra);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void getCrawlers(String palavra) {

		Boolean vote = false;
		// Captura dos elementos
		Elements elements = document.getElementsByClass("thing");
		for (Element element : elements) {
			// Captura de upvotes
			String upvote = element.getElementsByClass("score unvoted").attr("title");
			int upvote_int = Integer.parseInt(upvote);
			if (upvote_int >= 5000) {
				// Condicao para os dois nomes de titulos encontrados
				String title = element.getElementsByClass("title may-blank ").text();
				if (title == null || title.trim().isEmpty()) {
					title = element.getElementsByClass("title may-blank outbound").text();
				}
				String href = element.getElementsByClass("bylink comments may-blank").attr("href");

				System.out.println("Subreddit: " + palavra + "\nTítulo: " + title + "\nUpvotes: " + upvote
						+ "\nLink para os " + "Comentarios: " + href
						+ "\n-----------------------------------------------------------------------"
						+ "--------------------------------------------------------------------");
				vote = true;
			}
		}
		// Retorno caso a pesquisa de uma das palavras não retorne algo que se encaixa
		// nas condicoes
		if (vote == false) {
			System.out.println("A pesquisa " + palavra + " não possui nenhuma thread com mais de 5000 upvotes"
					+ "\n-----------------------------------------------------------------------"
					+ "--------------------------------------------------------------------");
		}
	}
}
