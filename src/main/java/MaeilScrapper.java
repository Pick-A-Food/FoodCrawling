import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MaeilScrapper {

  public static void main(String[] args) throws IOException {
    String path = "https://www.samyangfoods.com/kor/brand/list.do";
    Document document = Jsoup.connect(path).get();
    System.out.println(document);
    Elements productListDiv = document.select(".product-list");
    System.out.println(productListDiv);
  }

}
