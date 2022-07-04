package com.magonote;

import com.ictlab.kintone.Accessor;

/**
 * 処理ハンドラークラス
 */
public class Handler implements AutoCloseable {

    /**
     * Constructor
     */
    public Handler() {
    }

    /**
     * 実行
     */
    public void execute() {

    }

    /**
     * Accessor close
     */
    public void close() {
        Accessor.getInstance().close();
    }

}
