package au.com.cba.omnia.dataproducts.card.transform;

public class JobDetails {

    public JobDetails(){

    }

    private String jobName =  null;
    private Integer jobCount = null;
    private String jobId = null;
    private String loadDate = null;
    private String status = null;

    public String getJobName() {
        return jobName;
    }

    public String getJobId() {
        return jobId;
    }

    public String getLoadDate() {
        return loadDate;
    }

    public String getStatus() {
        return status;
    }

    public Integer getJobCount() {
        return jobCount;
    }

    public void setJobCount(Integer jobCount) {
        this.jobCount = jobCount;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setLoadDate(String loadDate) {
        this.loadDate = loadDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
