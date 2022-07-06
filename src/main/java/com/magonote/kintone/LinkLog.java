package com.magonote.kintone;

import com.google.api.client.util.DateTime;
import com.ictlab.kintone.Base;
import com.ictlab.kintone.Helper;
import com.kintone.client.model.Organization;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.record.*;
import com.magonote.environment.Config;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Calendar連携ログクラス
 */
public class LinkLog extends Base {

    /**
     * 情報保持クラス
     */
    public static class Info {
        public String office = "";
        public Integer count = 0;
    }

    /**
     * field code
     */
    private final String startField = "開始日時";
    private final String endField = "終了日時";

    /**
     * 履歴テーブルフィールド
     */
    private final String historyTableField = "履歴";

    /**
     * 事業所フィールド
     */
    private final String officeField = "事業所";

    /**
     * 件数フィールド
     */
    private final String countField = "件数";

    /**
     * Field情報マップ
     */
    private Map<String, FieldProperty> properties;

    /**
     * record id
     */
    private Long recordId = 0L;

    /**
     * Constructor
     */
    public LinkLog() {
        super(0);

        this.appId = Config.getInstance().getApps().get("log");

        this.createPropertyMap();
    }

    /**
     * 初期登録
     *
     * @return
     */
    public boolean initialize() {
        List<Record> records = new ArrayList<>();

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("UTC"));

        Record record = new Record();
        record.putField(this.startField, new DateTimeFieldValue(dateTime));

        records.add(record);

        Optional<List<Long>> ids = this.insert(records);

        ids.ifPresent(x -> {
            if (!x.isEmpty()) {
                this.recordId = x.get(0);
            }
        });

        return this.recordId != 0;
    }

    /**
     * 履歴登録（更新）
     *
     * @param info
     * @return
     */
    public boolean write(Info info) {
        Record targetRecord = this.getTargetRecord();

        if (targetRecord == null) {
            return false;
        }

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("UTC"));

        List<RecordForUpdate> updateRecords = new ArrayList<>();

        Record record = new Record();
        record.putField(this.endField, new DateTimeFieldValue(dateTime));

        List<TableRow> rows = this.setTableValue(targetRecord, info);

        SubtableFieldValue value = new SubtableFieldValue(rows);

        record.putField(this.historyTableField, value);

        RecordForUpdate updateRecord = new RecordForUpdate(this.recordId, record);
        updateRecords.add(updateRecord);

        Optional<List<RecordRevision>> revisions = this.update(updateRecords);

        return (revisions.isPresent() &&
                revisions.get().size() == 1);
    }

    /**
     * Field Property map生成
     */
    private void createPropertyMap() {
        Optional<Map<String, FieldProperty>> properties = this.getFields();

        properties.ifPresent(x -> this.properties = x);
    }

    /**
     * 処理対象レコード取得
     *
     * @return 処理対象レコード
     */
    private Record getTargetRecord() {
        final String query = String.format("レコード番号=\"%d\"", this.recordId);

        Optional<List<Record>> records = this.select(query);

        if (!records.isPresent()) {
            return null;
        }

        return records.get().get(0);
    }

    /**
     * 履歴データ生成
     *
     * @param record
     * @param info
     * @return
     */
    private List<TableRow> setTableValue(Record record, Info info) {
        List<TableRow> currentRows = record.getSubtableFieldValue(this.historyTableField);

        List<TableRow> rows = new ArrayList<>(currentRows);

        TableRow newRow = new TableRow((long) (rows.size() + 1));

        FieldValue value;

        value = new DropDownFieldValue(info.office);

        newRow.putField(this.officeField, value);

        value = new NumberFieldValue(info.count);

        newRow.putField(this.countField, value);

        rows.add(newRow);

        return rows;
    }
}
