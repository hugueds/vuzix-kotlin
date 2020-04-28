package com.scania.vuzixquality.utils;/*
Copyright (c) 2017, Vuzix Corporation
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

*  Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
    
*  Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
    
*  Neither the name of Vuzix Corporation nor the names of
   its contributors may be used to endorse or promote products derived
   from this software without specific prior written permission.
    
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

//package com.vuzix.sample.vuzix_speech_recognition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;

import static android.view.KeyEvent.KEYCODE_A;
import static android.view.KeyEvent.KEYCODE_AT;
import static android.view.KeyEvent.KEYCODE_B;
import static android.view.KeyEvent.KEYCODE_C;
import static android.view.KeyEvent.KEYCODE_CAPS_LOCK;
import static android.view.KeyEvent.KEYCODE_D;
import static android.view.KeyEvent.KEYCODE_DEL;
import static android.view.KeyEvent.KEYCODE_E;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_F;
import static android.view.KeyEvent.KEYCODE_G;
import static android.view.KeyEvent.KEYCODE_H;
import static android.view.KeyEvent.KEYCODE_I;
import static android.view.KeyEvent.KEYCODE_J;
import static android.view.KeyEvent.KEYCODE_K;
import static android.view.KeyEvent.KEYCODE_L;
import static android.view.KeyEvent.KEYCODE_M;
import static android.view.KeyEvent.KEYCODE_N;
import static android.view.KeyEvent.KEYCODE_O;
import static android.view.KeyEvent.KEYCODE_P;
import static android.view.KeyEvent.KEYCODE_PERIOD;
import static android.view.KeyEvent.KEYCODE_Q;
import static android.view.KeyEvent.KEYCODE_R;
import static android.view.KeyEvent.KEYCODE_S;
import static android.view.KeyEvent.KEYCODE_SHIFT_LEFT;
import static android.view.KeyEvent.KEYCODE_SPACE;
import static android.view.KeyEvent.KEYCODE_T;
import static android.view.KeyEvent.KEYCODE_U;
import static android.view.KeyEvent.KEYCODE_V;
import static android.view.KeyEvent.KEYCODE_W;
import static android.view.KeyEvent.KEYCODE_X;
import static android.view.KeyEvent.KEYCODE_Y;
import static android.view.KeyEvent.KEYCODE_Z;


/**
 * Class to encapsulate all voice commands
 */
public class VoiceCmdReceiver  extends BroadcastReceiver {
    // Voice command substitutions. These substitutions are returned when phrases are recognized.
    // This is done by registering a phrase with a substitution. This eliminates localization issues
    // and is encouraged
    final String MATCH_POPUP = "popup";
    final String MATCH_CLEAR = "clear_substitution"; // 18 char
    final String MATCH_RESTORE = "restore";
    final String MATCH_EDIT_TEXT = "edit_text_pressed"; // 17 char

    // Voice command custom intent names
    final String TOAST_EVENT = "other_toast";

    private AppCompatActivity mMainActivity;

    /**
     * Constructor which takes care of all speech recognizer registration
     * @param iActivity MainActivity from which we are created
     */
    public VoiceCmdReceiver(AppCompatActivity iActivity)
    {
        mMainActivity = iActivity;
        mMainActivity.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));
        Log.d("VUZIX", "Connecting to Vuzix Speech SDK");

        try {
            // Create a VuzixSpeechClient from the SDK
            VuzixSpeechClient sc = new VuzixSpeechClient(iActivity);
            // Delete specific phrases. This is useful if there are some that sound similar to yours, but
            // you want to keep the majority of them intact
            //sc.deletePhrase("go home");
            //sc.deletePhrase("go back");

            // Delete every phrase in the dictionary! (Available in SDK version 1.3 and newer)
            //
            // Note! When developing applications on the Vuzix Blade and Vuzix M400, deleting all
            // phrases in the dictionary removes the wake-up word(s) and voice-off words. The M300
            // cannot change the wake-up word, so "hello vuzix" is unaffected by the deletePhrase call.
            sc.deletePhrase("*");

            // For Blade and M400, the wake-word can be modified. This call has no impact on the M300,
            // which ignores added/deleted wake words.
            //
            // Please always keep the wake word "hello vuzix" to prevent confusion. You can add additional
            // wake words specific to your application by imitating the code below
            try {
                sc.insertWakeWordPhrase("hello vuzix");      // Add-back the default phrase for consistency (Blade and M400 only)
                sc.insertWakeWordPhrase("hello sample app"); // Add application specific wake-up phrase (Blade and M400 only)
            } catch (NoSuchMethodError e) {
                Log.i("VUZIX", "Setting wake words is not supported. It is introduced in M300 v1.6.6, Blade v2.6, and M400 v1.0.0");
            }

            try {
                // For all platforms, the voice-off phrase can be modified
                sc.insertVoiceOffPhrase("voice off");      // Add-back the default phrase for consistency
                sc.insertVoiceOffPhrase("privacy please"); // Add application specific stop listening phrase
            } catch (NoSuchMethodError e) {
                Log.i("VUZIX", "Setting voice off is not supported. It is introduced in M300 v1.6.6, Blade v2.6, and M400 v1.0.0");
            }

            // Now add any new strings.  If you put a substitution in the second argument, you will be passed that string instead of the full string
            sc.insertKeycodePhrase("Alfa", KEYCODE_A );
            sc.insertKeycodePhrase("Bravo", KEYCODE_B);
            sc.insertKeycodePhrase("Charlie", KEYCODE_C);
            sc.insertKeycodePhrase("Delta", KEYCODE_D);
            sc.insertKeycodePhrase("Echo", KEYCODE_E);
            sc.insertKeycodePhrase("Foxtrot", KEYCODE_F);
            sc.insertKeycodePhrase("Golf", KEYCODE_G);
            sc.insertKeycodePhrase("Hotel", KEYCODE_H);
            sc.insertKeycodePhrase("India", KEYCODE_I);
            sc.insertKeycodePhrase("Juliett", KEYCODE_J);
            sc.insertKeycodePhrase("Kilo", KEYCODE_K);
            sc.insertKeycodePhrase("Lima", KEYCODE_L);
            sc.insertKeycodePhrase("Mike", KEYCODE_M);
            sc.insertKeycodePhrase("November", KEYCODE_N);
            sc.insertKeycodePhrase("Oscar", KEYCODE_O);
            sc.insertKeycodePhrase("Papa", KEYCODE_P);
            sc.insertKeycodePhrase("Quebec", KEYCODE_Q);
            sc.insertKeycodePhrase("Romeo", KEYCODE_R);
            sc.insertKeycodePhrase("Sierra", KEYCODE_S);
            sc.insertKeycodePhrase("Tango", KEYCODE_T);
            sc.insertKeycodePhrase("Uniform", KEYCODE_U);
            sc.insertKeycodePhrase("Victor", KEYCODE_V);
            sc.insertKeycodePhrase("Whiskey", KEYCODE_W);
            sc.insertKeycodePhrase("X-Ray", KEYCODE_X);
            sc.insertKeycodePhrase("Yankee", KEYCODE_Y);
            sc.insertKeycodePhrase("Zulu", KEYCODE_Z);
            // Misc
            sc.insertKeycodePhrase("Space", KEYCODE_SPACE);
            sc.insertKeycodePhrase("shift", KEYCODE_SHIFT_LEFT);
            sc.insertKeycodePhrase("caps lock", KEYCODE_CAPS_LOCK);
            sc.insertKeycodePhrase("at sign", KEYCODE_AT);
            sc.insertKeycodePhrase("period", KEYCODE_PERIOD);
            sc.insertKeycodePhrase("erase", KEYCODE_DEL);
            sc.insertKeycodePhrase("enter", KEYCODE_ENTER);

            // Insert a custom intent.  Note: these are sent with sendBroadcastAsUser() from the service
            // If you are sending an event to another activity, be sure to test it from the adb shell
            // using: am broadcast -a "<your intent string>"
            // This example sends it to ourself, and we are sure we are active and registered for it
//            Intent customToastIntent = new Intent(mMainActivity.CUSTOM_SDK_INTENT);
//            sc.defineIntent(TOAST_EVENT, customToastIntent );
//            sc.insertIntentPhrase("canned toast", TOAST_EVENT);

            // Insert phrases for our broadcast handler
            //
            // ** NOTE **
            // The "s:" is required in the SDK version 1.2, but is not required in the latest JAR distribution
            // or SDK version 1.3.  But it is harmless when not required. It indicates that the recognizer is making a
            // substitution.  When the multi-word string is matched (in any language) the associated MATCH string
            // will be sent to the BroadcastReceiver
//            sc.insertPhrase(mMainActivity.getResources().getString(R.string.btn_text_pop_up),  MATCH_POPUP);
//            sc.insertPhrase(mMainActivity.getResources().getString(R.string.btn_text_restore), MATCH_RESTORE);
//            sc.insertPhrase(mMainActivity.getResources().getString(R.string.btn_text_clear),   MATCH_CLEAR);
//            sc.insertPhrase("Edit Text", MATCH_EDIT_TEXT);


            // See what we've done
//            Log.i(mMainActivity.LOG_TAG, sc.dump());

            // The recognizer may not yet be enabled in Settings. We can enable this directly
            VuzixSpeechClient.EnableRecognizer(mMainActivity, true);
        } catch(NoClassDefFoundError e) {
            // We get this exception if the SDK stubs against which we compiled cannot be resolved
            // at runtime. This occurs if the code is not being run on a Vuzix device supporting the voice
            // SDK
//            Toast.makeText(iActivity, R.string.only_on_vuzix, Toast.LENGTH_LONG).show();
//            Log.e(mMainActivity.LOG_TAG, iActivity.getResources().getString(R.string.only_on_vuzix) );
//            Log.e(mMainActivity.LOG_TAG, e.getMessage());
            e.printStackTrace();
            iActivity.finish();
        } catch (Exception e) {
//            Log.e(mMainActivity.LOG_TAG, "Error setting custom vocabulary: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * All custom phrases registered with insertPhrase() are handled here.
     *
     * Custom intents may also be directed here, but this example does not demonstrate this.
     *
     * Keycodes are never handled via this interface
     *
     * @param context Context in which the phrase is handled
     * @param intent Intent associated with the recognized phrase
     */
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.e("VUZIX", mMainActivity.getMethodName());
        // All phrases registered with insertPhrase() match ACTION_VOICE_COMMAND as do
        // recognizer status updates
        if (intent.getAction().equals(VuzixSpeechClient.ACTION_VOICE_COMMAND)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                // We will determine what type of message this is based upon the extras provided
                if (extras.containsKey(VuzixSpeechClient.PHRASE_STRING_EXTRA)) {
                    // If we get a phrase string extra, this was a recognized spoken phrase.
                    // The extra will contain the text that was recognized, unless a substitution
                    // was provided.  All phrases in this example have substitutions as it is
                    // considered best practice
                    String phrase = intent.getStringExtra(VuzixSpeechClient.PHRASE_STRING_EXTRA);

//                    Log.e(mMainActivity.LOG_TAG, mMainActivity.getMethodName() + " \"" + phrase + "\"");
//                    // Determine the specific phrase that was recognized and act accordingly
//                    if (phrase.equals(MATCH_POPUP)) {
//                        mMainActivity.OnPopupClick();
//                    } else if (phrase.equals(MATCH_RESTORE)) {
//                        mMainActivity.OnRestoreClick();
//                    } else if (phrase.equals(MATCH_CLEAR)) {
//                        mMainActivity.OnClearClick();
//                    } else if (phrase.equals(MATCH_EDIT_TEXT)) {
//                        mMainActivity.SelectTextBox();
//                    } else {
//                        Log.e(mMainActivity.LOG_TAG, "Phrase not handled");
//                    }
                } else if (extras.containsKey(VuzixSpeechClient.RECOGNIZER_ACTIVE_BOOL_EXTRA)) {
                    // if we get a recognizer active bool extra, it means the recognizer was
                    // activated or stopped
                    boolean isRecognizerActive = extras.getBoolean(VuzixSpeechClient.RECOGNIZER_ACTIVE_BOOL_EXTRA, false);
//                    mMainActivity.RecognizerChangeCallback(isRecognizerActive);
                } else {
                    Log.e("VUZIX", "Voice Intent not handled");
                }
            }
        }
        else {
            Log.e("VUZIX", "Other Intent not handled " + intent.getAction() );
        }
    }

    /**
     * Called to unregister for voice commands. An important cleanup step.
     */
    public void unregister() {
        try {
            mMainActivity.unregisterReceiver(this);
            Log.i("VUZIX", "Custom vocab removed");
            mMainActivity = null;
        }catch (Exception e) {
            Log.e("VUZIX", "Custom vocab died " + e.getMessage());
        }
    }

    /**
     * Handler called when "Listen" button is clicked. Activates the speech recognizer identically to
     * saying "Hello Vuzix"
     *
     * @param bOnOrOff boolean True to enable listening, false to cancel it
     */
    public void TriggerRecognizerToListen(boolean bOnOrOff) {
        try {
            VuzixSpeechClient.TriggerVoiceAudio(mMainActivity, bOnOrOff);
        } catch (NoClassDefFoundError e) {
            // The voice SDK was added in version 1.2. The constructor will have failed if the
            // target device is not a Vuzix device that is compatible with SDK version 1.2.  But the
            // trigger command with the bool was added in SDK version 1.4.  It is possible the Vuzix
            // device does not yet have the TriggerVoiceAudio interface. If so, we get this exception.
//            Toast.makeText(mMainActivity, R.string.upgrade_vuzix, Toast.LENGTH_LONG).show();
        }
    }

}
