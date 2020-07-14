package mycompany.service;

import mycompany.processor.ExportData2CSV;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

@Service
public class QueryDataServiceImpl implements QueryDataService{
    private SessionFactory sf;
    private ExportData2CSV csv;
    private ResourceLoader resourceLoader;
    private String pdn;
    @Value("${csv.file.location}")
    private String csvPath;

    public QueryDataServiceImpl(SessionFactory sf,ExportData2CSV csv,ResourceLoader resourceLoader) {
        this.sf = sf;
        this.csv = csv;
        this.resourceLoader = resourceLoader;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,readOnly = true)
    public void getRecords(String columns,String fileName) {
        getSession().doReturningWork(new ReturningWork<Void>() {
            @Override
            public Void execute(Connection conn) throws SQLException {
                try (
                        Statement stmt = conn.createStatement();
                        ResultSet rset = stmt.executeQuery("select "+columns+" from dimo."+fileName))
                {
                    saveRecordsAsCSV(rset,fileName);
                }
                catch (SQLException | IOException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.close();
                    }
                }
                return null;
            }
        });
    }

    @Override
    public void readTextFileTableRecords() throws IOException {

        Resource[] resource = this.loadResources("classpath*:*/*.data");
        for(Resource r:resource){
            File columnFile = r.getFile();
            String fn = columnFile.getName();
            InputStream is = new FileInputStream(columnFile);
            pdn  = columnFile.getParentFile().getName();
            String columns = readStream(is);
            getRecords(columns,fn.substring(0,fn.length()-5));}
    }

    private void saveRecordsAsCSV(ResultSet rset,String fileName) throws IOException, SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String ts = sdf.format(new Timestamp(System.currentTimeMillis()));
        csv.ExportData2CSV(rset,csvPath+pdn+"_"+fileName+"_"+ts+".csv",true,",");
        csv.createFileCsv();
    }

    private Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }

    private Session getSession(){
        Session session;
        try {
            session = sf.getCurrentSession();
        } catch (HibernateException e) {
            session = sf.openSession();
        }
        return session;
    }

    private String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder(512);
        try {
            Reader r = new InputStreamReader(is, "UTF-8");
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
