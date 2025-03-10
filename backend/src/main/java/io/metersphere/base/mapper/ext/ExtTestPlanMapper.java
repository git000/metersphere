package io.metersphere.base.mapper.ext;

import io.metersphere.api.dto.definition.ParamsDTO;
import io.metersphere.base.domain.TestPlan;
import io.metersphere.track.dto.TestPlanDTO;
import io.metersphere.track.dto.TestPlanDTOWithMetric;
import io.metersphere.track.request.testcase.QueryTestPlanRequest;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExtTestPlanMapper {
    List<TestPlanDTOWithMetric> list(@Param("request") QueryTestPlanRequest params);

    List<TestPlanDTO> planList(@Param("request") QueryTestPlanRequest params);

    List<TestPlanDTO> selectByIds(@Param("list") List<String> ids);

    /**
     * 通过关联表(test_plan_api_case/test_plan_api_scenario)查询testPlan
     *
     * @param params
     * @return
     */
    List<TestPlanDTO> selectTestPlanByRelevancy(@Param("request") QueryTestPlanRequest params);

    int checkIsHave(@Param("planId") String planId, @Param("projectIds") Set<String> projectIds);

    String findTestProjectNameByTestPlanID(String testPlanId);

    String findScheduleCreateUserById(String testPlanId);

    List<String> findIdByPerformanceReportId(String reportId);

    List<TestPlan> listRecent(@Param("userId") String userId, @Param("projectId") String currentProjectId);

    int updateActualEndTimeIsNullById(String testPlanID);

    @MapKey("id")
    Map<String, ParamsDTO> testPlanTestCaseCount(@Param("planIds")Set<String> planIds);

    @MapKey("id")
    Map<String, ParamsDTO> testPlanApiCaseCount(@Param("planIds")Set<String> planIds);

    @MapKey("id")
    Map<String, ParamsDTO> testPlanApiScenarioCount(@Param("planIds")Set<String> planIds);

    @MapKey("id")
    Map<String, ParamsDTO> testPlanLoadCaseCount(@Param("planIds")Set<String> planIds);

    @MapKey("id")
    Map<String, ParamsDTO> testPlanUiScenarioCount(@Param("planIds")Set<String> planIds);
}
