package com.magonote.kintone;

import com.kintone.client.model.record.*;
import com.magonote.environment.Config;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 施術スケジュール
 */
public class OperationSchedule extends com.ictlab.kintone.Base {
    /**
     * 情報クラス
     */
    public static class Schedule {
        public String startTime;
        public String endTime;
        public String allDay;
        public String operatorLookupId;
        public String patientLookupId;
        public String address;
        public String content;
        public String status;
    }

    /**
     * 登録用フィールド
     */
    private String[] fields = {
            "開始日時",
            "終了日時",
            "患者様",
            "施術者",
            "施術場所住所",
            "終日",
            "施術内容",
            "状況"
    };

    /**
     * Constructor
     */
    public OperationSchedule() {
        super(0);
        this.appId = Config.getInstance().getApps().get("schedule");
    }

    /**
     * スケジュール登録
     *
     * @param schedules 情報リスト
     * @return true/false
     */
    public boolean write(List<Schedule> schedules) {
        final List<Record> records = new ArrayList<>();

        schedules.stream().forEach(x -> {
            final Record record = new Record();

            for (String field : this.fields) {
                switch (field) {
                    case "開始日時": {
                        final ZonedDateTime dateTime = Helper.getZonedDateTime(x.startTime);
                        record.putField(field, new DateTimeFieldValue(dateTime));
                    }
                    break;
                    case "終了日時": {
                        final ZonedDateTime dateTime = Helper.getZonedDateTime(x.endTime);
                        record.putField(field, new DateTimeFieldValue(dateTime));
                    }
                    break;
                    case "患者様": {
                        record.putField(field, new SingleLineTextFieldValue(x.patientLookupId));
                    }
                    break;
                    case "施術者": {
                        record.putField(field, new SingleLineTextFieldValue(x.operatorLookupId));
                    }
                    break;
                    case "施術場所住所": {
                        record.putField(field, new SingleLineTextFieldValue(x.address));
                    }
                    break;
                    case "終日": {
                        final List<String> values = new ArrayList<>();

                        if (!x.allDay.isEmpty()) {
                            values.add(x.allDay);
                        }

                        record.putField(field, new CheckBoxFieldValue(values));
                    }
                    break;
                    case "施術内容": {
                        record.putField(field, new MultiLineTextFieldValue(x.content));
                    }
                    break;
                    case "状況": {
                        record.putField(field, new DropDownFieldValue(x.status));
                    }
                    break;
                    default:
                        break;
                }
            }

            records.add(record);
        });

        Optional<List<Long>> ids = this.insert(records);

        return ids.isPresent();
    }
}
