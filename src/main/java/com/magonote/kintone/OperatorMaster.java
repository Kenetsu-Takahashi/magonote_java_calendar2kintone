package com.magonote.kintone;

import com.magonote.environment.Config;

/**
 * 施術者マップ
 */
public class OperatorMaster extends Master{
    /**
     * Constructor
     */
    public OperatorMaster(){
        super();

        this.appId = Config.getInstance().getApps().get("operator");

        this.initialize();
    }
}
