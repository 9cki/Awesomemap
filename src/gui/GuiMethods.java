package gui;

public class GuiMethods {
	private DrawGraph dg;
	
	public GuiMethods() {
		this.dg = DrawGraph.getInstance();
	}
	
	public void zoomOut() {
		dg.zoomOut();
	}
}
