package org.thoughtcrime.securesms;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import java.util.concurrent.TimeUnit;

public class MuteDialog extends AlertDialog {


  protected MuteDialog(Context context) {
    super(context);
  }

  protected MuteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }

  protected MuteDialog(Context context, int theme) {
    super(context, theme);
  }

  public static void show(final Context context, final @NonNull MuteSelectionListener listener) {


  }

  public interface MuteSelectionListener {
    public void onMuted(long until);
  }

}
