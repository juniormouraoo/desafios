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
		String[] p = pesquisa.split(";");
		String url = "https://old.reddit.com/r/";
		for (String palavra : p) {

			try {
				Document document = Jsoup.connect(url + palavra + "/top/").get();
				HtmlParserCrawlers parserCrawlers = new HtmlParserCrawlers(document);
				parserCrawlers.getCrawlers(palavra);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void getCrawlers(String palavra) {

		Elements elements = document.getElementsByClass("thing");
		for(Element element: elements) {
			//String rank = element.getElementsByClass("rank").text();
			//int rank_int = Integer.parseInt(rank); 
			String upvote = element.getElementsByClass("score unvoted").attr("title");
			int upvote_int = Integer.parseInt(upvote);
			if(upvote_int >= 5000) {
				String title = element.getElementsByClass("title may-blank ").text();
				String href = element.getElementsByClass("bylink comments may-blank").attr("href");
				System.out.println("Subreddit: " + palavra + "\nTítulo: " + title + "\nUpvotes: " + upvote + "\nLink para os "
						+ "Comentarios: " + href + "\n-----------------------------------------------------------------------"
								+ "--------------------------------------------------------------------");
			}
		}

	}

}
