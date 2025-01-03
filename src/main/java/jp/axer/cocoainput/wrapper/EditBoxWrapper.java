
package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.Rect;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;

public class EditBoxWrapper extends IMEReceiver {
    private IMEOperator myIME;
    private EditBox owner;

    public EditBoxWrapper(EditBox field) {
        ModLogger.debug("EditBox init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
    }

    public void setCanLoseFocus(boolean newParam) {
        if (!newParam) setFocused(true);
    }

    public void setFocused(boolean newParam) {
    	owner.setFormatter( ((abc,def) -> Component.literal(abc).getVisualOrderText()     ));
        myIME.setFocused(newParam);
    }

    protected void setText(String text) {
    	owner.setValue(text);
    }

	protected String getText() {
		return owner.value;
	}

	protected void setCursorInvisible() {
        //(Util.getMillis() - this.focusedTime) / 300L % 2L == 0L ... true is drawing cursor
        owner.focusedTime = (long)(Util.getMillis() / 300) * 300 - 1;
	}

	protected int getCursorPos() {
		return owner.getCursorPosition();
	}

	protected void setCursorPos(int p) {
		owner.moveCursorTo(p, true);
	}

	protected void setSelectionPos(int p) {
		owner.setHighlightPos(p);
	}


    @Override
    public Rect getRect() {
        return new Rect(//{x,y}
                (owner.font.width(owner.getValue().substring(0, originalCursorPosition)) + (owner.bordered ? owner.getX() + 4 : owner.getX())),
                (owner.font.lineHeight + (owner.bordered ? owner.getY() + (owner.getHeight() - 8) / 2 : owner.getY())),
                owner.getWidth(),
                owner.getHeight()

        );
    }

	protected void notifyParent(String text) {
    	owner.onValueChange(text);

	}


}
