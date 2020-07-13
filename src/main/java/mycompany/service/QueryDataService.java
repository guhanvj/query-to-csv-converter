package mycompany.service;

import java.io.IOException;

public interface QueryDataService {
    void getRecords(String columns,String fileName);
    void readTextFileTableRecords() throws IOException;
}
