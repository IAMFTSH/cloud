package learn.cloud.shop.response;

/**
 * @Author 吴典秋
 * @Date 2020/5/25 15:33
 * @Description: 响应码枚举
 * @Version 1.0
 */

public enum ResultCode {
    SUCCESS(0, "操作成功"),
    FAIL(-1,"操作失败"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),

    /* 用户错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录，请先登录"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),

    PERMISSION_TOKEN_MALFORMEDJWT_EXCEPTION(70003, "json web token格式错误"),
    PERMISSION_TOKEN_EXPIRED_EXCEPTION(70004,"token已过期"),
    PERMISSION_TOKEN_INVALID(70006,"token已失效"),
    PERMISSION_SIGNATURE_ERROR(70007,"签名失败");

    int code;

    String message;

    ResultCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
