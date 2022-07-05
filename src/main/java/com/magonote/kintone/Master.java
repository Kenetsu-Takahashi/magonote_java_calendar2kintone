package com.magonote.kintone;

import com.ictlab.kintone.Helper;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.record.FieldType;
import com.kintone.client.model.record.Record;

import java.util.*;

/**
 * マスタ系のベースクラス
 */
public class Master  extends com.ictlab.kintone.Base{
    /**
     * IDとルックアップIDのマップ
     */
    protected Map<String, String> idMap = new HashMap<>();

    /**
     * Field情報マップ
     */
    protected Map<String, FieldProperty> properties;

    /**
     * 処理対象フィールド
     */
    protected List<String> fields = new ArrayList<>();

    /**
     * Constructor
     */
    public Master() {
        super(0);

        this.fields.add("ID");
        this.fields.add("ルックアップ用");
    }

    /**
     * IDとルックアップIDのマップ取得
     * @return IDとルックアップIDのマップ
     */
    public Map<String, String> getIdMap(){
        return this.idMap;
    }

    /**
     * Field Property map生成
     */
    protected void createPropertyMap(){
        Optional<Map<String, FieldProperty>> properties = this.getFields();

        properties.ifPresent(x -> this.properties = x);
    }

    /**
     * 初期化
     */
    protected void initialize() {
        this.createPropertyMap();

        Optional<List<Record>> records = this.select("");

        final Helper helper = new Helper();

        records.ifPresent(list -> {
            list.stream().forEach(v -> {
                FieldType type;
                String code;

                code = this.fields.get(0);
                type = this.properties.get(code).getType();
                String id = helper.getFieldValue(v, code, type);

                code = this.fields.get(1);
                type = this.properties.get(code).getType();
                String lookupId = helper.getFieldValue(v, code, type);

                this.idMap.put(id, lookupId);
            });
        });
    }
}
