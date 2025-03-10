package io.metersphere.base.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class ApiTestCase implements Serializable {
    private String id;

    private String projectId;

    private String name;

    private String priority;

    private String apiDefinitionId;

    private String createUserId;

    private String updateUserId;

    private Long createTime;

    private Long updateTime;

    private Integer num;

    private String tags;

    private String lastResultId;

    private String status;

    private String originalStatus;

    private Long deleteTime;

    private String deleteUserId;

    private Integer version;

    private Long order;

    private String caseStatus;

    private String versionId;

    private Boolean toBeUpdated;

    private Long toBeUpdateTime;

    private static final long serialVersionUID = 1L;
}