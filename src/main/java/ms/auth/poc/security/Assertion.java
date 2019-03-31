package ms.auth.poc.security;

public class Assertion {
    private Boolean success;

    public Assertion(){}
    public Assertion(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
