package cn.xa;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    /**
     * Override for better performance
     * @return Throwable
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
