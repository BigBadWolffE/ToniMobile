package co.crowde.toni.base;

/**
 * Created by WayangForce on 11/14/17.
 */

public class BaseResponse {

    private MetaBean meta;

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public static class MetaBean {
        /**
         * code : 200
         * status : Success
         * message : Login success
         */

        private int code;
        private String status;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
