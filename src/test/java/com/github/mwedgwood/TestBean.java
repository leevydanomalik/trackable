package com.github.mwedgwood;

public class TestBean implements Cloneable {

    private String fieldOne;
    private String fieldTwo;
    private TestBean fieldThree;

    public String getFieldOne() {
        return fieldOne;
    }

    public TestBean setFieldOne(String fieldOne) {
        this.fieldOne = fieldOne;
        return this;
    }

    public String getFieldTwo() {
        return fieldTwo;
    }

    public TestBean setFieldTwo(String fieldTwo) {
        this.fieldTwo = fieldTwo;
        return this;
    }

    public TestBean getFieldThree() {
        return fieldThree;
    }

    public TestBean setFieldThree(TestBean fieldThree) {
        this.fieldThree = fieldThree;
        return this;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
