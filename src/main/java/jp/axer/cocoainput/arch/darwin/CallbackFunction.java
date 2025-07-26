package jp.axer.cocoainput.arch.darwin;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class CallbackFunction {
    //used to interact Objective-C code
    interface Func_insertText extends Callback {
        void invoke(String str, int position, int length);
    }

    interface Func_setMarkedText extends Callback {
        void invoke(String str, int position1, int length1, int position2, int length2);
    }

    interface Func_firstRectForCharacterRange extends Callback {
        Pointer invoke();
    }
}