package kz.talipovsn.rates;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView; // Компонент для данных котировок

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        JSOUPRatesTask task = new JSOUPRatesTask(); // Создание потока определения котировок
        task.execute(); // Активация потока
    }

    // Кнопка "Обновить"
    public void onClick(View view) {
        new JSOUPRatesTask().execute(); // Создание и активация потока определения котировок
    }

    // Класс отдельного асинхронного потока
    @SuppressLint("StaticFieldLeak")
    private class JSOUPRatesTask extends AsyncTask<String, Void, String> {

        // Тут реализуем фоновую асинхронную загрузку данных, требующих много времени
        @Override
        protected String doInBackground(String... params) {
            return RatesReader.getRatesData(); // Получаем данные котировок
        }
        // ----------------------------------------------------------------------------

        // Тут реализуем что нужно сделать после окончания загрузки данных
        @Override
        protected void onPostExecute(final String rates) {
            super.onPostExecute(rates);

            // Выдаем данные о котировках в компонент
            textView.post(new Runnable() { //  с использованием синхронизации UI
                @Override
                public void run() {
                    if (rates != null) {
                            textView.setText(rates);
                    } else {
                        textView.setText("");
                        textView.append("Нет данных!" + "\n");
                        textView.append("Проверьте доступность Интернета");
                    }
                }
            });

        }
        // ------------------------------------------------------------------------------------

    }

}
