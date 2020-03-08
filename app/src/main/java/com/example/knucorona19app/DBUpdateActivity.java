package com.example.knucorona19app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DBUpdateActivity extends AppCompatActivity {
    private String url = "https://www.cdc.go.kr/board.es?mid=a20501000000&bid=0015&nPage=";
    ArrayList<ChartData> data = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    int PageLimit = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbupdate);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {
        ProgressDialog pd = new ProgressDialog(DBUpdateActivity.this);

        @Override
        protected void onPreExecute() {
            pd.setMessage("Updating...");
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String _url;
            for(int i=1;i<=PageLimit;i++){
                String suffix = Integer.toString(i);
                _url = url+suffix;
                try {
                    Document document = Jsoup.connect(_url).get();
                    Elements elements = document.select("div.tstyle_list div.dbody li.title a");
                    Log.e("!!!!!", "hey"+elements.size());
                    for(Element element : elements){
                        String chk = element.attr("title");
                        if(chk.length()<6) continue;
                        chk = chk.substring(chk.length()-6,chk.length()-1);
                        if(chk.equals("정례브리핑")){
                            String link = "https://www.cdc.go.kr"+element.attr("href");
                            Document innerDocument = Jsoup.connect(link).get();
                            Element date = innerDocument.selectFirst("ul.head.info li b");
                            String sd = date.text().replace("-", "");
                            int sDate = Integer.parseInt(sd);
                            String infection, recovered, deaths;
                            String testNow, testNegative;
                            String dInfection, dRecovered, dDeaths, dTestNow, dTestNegative;
                            String [] input = new String[11];
                            if(sDate<20200215) continue;
                            if(sDate<=20200220){
                                Element parse1 = innerDocument.selectFirst("tbody");
                                Elements parse2 = parse1.select("tr");
                                Element parse3 = parse2.get(3);
                                Elements parse4 = parse3.select("td");
                                input[0] = sd;
                                input[1]= recovered = parse4.get(0).text().replace(",","").replace("*","");
                                input[2] = infection = parse4.get(1).text().replace(",","").replace("*","");
                                input[3] = deaths = "0";

                                Element parse5 = parse2.get(2);
                                Elements parse6 = parse5.select("td");
                                input[4] = testNow = parse6.get(4).text().replace(",","").replace("*","");
                                input[5] = testNegative = parse6.get(5).text().replace(",","").replace("*","");

                                Element parse7 = parse2.get(6);
                                Elements parse8 = parse7.select("td");
                                input[9] = dTestNow = parse8.get(4).text().replace(",","").replace("*","");
                                input[10] = dTestNegative = parse8.get(5).text().replace(",","").replace("*","");

                                Element parse9 = parse2.get(7);
                                Elements parse10 = parse9.select("td");
                                input[6] = dInfection = parse10.get(0).text().replace(",","").replace("*","");
                                input[7] = dRecovered = parse10.get(1).text().replace(",","").replace("*","");
                                input[8] = dDeaths = "+0";
                            }else{
                                Element parse1 = innerDocument.selectFirst("tbody");
                                Elements parse2 = parse1.select("tr");
                                Element parse3 = parse2.get(3);
                                Elements parse4 = parse3.select("td");
                                input[0] = sd;
                                input[1]= recovered = parse4.get(3).text().replace(",","").replace("*","");
                                input[2] = infection = parse4.get(4).text().replace(",","").replace("*","");
                                input[3] = deaths = parse4.get(5).text().replace(",","").replace("*","");
                                input[4] = testNow = parse4.get(7).text().replace(",","").replace("*","");
                                input[5] = testNegative = parse4.get(8).text().replace(",","").replace("*","");

                                Element parse5 = parse2.get(4);
                                Elements parse6 = parse5.select("td");
                                input[6] = dInfection = parse6.get(3).text().replace(",","").replace("*","");
                                input[7] = dRecovered = parse6.get(4).text().replace(",","").replace("*","");
                                input[8] = dDeaths = parse6.get(5).text().replace(",","").replace("*","");
                                input[9] = dTestNow = parse6.get(7).text().replace(",","").replace("*","");
                                input[10] = dTestNegative = parse6.get(8).text().replace(",","").replace("*","");
                            }
                            ChartData chartData = new ChartData(input);
                            data.add(chartData);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            for(ChartData temp : data){
                databaseReference.child("ChartData").child(temp.date).setValue(temp);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            finish();
        }
    }
}
