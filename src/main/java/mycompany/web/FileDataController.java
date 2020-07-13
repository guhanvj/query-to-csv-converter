package mycompany.web;

import mycompany.service.QueryDataService;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
public class FileDataController {
    private QueryDataService queryDataService;

    public FileDataController(QueryDataService queryDataService) {
        this.queryDataService = queryDataService;
    }

    @GetMapping(path = "/data/fetch")
    public void getTableRecords(@RequestParam String columns,@RequestParam String fileName){
        queryDataService.getRecords(columns,fileName);
    }

    @GetMapping(path = "/read/files")
    public void readTextFileTableRecords() throws IOException {
        queryDataService.readTextFileTableRecords();
    }


}
