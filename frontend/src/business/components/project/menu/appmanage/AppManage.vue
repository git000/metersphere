<template>
  <ms-container>
    <ms-main-container>
      <div v-loading="result.loading">
        <el-card class="table-card">
          <el-tabs v-model="activeName" style="height: 600px">
            <el-tab-pane :label="$t('commons.my_workstation')" name="my_workstation">
              <el-row style="margin-top: 10px">
                <span style="font-weight:bold">{{ this.$t('commons.enable_settings') }}</span>
              </el-row>
              <el-row style="margin-top: 15px">
                <app-manage-item
                  :title="$t('workstation.upcoming')+'-'+$t('commons.pending_upgrade')+$t('test_track.case.list')"
                  v-if="isXpack">
                  <template #append>
                    <el-button type="text" @click="openRuleSetting">
                      {{ $t('commons.setting') + $t('commons.rule') }}
                    </el-button>
                  </template>
                </app-manage-item>
              </el-row>
            </el-tab-pane>

            <el-tab-pane :label="$t('test_track.test_track')" name="test_track">
              <el-row style="margin-top: 10px">
                <span style="font-weight:bold">{{ this.$t('commons.enable_settings') }}</span>
              </el-row>
              <el-row style="margin-top: 15px">
                <app-manage-item :title="$t('project.public')" :description="$t('project.public_info')"
                                 v-if="isXpack">
                  <template #append>
                    <el-switch v-model="config.casePublic" @change="switchChange('CASE_PUBLIC', $event)"></el-switch>
                  </template>
                </app-manage-item>

                <app-manage-item :title="$t('project.test_case_custom_id')"
                                 :description="$t('project.test_case_custom_id_info')">
                  <template #append>
                    <el-switch v-model="config.caseCustomNum"
                               @change="switchChange('CASE_CUSTOM_NUM', $event)"></el-switch>
                  </template>
                </app-manage-item>
                <timing-item ref="trackTimingItem" :choose.sync="config.cleanTrackReport"
                             :expr.sync="config.cleanTrackReportExpr"
                             @chooseChange="switchChange('CLEAN_TRACK_REPORT', config.cleanTrackReport, ['CLEAN_TRACK_REPORT_EXPR', config.cleanTrackReportExpr])"
                             :title="$t('project.timing_clean_plan_report')"/>
                <timing-item ref="trackTimingItem" :choose.sync="config.shareReport"
                             :expr.sync="config.trackShareReportTime" :share-link="true"
                             :unit-options="applyUnitOptions"
                             @chooseChange="switchChange('TRACK_SHARE_REPORT_TIME', config.trackShareReportTime)"
                             :title="$t('report.report_sharing_link')"/>
              </el-row>
            </el-tab-pane>

            <el-tab-pane :label="$t('commons.api')" name="api">
              <el-row :gutter="20">
                <el-col :span="8" class="commons-api-enable">
                  <el-row style="margin-top: 10px">
                    <span style="font-weight:bold">{{ $t('commons.enable_settings') }}</span>
                  </el-row>
                  <el-row style="margin-top: 15px">
                    <app-manage-item :title="$t('project.repeatable')" :description="$t('project.repeatable_info')">
                      <template #append>
                        <el-switch v-model="config.urlRepeatable"
                                   @change="switchChange('URL_REPEATABLE', $event)"></el-switch>
                      </template>
                    </app-manage-item>
                    <app-manage-item :title="$t('project.scenario_custom_id')"
                                     :description="$t('project.scenario_custom_id_info')">
                      <template #append>
                        <el-switch v-model="config.scenarioCustomNum"
                                   @change="switchChange('SCENARIO_CUSTOM_NUM', $event)"></el-switch>
                      </template>
                    </app-manage-item>
                    <app-manage-item :title="'TCP Mock Port'" :prepend-span="8" :middle-span="12" :append-span="4">
                      <template #middle>
                        <el-input-number v-model="config.mockTcpPort" :controls="false" size="medium"
                                         :disabled="config.mockTcpOpen"></el-input-number>
                      </template>
                      <template #append>
                        <el-switch v-model="config.mockTcpOpen"
                                   @change="tcpMockSwitchChange($event, ['MOCK_TCP_PORT', config.mockTcpPort])"></el-switch>
                      </template>
                    </app-manage-item>
                    <timing-item ref="apiTimingItem" :choose.sync="config.cleanApiReport"
                                 :expr.sync="config.cleanApiReportExpr"
                                 @chooseChange="switchChange('CLEAN_API_REPORT', config.cleanApiReport, ['CLEAN_API_REPORT_EXPR', config.cleanApiReportExpr])"
                                 :title="$t('project.timing_clean_api_report')"/>
                    <timing-item ref="trackTimingItem" :choose.sync="config.shareReport"
                                 :expr.sync="config.apiShareReportTime" :share-link="true"
                                 :unit-options="applyUnitOptions"
                                 @chooseChange="switchChange('API_SHARE_REPORT_TIME', config.apiShareReportTime)"
                                 :title="$t('report.report_sharing_link')"/>
                  </el-row>
                </el-col>
                <el-col :span="8" class="commons-view-setting">
                  <el-row style="margin-top: 10px">
                    <span style="font-weight:bold">{{ $t('commons.view_settings') }}</span>
                  </el-row>
                  <el-row style="margin-top: 15px">
                    <app-manage-item :title="$t('api_test.definition.api_quick_button')"
                                     :append-span="12" :prepend-span="12" :middle-span="0">
                      <template #append>
                        <el-radio-group v-model="config.apiQuickMenu" @change="switchChange('API_QUICK_MENU', $event)">
                          <el-radio label="debug" value="debug">
                            {{ $t('api_test.definition.request.fast_debug') }}
                          </el-radio>
                          <el-radio label="api" value="api">
                            {{ $t('api_test.definition.request.title') }}
                          </el-radio>
                        </el-radio-group>
                      </template>
                    </app-manage-item>
                  </el-row>
                </el-col>
              </el-row>
            </el-tab-pane>

            <!-- UI 测试 -->
            <el-tab-pane v-if="isXpack" :label="$t('commons.ui_test')" name="ui_test">
              <el-row style="margin-top: 10px">
                <span style="font-weight:bold">{{ $t('commons.view_settings') }}</span>
              </el-row>
              <el-row style="margin-top: 15px">
                <app-manage-item :title="$t('ui.ui_debug_mode')"
                                 :append-span="12" :prepend-span="12" :middle-span="0">
                  <template #append>
                    <el-radio-group v-model="config.uiQuickMenu" @change="switchChange('UI_QUICK_MENU', $event)">
                      <el-radio label="local" value="local">
                        {{ $t('ui.ui_local_debug') }}
                      </el-radio>
                      <el-radio label="server" value="server">
                        {{ $t('ui.ui_server_debug') }}
                      </el-radio>
                    </el-radio-group>
                  </template>
                </app-manage-item>
              </el-row>
            </el-tab-pane>

            <el-tab-pane :label="$t('commons.performance')" name="performance">
              <el-row style="margin-top: 10px">
                <span style="font-weight:bold">{{ this.$t('commons.enable_settings') }}</span>
              </el-row>
              <el-row style="margin-top: 15px">
                <timing-item ref="loadTimingItem" :choose.sync="config.cleanLoadReport"
                             :expr.sync="config.cleanLoadReportExpr"
                             @chooseChange="switchChange('CLEAN_LOAD_REPORT', config.cleanLoadReport, ['CLEAN_LOAD_REPORT_EXPR', config.cleanLoadReportExpr])"
                             :title="$t('project.timing_clean_load_report')"/>
                <timing-item ref="trackTimingItem" :choose.sync="config.shareReport"
                             :expr.sync="config.performanceShareReportTime" :share-link="true"
                             :unit-options="applyUnitOptions"
                             @chooseChange="switchChange('PERFORMANCE_SHARE_REPORT_TIME', config.performanceShareReportTime)"
                             :title="$t('report.report_sharing_link')"/>
              </el-row>
            </el-tab-pane>

          </el-tabs>
        </el-card>
        <el-dialog
          :title="$t('commons.rule') + $t('commons.setting')"
          :visible.sync="showRuleSetting"
          width="720px"
        >
          <div style="margin-top: -25px; margin-left: -20px; width:720px; height:1px; background:#DCDFE6;"></div>
          <el-row style="margin-top: 15px">
            <div class="timeClass">
              <span>{{ $t('api_test.request.time') + $t('commons.setting') }}</span>
              <el-switch v-model="config.openUpdateTime"
                         @change="setSyncTime"></el-switch>
            </div>
          </el-row>
          <el-row v-if="showSyncTimeSetting" style="margin-top: 15px">
            <div class="setTimeClass">
              <span>{{ $t('workstation.past') }}</span>
              <el-select style="margin-left: 5px" v-model="pastQuantity" placeholder=" " size="mini" filterable
                         default-first-option
                         allow-create
                         class="timing_select">
                <el-option
                  v-for="item in applyQuantityOptions"
                  :key="item"
                  :label="item"
                  :value="item">
                </el-option>
              </el-select>
              <el-select style="margin-left: 5px" v-model="pastUnit" placeholder=" " size="mini"
                         class="timing_select">
                <el-option
                  v-for="item in applyUnitOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
          </el-row>
          <el-row style="margin-top: 15px">
            <span>{{
                $t('commons.pending_upgrade') + $t('api_test.request.condition') + $t('commons.setting')
              }}</span>
          </el-row>
          <div style="margin-top: 15px" class="setApiClass">
            <span>{{ $t('workstation.api_change') + $t('commons.setting') }}</span>
            <el-row>
              <el-col :span="4">{{ $t('api_test.mock.base_info') + ":" }}</el-col>
              <el-col :span="20" style="color: #783887">
                <el-checkbox v-model="apiSyncCaseRequest.protocol" disabled>{{
                    $t('api_report.request') + $t('api_test.request.protocol')
                  }}
                </el-checkbox>
                <el-checkbox v-model="apiSyncCaseRequest.method" disabled>
                  {{ $t('api_test.definition.document.request_method') + '\xa0\xa0\xa0\xa0\xa0' }}
                </el-checkbox>
                <el-checkbox v-model="apiSyncCaseRequest.path" disabled>{{ "URL" }}</el-checkbox>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="4">{{ $t('api_test.mock.req_param') + ":" }}</el-col>
              <el-col :span="20" style="color: #783887">
                <el-checkbox v-model="apiSyncCaseRequest.headers">{{ "Header" + '\xa0\xa0' }}</el-checkbox>
                <el-checkbox v-model="apiSyncCaseRequest.query">{{
                    $t('api_test.definition.request.query_param')
                  }}
                </el-checkbox>
                <el-checkbox v-model="apiSyncCaseRequest.rest">{{
                    $t('api_test.definition.request.rest_param')
                  }}
                </el-checkbox>
                <el-checkbox v-model="apiSyncCaseRequest.body">{{ $t('api_test.request.body') }}</el-checkbox>
              </el-col>
            </el-row>
            <span>{{ $t('commons.track') + $t('commons.setting') }}</span>
            <el-row>
              <el-col :span="4">{{ $t('project.code_segment.result') + ":" }}</el-col>
              <el-col :span="20" style="color: #783887">
                <el-checkbox v-model="apiSyncCaseRequest.failed">{{ $t('schedule.event_failed') }}</el-checkbox>
                <el-checkbox v-model="apiSyncCaseRequest.unExecute">{{ $t('api_test.home_page.detail_card.unexecute') }}
                </el-checkbox>
              </el-col>
            </el-row>
          </div>
          <span slot="footer" class="dialog-footer">
            <el-button @click="showRuleSetting = false">取 消</el-button>
            <el-button type="primary" @click="saveSync">确 定</el-button>
          </span>
        </el-dialog>
      </div>
    </ms-main-container>
  </ms-container>
</template>


<script>

import MsContainer from "@/business/components/common/components/MsContainer";
import MsMainContainer from "@/business/components/common/components/MsMainContainer";

import {getCurrentProjectID, hasLicense,} from "@/common/js/utils";
import AppManageItem from "@/business/components/project/menu/appmanage/AppManageItem";
import TimingItem from "@/business/components/project/menu/appmanage/TimingItem";

export default {
  name: "appManage",
  components: {
    TimingItem,
    AppManageItem,
    MsMainContainer,
    MsContainer
  },
  data() {
    return {
      activeName: 'test_track',
      form: {
        cleanTrackReport: false,
        cleanTrackReportExpr: "",
        cleanApiReport: false,
        cleanApiReportExpr: "",
        cleanLoadReport: false,
        cleanLoadReportExpr: ""
      },
      count: 0,
      isXpack: false,
      result: {},
      quantity: "",
      unit: "",
      choose: false,
      applyUnitOptions: [
        {value: "H", label: this.$t('commons.date_unit.hour')},
        {value: "D", label: this.$t('commons.date_unit.day')},
        {value: "M", label: this.$t('commons.date_unit.month')},
        {value: "Y", label: this.$t('commons.date_unit.year')},
      ],
      applyQuantityOptions: [
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
        "31"
      ],
      config: {
        trackShareReportTime: "",
        performanceShareReportTime: "",
        apiShareReportTime: "",
        caseCustomNum: false,
        scenarioCustomNum: false,
        apiQuickMenu: "",
        uiQuickMenu: "server",
        casePublic: false,
        mockTcpPort: 0,
        mockTcpOpen: false,
        cleanTrackReport: false,
        cleanTrackReportExpr: "",
        cleanApiReport: false,
        cleanApiReportExpr: "",
        cleanLoadReport: false,
        cleanLoadReportExpr: "",
        urlRepeatable: false,
        shareReport: true,
        openUpdateTime: false,
        openUpdateRuleTime: "",
        triggerUpdate: "",
      },
      showRuleSetting: false,
      showSyncTimeSetting: false,
      apiSyncCaseRequest: {},
      pastQuantity: '',
      pastUnit: ''
    };
  },
  created() {
    this.init();
    this.isXpack = !!hasLicense();
  },
  computed: {
    projectId() {
      return getCurrentProjectID();
    },
  },
  methods: {
    tcpMockSwitchChange(value, other) {
      if (value && this.config.mockTcpPort === 0) {
        this.$get('/project/genTcpMockPort/' + this.projectId).then(res => {
          let port = res.data.data;
          this.config.mockTcpPort = port;
          this.$nextTick(() => {
            this.switchChange("MOCK_TCP_OPEN", value, ['MOCK_TCP_PORT', this.config.mockTcpPort]);
          })
        }).catch(resp => {
          this.config.mockTcpOpen = false;
          if (resp.response && resp.response.data && resp.response.data.message) {
            this.$error(resp.response.data.message);
          }
        });
      } else {
        this.switchChange("MOCK_TCP_OPEN", value, other);
      }
    },
    switchChange(type, value, other) {
      let configs = [];
      if (other && value) {
        // 在开启开关时需要保存的其它信息
        configs.push({projectId: this.projectId, typeValue: other[1], type: other[0]});
      }
      // 开关信息在最后保存
      // 后台按照顺序先校验其它数据合法性，如tcp端口合法性，合法后保存是否开启
      configs.push({projectId: this.projectId, typeValue: value, type});
      let params = {configs};
      this.startSaveData(params)
    },
    startSaveData(params) {
      this.$post("/project_application/update/batch", params).then(() => {
        this.$success(this.$t('commons.save_success'));
        this.init();
      }).catch(resp => {
        this.init();
        if (resp.response && resp.response.data && resp.response.data.message) {
          this.$error(resp.response.data.message);
        }
      });
    },
    init() {
      this.$get('/project_application/get/config/' + this.projectId, res => {
        if (res.data) {
          this.config = res.data;
          this.config.shareReport = true;
          if (!this.config.uiQuickMenu) {
            this.config.uiQuickMenu = "server";
          }
          if (this.config.triggerUpdate) {
            this.apiSyncCaseRequest = JSON.parse(this.config.triggerUpdate);
          }
          if (this.config.openUpdateRuleTime) {
            this.pastUnit = this.config.openUpdateRuleTime.substring(this.config.openUpdateRuleTime.length - 1);
            this.pastQuantity = this.config.openUpdateRuleTime.substring(0, this.config.openUpdateRuleTime.length - 1);
            if (this.config.openUpdateTime) {
              this.showSyncTimeSetting = true;
            }
          }
        }
      });
    },
    openRuleSetting() {
      this.showRuleSetting = true;
      if (!this.apiSyncCaseRequest) {
        this.apiSyncCaseRequest = {
          protocol: true,
          method: true,
          path: true,
          headers: true,
          query: true,
          rest: true,
          body: true,
          failed: true,
          unExecute: true
        }
      }
      this.apiSyncCaseRequest.protocol = true;
      this.apiSyncCaseRequest.method = true;
      this.apiSyncCaseRequest.path = true;
    },
    setSyncTime() {
      this.showSyncTimeSetting = !this.showSyncTimeSetting;
    },
    saveSync() {
      let configs = [];
      if (this.showSyncTimeSetting) {
        if (!this.pastQuantity) {
          this.$message.error("请选择时间")
        }
        if (!this.pastUnit) {
          this.$message.error("请选择时间单位")
        }
        this.config.openUpdateRuleTime = this.pastQuantity + this.pastUnit;
        configs.push({
          projectId: this.projectId,
          typeValue: this.config.openUpdateRuleTime,
          type: 'OPEN_UPDATE_RULE_TIME'
        });
      }
      configs.push({projectId: this.projectId, typeValue: this.config.openUpdateTime, type: 'OPEN_UPDATE_TIME'});
      configs.push({
        projectId: this.projectId,
        typeValue: JSON.stringify(this.apiSyncCaseRequest),
        type: 'TRIGGER_UPDATE'
      });
      let params = {configs};
      this.startSaveData(params)
      this.showRuleSetting = false
    }
  }
};
</script>

<style scoped>
@media only screen and (max-width: 1425px) {
  .commons-api-enable {
    width: 100%;
  }
}

@media only screen and (min-width: 1426px) {
  .commons-view-setting {
    margin-left: 200px;
  }
}

.timeClass {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.setTimeClass {
  border-radius: 2px;
  width: 680px;
  height: 35px;
  border: 1px solid #DCDFE6;
  padding-bottom: 6px;
  padding-top: 10px;
}

.setApiClass {
  border-radius: 2px;
  width: 680px;
  border: 1px solid #DCDFE6;
  padding-bottom: 6px;
  padding-top: 10px;
  padding-left: 5px;
}
</style>
