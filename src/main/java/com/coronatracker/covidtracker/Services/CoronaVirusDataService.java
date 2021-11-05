package com.coronatracker.covidtracker.Services;
import com.coronatracker.covidtracker.Models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
@Service
public class CoronaVirusDataService {
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> allstats=new ArrayList<>();
    public List<LocationStats> getAllstats() {
        return allstats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchvirusData() throws IOException, InterruptedException {
        List<LocationStats> newstats=new ArrayList<>();
        HttpClient client=HttpClient.newHttpClient();
        HttpRequest request=HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
        HttpResponse<String> httpResponse=client.send(request,HttpResponse.BodyHandlers.ofString());
//        System.out.println(httpResponse.body());
        StringReader csvbodyreader=new StringReader(httpResponse.body());
        Iterable<CSVRecord> records= CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvbodyreader);
        for (CSVRecord record:records){
            LocationStats locationStats=new LocationStats();
            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));
//                    System.out.println(record.get("Province/State"));

//            locationStats.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
            int latestcases=Integer.parseInt(record.get(record.size()-1));
            int prevDaycases=Integer.parseInt(record.get(record.size()-2));
            locationStats.setLatestTotalCases(latestcases);
            locationStats.setLatestTotalCases(prevDaycases);
           newstats.add(locationStats);
        }
        this.allstats=newstats;
    }
}
