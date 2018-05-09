package com.zhuanghongji.android.webview.demo.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.zhuanghongji.android.webview.demo.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 封装了 Logger 的日志管理类
 */

public class MLog {
    private static boolean sIsLogEnable;

    private MLog() {
        // no instance
    }

    public static void init(final boolean isTestApk) {
        // 定义 MLog 是否打印日志
        // （可结合 PrettyFormatStrategy 和 CsvFormatStrategy 的 isLoggable 一起使用）
        sIsLogEnable = isTestApk;

        // pretty
        FormatStrategy pretty = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(3)         // (Optional) How many method line to show. Default 2
                .methodOffset(1)        // 因为多了一层封装调用逻辑，所以可适当 +1 （默认值 5）
                .tag("MLog")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(pretty) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                // return isTestApk;  // 只要是测试包都进行 Logcat 打印
                return BuildConfig.DEBUG;
            }
        });

        // csv  当前 2.2.0 版本的 Logger 好像有点问题，可到 GitHub issue 中追踪相关信息
        FormatStrategy csv = CsvFormatStrategy.newBuilder()
                .date(new Date())
                .dateFormat(new SimpleDateFormat("yyMMddHHmmss"))
                .tag("MLog")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(csv) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                // return priority >= Log.WARN;  // 只要是 warn 级别及以上都写入 Disk
                return BuildConfig.DEBUG;
            }
        });
    }

    /**
     * General log function that accepts all configurations as parameter
     */
    public static void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.log(priority, tag, message, throwable);
    }

    public static void d(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).d(message, args);
    }

    public static void d(@Nullable String tag, @Nullable Object object) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).d(object);
    }

    public static void e(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).e(message, args);
    }

    public static void e(@Nullable String tag, @Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).e(throwable, message, args);
    }

    public static void i(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).i(message, args);
    }

    public static void v(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).v(message, args);
    }

    public static void w(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).w(message, args);
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    public static void wtf(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).wtf(message, args);
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(@Nullable String tag, @Nullable String json) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).json(json);
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(@Nullable String tag, @Nullable String xml) {
        if (!sIsLogEnable) {
            return;
        }
        Logger.t(tag).xml(xml);
    }
}
