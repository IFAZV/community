package com.nowcoder.community.entity;

//
//用于封装分页相关的信息
//
public class Page {

    //当前页码,有默认值
    private int current = 1;

    //显示上限，有默认值
    private int limit = 10;

    //数据总数，用于计算总页数，必要
    private int rows;

    //查询路径，用于复用分页链接，必要
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current >= 1) {//防止传入垃圾数
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <= 100) {//防止传入垃圾数
            this.limit = limit;
        }

    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
    *以下的方法是用于通过已有必要的值计算剩下的可计算值
    */


    /**
    *通过计算获取当前页的起始行
    *
    * @return
    */
    public int getOffset(){
        //current * limit - limit
        return (current - 1) * limit;
    }

    /**
     *通过计算获取总页数
     *
     * @return
     */
    public int getTotal(){
        //rows / limit [+1] 判断能否整除，不能则多出一页
        int total;
        if(rows % limit == 0) {
            total = rows / limit;
        } else {
            total = rows / limit + 1;
        }
        return total;
    }

    //分页显示从第m页到第n页
    //获取分页起始页码
    public int getFrom(){
        int from = current - 2;
        return (from < 1) ? 1 : from;
    }

    //获取结束页码
    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return (to > total) ? total : to;
    }

}
