package com.magonote;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Revision;
import com.ictlab.kintone.Accessor;
import com.ictlab.log.SystemLogger;
import com.kintone.client.model.record.Record;
import com.magonote.csv.ScheduleFile;
import com.magonote.drive.GoogleDriveUtils;
import com.magonote.drive.SubFolder;
import com.magonote.environment.Config;
import com.magonote.kintone.LinkLog;
import com.magonote.kintone.OperationSchedule;
import com.magonote.kintone.OperatorMaster;
import com.magonote.kintone.PatientMaster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 処理ハンドラークラス
 */
public class Handler implements AutoCloseable {

    /**
     * 施術者マスタ
     */
    private final OperatorMaster operatorMaster = new OperatorMaster();

    /**
     * 患者様マスタ
     */
    private final PatientMaster patientMaster = new PatientMaster();

    /**
     * 施術者IDマップ
     */
    private final Map<String, String> operatorIdMap;

    /**
     * 患者様IDマップ
     */
    private final Map<String, String> patientIdMap;

    /**
     * メッセージ
     */
    private String[] messages = {
            "カレンダー連携開始",
            "カレンダー連携終了",
            "データファイルのダウンロードに失敗しました",
            "スケジュール登録に失敗しました",
            "カレンダー連携ログ登録に失敗しました"
    };

    /**
     * データファイルの接頭辞
     */
    private String fileNameKeyword = "cal_events_";

    /**
     * データファイル出力フォルダ
     */
    private String outputFolder;

    /**
     * 処理対象ファイル拡張子
     */
    private String targetExtension = "csv";

    /**
     * 事業所別のスケジュールデータ
     */
    private final Map<String, List<OperationSchedule.Schedule>> scheduleMap = new HashMap<>();

    /**
     * 連携ログアプリ
     */
    private final LinkLog linkLog = new LinkLog();

    /**
     * 施術スケジュールアプリ
     */
    private final OperationSchedule operationSchedule = new OperationSchedule();

    /**
     * Constructor
     */
    public Handler() {
        this.operatorIdMap = this.operatorMaster.getIdMap();
        this.patientIdMap = this.patientMaster.getIdMap();

        this.outputFolder = String.format("%s/%s/%s", System.getenv("MAGONOTE_PROGRAM_PATH"), Main.MODULE_NAME, Config.getInstance().getOutputPath());
    }

    /**
     * 実行
     */
    public boolean execute() {
        SystemLogger.getInstance().write(this.messages[0], SystemLogger.Level.INFO);

        if (Main.validDownload) {
            this.deleteFile();

            if (!this.download()) {
                SystemLogger.getInstance().write(this.messages[2], SystemLogger.Level.INFO);
                return false;
            }
        }

        if (!this.linkLog.initialize()) {
            SystemLogger.getInstance().write(this.messages[4]);
            return false;
        }

        this.delete();

        this.getSchedule();

        this.register();

        SystemLogger.getInstance().write(this.messages[1], SystemLogger.Level.INFO);

        return true;
    }

    /**
     * Accessor close
     */
    public void close() {
        Accessor.getInstance().close();
    }

    /**
     * レコード削除
     */
    private void delete() {
        final List<String> fields =new ArrayList<>();
        fields.add("$id");

        Optional<List<Record>> records = this.operationSchedule.select("", fields);

        records.ifPresent(list -> {
            final List<Long> ids = list.stream().map(x -> x.getId()).collect(Collectors.toList());

            if (!ids.isEmpty()) {
                this.operationSchedule.delete(ids);
            }
        });
    }

    /**
     * ファイルダウンロード
     *
     * @return
     */
    private boolean download() {
        final String folderId = Config.getInstance().getDataFolderId();

        AtomicBoolean flag = new AtomicBoolean(true);

        Drive service;

        try {
            service = GoogleDriveUtils.getDriveService();

            if (service == null) {
                return false;
            }

            List<File> files = SubFolder.getFilesInFolder(service, folderId);

            files.stream().filter(x -> x.getName().contains(this.fileNameKeyword)).forEach(x -> {
                final String name = x.getName();

                final String outputFile = String.format("%s/%s", this.outputFolder, name);

                boolean isSuccess = false;

                try {
                    isSuccess = com.magonote.drive.Helper.downloadFile(service, x.getId(), outputFile);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                if (!isSuccess) {
                    flag.set(false);
                }
            });

        } catch (IOException e) {
            System.out.println("getDriveService error:" + e.getMessage());
        }

        return flag.get();
    }

    /**
     * 出力フォルダ内のファイル削除
     */
    private void deleteFile() {
        try {
            java.io.File outputFolder = new java.io.File(this.outputFolder);

            Files.walk(outputFolder.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .filter(x -> x.getName().contains(this.targetExtension))
                    .forEach(x -> {
                        x.delete();
                    });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 事業所別のスケジュールデータ取得
     */
    private void getSchedule() {
        try {
            final java.io.File outputFolder = new java.io.File(this.outputFolder);

            Files.walk(outputFolder.toPath())
                    .map(Path::toFile)
                    .filter(x -> x.getName().contains(this.targetExtension))
                    .forEach(this::addSchedule);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 施術スケジュール登録
     */
    private void register() {
        final LinkLog.Log log = new LinkLog.Log();

        for (Map.Entry<String, List<OperationSchedule.Schedule>> entry : this.scheduleMap.entrySet()) {
            final List<OperationSchedule.Schedule> schedules = entry.getValue();

            boolean flag = this.operationSchedule.write(schedules);

            if (!flag) {
                SystemLogger.getInstance().write(this.messages[3]);
                break;
            }

            log.office = entry.getKey();
            log.count = schedules.size();
            this.linkLog.write(log);
        }
    }

    /**
     * スケジュール追加
     *
     * @param file データファイル
     */
    private void addSchedule(java.io.File file) {
        final ScheduleFile scheduleFile = new ScheduleFile(file.getPath(), this.operatorIdMap, this.patientIdMap);

        final String operatorId = scheduleFile.getFirstOperator();

        final String office = this.operatorMaster.getOffice(operatorId);

        if (!this.scheduleMap.containsKey(office)) {
            this.scheduleMap.put(office, new ArrayList<>());
        }

        final List<OperationSchedule.Schedule> schedules = this.scheduleMap.get(office);

        schedules.addAll(scheduleFile.getScheduleList());
    }
}
