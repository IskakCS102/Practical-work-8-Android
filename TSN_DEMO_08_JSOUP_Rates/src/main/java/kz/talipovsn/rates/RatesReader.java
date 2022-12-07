package kz.talipovsn.rates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// СОЗДАТЕЛЬ КОТИРОВОК ВАЛЮТ
public class RatesReader {

    private static final String BASE_URL = "https://www.iqair.com/ru/kazakhstan/pavlodar"; // Адрес с котировками

    // Парсинг котировок из формата html web-страницы банка, при ошибке доступа возвращаем null
    public static String getRatesData() {
        StringBuilder data = new StringBuilder();
        try {
            Document doc = Jsoup.connect(BASE_URL).timeout(5000).get(); // Создание документа JSOUP из html
            data.append("Какое сейчас качество воздуха в Павлодаре?\n \n "); // Считываем заголовок страницы
            data.append(String.format("%14s %14s %20s \n", "Загрязнения","    Воздух", "Загрязнитель").trim());
            data.append("\n");
            Elements e = doc.select("div.table-wrapper"); // Ищем в документе "<div class="exchange"> с данными о валютах
            Elements tables = e.select("table.aqi-overview-detail__main-pollution-table"); // Ищем таблицы с котировками
            Elements tablesk = doc.select("table.aqi-overview-detail__other-pollution-table");
            Elements head = tablesk.select("thead");
            Element table = tables.get(0); // Берем 1 таблицу
            Element tablek = tablesk.get(0);
            Element theadk = head.get(0);
            for (Element row : table.select("tr")) {
                // Цикл по столбцам таблицы
                for (Element col : row.select("td")) { //
                    data.append(String.format("%14s ", col.text())); // Считываем данные с ячейки таблицы
                }
                data.append("\n"); // Добавляем переход на следующую строку;
            }
            data.append("\n \n \n");
            for (Element cole : theadk.select("th")) {
                    data.append(String.format("%14s ", cole.text()));
            }
            data.append("\n");
            for (Element rowk : tablek.select("tr")) {
                for (Element col : rowk.select("td")) {
                    data.append(String.format("%14s ", col.text()));
                }
                data.append("\n");
            }
        } catch (Exception ignored) {
            return null; // При ошибке доступа возвращаем null
        }
        return data.toString().trim(); // Возвращаем результат
    }

}