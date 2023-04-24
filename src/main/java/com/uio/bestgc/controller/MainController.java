package com.uio.bestgc.controller;

import com.uio.bestgc.model.Statistics;
import com.uio.bestgc.model.UserInputs;
import com.uio.bestgc.service.MainService;
import com.uio.bestgc.service.ResultsService;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private final MainService mainService;
    private final ResultsService resultsService;
    private final ApplicationContext context;

    public MainController(MainService mainService, ResultsService resultsService1, ApplicationContext context) {
        this.mainService = mainService;
        this.resultsService = resultsService1;
        this.context = context;
    }

    public void main(UserInputs inputs) {
        mainService.run(inputs);
        //run the command
        Statistics statistics = mainService.findStatistics(inputs.getSamplingTime());
        System.out.println("The average CPU usage per core by the user's application is: " + statistics.getAvgCpuPerCore() + "%");
        resultsService.fetchMatrix(inputs, statistics);
        String executableJar = resultsService.getExecutableJar();
        if (inputs.getRunAppWithBestGC() == null || inputs.getRunAppWithBestGC()) {
            mainService.getUserappProcess().destroy();
            System.out.println("Running app with the best GC...");
        } else {
            System.out.println("The command to run user app with the best GC is: ");
            System.out.println(executableJar);
            SpringApplication.exit(context, new ExitCodeGenerator() {
                @Override
                public int getExitCode() {
                    return 0;
                }
            });
        }
    }
}




