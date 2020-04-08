package com.example.knucorona19app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class DBAsyncTask extends AsyncTask<String,Void,String> {
    String url = "https://www.cdc.go.kr/board.es?mid=a20501000000&bid=0015&nPage=";
    int lastUpdate;
    Context context;
    ArrayList<ChartData> data;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog pd;

    public DBAsyncTask(){

    }

    public DBAsyncTask(int lastUpdate, Context context){
        this.lastUpdate = lastUpdate;
        this.context = context;
        data = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onPreExecute() {
        pd.setMessage("데이터 업데이트 중입니다");
        pd.show();
        super.onPreExecute();
    }

    @Override
    public String doInBackground(String... strings) {
        Log.e("@@@",lastUpdate+"!");
        String _url;
        int i=1;
        boolean flag=false;
        boolean flag2=false;
        int ld = lastUpdate;
        while(true){
            String suffix = Integer.toString(i++);
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
                        Log.d("!", sd);
                        if(!flag2){
                            flag2=true;
                            ld=sDate;
                        }
                        if(sDate == lastUpdate || sDate==20200215){
                            flag = true;
                            break;
                        }
                        if(sDate>=20200314){
                            Element parse1 = innerDocument.selectFirst("tbody");
                            Elements parse2 = parse1.select("tr");
                            Element parse3 = parse2.get(3);
                            Elements parse4 = parse3.select("td");
                            input[0] = sd;
                            input[1] = infection = parse4.get(4).text().replace(",","").replace("*","");
                            input[2] = recovered = parse4.get(3).text().replace(",","").replace("*","");
                            input[3] = deaths = parse4.get(5).text().replace(",","").replace("*","");
                            input[4] = testNow = parse4.get(6).text().replace(",","").replace("*","");
                            input[5] = testNegative = parse4.get(7).text().replace(",","").replace("*","");

                            Element parse5 = parse2.get(4);
                            Elements parse6 = parse5.select("td");
                            input[6] = dInfection = parse6.get(4).text().replace(",","").replace("*","").replace("(","").replace(")","");
                            input[7] = dRecovered = parse6.get(3).text().replace(",","").replace("*","").replace("(","").replace(")","");
                            input[8] = dDeaths = parse6.get(5).text().replace(",","").replace("*","").replace("(","").replace(")","");
                            input[9] = dTestNow = parse6.get(6).text().replace(",","").replace("*","").replace("(","").replace(")","");
                            input[10] = dTestNegative = parse6.get(7).text().replace(",","").replace("*","").replace("(","").replace(")","");
                        }
                        else if(sDate<=20200220){
                            Element parse1 = innerDocument.selectFirst("tbody");
                            Elements parse2 = parse1.select("tr");
                            Element parse3 = parse2.get(3);
                            Elements parse4 = parse3.select("td");
                            input[0] = sd;
                            input[1] = infection = parse4.get(0).text().replace(",","").replace("*","");
                            input[2]= recovered = parse4.get(1).text().replace(",","").replace("*","");
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
                            input[1] = infection = parse4.get(4).text().replace(",","").replace("*","");
                            input[2] = recovered = parse4.get(3).text().replace(",","").replace("*","");
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
            if(flag){
                break;
            }
        }
        for(ChartData temp : data){
            databaseReference.child("ChartData").child(temp.date).setValue(temp);
        }
        databaseReference.child("LastUpdate").setValue(Integer.toString(ld));
        return null;
    }

    @Override
    public void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    public void onPostExecute(String result) {
        pd.dismiss();
        if(context.getClass() == ChartViewActivity.class){
            ((ChartViewActivity)context).getData();
        }
        if(context.getClass() == ChartViewActivityDeath.class){
            ((ChartViewActivityDeath)context).getData();
        }
        if(context.getClass() == ChartViewActivityRecovered.class){
            ((ChartViewActivityRecovered)context).getData();
        }
        if(context.getClass() == ChartViewActivityTest.class){
            ((ChartViewActivityTest)context).getData();
        }
        //if(context.getClass()==Main2Activity.class){
        //    ((Main2Activity)context).getData();
        //}
        super.onPostExecute(result);
    }
}

