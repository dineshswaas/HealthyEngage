package vidyo.tiles;

import android.support.annotation.NonNull;

import com.vidyo.VidyoClient.Device.RemoteCamera;
import com.vidyo.VidyoClient.Device.RemoteWindowShare;
import com.vidyo.VidyoClient.Endpoint.Participant;

import java.util.Objects;

public class RemoteHolder {

    private ViewFrame frame;
    private Participant participant;

    private RemoteCamera camera;
    private RemoteWindowShare share;

    private boolean isShare;

    private RemoteHolder(Participant participant) {
        this.participant = participant;
    }

    public RemoteHolder(Participant participant, RemoteCamera camera) {
        this(participant);
        this.camera = camera;
        this.isShare = false;
    }

    public RemoteHolder(Participant participant, RemoteWindowShare share) {
        this(participant);
        this.share = share;
        this.isShare = true;
    }

    public ViewFrame getFrame() {
        return frame;
    }

    public RemoteCamera getCamera() {
        return camera;
    }

    public RemoteWindowShare getShare() {
        return share;
    }

    public String getId() {
        return participant.id;
    }

    public void updateFrame(ViewFrame frame) {
        this.frame = frame;
    }

    public boolean isShare() {
        return this.isShare;
    }

    public boolean isValid() {
        return this.frame != null && this.participant != null;
    }

    public void release() {
        this.camera = null;
        this.participant = null;
        this.frame = null;
        this.share = null;
    }

    @NonNull
    @Override
    public String toString() {
        return "RemoteID: " + getId() + ", FrameID: " + frame.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteHolder remote = (RemoteHolder) o;
        return isShare == remote.isShare && Objects.equals(participant, remote.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participant, isShare);
    }
}