package kz.talipovsn.jsoup_micro;

import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView; // Иконка погоды
    private TextView textView; // Компонент для данных погоды

    private static final String BASE_URL = "https://kazfin.info/kazakhstan"; // Адрес с котировками

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Разрешаем запуск в общем потоке выполнеия длительных задач (например, чтение с сети)
        // ЭТО ТОЛЬКО ДЛЯ ПРИМЕРА, ПО-НОРМАЛЬНОМУ НАДО ВСЕ В ОТДЕЛЬНЫХ ПОТОКАХ
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textView = findViewById(R.id.textView);

        onClick(null); // Нажмем на кнопку "Обновить"
    }

    // Кнопка "Обновить"
    public void onClick(View view) {
        textView.setText(R.string.not_data);
        try {
            StringBuilder data = new StringBuilder();
            Document doc = Jsoup.connect(BASE_URL).timeout(5000).get(); // Создание документа JSOUP из html
            data.append("Курсы валюты:\n"); // Считываем заголовок страницы
            data.append(String.format("%12s %12s %12s %12s\n", "валюта", "наименование", "курс", "изменение").trim());
            data.append("\n");
            Elements e = doc.select("div.exchange"); // Ищем в документе "<div class="exchange"> с данными о валютах
            Elements tables = e.select("table"); // Ищем таблицы с котировками
            Element table = tables.get(0); // Берем 1 таблицу
            int i = 0;
            // Цикл по строкам таблицы
            for (Element row : table.select("tr")) { // все строки)
//          for (Element row : table.select("tr:lt(3)")) { // чтение 3 строк (eq - конкретная строка ,
//          lt - ниже, gt - выше)
                // Цикл по столбцам таблицы
                for (Element col : row.select("td")) { //
                    data.append(String.format("%12s ", col.text())); // Считываем данные с ячейки таблицы
                }
                data.append("\n"); // Добавляем переход на следующую строку;
            }
            textView.setText(data.toString().trim());
        } catch (Exception e) {
            textView.setText(R.string.error);
        }
    }

}
