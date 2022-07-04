package com.magonote;

import com.ictlab.kintone.Accessor;
import com.ictlab.log.SystemLogger;

import java.util.HashMap;
import java.util.Map;

public class Main {

    /**
     * メイン処理
     *
     * @param args 起動パラメータ exec_type(kintone or kasika)
     *             kintone --- kintone to KASIKA
     *             kasika  --- KASIKA to kintone
     */
    public static void main(String[] args) {
        //初期化
        initialize();

        try {
            execute();
        } catch (Exception e) {
            SystemLogger.getInstance().write(String.format("MA連携中に予期しないエラーが発生しました\n%s", e.getMessage()));
            System.exit(-1);
        }

        System.exit(0);
    }

    /**
     * Config & Accessor初期化
     */
    public static void initialize() {
        // Config.getInstance().initialize();
        // Map<String, String> map = Config.getInstance().getAuth();
        Map<String, String> map = new HashMap<>();

        Accessor.getInstance().initialize(map.get("domain"), map.get("user"), map.get("password"));
    }


    /**
     * 実行
     *
     * @param args コマンドライン引数
     */

    /**
     * 実行
     */
    private static void execute() {
        try (Handler handler = new Handler()) {
            handler.execute();
        }
    }
}
