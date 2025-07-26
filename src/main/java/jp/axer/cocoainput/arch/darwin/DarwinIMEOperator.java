package jp.axer.cocoainput.arch.darwin;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.arch.darwin.CallbackFunction.Func_firstRectForCharacterRange;
import jp.axer.cocoainput.arch.darwin.CallbackFunction.Func_insertText;
import jp.axer.cocoainput.arch.darwin.CallbackFunction.Func_setMarkedText;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.Rect;

import java.util.UUID;

public class DarwinIMEOperator implements IMEOperator {
    IMEReceiver owner;
    String uuid;
    Func_insertText insertText_p;
    Func_setMarkedText setMarkedText_p;
    Func_firstRectForCharacterRange firstRectForCharacterRange_p;
    boolean isFocused = false;

    public DarwinIMEOperator(IMEReceiver field) {
        this.owner = field;
        uuid = UUID.randomUUID().toString();
        insertText_p = (str, position, length) -> {
            CocoaInput.LOGGER.debug("Textfield " + uuid + " received inserted text.");
            owner.insertText(str);
        };
        setMarkedText_p = (str, position1, length1, position2, length2) -> {
            CocoaInput.LOGGER.debug("MarkedText changed at " + uuid + ".");
            owner.setMarkedText(str, position1, length1);
        };
        firstRectForCharacterRange_p = () -> {
            CocoaInput.LOGGER.debug("Called to determine where to draw.");
            Rect point = owner.getRect();
            float[] buff;
            if (point == null) {
                buff = new float[]{0, 0, 0, 0};
            } else {
                buff = new float[]{point.getX(), point.getY(), point.getWidth(), point.getHeight()};
            }
            double factor = CocoaInput.getScreenScaledFactor();
            buff[0] *= factor;
            buff[1] *= factor;
            buff[2] *= factor;
            buff[3] *= factor;

            Pointer ret = new Memory(Float.BYTES * 4);
            ret.write(0, buff, 0, 4);
            return ret;
        };
        CocoaInput.LOGGER.info("IMEOperator addInstance: " + uuid);
        Handle.INSTANCE.addInstance(uuid, insertText_p, setMarkedText_p, firstRectForCharacterRange_p);
    }

    public void discardMarkedText() {
        Handle.INSTANCE.discardMarkedText(uuid);
    }

    public void removeInstance() {
        Handle.INSTANCE.removeInstance(uuid);
    }

    public void setFocused(boolean yn) {
        if (yn != isFocused) {
            CocoaInput.LOGGER.info("IMEOperator.setFocused: " + (yn ? "true" : "false"));
            Handle.INSTANCE.setIfReceiveEvent(uuid, yn ? 1 : 0);
            isFocused = yn;
        }
    }
}