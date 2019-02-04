package idwall.desafio.string;

/**
 * Created by Rodrigo Catão Araujo on 06/02/2018.
 */
public class IdwallFormatter extends StringFormatter {

	/**
	 * Should format as described in the challenge
	 *
	 * @param text
	 * @return
	 */
	@Override
	public String format(String text) {

		StringBuffer bufferResult = new StringBuffer();

		char[] textoArray = text.toCharArray();
		StringBuffer linha = new StringBuffer();
		StringBuffer palavra = new StringBuffer();

		boolean openQuotes = false;
		for (int i = 0; i < textoArray.length + 1; i++) {
			if (i >= textoArray.length) {
				// tratamento para nao perder a ultima palavra do texto
				linha.append(palavra);
				// System.out.println(linha.toString());
				bufferResult.append(linha.toString() + System.lineSeparator());
			} else {
				char a = textoArray[i];
				if (Character.isLetterOrDigit(a)) {
					palavra.append(a);
				} else if (a == '\"' && openQuotes == false) {
					// se tiver aspas, eh preciso tratar o conteudo das aspas como uma palavra
					// inteira
					openQuotes = true;
					palavra.append(a);
				} else if ((a == '\"' || a == '.') && openQuotes == true) {
					openQuotes = false;
					palavra.append(a);
				} else {
					// verificar se a palavra cabe na linha
					if (palavra.length() + linha.length() <= limit) {
						linha.append(palavra);
						if (linha.length() + 1 <= limit) {
							linha.append(a);
						}
					} else {
						bufferResult.append(linha.toString() + System.lineSeparator());
						linha = new StringBuffer();
						linha.append(palavra);
						linha.append(a);
					}
					palavra = new StringBuffer();
				}
			}
		}

		return bufferResult.toString();
	}
}
