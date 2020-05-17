package cn.xa.common.tcc;

/**
 * Created by sanyu on 2020/5/15.
 */
public interface TccConfig {
    long TRANSACTION_TIMEOUT_MS = 30000;

    String COLLABORATION_TCC_URL = "http://localhost:8088/v1/collaboration/tcc/%s/%d/%d/%d/%d";
    String TRACKING_TCC_URL = "http://localhost:8087/v1/tracking/tcc/%s/%d/%d";
    String TASK_TCC_URL = "http://localhost:8086/v1/task/tcc/%s/%d/%d";
    String RATING_TCC_URL = "http://localhost:8085/v1/rating/tcc/%s";
}
