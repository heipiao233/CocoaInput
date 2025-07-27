package jp.axer.cocoainput.arch.dummy;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;

public class DummyController implements CocoaInputController{
    public DummyController() {
        CocoaInput.LOGGER.info("This is a dummy controller.");
    }

    @Override
    public IMEOperator generateIMEOperator(IMEReceiver ime) {
        return new DummyIMEOperator();
    }

	@Override
	public void clearOperator() {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}