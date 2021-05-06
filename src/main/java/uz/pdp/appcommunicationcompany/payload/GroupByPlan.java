package uz.pdp.appcommunicationcompany.payload;

import lombok.Data;

@Data
public class GroupByPlan {
    private Integer planId;
    private Long count;
    private String planName;
}
