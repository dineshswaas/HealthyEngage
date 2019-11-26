package vidyo.tiles;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.UUID;

public class ViewFrame {

    private View view;
    private boolean available;

    private ViewType viewType;

    private String unique;

    public ViewFrame(View view, ViewType viewType) {
        this.view = view;
        this.available = true;
        this.unique = UUID.randomUUID().toString();
        this.viewType = viewType;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    @NonNull
    public View getView() {
        return view;
    }

    public String getId() {
        return unique;
    }

    public ViewType getType() {
        return viewType;
    }
}