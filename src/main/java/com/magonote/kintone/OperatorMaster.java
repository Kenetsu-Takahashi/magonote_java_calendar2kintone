package com.magonote.kintone;

import com.kintone.client.model.Organization;
import com.kintone.client.model.record.Record;
import com.magonote.environment.Config;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 施術者マップ
 */
public class OperatorMaster extends Master{
    /**
     * 対応事業所フィールド
     */
    private String officeField="対応事業所";
    /**
     * Constructor
     */
    public OperatorMaster(){
        super();

        this.appId = Config.getInstance().getApps().get("operator");

        this.initialize();
    }

    /**
     * 所属事業所取得
     * @param operatorId 施術者ID
     * @return  所属事業所
     */
    public String getOffice(String operatorId){
        AtomicReference<String> office= new AtomicReference<>("");

        final String query =String.format("ID=\"%s\"",operatorId);

        Optional<List<Record>> records = this.select(query);

        records.ifPresent(x->{
            if(!x.isEmpty()){
                Record record = x.get(0);

                List<Organization> organizations = record.getOrganizationSelectFieldValue(this.officeField);

                if(!organizations.isEmpty()){
                    Organization organization = organizations.get(0);
                    office.set(organization.getName());
                }
            }
        });

        return office.get();
    }
}
