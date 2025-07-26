package jp.axer.cocoainput.arch.wayland;

import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;

public class WaylandIMEOperator implements IMEOperator {
	public IMEReceiver owner;
	private boolean focus=false;
	public WaylandIMEOperator(IMEReceiver op) {
		this.owner=op;
	}
	
	@Override
	public void discardMarkedText() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeInstance() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocused(boolean isFocused) {
		if(isFocused==focus) {
			return ;
		}
		focus=isFocused;
		Logger.debug("setFocusedCalled "+ isFocused);
		if(isFocused) {
			WaylandController.focusedOperator=this;
			Handle.INSTANCE.focus();
		}
		else {
			if(WaylandController.focusedOperator==this) {
				owner.insertText("");
				WaylandController.focusedOperator=null;
				Handle.INSTANCE.unfocus();
			}
		}

	}

}
