package com.expledia.metroinfo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainActivity extends AppCompatActivity {

    TextView text;

    public class lists{

        public ArrayList<String> l1 = new ArrayList<>();
        public ArrayList<String> l2 = new ArrayList<>();
        public ArrayList<String> l3 = new ArrayList<>();

    }

    public class DownloadInfo extends AsyncTask<String, Void, lists> {


        @Override
        protected lists doInBackground(String... urls) {
            lists info = new lists();

            try {

                text = findViewById(R.id.textView5);

                Document document = Jsoup.connect(urls[0]).get();

                Log.i("URL", urls[0]);

                Elements all = document.select("table tr td:eq(1)");
                Elements numbers = document.select("table tr td:eq(3)");
                Elements landline = document.select("table tr td:eq(2)");

                int i = 0;

                for(Element stationName : all){


                    info.l1.add(stationName.text());
                    Log.i("Hmmou" + i, stationName.text());
                    i++;

                }

                for(Element number: numbers){

                    info.l2.add(number.text());

                }

                for(Element landlin: landline){

                    info.l3.add(landlin.text());

                }

                return info;


            } catch (Exception e) {
                e.printStackTrace();
                return info;
            }


        }
    }
//
//    public class DownloadImage extends AsyncTask<String, Void, Bitmap>{
//
//        @Override
//        protected Bitmap doInBackground(String... urls) {
//
//            URL url = null;
//            try {
//                url = new URL(urls[0]);
//
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                connection.connect();
//
//                InputStream inputStream = connection.getInputStream();
//
//                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
//
//                return myBitmap;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//
//        }
//    }
    SearchableSpinner spinner;
    TextView heading, number, landline;
    lists info = new lists();
    ArrayList<String> stationNumbers;
    ImageView image;
    TextView strip, strip2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stationNumbers = new ArrayList<>();
        image = (ImageView) findViewById(R.id.imageView);
        heading = findViewById(R.id.textView);
        landline = findViewById(R.id.textView5);
        number = findViewById(R.id.textView7);
        strip = findViewById(R.id.strip);
        strip2 = findViewById(R.id.strip2);

        DownloadInfo task = new DownloadInfo();

        try{

            info = task.execute("http://www.delhimetrorail.com/station-numbers.aspx").get();
            Log.d("info", String.valueOf(info));
            spinner = findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, info.l1);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


//            Log.i("selected item", spinner.getSelectedItem().toString());
        } catch (Exception e) {

            e.printStackTrace();

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heading.setText(spinner.getSelectedItem().toString());
                number.setText(info.l2.get(position));
                landline.setText(info.l3.get(position));

                if (position >= 1 && position <=21){
                    image.setImageResource(R.drawable.redline);
                    strip.setBackgroundColor(Color.rgb(225,11,11));
                    strip.setText("RED LINE");
                }
                else if (position > 22 && position <= 57){
                    image.setImageResource(R.drawable.yellowline);
                    strip.setBackgroundColor(Color.rgb(225,225,11));
                    strip.setText("YELLOW LINE");
                }
                else if (position > 58 && position <= 109){
                    strip.setBackgroundColor(Color.rgb(11,11,225));
                    image.setImageResource(R.drawable.blueline);
                    strip.setText("BLUE LINE");
                }
                else if (position > 110 && position <= 124){
                    image.setImageResource(R.drawable.greenline);
                    strip.setBackgroundColor(Color.rgb(11,225,11));
                    strip.setText("GREEN LINE");
                }
                else if (position > 125 && position <= 140){
                    image.setImageResource(R.drawable.violetline);
                    strip.setBackgroundColor(Color.rgb(66,28,82));
                    strip.setText("VIOLET LINE");
                }
                else
                    image.setImageResource(R.drawable.logobig);

                if(strip.getBackground() != null) {
                    strip2.setBackgroundColor(((ColorDrawable) strip.getBackground()).getColor());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                heading.setText("Choose any Station");

            }
        });

    }

}
