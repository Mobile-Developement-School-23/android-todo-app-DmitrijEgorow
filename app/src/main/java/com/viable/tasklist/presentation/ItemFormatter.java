package com.viable.tasklist.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public interface ItemFormatter {

    public CharSequence formatDate(@Nullable String date);
    public CharSequence wrapWithTemplate(@Nullable CharSequence text, @StringRes int stringId, boolean shouldStrikethrough);
}
