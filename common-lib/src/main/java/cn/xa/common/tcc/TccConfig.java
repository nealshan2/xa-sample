package cn.xa.common.tcc;

/**
 * Created by sanyu on 2020/5/15.
 */
public interface TccConfig {
    long TRANSACTION_TIMEOUT_MS = 30000;

    String COLLABORATION_TCC_URL = "http://localhost:8088/v1/collaboration/tcc/%s";
    String TRACKING_TCC_URL = "http://localhost:8087/v1/tracking/tcc/%s";
    String TASK_TCC_URL = "http://localhost:8086/v1/task/tcc/%s";
}
