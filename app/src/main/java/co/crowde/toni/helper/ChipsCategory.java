package co.crowde.toni.helper;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.pchmn.materialchips.model.ChipInterface;

public class ChipsCategory implements ChipInterface {

    private Object id;
    private Uri avatarUri;
    private Drawable avatarDrawable;
    private String label;
    private String info;

    public ChipsCategory(@NonNull Object id, @Nullable Uri avatarUri, @NonNull String label, @Nullable String info) {
        this.id = id;
        this.avatarUri = avatarUri;
        this.label = label;
        this.info = info;
    }

    public ChipsCategory(@NonNull Object id, @Nullable Drawable avatarDrawable, @NonNull String label, @Nullable String info) {
        this.id = id;
        this.avatarDrawable = avatarDrawable;
        this.label = label;
        this.info = info;
    }

    public ChipsCategory(@Nullable Uri avatarUri, @NonNull String label, @Nullable String info) {
        this.avatarUri = avatarUri;
        this.label = label;
        this.info = info;
    }

    public ChipsCategory(@Nullable Drawable avatarDrawable, @NonNull String label, @Nullable String info) {
        this.avatarDrawable = avatarDrawable;
        this.label = label;
        this.info = info;
    }

    public ChipsCategory(@NonNull Object id, @NonNull String label, @Nullable String info) {
        this.id = id;
        this.label = label;
        this.info = info;
    }

    public ChipsCategory(@NonNull String label, @Nullable String info) {
        this.label = label;
        this.info = info;
    }

    public ChipsCategory(@NonNull String label) {
        this.label = label;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return avatarDrawable;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getInfo() {
        return info;
    }
}
