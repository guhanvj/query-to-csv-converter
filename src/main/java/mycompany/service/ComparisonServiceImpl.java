package mycompany.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ComparisonServiceImpl implements ComparisonService {

    private ResourceLoader resourceLoader;

    @Override
    public void compareCsvs() throws IOException {

        Resource[] resource = this.loadResources();
            File csvFile1 = resource[0].getFile();
        System.out.println("file name1 : "+ csvFile1.getName());
            BufferedReader br1 = new BufferedReader(new FileReader(csvFile1));
            String lineExtractor1;
            HashMap<String, List<String>> file1 = new LinkedHashMap<>();
            while ((lineExtractor1 = br1.readLine()) != null) {
                String[] strArr1 =lineExtractor1.split(",");
                List<String> lineAsStrList1 = new ArrayList<>(Arrays.asList(strArr1));
                String key1 = strArr1[0];
                file1.put(key1, lineAsStrList1);
            }

        File csvFile2 = resource[1].getFile();
        System.out.println("file name2 : "+ csvFile2.getName());
        BufferedReader br2 = new BufferedReader(new FileReader(csvFile2));
        String lineExtractor2;
        //HashMap<String, List<String>> file2 = new LinkedHashMap<>();
        HashMap<String, List<String>> file3 = new LinkedHashMap<>();
        int h=0;
        String[] strArr3 = null;
        while ((lineExtractor2 = br2.readLine()) != null) {
            h++;
            String[] strArr2 =lineExtractor2.split(",");
            if(h == 1) {
                strArr3 = new String[strArr2.length];
                System.arraycopy(strArr2, 0, strArr3, 0, strArr2.length);
            }
            List<String> lineAsStrList2 = new ArrayList<>(Arrays.asList(strArr2));
            List<String> lineAsStrList3 = new ArrayList<>();
            String key2 = strArr2[0];
            List<String> lineAlreadySaved = null;
            if (file1.containsKey(key2)) {
                lineAlreadySaved = file1.get(key2);
                for(int i=0;i<lineAsStrList2.size();i++){
                    if(lineAsStrList2.get(i).equalsIgnoreCase(lineAlreadySaved.get(i)))
                        lineAsStrList3.add(lineAsStrList2.get(i));
                    else
                        lineAsStrList3.add("column: "+strArr3[i]+" expected: "
                                +lineAlreadySaved.get(i)
                                +" Actual: "+lineAsStrList2.get(i));
                }
                file3.put(key2, lineAsStrList3);
                file1.remove(key2);
            }
            else {
                file3.put(key2+" not found in file1 ", lineAsStrList2);
            }
            //file2.put(key2, lineAsStrList2);
        }

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter
                             ("diff.csv")))
        {
            for (Map.Entry<String,List<String>> entry : file3.entrySet())
            {
            writer.append(entry.getKey())
                    .append(" : ")
                    .append(String.join(",", entry.getValue()))
                    .append("\n");
            }
            for (Map.Entry<String,List<String>> entry : file1.entrySet())
            {
                writer.append(entry.getKey())
                        .append(" not found in this file2 ")
                        .append(" : ")
                        .append(String.join(",", entry.getValue()))
                        .append("\n");
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private Resource[] loadResources() throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath*:*.csv");
    }

    private String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder(512);
        try {
            Reader r = new InputStreamReader(is, StandardCharsets.UTF_8);
            int c = 0;
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
