package com.example.loaderweather.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.loaderweather.R;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

// класс диалогового окна загрузки
public class LoadingDialog extends DialogFragment {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    // интерфейс загрузочного процесса
    @NonNull
    public static LoadingView view(@NonNull FragmentManager fm) {

        // переопределение функций индикации
        return new LoadingView() {

            private final AtomicBoolean mWaitForHide = new AtomicBoolean();

            @Override
            public void showLoadingIndicator() {
                if (mWaitForHide.compareAndSet(false, true)) {
                    if (fm.findFragmentByTag(LoadingDialog.class.getName()) == null) {
                        LoadingDialog dialog = new LoadingDialog();
                        dialog.show(fm, LoadingDialog.class.getName());
                    }
                }
            }

            @Override
            public void hideLoadingIndicator() {
                if (mWaitForHide.compareAndSet(true, false)) {
                    HANDLER.post(new HideTask(fm));
                }
            }
        };
    }

    // отображение диалогового окна
    @NonNull
    public static LoadingView view(@NonNull Fragment fragment) {
        return view(fragment.getFragmentManager());
    }

    // создание диалогового окна
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
        setCancelable(false);
    }

    // получение диалогового окна з возвратом экземпляра
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(View.inflate(getActivity(), R.layout.dialog_loading, null))
                .create();
    }

    // класс задачи в диалоговом окне загрузки
    private static class HideTask implements Runnable {

        private final Reference<FragmentManager> mFmRef;

        private int mAttempts = 10;

        public HideTask(@NonNull FragmentManager fm) {
            mFmRef = new WeakReference<>(fm);
        }

        @Override
        public void run() {
            HANDLER.removeCallbacks(this);
            final FragmentManager fm = mFmRef.get();
            if (fm != null) {
                final LoadingDialog dialog = (LoadingDialog) fm.findFragmentByTag(LoadingDialog.class.getName());
                if (dialog != null) {
                    dialog.dismissAllowingStateLoss();
                } else if (--mAttempts >= 0) {
                    HANDLER.postDelayed(this, 300);
                }
            }
        }
    }
}
