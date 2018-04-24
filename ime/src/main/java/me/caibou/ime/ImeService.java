package me.caibou.ime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.inputmethodservice.InputMethodService;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.android.inputmethod.pinyin.DecodingInfo;
import com.android.inputmethod.pinyin.IPinyinDecoderService;
import com.android.inputmethod.pinyin.ImeState;
import com.android.inputmethod.pinyin.PinyinDecoderService;

import java.util.Arrays;
import java.util.List;

import me.caibou.ime.keyboard.CandidateContainer;
import me.caibou.ime.keyboard.CandidatesView;
import me.caibou.ime.keyboard.KeyboardListener;
import me.caibou.ime.keyboard.SkbContainer;
import me.caibou.ime.pattern.SoftKey;

/**
 * @author caibou
 */
public class ImeService extends InputMethodService implements KeyboardListener {

    private static final String TAG = "ImeService";

    private SkbContainer skbContainer;
    private CandidateContainer candidateContainer;
    private CandidatesView candidatesView;
    private DecodingInfo decodingInfo;
    private PinyinDecoderServiceConnection pinyinDecoderServiceConnection;
    private InputMethodSwitcher inputMethodSwitcher;

    @Override
    public void onCreate() {
        super.onCreate();
        MeasureHelper.measure(getApplicationContext());
        inputMethodSwitcher = InputMethodSwitcher.getInstance();
        decodingInfo = new DecodingInfo();
        startPinyinDecoderService();
    }

    private void startPinyinDecoderService() {
        if (null == decodingInfo.pinyinDecoderService) {
            Intent serviceIntent = new Intent();
            serviceIntent.setClass(this, PinyinDecoderService.class);

            if (null == pinyinDecoderServiceConnection) {
                pinyinDecoderServiceConnection = new PinyinDecoderServiceConnection();
            }
            // Bind service
            bindService(serviceIntent, pinyinDecoderServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onDestroy() {
        unbindService(pinyinDecoderServiceConnection);
        super.onDestroy();
    }

    @Override
    public View onCreateInputView() {
        LayoutInflater inflater = getLayoutInflater();
        skbContainer = (SkbContainer) inflater.inflate(R.layout.layout_skb_container, null);
        skbContainer.setKeyboardListener(this);
        setCandidatesViewShown(true);
        return skbContainer;
    }

    @Override
    public View onCreateCandidatesView() {
        if (candidateContainer == null) {
            LayoutInflater inflater = getLayoutInflater();
            candidateContainer = (CandidateContainer) inflater.inflate(R.layout.layout_candi_container, null);
            candidateContainer.setInputMethodService(this);
            candidatesView = candidateContainer.candidatesView;
            skbContainer.setCandidatesView(candidatesView);
        }
        return candidateContainer;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isImeServiceStop()) {
            if (candidateContainer.onSoftKeyDown(keyCode, event)){
                return true;
            }

            if (skbContainer.onSoftKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isImeServiceStop()) {
            if (candidateContainer.onSoftKeyUp(keyCode, event)){
                return true;
            }

            if (skbContainer.onSoftKeyUp(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public void commitResultText(String resultText) {
        InputConnection ic = getCurrentInputConnection();
        if (null != ic && !TextUtils.isEmpty(resultText)) {
            ic.commitText(resultText, 1);
        }
    }

    public boolean isImeServiceStop() {
        return skbContainer != null && isInputViewShown();
    }


    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }

    @Override
    public void onCommitText(String text) {
        commitResultText(text);
    }

    @Override
    public void onSoftKeyClick(SoftKey softKey) {
        InputConnection connection = getCurrentInputConnection();
        int keyCode = softKey.getKeyCode();
        if (KeyEvent.KEYCODE_A <= keyCode && keyCode <= KeyEvent.KEYCODE_Z) {
            if (inputMethodSwitcher.isChineseText()) {
                char splChar = softKey.getKeyLabel().charAt(0);
                decodingInfo.addSplChar(splChar, false);
                chooseAndUpdate(-1);
                connection.setComposingText(decodingInfo.getOrigianlSplStr(), 1);

            } else {
                String label = softKey.getKeyLabel();
                commitResultText(inputMethodSwitcher.isUpperCase() ? label.toUpperCase() : label);
            }
            return;
        }

        if (keyCode == SkbContainer.KEYCODE_COMPOSE_WORDS){
            for (char splChar : softKey.getKeyLabel().toCharArray()){
                decodingInfo.addSplChar(splChar, false);
                chooseAndUpdate(-1);
                connection.setComposingText(decodingInfo.getOrigianlSplStr(), 1);
            }
            return;
        }

        if (keyCode == SkbContainer.KEYCODE_CANDI_SYMBOL){
            String[] symbols = getResources().getStringArray(R.array.candidate_symbols);
            List<String> symbolList = Arrays.asList(symbols);
            candidatesView.updateCandidates(symbolList);
        }

        if (KeyEvent.KEYCODE_DEL == keyCode) {
            if (inputMethodSwitcher.isChineseText() && decodingInfo.imeState() != ImeState.STATE_IDLE) {
                decodingInfo.prepareDeleteBeforeCursor();
                chooseAndUpdate(-1);
                connection.setComposingText(decodingInfo.getOrigianlSplStr(), 1);
                return;
            }
        }

        sendDownUpKeyEvents(keyCode);
    }

    private void chooseAndUpdate(int candId) {
        if (ImeState.STATE_PREDICT != decodingInfo.imeState()) {
            // Get result candidate list, if choice_id < 0, do a new decoding.
            // If choice_id >=0, select the candidate, and get the new candidate
            // list.
            decodingInfo.chooseDecodingCandidate(candId);
        } else {
            // Choose a prediction item.
            decodingInfo.choosePredictChoice(candId);
        }

        if (decodingInfo.getComposingStr().length() > 0) {
            String resultStr;
            resultStr = decodingInfo.getComposingStrActivePart();
            // choiceId >= 0 means user finishes a choice selection.
            if (candId >= 0 && decodingInfo.canDoPrediction()) {
                commitResultText(resultStr);
                decodingInfo.updateImeState(ImeState.STATE_PREDICT);
                decodingInfo.resetCandidates();
                if (decodingInfo.mCandidatesList.size() > 0) {
                    candidatesView.updateCandidates(decodingInfo.mCandidatesList);
                } else {
                    resetToIdleState();
                }
            } else {
                if (ImeState.STATE_IDLE == decodingInfo.imeState()) {
                    if (decodingInfo.getSplStrDecodedLen() == 0) {
                        decodingInfo.updateImeState(ImeState.STATE_COMPOSING);
                    } else {
                        decodingInfo.updateImeState(ImeState.STATE_INPUT);
                    }
                } else {
                    if (decodingInfo.selectionFinished()) {
                        decodingInfo.updateImeState(ImeState.STATE_COMPOSING);
                    }
                }
                candidatesView.updateCandidates(decodingInfo.mCandidatesList);
            }
        } else {
            resetToIdleState();
        }
    }

    private void resetToIdleState() {
        if (ImeState.STATE_IDLE == decodingInfo.imeState()) return;
        decodingInfo.updateImeState(ImeState.STATE_IDLE);
        decodingInfo.reset();
        commitResultText("");
        resetCandidateWindow();
    }

    private void resetCandidateWindow() {
        decodingInfo.resetCandidates();
        candidatesView.clean();
    }

    /**
     * Connection used for binding to the Pinyin decoding service.
     */
    public class PinyinDecoderServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder service) {
            decodingInfo.pinyinDecoderService = IPinyinDecoderService.Stub
                    .asInterface(service);
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }
}
