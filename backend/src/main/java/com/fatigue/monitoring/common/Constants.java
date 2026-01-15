package com.fatigue.monitoring.common;

/**
 * 系统常量
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
public class Constants {

    /**
     * UTF-8编码
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer ERROR = 500;

    /**
     * 登录用户Redis Key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码Redis Key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 登录用户权限缓存
     */
    public static final String USER_PERMISSIONS_KEY = "user_permissions:";

    /**
     * 登录用户角色缓存
     */
    public static final String USER_ROLES_KEY = "user_roles:";

    /**
     * 设备状态-正常
     */
    public static final Integer DEVICE_STATUS_NORMAL = 1;

    /**
     * 设备状态-异常
     */
    public static final Integer DEVICE_STATUS_ABNORMAL = 2;

    /**
     * 设备状态-维护中
     */
    public static final Integer DEVICE_STATUS_MAINTENANCE = 3;

    /**
     * 任务状态-待处理
     */
    public static final Integer TASK_STATUS_PENDING = 1;

    /**
     * 任务状态-进行中
     */
    public static final Integer TASK_STATUS_IN_PROGRESS = 2;

    /**
     * 任务状态-已完成
     */
    public static final Integer TASK_STATUS_COMPLETED = 3;

    /**
     * 任务状态-已取消
     */
    public static final Integer TASK_STATUS_CANCELLED = 4;

    /**
     * 告警级别-低
     */
    public static final Integer ALARM_LEVEL_LOW = 1;

    /**
     * 告警级别-中
     */
    public static final Integer ALARM_LEVEL_MEDIUM = 2;

    /**
     * 告警级别-高
     */
    public static final Integer ALARM_LEVEL_HIGH = 3;

    /**
     * 告警级别-紧急
     */
    public static final Integer ALARM_LEVEL_CRITICAL = 4;

    /**
     * 告警状态-未处理
     */
    public static final Integer ALARM_STATUS_UNHANDLED = 1;

    /**
     * 告警状态-处理中
     */
    public static final Integer ALARM_STATUS_HANDLING = 2;

    /**
     * 告警状态-已处理
     */
    public static final Integer ALARM_STATUS_HANDLED = 3;

    /**
     * 告警状态-已忽略
     */
    public static final Integer ALARM_STATUS_IGNORED = 4;

    /**
     * 用户状态-正常
     */
    public static final Integer USER_STATUS_NORMAL = 1;

    /**
     * 用户状态-禁用
     */
    public static final Integer USER_STATUS_DISABLED = 0;

    /**
     * 性别-男
     */
    public static final String SEX_MALE = "0";

    /**
     * 性别-女
     */
    public static final String SEX_FEMALE = "1";

    /**
     * 是否-是
     */
    public static final String YES = "Y";

    /**
     * 是否-否
     */
    public static final String NO = "N";

    /**
     * 管理员ID
     */
    public static final Long ADMIN_ID = 1L;

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Token请求头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 分页大小
     */
    public static final Integer PAGE_SIZE = 10;

    /**
     * 最大分页大小
     */
    public static final Integer MAX_PAGE_SIZE = 100;

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌有效期（天）
     */
    public static final Integer TOKEN_EXPIRATION = 7;

    /**
     * 登录失败锁定时间（分钟）
     */
    public static final Integer LOGIN_LOCK_TIME = 10;

    /**
     * 登录失败最大次数
     */
    public static final Integer LOGIN_MAX_FAIL_COUNT = 5;

    private Constants() {
    }
}
