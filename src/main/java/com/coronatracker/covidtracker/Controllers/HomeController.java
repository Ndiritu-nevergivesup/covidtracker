package com.coronatracker.covidtracker.Controllers;

import com.coronatracker.covidtracker.Models.LocationStats;
import com.coronatracker.covidtracker.Services.CoronaVirusDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
@Controller
public class HomeController {

    private final CoronaVirusDataService coronaVirusDataService;

    public HomeController(CoronaVirusDataService coronaVirusDataService) {
        this.coronaVirusDataService = coronaVirusDataService;
    }
    @GetMapping("/")
    public String home(Model model){
        List<LocationStats> allstats=coronaVirusDataService.getAllstats();
        int totalcasereported=allstats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
        int totalnewcases=allstats.stream().mapToInt(stat->stat.getDiffFromPrevDay()).sum();
        model.addAttribute("allstats",allstats);
        model.addAttribute("totalcasereported",totalcasereported);
        model.addAttribute("totalnewcases",totalnewcases);
         return "home";

    }

}
