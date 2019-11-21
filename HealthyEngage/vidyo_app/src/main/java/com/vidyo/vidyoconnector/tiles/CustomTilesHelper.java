package com.vidyo.vidyoconnector.tiles;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Device.LocalCamera;
import com.vidyo.VidyoClient.Endpoint.Participant;
import com.vidyo.vidyoconnector.utils.Logger;
import com.vidyo.vidyoconnector.view.VideoFrameLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CustomTilesHelper implements View.OnLayoutChangeListener {

    private final Context context;
    private final Connector connector;

    private final RelativeLayout container;

    private final List<ViewFrame> viewFrameList;

    private final List<RemoteHolder> remoteHolderList;

    private LocalCamera localCamera;

    public CustomTilesHelper(Context context, Connector connector, RelativeLayout container) {
        this.context = context;
        this.connector = connector;

        this.container = container;

        this.viewFrameList = new LinkedList<>();
        this.remoteHolderList = new ArrayList<>();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        v.removeOnLayoutChangeListener(this);

        int width = v.getWidth();
        int height = v.getHeight();

        if (v == container) {
            Logger.i(CustomTilesHelper.class, "Refresh container: " + width + ", " + height);
            attachLocal(this.localCamera);
        } else {
            connector.showViewAt(v, 0, 0, width, height);
            Logger.i(CustomTilesHelper.class, "Show view at: " + width + ", " + height);
        }
    }

    public void attachLocal(LocalCamera localCamera) {
        assertInstance();

        if (localCamera == null) return;

        this.localCamera = localCamera;

        /* Refresh container */
        if (this.container.getWidth() == 0 && this.container.getHeight() == 0) {
            this.container.addOnLayoutChangeListener(this);
            this.container.requestLayout();
            return;
        }

        ViewFrame localFrame = findOrAddAvailable(ViewType.LOCAL);
        connector.assignViewToLocalCamera(localFrame.getView(), localCamera, true, false);

        localFrame.getView().addOnLayoutChangeListener(this);
        localFrame.getView().requestLayout();
    }

    public void detachLocal() {
        assertInstance();

        ViewFrame localFrame = findOrAddAvailable(ViewType.LOCAL);
        connector.hideView(localFrame.getView());
    }

    public void attachRemote(RemoteHolder remote) {
        assertInstance();

        remote.updateFrame(findOrAddAvailable(ViewType.REMOTE));

        ViewFrame viewFrame = remote.getFrame();
        viewFrame.setAvailable(false);

        final View frame = viewFrame.getView();

        if (remote.getCamera() != null) {
            connector.assignViewToRemoteCamera(frame, remote.getCamera(), true, false);
        }

        if (remote.getShare() != null) {
            connector.assignViewToRemoteWindowShare(frame, remote.getShare(), true, false);
        }

        if (!this.remoteHolderList.contains(remote))
            this.remoteHolderList.add(remote);

        Logger.i(CustomTilesHelper.class, "RemoteHolder items attach: " + remoteHolderList.size());

        frame.addOnLayoutChangeListener(this);
        frame.requestLayout();
    }

    public void detachRemote(Participant participant, boolean isShare) {
        assertInstance();

        RemoteHolder remote = findRemote(participant, isShare);
        if (remote == null || !remote.isValid()) return;

        ViewFrame viewFrame = remote.getFrame();
        viewFrame.setAvailable(true);

        this.connector.hideView(viewFrame.getView());

        remote.release();
        this.remoteHolderList.remove(remote);

        Logger.i(CustomTilesHelper.class, "RemoteHolder items detach: " + remoteHolderList.size());
    }

    public void freeFrames() {
        for (ViewFrame viewFrame : viewFrameList) {
            viewFrame.setAvailable(true);
        }
    }

    public List<RemoteHolder> getRemoteList() {
        return remoteHolderList;
    }

    public LocalCamera getLastSelectedLocalCamera() {
        return this.localCamera;
    }

    private ViewFrame findOrAddAvailable(ViewType viewType) {
        for (ViewFrame viewFrame : viewFrameList) {
            if (viewFrame.isAvailable() && viewFrame.getType() == viewType) return viewFrame;
        }

        /* Was not able to find. Add dynamically. */
        return generateFrame(viewType);
    }

    private RemoteHolder findRemote(Participant participant, boolean lookShare) {
        for (RemoteHolder remote : remoteHolderList) {
            if (lookShare && !remote.isShare()) continue;

            if (remote.getId().equals(participant.id)) return remote;
        }

        return null;
    }

    private ViewFrame generateFrame(ViewType viewType) {
        int containerWidth = container.getMeasuredWidth();
        int containerHeight = container.getMeasuredHeight();
        int margin = 15;

        Logger.i("Container width: %1d, height: %2d", containerWidth, containerHeight);

        VideoFrameLayout frame = new VideoFrameLayout(this.context);

        switch (viewType) {
            case LOCAL:
                RelativeLayout.LayoutParams localParams = new RelativeLayout.LayoutParams(containerWidth / 2, containerHeight / 2);
                localParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                localParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                localParams.bottomMargin = margin;
                frame.setLayoutParams(localParams);
                break;

            case REMOTE:
                RelativeLayout.LayoutParams remoteParams;

                if (remoteHolderList.isEmpty()) {
                    remoteParams = new RelativeLayout.LayoutParams(containerWidth, containerHeight);
                    remoteParams.bottomMargin = (containerHeight / 2) + margin;
                } else {
                    remoteParams = new RelativeLayout.LayoutParams(containerWidth / 2, containerHeight / 2);
                    remoteParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    remoteParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    remoteParams.bottomMargin = margin;
                }

                frame.setLayoutParams(remoteParams);
                break;
        }

        frame.setBackgroundColor(Color.GRAY);
        ViewFrame viewFrame = new ViewFrame(frame, viewType);
        container.addView(frame);
        viewFrameList.add(viewFrame);
        return viewFrame;
    }

    private void assertInstance() {
        if (context == null || connector == null || container == null)
            throw new RuntimeException("Object assertion!");
    }
}