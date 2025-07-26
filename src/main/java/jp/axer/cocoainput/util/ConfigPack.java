package jp.axer.cocoainput.util;

public interface ConfigPack {

	ConfigPack DEFAULT_CONFIG = new ConfigPack() {
		@Override
		public boolean isAdvancedPreeditDraw() {
			return true;
		}
		@Override
		public boolean isNativeCharTyped() {
			return true;
		}
	};

	boolean isAdvancedPreeditDraw();
	boolean isNativeCharTyped();
}
