package au.com.cba.omnia.dataproducts.card.transform;

import java.sql.*;

public class JavaMysqlSelectExample
{
    private String jobNameField = "jobname";
    private String countFieldName = "newcount";
    private String jobIdField = "jobid";

    public String getInputQuery() {
        return inputQuery;
    }

    public void setInputQuery(String inputQuery) {
        this.inputQuery = inputQuery;
    }

    // Assumption: input Query is a Select query with only two columns in the select with names `jobNameField`, `countFieldName`
    private String inputQuery = null;

    private String getJobIdQuery(String jobName) {
        String query = "SELECT "+ jobIdField + " FROM table2 WHERE jobname = '"+ jobName + "'";
        return query;
    }

    public JavaMysqlSelectExample(
            String jobNameField,
            String countFieldName,
            String jobIdField,
            String inputQuery

    ) {
        this.jobNameField = jobNameField;
        this.countFieldName = countFieldName;
        this.jobIdField = jobIdField;
        this.inputQuery = inputQuery;
    }

    public JavaMysqlSelectExample(String inputQuery){
        this.inputQuery = inputQuery;
    }

    // Assumption: Result set has only one row and output of `inputQuery`
    public JobDetails getJobDetails(ResultSet resultSet) throws Exception {
        try {
            // set pointer to first row
            resultSet.next();
            String jobName = resultSet.getString(jobNameField);
            Integer jobCount =  resultSet.getInt(countFieldName);

            JobDetails jobDetails = new JobDetails();
            jobDetails.setJobName(jobName);
            jobDetails.setJobCount(jobCount);

            return jobDetails;

        } catch (Exception e) {
            throw  new Exception("Unexpected behaviour in getJobDetails, expecting a valid ResultSet with only one row which is output of '"+ inputQuery + "'", e);
        }
    }

    public JobDetails setJobId(ResultSet resultSet, JobDetails jobDetails) throws Exception {
            try {
                // set pointer to first row
                resultSet.next();
                String jobId = resultSet.getString(jobIdField);

                jobDetails.setJobId(jobId);

                return jobDetails;

            } catch (Exception e) {
                throw  new Exception("Unexpected behaviour in setJobId, expecting a valid ResultSet with only one row which is output of '"+ inputQuery + "'", e);
            }
    }

    public String getInsertQuery(JobDetails jd){
       return  "INSERT INTO table3 (job_id, count, job_name, load_date, status) VALUES ("
                + jd.getJobId()
                + ", "
                + jd.getJobCount()
                + ", "
                + jd.getJobName()
                + ", "
                + jd.getLoadDate()
                + ", "
                + jd.getStatus()
                + ")" ;
    }

    public static void main(String[] args)
    {

        try
        {
            // You could make all these parameter driven.

            // replace it with your DB driver
            String myDriver = "org.gjt.mm.mysql.Driver";
            // replace it with your connection string
            String myUrl = "jdbc:mysql://localhost/test";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl);
            Statement stmt = conn.createStatement();

            String inputQuery = "SELECT COUNT(*) as newcount, jobname from table1";

            //stmt.executeUpdate( "INSERT INTO MyTable( name ) VALUES ( 'my name' ) " );

            JavaMysqlSelectExample mySql = new JavaMysqlSelectExample(inputQuery);
            // Get job name and count
            ResultSet jobDetailsRs =  stmt.executeQuery(mySql.getInputQuery());
            JobDetails jd = mySql.getJobDetails(jobDetailsRs);

            // Get job id
            ResultSet jobIdRs =  stmt.executeQuery(mySql.getJobIdQuery(jd.getJobName()));
            mySql.setJobId(jobIdRs, jd);

            // Set extra details here
            // jd.setLoadDate();
            // jd.setStatus();
            String inserQuery  = mySql.getInsertQuery(jd);
            stmt.executeUpdate(inputQuery);
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}
