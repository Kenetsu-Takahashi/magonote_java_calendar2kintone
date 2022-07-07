package com.magonote.csv;

import com.ictlab.csv.Reader;
import com.magonote.kintone.OperationSchedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * スケジュールデータファイル操作クラス
 */
public class ScheduleFile {
    /**
     * データ
     */
    private List<String[]> dataList = null;

    /**
     * データインデックマップ
     */
    private Map<String, Integer> indexMap = new HashMap<>();

    /**
     * 施術者IDマップ
     */
    private Map<String, String> operatorIdMap;

    /**
     * 患者様IDマップ
     */
    private Map<String, String> patientIdMap;

    /**
     * @param fileName      ファイル名
     * @param operatorIdMap 施術者IDマップ
     * @param patientIdMap  患者様IDマップ
     */
    public ScheduleFile(String fileName, Map<String, String> operatorIdMap, Map<String, String> patientIdMap) {
        this.operatorIdMap = operatorIdMap;
        this.patientIdMap = patientIdMap;

        Reader reader = new Reader();

        if (reader.parse(fileName)) {
            this.dataList = reader.getDataList();
        }

        Integer index = 0;

        this.indexMap.put("start", index++);
        this.indexMap.put("end", index++);
        this.indexMap.put("allday", index++);
        this.indexMap.put("patientid", index++);
        this.indexMap.put("operatorid", index++);
        this.indexMap.put("address", index++);
        this.indexMap.put("comment", index++);
        this.indexMap.put("status", index++);
    }

    /**
     * スケジュールリスト取得
     *
     * @return スケジュールリスト
     */
    public List<OperationSchedule.Schedule> getScheduleList() {
        List<OperationSchedule.Schedule> schedules = new ArrayList<>();

        this.dataList.stream().forEach(row -> {
            OperationSchedule.Schedule schedule = new OperationSchedule.Schedule();

            for (String key : indexMap.keySet()) {
                switch (key) {
                    case "start":
                        schedule.startTime = row[this.indexMap.get(key)];
                        break;
                    case "end":
                        schedule.endTime = row[this.indexMap.get(key)];
                        break;
                    case "allday":
                        schedule.allDay = row[this.indexMap.get(key)];
                        break;
                    case "patientid": {
                        String id = row[this.indexMap.get(key)];
                        schedule.patientLookupId = this.patientIdMap.get(id);
                    }
                    break;
                    case "operatorid": {
                        String id = row[this.indexMap.get(key)];
                        schedule.operatorLookupId = this.operatorIdMap.get(id);
                    }
                    break;
                    case "address":
                        schedule.address = row[this.indexMap.get(key)];
                        break;
                    case "comment":
                        schedule.content = row[this.indexMap.get(key)];
                        break;
                    case "status":
                        schedule.status = row[this.indexMap.get(key)];
                        break;
                    default:
                        break;
                }
            }

            schedules.add(schedule);
        });

        return schedules;
    }

    /**
     * 最初の施術者取得
     *
     * @return 最初の施術者ID
     */
    public String getFirstOperator() {
        String[] row = this.dataList.get(0);

        String operatorId = row[this.indexMap.get("operatorid")];

        return operatorId;
    }
}
