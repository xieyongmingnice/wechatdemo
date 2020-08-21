package com.blue.wechatdemo.model.resp;

/**
 * @Author liusc
 * @Date 2019/7/24
 * @Description 返回给前台的单个的结果类
 */
public class SingleDataResult<T> {

    /**
     * 结果信息
     */
    private String message;

    /**
     * 结果码
     */
    private String code;

    /**
     * 数据总条数
     */
    private Integer dataNum;
    /**
     * 结果
     */
    private T result;

    /**
     * 扩展字段1
     *
     * @return
     */
    private String firstExtendColumn;

    /**
     * 扩展字段2
     *
     * @return
     */
    private String secondExtendColumn;

    /**
     * 扩展字段3
     *
     * @return
     */
    private String thirdExtendColumn;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDataNum() {
        return dataNum;
    }

    public void setDataNum(Integer dataNum) {
        this.dataNum = dataNum;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getFirstExtendColumn() {
        return firstExtendColumn;
    }

    public void setFirstExtendColumn(String firstExtendColumn) {
        this.firstExtendColumn = firstExtendColumn;
    }

    public String getSecondExtendColumn() {
        return secondExtendColumn;
    }

    public void setSecondExtendColumn(String secondExtendColumn) {
        this.secondExtendColumn = secondExtendColumn;
    }

    public String getThirdExtendColumn() {
        return thirdExtendColumn;
    }

    public void setThirdExtendColumn(String thirdExtendColumn) {
        this.thirdExtendColumn = thirdExtendColumn;
    }

    @Override
    public String toString() {
        return "SingleDataResult{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", result=" + result +
                '}';
    }
}
