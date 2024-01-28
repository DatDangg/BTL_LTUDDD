
package com.example.BTL.Domain;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Metadata {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
