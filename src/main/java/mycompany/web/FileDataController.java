package mycompany.web;

import mycompany.service.ComparisonService;
import mycompany.service.QueryDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
public class FileDataController {
    private QueryDataService queryDataService;
    private ComparisonService comparisonService;

    public FileDataController(QueryDataService queryDataService, ComparisonService comparisonService) {
        this.queryDataService = queryDataService;
        this.comparisonService = comparisonService;
    }

    @GetMapping(path = "/data/fetch")
    public void getTableRecords(@RequestParam String columns,@RequestParam String fileName){
        queryDataService.getRecords(columns,fileName);
    }

    @GetMapping(path = "/read/files")
    @ResponseStatus(HttpStatus.OK)
    public void readTextFileTableRecords() throws IOException {
        queryDataService.readTextFileTableRecords();
    }

    @GetMapping(path = "/compare/files")
    @ResponseStatus(HttpStatus.OK)
    public void comparefiles() throws IOException {
        comparisonService.compareCsvs();
    }


}
