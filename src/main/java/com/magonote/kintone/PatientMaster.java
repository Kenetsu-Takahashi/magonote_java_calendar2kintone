package com.magonote.kintone;

import com.ictlab.kintone.Helper;
import com.kintone.client.model.record.FieldType;
import com.kintone.client.model.record.Record;
import com.magonote.environment.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 患者様マスタ
 */
public class PatientMaster extends Master {
    /**
     * Constructor
     */
    public PatientMaster() {
        super();

        this.appId = Config.getInstance().getApps().get("patient");

        this.initialize();
    }
}
