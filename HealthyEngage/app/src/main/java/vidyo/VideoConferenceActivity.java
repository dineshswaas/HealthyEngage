package vidyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.healthyengage.patientcare.HomePageActivity;
import com.healthyengage.patientcare.R;
import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Connector.ConnectorPkg;
import com.vidyo.VidyoClient.Device.Device;
import com.vidyo.VidyoClient.Device.LocalCamera;
import com.vidyo.VidyoClient.Device.LocalMicrophone;
import com.vidyo.VidyoClient.Device.LocalSpeaker;
import com.vidyo.VidyoClient.Device.RemoteCamera;
import com.vidyo.VidyoClient.Device.RemoteMicrophone;
import com.vidyo.VidyoClient.Device.RemoteWindowShare;
import com.vidyo.VidyoClient.Endpoint.Participant;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import Repositories.APIRepository;
import models.APIResponseModels;
import models.VideoData;
import utils.Constants;
import utils.NetworkUtils;
import utils.PreferenceUtils;
import vidyo.connect.ConnectParams;
import vidyo.event.ControlEvent;
import vidyo.event.IControlLink;
import vidyo.tiles.CustomTilesHelper;
import vidyo.tiles.RemoteHolder;
import vidyo.utils.AppUtils;
import vidyo.utils.Logger;
import vidyo.utils.Preferences;
import vidyo.view.ControlView;


public class VideoConferenceActivity extends FragmentActivity  implements
        Connector.IConnect,Connector.IRegisterLocalCameraEventListener,
        Connector.IRegisterRemoteCameraEventListener,
        Connector.IRegisterLocalSpeakerEventListener,
        Connector.IRegisterRemoteMicrophoneEventListener,
        Connector.IRegisterLocalMicrophoneEventListener,
        Connector.IRegisterResourceManagerEventListener,
        Connector.IRegisterRemoteWindowShareEventListener,
        Connector.IRegisterParticipantEventListener,IControlLink{

    private ControlView controlView;
    private View progressBar;

    private Connector connector;

    private AtomicBoolean isCameraDisabledForBackground = new AtomicBoolean(false);

    private CustomTilesHelper customTilesHelper;
    private ProgressDialog progressDialog;
    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        if (connector != null) {
            ControlView.State state = controlView.getState();
            connector.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Foreground);

            connector.setCameraPrivacy(state.isMuteCamera());
            connector.setMicrophonePrivacy(state.isMuteMic());
            connector.setSpeakerPrivacy(state.isMuteSpeaker());
        }

        LocalCamera localCamera = customTilesHelper.getLastSelectedLocalCamera();
        if (connector != null && localCamera != null && isCameraDisabledForBackground.getAndSet(false)) {
            connector.selectLocalCamera(localCamera);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (connector != null) {
            connector.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Background);

            connector.setCameraPrivacy(true);
            connector.setMicrophonePrivacy(true);
            connector.setSpeakerPrivacy(true);
        }

        if (!isFinishing() && connector != null && !controlView.getState().isMuteCamera() && !isCameraDisabledForBackground.getAndSet(true)) {
            connector.selectLocalCamera(null);
            customTilesHelper.detachLocal();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_layouts);

        ConnectorPkg.initialize();
        ConnectorPkg.setApplicationUIContext(this);
        progressBar = findViewById(R.id.progress);
        progressDialog = new ProgressDialog(this);
        progressBar.setVisibility(View.GONE);
        controlView = findViewById(R.id.control_view);
        controlView.registerListener(this);
        videoCallConfigs();


    }

    private void getVideoCallDetails() {
        if(NetworkUtils.isNetworkAvailable(this)) {
            showProgressDialog();
            APIRepository apiRepository = new APIRepository(this);
            apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
                @Override
                public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
                    hideProgress();
                    if (apiResponseModels != null) {
                        PreferenceUtils.setVideoToken(VideoConferenceActivity.this, apiResponseModels.getToken());
                        VideoData videoData = apiResponseModels.getVideoData();
                        PreferenceUtils.setVideoRoomId(VideoConferenceActivity.this, videoData.getRoom_id());

                        connector.connect(Constants.HOST, PreferenceUtils.getVideoToken(VideoConferenceActivity.this),
                                ConnectParams.DISPLAY_NAME, PreferenceUtils.getVideoRoomId(VideoConferenceActivity.this), VideoConferenceActivity.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to connect the call,please try again", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void getAPIResponseModelFailure(String s) {
                    Toast.makeText(getApplicationContext(), "Unable to connect the call,please try again", Toast.LENGTH_LONG).show();
                    hideProgress();
                }
            });
            APIResponseModels apiResponseModels = new APIResponseModels();
            apiResponseModels.setPatientId(PreferenceUtils.getPatientId(this));
            apiRepository.joinVideoCall(apiResponseModels);
        }
    }

    private void showProgressDialog() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("Connecting video call...please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    private void hideProgress(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void videoCallConfigs() {
        /*
         * Connector instance created with NULL passed as video frame. Local & RemoteHolder camera will be assigned later.
         */
        connector = new Connector(null, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default,
                8, "*@VidyoClient info@VidyoConnector info warning", AppUtils.configLogFile(this), 0);
        Logger.i("Connector instance has been created.");

        controlView.showVersion(connector.getVersion());
        RelativeLayout container = findViewById(R.id.master_container);
        customTilesHelper = new CustomTilesHelper(this, connector, container);
        /*
         * Register all the  listeners required for custom implementation
         */
        connector.registerLocalCameraEventListener(this);
        connector.registerLocalSpeakerEventListener(this);
        connector.registerLocalMicrophoneEventListener(this);
        connector.registerRemoteCameraEventListener(this);
        connector.registerRemoteMicrophoneEventListener(this);
        connector.registerRemoteWindowShareEventListener(this);
        connector.registerParticipantEventListener(this);
    }

    @Override
    public void onControlEvent(ControlEvent event) {
        if (connector == null) return;

        switch (event.getCall()) {
            case CONNECT_DISCONNECT:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                progressBar.setVisibility(View.VISIBLE);
                controlView.disable(true);
                boolean state = (boolean) event.getValue();
                controlView.updateConnectionState(state ? ControlView.ConnectionState.CONNECTING : ControlView.ConnectionState.DISCONNECTING);

                if (state) {
                    getVideoCallDetails();
                } else {
                    if (connector != null) connector.disconnect();
                    startActivity(new Intent(this, HomePageActivity.class));
                    finish();
                }
                break;
            case MUTE_CAMERA:
                boolean cameraPrivacy = (boolean) event.getValue();
                connector.setCameraPrivacy(cameraPrivacy);

                if (cameraPrivacy) {
                    connector.selectLocalCamera(null);
                    customTilesHelper.detachLocal();
                } else {
                    connector.selectLocalCamera(customTilesHelper.getLastSelectedLocalCamera());
                }
                break;
            case MUTE_MIC:
                connector.setMicrophonePrivacy((boolean) event.getValue());
                break;
            case MUTE_SPEAKER:
                connector.setSpeakerPrivacy((boolean) event.getValue());
                break;
            case CYCLE_CAMERA:
                connector.cycleCamera();
                break;
            case DEBUG_OPTION:
                boolean value = (boolean) event.getValue();
                if (value) {
                    connector.enableDebug(7776, "");
                } else {
                    connector.disableDebug();
                }

                Toast.makeText(VideoConferenceActivity.this, "Debug" + value, Toast.LENGTH_SHORT).show();
                break;
            case SEND_LOGS:
                AppUtils.sendLogs(this);
                break;
        }
    }
    @Override
    public void onSuccess() {
        if (!connector.registerResourceManagerEventListener(this)) {
            Logger.e("Failed to register resource manager event listener");
        } else {
            Logger.e("Resource manager event listener succeed.");
        }

        runOnUiThread(() -> {
            Toast.makeText(VideoConferenceActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);

            controlView.connectedCall(true);
            controlView.updateConnectionState(ControlView.ConnectionState.CONNECTED);
            controlView.disable(false);
        });
    }

    @Override
    public void onFailure(Connector.ConnectorFailReason connectorFailReason) {
        if (connector != null) connector.unregisterResourceManagerEventListener();

        runOnUiThread(() -> {
            Toast.makeText(VideoConferenceActivity.this, connectorFailReason.name(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);

            controlView.connectedCall(false);
            controlView.updateConnectionState(ControlView.ConnectionState.FAILED);
            controlView.disable(false);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        });
    }

    @Override
    public void onDisconnected(Connector.ConnectorDisconnectReason connectorDisconnectReason) {
        if (connector != null) connector.unregisterResourceManagerEventListener();

        runOnUiThread(() -> {
            Toast.makeText(VideoConferenceActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);

            controlView.connectedCall(false);
            controlView.updateConnectionState(ControlView.ConnectionState.DISCONNECTED);
            controlView.disable(false);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (controlView != null) controlView.unregisterListener();

        if (connector != null) {
            connector.unregisterLocalCameraEventListener();
            connector.unregisterLocalSpeakerEventListener();
            connector.unregisterLocalMicrophoneEventListener();

            connector.unregisterRemoteCameraEventListener();
            connector.unregisterRemoteMicrophoneEventListener();

            connector.unregisterParticipantEventListener();

            connector.disable();
            connector = null;
        }

        ConnectorPkg.uninitialize();
        ConnectorPkg.setApplicationUIContext(null);

        Logger.i("Connector instance has been released.");
    }

    @Override
    public void onLocalCameraAdded(LocalCamera localCamera) {
        Logger.i("Local camera added.");
    }

    @Override
    public void onLocalCameraRemoved(LocalCamera localCamera) {

    }

    @Override
    public void onLocalCameraSelected(LocalCamera localCamera) {
        Logger.i(VideoConferenceActivity.class, "Local camera selected");

        runOnUiThread(() -> customTilesHelper.attachLocal(localCamera));
    }

    @Override
    public void onLocalCameraStateUpdated(LocalCamera localCamera, Device.DeviceState deviceState) {

    }


    @Override
    public void onRemoteCameraAdded(RemoteCamera remoteCamera, Participant participant) {
        Logger.i(VideoConferenceActivity.class, "RemoteHolder camera added");

        runOnUiThread(() -> customTilesHelper.attachRemote(new RemoteHolder(participant, remoteCamera)));
    }

    @Override
    public void onRemoteCameraRemoved(RemoteCamera remoteCamera, Participant participant) {
        Logger.i(VideoConferenceActivity.class, "RemoteHolder camera removed");

        runOnUiThread(() -> customTilesHelper.detachRemote(participant, false));
    }

    @Override
    public void onRemoteCameraStateUpdated(RemoteCamera remoteCamera, Participant participant, Device.DeviceState deviceState) {

    }

    @Override
    public void onLocalSpeakerAdded(LocalSpeaker localSpeaker) {

    }

    @Override
    public void onLocalSpeakerRemoved(LocalSpeaker localSpeaker) {

    }

    @Override
    public void onLocalSpeakerSelected(LocalSpeaker localSpeaker) {

    }

    @Override
    public void onLocalSpeakerStateUpdated(LocalSpeaker localSpeaker, Device.DeviceState deviceState) {

    }

    @Override
    public void onRemoteMicrophoneAdded(RemoteMicrophone remoteMicrophone, Participant participant) {

    }

    @Override
    public void onRemoteMicrophoneRemoved(RemoteMicrophone remoteMicrophone, Participant participant) {

    }

    @Override
    public void onRemoteMicrophoneStateUpdated(RemoteMicrophone remoteMicrophone, Participant participant, Device.DeviceState deviceState) {

    }

    @Override
    public void onLocalMicrophoneAdded(LocalMicrophone localMicrophone) {

    }

    @Override
    public void onLocalMicrophoneRemoved(LocalMicrophone localMicrophone) {

    }

    @Override
    public void onLocalMicrophoneSelected(LocalMicrophone localMicrophone) {

    }

    @Override
    public void onLocalMicrophoneStateUpdated(LocalMicrophone localMicrophone, Device.DeviceState deviceState) {

    }

    @Override
    public void onAvailableResourcesChanged(int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMaxRemoteSourcesChanged(int i) {

    }

    @Override
    public void onRemoteWindowShareAdded(RemoteWindowShare remoteWindowShare, Participant participant) {
        Logger.i(VideoConferenceActivity.class, "RemoteHolder share added");

        runOnUiThread(() -> customTilesHelper.attachRemote(new RemoteHolder(participant, remoteWindowShare)));
    }

    @Override
    public void onRemoteWindowShareRemoved(RemoteWindowShare remoteWindowShare, Participant participant) {
        Logger.i(VideoConferenceActivity.class, "RemoteHolder share removed");

        runOnUiThread(() -> customTilesHelper.detachRemote(participant, true));
    }

    @Override
    public void onRemoteWindowShareStateUpdated(RemoteWindowShare remoteWindowShare, Participant participant, Device.DeviceState deviceState) {

    }

    @Override
    public void onParticipantJoined(Participant participant) {

    }

    @Override
    public void onParticipantLeft(Participant participant) {

    }

    @Override
    public void onDynamicParticipantChanged(ArrayList<Participant> arrayList) {

    }

    @Override
    public void onLoudestParticipantChanged(Participant participant, boolean b) {

    }


}