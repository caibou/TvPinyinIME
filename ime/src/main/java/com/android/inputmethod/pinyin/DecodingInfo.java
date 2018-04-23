package com.android.inputmethod.pinyin;

import android.os.RemoteException;
import android.util.Log;
import android.view.inputmethod.CompletionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author caibou
 */
public class DecodingInfo {


    private static final String TAG = "DecodingInfo";

    /**
     * Maximum length of the Pinyin string
     */
    private static final int PY_STRING_MAX = 28;

    /**
     * Maximum number of candidates to display in one page.
     */
    private static final int MAX_PAGE_SIZE_DISPLAY = 10;

    /**
     * Spelling (Pinyin) string.
     */
    private StringBuffer mSurface;

    /**
     * Byte buffer used as the Pinyin string parameter for native function
     * call.
     */
    private byte mPyBuf[];

    /**
     * The length of surface string successfully decoded by engine.
     */
    private int mSurfaceDecodedLen;

    /**
     * Composing string.
     */
    private String mComposingStr;

    /**
     * Length of the active composing string.
     */
    private int mActiveCmpsLen;

    /**
     * Composing string for display, it is copied from mComposingStr, and
     * add spaces between spellings.
     **/
    private String mComposingStrDisplay;

    /**
     * Length of the active composing string for display.
     */
    private int mActiveCmpsDisplayLen;

    /**
     * The first full sentence choice.
     */
    private String mFullSent;

    /**
     * Number of characters which have been fixed.
     */
    private int mFixedLen;

    /**
     * If this flag is true, selection is finished.
     */
    private boolean mFinishSelection;

    /**
     * The starting position for each spelling. The first one is the number
     * of the real starting position elements.
     */
    private int mSplStart[];

    /**
     * Editing cursor in mSurface.
     */
    private int mCursorPos;

    /**
     * Remote Pinyin-to-Hanzi decoding engine service.
     */
    public IPinyinDecoderService pinyinDecoderService;

    /**
     * The complication information suggested by application.
     */
    CompletionInfo[] mAppCompletions;

    /**
     * The total number of choices for display. The list may only contains
     * the first part. If user tries to navigate to next page which is not
     * in the result list, we need to get these items.
     **/
    public int mTotalChoicesNum;

    /**
     * Candidate list. The first one is the full-sentence candidate.
     */
    public List<String> mCandidatesList = new Vector<String>();

    /**
     * Element i stores the starting position of page i.
     */
    public Vector<Integer> mPageStart = new Vector<Integer>();

    /**
     * Element i stores the number of characters to page i.
     */
    public Vector<Integer> mCnToPage = new Vector<Integer>();

    /**
     * The position to delete in Pinyin string. If it is less than 0, IME
     * will do an incremental search, otherwise IME will do a deletion
     * operation. if {@link #mIsPosInSpl} is true, IME will delete the whole
     * string for mPosDelSpl-th spelling, otherwise it will only delete
     * mPosDelSpl-th character in the Pinyin string.
     */
    public int mPosDelSpl = -1;

    /**
     * If {@link #mPosDelSpl} is big than or equal to 0, this member is used
     * to indicate that whether the postion is counted in spelling id or
     * character.
     */
    public boolean mIsPosInSpl;

    private ImeState mImeState = ImeState.STATE_IDLE;

    public DecodingInfo() {
        mSurface = new StringBuffer();
        mSurfaceDecodedLen = 0;
    }

    public void reset() {
        mSurface.delete(0, mSurface.length());
        mSurfaceDecodedLen = 0;
        mCursorPos = 0;
        mFullSent = "";
        mFixedLen = 0;
        mFinishSelection = false;
        mComposingStr = "";
        mComposingStrDisplay = "";
        mActiveCmpsLen = 0;
        mActiveCmpsDisplayLen = 0;

        resetCandidates();
    }

    public boolean isCandidatesListEmpty() {
        return mCandidatesList.size() == 0;
    }

    public boolean isSplStrFull() {
        if (mSurface.length() >= PY_STRING_MAX - 1) return true;
        return false;
    }

    public void addSplChar(char ch, boolean reset) {
        if (reset) {
            mSurface.delete(0, mSurface.length());
            mSurfaceDecodedLen = 0;
            mCursorPos = 0;
            try {
                pinyinDecoderService.imResetSearch();
            } catch (RemoteException e) {
            }
        }
        mSurface.insert(mCursorPos, ch);
        mCursorPos++;
    }

    // Prepare to delete before cursor. We may delete a spelling char if
    // the cursor is in the range of unfixed part, delete a whole spelling
    // if the cursor in inside the range of the fixed part.
    // This function only marks the position used to delete.
    public void prepareDeleteBeforeCursor() {
        if (mCursorPos > 0) {
            int pos;
            for (pos = 0; pos < mFixedLen; pos++) {
                if (mSplStart[pos + 2] >= mCursorPos
                        && mSplStart[pos + 1] < mCursorPos) {
                    mPosDelSpl = pos;
                    mCursorPos = mSplStart[pos + 1];
                    mIsPosInSpl = true;
                    break;
                }
            }
            if (mPosDelSpl < 0) {
                mPosDelSpl = mCursorPos - 1;
                mCursorPos--;
                mIsPosInSpl = false;
            }
        }
    }

    public int length() {
        return mSurface.length();
    }

    public char charAt(int index) {
        return mSurface.charAt(index);
    }

    public StringBuffer getOrigianlSplStr() {
        return mSurface;
    }

    public int getSplStrDecodedLen() {
        return mSurfaceDecodedLen;
    }

    public int[] getSplStart() {
        return mSplStart;
    }

    public String getComposingStr() {
        return mComposingStr;
    }

    public String getComposingStrActivePart() {
        assert (mActiveCmpsLen <= mComposingStr.length());
        return mComposingStr.substring(0, mActiveCmpsLen);
    }

    public int getActiveCmpsLen() {
        return mActiveCmpsLen;
    }

    public String getComposingStrForDisplay() {
        return mComposingStrDisplay;
    }

    public int getActiveCmpsDisplayLen() {
        return mActiveCmpsDisplayLen;
    }

    public String getFullSent() {
        return mFullSent;
    }

    public String getCurrentFullSent(int activeCandPos) {
        try {
            String retStr = mFullSent.substring(0, mFixedLen);
            retStr += mCandidatesList.get(activeCandPos);
            return retStr;
        } catch (Exception e) {
            return "";
        }
    }

    public void resetCandidates() {
        mCandidatesList.clear();
        mTotalChoicesNum = 0;

        mPageStart.clear();
        mPageStart.add(0);
        mCnToPage.clear();
        mCnToPage.add(0);
    }

    public boolean candidatesFromApp() {
        return ImeState.STATE_APP_COMPLETION == mImeState;
    }

    public boolean canDoPrediction() {
        return mComposingStr.length() == mFixedLen;
    }

    public boolean selectionFinished() {
        return mFinishSelection;
    }

    // After the user chooses a candidate, input method will do a
    // re-decoding and give the new candidate list.
    // If candidate id is less than 0, means user is inputting Pinyin,
    // not selecting any choice.
    public void chooseDecodingCandidate(int candId) {
        if (mImeState != ImeState.STATE_PREDICT) {
            resetCandidates();
            int totalChoicesNum = 0;
            try {
                if (candId < 0) {
                    if (length() == 0) {
                        totalChoicesNum = 0;
                    } else {
                        if (mPyBuf == null)
                            mPyBuf = new byte[PY_STRING_MAX];
                        for (int i = 0; i < length(); i++)
                            mPyBuf[i] = (byte) charAt(i);
                        mPyBuf[length()] = 0;

                        if (mPosDelSpl < 0) {
                            totalChoicesNum = pinyinDecoderService
                                    .imSearch(mPyBuf, length());
                        } else {
                            boolean clear_fixed_this_step = true;
                            if (ImeState.STATE_COMPOSING == mImeState) {
                                clear_fixed_this_step = false;
                            }
                            totalChoicesNum = pinyinDecoderService
                                    .imDelSearch(mPosDelSpl, mIsPosInSpl,
                                            clear_fixed_this_step);
                            mPosDelSpl = -1;
                        }
                    }
                } else {
                    totalChoicesNum = pinyinDecoderService
                            .imChoose(candId);
                }
            } catch (RemoteException e) {
            }
            updateDecInfoForSearch(totalChoicesNum);
        }
    }

    private void updateDecInfoForSearch(int totalChoicesNum) {
        mTotalChoicesNum = totalChoicesNum;
        if (mTotalChoicesNum < 0) {
            mTotalChoicesNum = 0;
            return;
        }

        try {
            String pyStr;

            mSplStart = pinyinDecoderService.imGetSplStart();
            pyStr = pinyinDecoderService.imGetPyStr(false);
            mSurfaceDecodedLen = pinyinDecoderService.imGetPyStrLen(true);
            assert (mSurfaceDecodedLen <= pyStr.length());

            mFullSent = pinyinDecoderService.imGetChoice(0);
            mFixedLen = pinyinDecoderService.imGetFixedLen();

            // Update the surface string to the one kept by engine.
            mSurface.replace(0, mSurface.length(), pyStr);

            if (mCursorPos > mSurface.length())
                mCursorPos = mSurface.length();
            mComposingStr = mFullSent.substring(0, mFixedLen)
                    + mSurface.substring(mSplStart[mFixedLen + 1]);

            mActiveCmpsLen = mComposingStr.length();
            if (mSurfaceDecodedLen > 0) {
                mActiveCmpsLen = mActiveCmpsLen
                        - (mSurface.length() - mSurfaceDecodedLen);
            }

            // Prepare the display string.
            if (0 == mSurfaceDecodedLen) {
                mComposingStrDisplay = mComposingStr;
                mActiveCmpsDisplayLen = mComposingStr.length();
            } else {
                mComposingStrDisplay = mFullSent.substring(0, mFixedLen);
                for (int pos = mFixedLen + 1; pos < mSplStart.length - 1; pos++) {
                    mComposingStrDisplay += mSurface.substring(
                            mSplStart[pos], mSplStart[pos + 1]);
                    if (mSplStart[pos + 1] < mSurfaceDecodedLen) {
                        mComposingStrDisplay += " ";
                    }
                }
                mActiveCmpsDisplayLen = mComposingStrDisplay.length();
                if (mSurfaceDecodedLen < mSurface.length()) {
                    mComposingStrDisplay += mSurface
                            .substring(mSurfaceDecodedLen);
                }
            }

            if (mSplStart.length == mFixedLen + 2) {
                mFinishSelection = true;
            } else {
                mFinishSelection = false;
            }
        } catch (RemoteException e) {
            Log.w(TAG, "PinyinDecoderService died", e);
        } catch (Exception e) {
            mTotalChoicesNum = 0;
            mComposingStr = "";
        }
        // Prepare page 0.
        if (!mFinishSelection) {
            preparePage(0);
        }
    }

    public void choosePredictChoice(int choiceId) {
        if (ImeState.STATE_PREDICT != mImeState || choiceId < 0
                || choiceId >= mTotalChoicesNum) {
            return;
        }

        String tmp = mCandidatesList.get(choiceId);

        resetCandidates();

        mCandidatesList.add(tmp);
        mTotalChoicesNum = 1;

        mSurface.replace(0, mSurface.length(), "");
        mCursorPos = 0;
        mFullSent = tmp;
        mFixedLen = tmp.length();
        mComposingStr = mFullSent;
        mActiveCmpsLen = mFixedLen;

        mFinishSelection = true;
    }

    public String getCandidate(int candId) {
        // Only loaded items can be gotten, so we use mCandidatesList.size()
        // instead mTotalChoiceNum.
        if (candId < 0 || candId > mCandidatesList.size()) {
            return null;
        }
        return mCandidatesList.get(candId);
    }

    private void getCandiagtesForCache() {
        int fetchStart = mCandidatesList.size();
        int fetchSize = mTotalChoicesNum - fetchStart;
        if (fetchSize > MAX_PAGE_SIZE_DISPLAY) {
            fetchSize = MAX_PAGE_SIZE_DISPLAY;
        }
        try {
            List<String> newList = null;
            if (ImeState.STATE_INPUT == mImeState ||
                    ImeState.STATE_IDLE == mImeState ||
                    ImeState.STATE_COMPOSING == mImeState){
                newList = pinyinDecoderService.imGetChoiceList(
                        fetchStart, fetchSize, mFixedLen);
            } else if (ImeState.STATE_PREDICT == mImeState) {
                newList = pinyinDecoderService.imGetPredictList(
                        fetchStart, fetchSize);
            } else if (ImeState.STATE_APP_COMPLETION == mImeState) {
                newList = new ArrayList<String>();
                if (null != mAppCompletions) {
                    for (int pos = fetchStart; pos < fetchSize; pos++) {
                        CompletionInfo ci = mAppCompletions[pos];
                        if (null != ci) {
                            CharSequence s = ci.getText();
                            if (null != s) newList.add(s.toString());
                        }
                    }
                }
            }
            mCandidatesList.addAll(newList);
        } catch (RemoteException e) {
            Log.w(TAG, "PinyinDecoderService died", e);
        }
    }

    public boolean pageReady(int pageNo) {
        // If the page number is less than 0, return false
        if (pageNo < 0) return false;

        // Page pageNo's ending information is not ready.
        if (mPageStart.size() <= pageNo + 1) {
            return false;
        }

        return true;
    }

    public boolean preparePage(int pageNo) {
        // If the page number is less than 0, return false
        if (pageNo < 0) return false;

        // Make sure the starting information for page pageNo is ready.
        if (mPageStart.size() <= pageNo) {
            return false;
        }

        // Page pageNo's ending information is also ready.
        if (mPageStart.size() > pageNo + 1) {
            return true;
        }

        // If cached items is enough for page pageNo.
        if (mCandidatesList.size() - mPageStart.elementAt(pageNo) >= MAX_PAGE_SIZE_DISPLAY) {
            return true;
        }

        // Try to get more items from engine
        getCandiagtesForCache();

        // Try to find if there are available new items to display.
        // If no new item, return false;
        if (mPageStart.elementAt(pageNo) >= mCandidatesList.size()) {
            return false;
        }

        // If there are new items, return true;
        return true;
    }

    public void updateImeState(ImeState state){
        this.mImeState = state;
    }

    public ImeState imeState(){
        return mImeState;
    }

    public void preparePredicts(CharSequence history) {
        if (null == history) return;

        resetCandidates();

        /*if (Settings.getPrediction()) {
            String preEdit = history.toString();
            int predictNum = 0;
            if (null != preEdit) {
                try {
                    mTotalChoicesNum = mIPinyinDecoderService
                            .imGetPredictsNum(preEdit);
                } catch (RemoteException e) {
                    return;
                }
            }
        }*/

        preparePage(0);
        mFinishSelection = false;
    }

    void prepareAppCompletions(CompletionInfo completions[]) {
        resetCandidates();
        mAppCompletions = completions;
        mTotalChoicesNum = completions.length;
        preparePage(0);
        mFinishSelection = false;
        return;
    }

    public int getCurrentPageSize(int currentPage) {
        if (mPageStart.size() <= currentPage + 1) return 0;
        return mPageStart.elementAt(currentPage + 1)
                - mPageStart.elementAt(currentPage);
    }

    public int getCurrentPageStart(int currentPage) {
        if (mPageStart.size() < currentPage + 1) return mTotalChoicesNum;
        return mPageStart.elementAt(currentPage);
    }

    public boolean pageForwardable(int currentPage) {
        if (mPageStart.size() <= currentPage + 1) return false;
        if (mPageStart.elementAt(currentPage + 1) >= mTotalChoicesNum) {
            return false;
        }
        return true;
    }

    public boolean pageBackwardable(int currentPage) {
        if (currentPage > 0) return true;
        return false;
    }

    public boolean charBeforeCursorIsSeparator() {
        int len = mSurface.length();
        if (mCursorPos > len) return false;
        if (mCursorPos > 0 && mSurface.charAt(mCursorPos - 1) == '\'') {
            return true;
        }
        return false;
    }

    public int getCursorPos() {
        return mCursorPos;
    }

    public int getCursorPosInCmps() {
        int cursorPos = mCursorPos;
        int fixedLen = 0;

        for (int hzPos = 0; hzPos < mFixedLen; hzPos++) {
            if (mCursorPos >= mSplStart[hzPos + 2]) {
                cursorPos -= mSplStart[hzPos + 2] - mSplStart[hzPos + 1];
                cursorPos += 1;
            }
        }
        return cursorPos;
    }

    public int getCursorPosInCmpsDisplay() {
        int cursorPos = getCursorPosInCmps();
        // +2 is because: one for mSplStart[0], which is used for other
        // purpose(The length of the segmentation string), and another
        // for the first spelling which does not need a space before it.
        for (int pos = mFixedLen + 2; pos < mSplStart.length - 1; pos++) {
            if (mCursorPos <= mSplStart[pos]) {
                break;
            } else {
                cursorPos++;
            }
        }
        return cursorPos;
    }

    public void moveCursorToEdge(boolean left) {
        if (left)
            mCursorPos = 0;
        else
            mCursorPos = mSurface.length();
    }

    // Move cursor. If offset is 0, this function can be used to adjust
    // the cursor into the bounds of the string.
    public void moveCursor(int offset) {
        if (offset > 1 || offset < -1) return;

        if (offset != 0) {
            int hzPos = 0;
            for (hzPos = 0; hzPos <= mFixedLen; hzPos++) {
                if (mCursorPos == mSplStart[hzPos + 1]) {
                    if (offset < 0) {
                        if (hzPos > 0) {
                            offset = mSplStart[hzPos]
                                    - mSplStart[hzPos + 1];
                        }
                    } else {
                        if (hzPos < mFixedLen) {
                            offset = mSplStart[hzPos + 2]
                                    - mSplStart[hzPos + 1];
                        }
                    }
                    break;
                }
            }
        }
        mCursorPos += offset;
        if (mCursorPos < 0) {
            mCursorPos = 0;
        } else if (mCursorPos > mSurface.length()) {
            mCursorPos = mSurface.length();
        }
    }

    public int getSplNum() {
        return mSplStart[0];
    }

    public int getFixedLen() {
        return mFixedLen;
    }
}